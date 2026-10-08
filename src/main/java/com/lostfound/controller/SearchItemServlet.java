package com.lostfound.controller;

import com.google.gson.Gson;
import com.lostfound.model.Category;
import com.lostfound.model.Item;
import com.lostfound.service.ItemOperations;
import com.lostfound.service.ItemService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Controller for searching items.
 * Supports both standard HTML page rendering and dynamic JSON responses for AJAX live search.
 */
@WebServlet("/item/search")
public class SearchItemServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ItemOperations itemService = new ItemService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String keyword = request.getParameter("keyword");
        String categoryIdStr = request.getParameter("categoryId");
        String location = request.getParameter("location");
        String itemType = request.getParameter("itemType");
        String status = request.getParameter("status");
        String dateStr = request.getParameter("date");
        String format = request.getParameter("format"); // "json" or null

        Integer categoryId = null;
        try {
            if (categoryIdStr != null && !categoryIdStr.trim().isEmpty()) {
                categoryId = Integer.parseInt(categoryIdStr.trim());
            }
        } catch (NumberFormatException ignored) {}

        List<Item> searchResults = itemService.searchItems(keyword, categoryId, location, itemType, status, dateStr);

        // Check if AJAX / JSON response is requested
        String acceptHeader = request.getHeader("Accept");
        boolean isJson = "json".equalsIgnoreCase(format) || (acceptHeader != null && acceptHeader.contains("application/json"));

        if (isJson) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(gson.toJson(searchResults));
            out.flush();
        } else {
            List<Category> categories = itemService.getAllCategories();
            request.setAttribute("items", searchResults);
            request.setAttribute("categories", categories);
            request.setAttribute("keyword", keyword);
            request.setAttribute("selectedCategory", categoryId);
            request.setAttribute("location", location);
            request.setAttribute("selectedType", itemType);
            request.getRequestDispatcher("/items.jsp").forward(request, response);
        }
    }
}
