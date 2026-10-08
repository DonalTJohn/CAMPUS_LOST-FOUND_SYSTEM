package com.lostfound.controller;

import com.lostfound.exception.ItemNotFoundException;
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
 * Controller for viewing single item details and calculating potential matches.
 */
@WebServlet("/item/details")
public class ItemDetailsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ItemOperations itemService = new ItemService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/items");
            return;
        }

        try {
            int itemId = Integer.parseInt(idStr);
            Item item = itemService.getItemById(itemId);
            List<Item> potentialMatches = itemService.getPotentialMatches(itemId);

            HttpSession session = request.getSession(false);
            User currentUser = (session != null) ? (User) session.getAttribute("loggedUser") : null;

            boolean isOwner = currentUser != null && currentUser.getUserId() == item.getUserId();
            boolean isAdmin = currentUser != null && currentUser.isAdmin();

            request.setAttribute("item", item);
            request.setAttribute("potentialMatches", potentialMatches);
            request.setAttribute("isOwner", isOwner);
            request.setAttribute("isAdmin", isAdmin);

            if (request.getParameter("created") != null) {
                request.setAttribute("successMessage", "Item report has been submitted successfully!");
            }

            request.getRequestDispatcher("/item-details.jsp").forward(request, response);

        } catch (ItemNotFoundException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/items");
        }
    }
}
