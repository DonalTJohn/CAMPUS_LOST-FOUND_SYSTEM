package com.lostfound.controller;

import com.lostfound.exception.InvalidInputException;
import com.lostfound.exception.UserNotFoundException;
import com.lostfound.model.User;
import com.lostfound.service.UserOperations;
import com.lostfound.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller for viewing and editing User Profile.
 */
@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UserOperations userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User sessionUser = (User) session.getAttribute("loggedUser");
        try {
            User user = userService.getUserById(sessionUser.getUserId());
            session.setAttribute("loggedUser", user);
            request.setAttribute("user", user);
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
        } catch (UserNotFoundException e) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User sessionUser = (User) session.getAttribute("loggedUser");
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            if ("updateDetails".equalsIgnoreCase(action)) {
                String name = request.getParameter("name");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");

                User updatedUser = new User();
                updatedUser.setUserId(sessionUser.getUserId());
                updatedUser.setName(name);
                updatedUser.setEmail(email);
                updatedUser.setPhone(phone);

                userService.updateProfile(updatedUser);
                User freshUser = userService.getUserById(sessionUser.getUserId());
                session.setAttribute("loggedUser", freshUser);
                request.setAttribute("successMessage", "Profile updated successfully!");

            } else if ("changePassword".equalsIgnoreCase(action)) {
                String currentPassword = request.getParameter("currentPassword");
                String newPassword = request.getParameter("newPassword");
                String confirmPassword = request.getParameter("confirmPassword");

                if (!newPassword.equals(confirmPassword)) {
                    throw new InvalidInputException("New passwords do not match.");
                }

                userService.changePassword(sessionUser.getUserId(), currentPassword, newPassword);
                request.setAttribute("successMessage", "Password changed successfully!");
            }
        } catch (InvalidInputException e) {
            request.setAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error updating profile: " + e.getMessage());
        }

        doGet(request, response);
    }
}
