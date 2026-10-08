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
 * Controller for viewing items reported by the logged-in user.
 */
@WebServlet("/my-items")
public class MyItemsServlet extends HttpServlet {

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
        List<Item> myItems = itemService.getItemsByUser(user.getUserId());

        request.setAttribute("items", myItems);
        if (request.getParameter("deleted") != null) {
            request.setAttribute("successMessage", "Item deleted successfully.");
        }
        request.getRequestDispatcher("/my-items.jsp").forward(request, response);
    }
}
