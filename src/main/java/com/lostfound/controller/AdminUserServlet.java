package com.lostfound.controller;

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
import java.util.List;

/**
 * Controller for Admin User Management (list users, toggle block/active status).
 */
@WebServlet(urlPatterns = {"/admin/users", "/admin/user/status"})
public class AdminUserServlet extends HttpServlet {

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

        User user = (User) session.getAttribute("loggedUser");
        if (!user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        List<User> userList = userService.getAllUsers();
        request.setAttribute("users", userList);
        request.getRequestDispatcher("/admin/users.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User admin = (User) session.getAttribute("loggedUser");
        if (!admin.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        String userIdStr = request.getParameter("userId");
        String status = request.getParameter("status"); // ACTIVE or BLOCKED

        try {
            int userId = Integer.parseInt(userIdStr);
            if (userId == admin.getUserId()) {
                response.sendRedirect(request.getContextPath() + "/admin/users?error=CannotModifySelf");
                return;
            }

            userService.changeUserStatus(userId, status);
            response.sendRedirect(request.getContextPath() + "/admin/users?updated=true");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/users?error=" + e.getMessage());
        }
    }
}
