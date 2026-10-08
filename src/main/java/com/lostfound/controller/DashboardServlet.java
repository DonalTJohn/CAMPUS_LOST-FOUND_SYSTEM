package com.lostfound.controller;

import com.lostfound.dao.NotificationDAO;
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
import java.util.List;
import java.util.Map;

/**
 * Controller for User Dashboard.
 */
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ItemOperations itemService = new ItemService();
    private final ClaimOperations claimService = new ClaimService();
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

        Map<String, Integer> stats = itemService.getDashboardStatistics();
        List<Item> myItems = itemService.getItemsByUser(user.getUserId());
        List<Claim> myClaims = claimService.getClaimsByUser(user.getUserId());
        List<Item> recentItems = itemService.getAllItems();
        if (recentItems.size() > 6) {
            recentItems = recentItems.subList(0, 6);
        }
        int unreadNotifs = notificationDAO.getUnreadCount(user.getUserId());

        request.setAttribute("stats", stats);
        request.setAttribute("myItems", myItems);
        request.setAttribute("myClaims", myClaims);
        request.setAttribute("recentItems", recentItems);
        request.setAttribute("unreadNotificationsCount", unreadNotifs);

        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }
}
