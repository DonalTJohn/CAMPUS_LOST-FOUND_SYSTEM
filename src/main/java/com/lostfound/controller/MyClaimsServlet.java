package com.lostfound.controller;

import com.lostfound.model.Claim;
import com.lostfound.model.User;
import com.lostfound.service.ClaimOperations;
import com.lostfound.service.ClaimService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Controller for viewing current user's submitted claims.
 */
@WebServlet("/claims/my")
public class MyClaimsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ClaimOperations claimService = new ClaimService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("loggedUser");
        List<Claim> myClaims = claimService.getClaimsByUser(user.getUserId());

        request.setAttribute("claims", myClaims);
        if (request.getParameter("submitted") != null) {
            request.setAttribute("successMessage", "Your claim was submitted successfully and is pending administrator review.");
        }
        request.getRequestDispatcher("/my-claims.jsp").forward(request, response);
    }
}
