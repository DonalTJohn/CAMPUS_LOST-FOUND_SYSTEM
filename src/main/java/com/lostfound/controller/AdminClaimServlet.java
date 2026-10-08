package com.lostfound.controller;

import com.lostfound.exception.InvalidInputException;
import com.lostfound.model.Claim;
import com.lostfound.model.ClaimVerification;
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
 * Controller for Admin Claim management and verification reviews.
 */
@WebServlet(urlPatterns = {"/admin/claims", "/admin/claim/verify"})
public class AdminClaimServlet extends HttpServlet {

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
        if (!user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        String path = request.getServletPath();

        if ("/admin/claim/verify".equals(path)) {
            String claimIdStr = request.getParameter("id");
            try {
                int claimId = Integer.parseInt(claimIdStr);
                Claim claim = claimService.getClaimById(claimId);
                ClaimVerification verification = claimService.getVerification(claimId);

                request.setAttribute("claim", claim);
                request.setAttribute("verification", verification);
                request.getRequestDispatcher("/admin/verify-claim.jsp").forward(request, response);
                return;
            } catch (Exception e) {
                response.sendRedirect(request.getContextPath() + "/admin/claims?error=" + e.getMessage());
                return;
            }
        }

        // List all claims
        List<Claim> claims = claimService.getAllClaims();
        request.setAttribute("claims", claims);
        request.getRequestDispatcher("/admin/claims.jsp").forward(request, response);
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

        request.setCharacterEncoding("UTF-8");
        String claimIdStr = request.getParameter("claimId");
        String remarks = request.getParameter("remarks");
        String decision = request.getParameter("decision"); // APPROVED or REJECTED

        try {
            int claimId = Integer.parseInt(claimIdStr);
            boolean verified = claimService.verifyClaim(claimId, admin.getUserId(), remarks, decision);

            if (verified) {
                response.sendRedirect(request.getContextPath() + "/admin/claims?verified=true");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/claim/verify?id=" + claimId + "&error=VerificationFailed");
            }
        } catch (InvalidInputException e) {
            response.sendRedirect(request.getContextPath() + "/admin/claim/verify?id=" + claimIdStr + "&error=" + e.getMessage());
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/claims?error=" + e.getMessage());
        }
    }
}
