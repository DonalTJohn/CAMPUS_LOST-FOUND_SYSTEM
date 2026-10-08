package com.lostfound.controller;

import com.lostfound.model.Item;
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
import java.util.List;

/**
 * Controller for Admin Item Management (moderation, status changes, and deletion).
 */
@WebServlet(urlPatterns = {"/admin/items", "/admin/item/status"})
public class AdminItemServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ItemOperations itemService = new ItemService();

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

        String filterType = request.getParameter("type");
        String filterStatus = request.getParameter("status");
        String keyword = request.getParameter("keyword");

        List<Item> items;
        if ((filterType != null && !filterType.isEmpty()) || 
            (filterStatus != null && !filterStatus.isEmpty()) || 
            (keyword != null && !keyword.isEmpty())) {
            items = itemService.searchItems(keyword, null, null, filterType, filterStatus, null);
        } else {
            items = itemService.getAllItems();
        }

        request.setAttribute("items", items);
        request.setAttribute("filterType", filterType);
        request.setAttribute("filterStatus", filterStatus);
        request.setAttribute("keyword", keyword);

        request.getRequestDispatcher("/admin/items.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
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

        String action = request.getParameter("action");
        String itemIdStr = request.getParameter("itemId");

        try {
            int itemId = Integer.parseInt(itemIdStr);

            if ("updateStatus".equalsIgnoreCase(action)) {
                String newStatus = request.getParameter("status");
                itemService.updateItemStatus(itemId, newStatus);
                response.sendRedirect(request.getContextPath() + "/admin/items?statusUpdated=true");
                return;
            }

            response.sendRedirect(request.getContextPath() + "/admin/items");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/items?error=" + e.getMessage());
        }
    }
}
