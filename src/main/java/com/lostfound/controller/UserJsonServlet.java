package com.lostfound.controller;

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
import java.io.PrintWriter;

/**
 * RESTful endpoint representing user data in simple JSON format.
 * Satisfies Phase 3: Represent user data format using JSON (simple structure).
 */
@WebServlet(urlPatterns = {"/api/user", "/user/json"})
public class UserJsonServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UserOperations userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedUser") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print("{\"error\": \"Unauthorized. Please login first.\"}");
            out.flush();
            return;
        }

        User sessionUser = (User) session.getAttribute("loggedUser");
        String idParam = request.getParameter("id");
        int targetUserId = sessionUser.getUserId();

        if (idParam != null && !idParam.trim().isEmpty()) {
            try {
                int requestedId = Integer.parseInt(idParam.trim());
                // Non-admins can only fetch their own user JSON
                if (sessionUser.isAdmin() || requestedId == sessionUser.getUserId()) {
                    targetUserId = requestedId;
                } else {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    out.print("{\"error\": \"Forbidden: Access denied.\"}");
                    out.flush();
                    return;
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Invalid user ID format.\"}");
                out.flush();
                return;
            }
        }

        try {
            User user = userService.getUserById(targetUserId);
            out.print(user.toJson());
        } catch (UserNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }
}
