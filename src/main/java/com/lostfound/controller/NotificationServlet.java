package com.lostfound.controller;

import com.lostfound.dao.NotificationDAO;
import com.lostfound.model.Notification;
import com.lostfound.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Controller for managing user notifications.
 */
@WebServlet(urlPatterns = {"/notifications", "/notification/read", "/notification/read-all"})
public class NotificationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final NotificationDAO notificationDAO = new NotificationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("loggedUser");
        String path = request.getServletPath();

        if ("/notification/read".equals(path)) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                try {
                    int notifId = Integer.parseInt(idStr);
                    notificationDAO.markAsRead(notifId);
                } catch (NumberFormatException ignored) {}
            }
            response.sendRedirect(request.getContextPath() + "/notifications");
            return;
        }

        if ("/notification/read-all".equals(path)) {
            notificationDAO.markAllAsRead(user.getUserId());
            response.sendRedirect(request.getContextPath() + "/notifications");
            return;
        }

        List<Notification> notifications = notificationDAO.getNotificationsByUser(user.getUserId());
        request.setAttribute("notifications", notifications);
        request.getRequestDispatcher("/notifications.jsp").forward(request, response);
    }
}
