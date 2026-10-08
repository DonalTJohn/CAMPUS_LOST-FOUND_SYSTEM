package com.lostfound.controller;

import com.lostfound.exception.InvalidInputException;
import com.lostfound.exception.ItemNotFoundException;
import com.lostfound.model.Claim;
import com.lostfound.model.Item;
import com.lostfound.model.User;
import com.lostfound.service.ClaimOperations;
import com.lostfound.service.ClaimService;
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
 * Controller for submitting ownership claims on items.
 */
@WebServlet("/claim/submit")
public class ClaimServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ClaimOperations claimService = new ClaimService();
    private final ItemOperations itemService = new ItemService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String itemIdStr = request.getParameter("itemId");
        if (itemIdStr == null || itemIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/items");
            return;
        }

        try {
            int itemId = Integer.parseInt(itemIdStr);
            Item item = itemService.getItemById(itemId);
            request.setAttribute("item", item);
            request.getRequestDispatcher("/submit-claim.jsp").forward(request, response);
        } catch (ItemNotFoundException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/items");
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

        User user = (User) session.getAttribute("loggedUser");
        request.setCharacterEncoding("UTF-8");

        String itemIdStr = request.getParameter("itemId");
        String proofDescription = request.getParameter("proofDescription");

        try {
            int itemId = Integer.parseInt(itemIdStr);
            Claim claim = new Claim(itemId, user.getUserId(), proofDescription, "PENDING");
            boolean success = claimService.submitClaim(claim);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/claims/my?submitted=true");
            } else {
                request.setAttribute("errorMessage", "Failed to submit claim. Please try again.");
                doGet(request, response);
            }
        } catch (InvalidInputException | ItemNotFoundException e) {
            request.setAttribute("errorMessage", e.getMessage());
            doGet(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            doGet(request, response);
        }
    }
}
