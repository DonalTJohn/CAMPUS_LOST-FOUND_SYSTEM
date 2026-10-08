package com.lostfound.controller;

import com.lostfound.model.User;
import com.lostfound.service.ItemOperations;
import com.lostfound.service.ItemService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller for deleting reported items.
 */
@WebServlet("/item/delete")
public class DeleteItemServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ItemOperations itemService = new ItemService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("loggedUser");
        String idStr = request.getParameter("id");

        try {
            int itemId = Integer.parseInt(idStr);
            itemService.deleteItem(itemId, user.getUserId(), user.isAdmin());
            
            String redirect = request.getParameter("redirect");
            if ("admin".equalsIgnoreCase(redirect)) {
                response.sendRedirect(request.getContextPath() + "/admin/items?deleted=true");
            } else {
                response.sendRedirect(request.getContextPath() + "/my-items?deleted=true");
            }
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/items?error=" + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doPost(request, response);
    }
}
