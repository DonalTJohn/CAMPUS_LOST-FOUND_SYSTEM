package com.lostfound.controller;

import com.lostfound.model.Claim;
import com.lostfound.model.Item;
import com.lostfound.model.User;
import com.lostfound.service.ClaimOperations;
import com.lostfound.service.ClaimService;
import com.lostfound.service.ItemOperations;
import com.lostfound.service.ItemService;
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
import java.util.Map;

/**
 * Controller for Administrator Dashboard.
 * Enforces role-based security: only users with role='ADMIN' can access.
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ItemOperations itemService = new ItemService();
    private final ClaimOperations claimService = new ClaimService();
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
            response.sendRedirect(request.getContextPath() + "/dashboard?error=UnauthorizedAccess");
            return;
        }

        Map<String, Integer> stats = itemService.getDashboardStatistics();
        int totalUsers = userService.getTotalUsers();
        stats.put("totalUsers", totalUsers);

        List<Item> recentItems = itemService.getAllItems();
        if (recentItems.size() > 8) {
            recentItems = recentItems.subList(0, 8);
        }

        List<Claim> allClaims = claimService.getAllClaims();
        long pendingClaimsCount = allClaims.stream().filter(Claim::isPending).count();
        stats.put("pendingClaims", (int) pendingClaimsCount);

        request.setAttribute("stats", stats);
        request.setAttribute("recentItems", recentItems);
        request.setAttribute("recentClaims", allClaims.size() > 5 ? allClaims.subList(0, 5) : allClaims);

        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }
}
