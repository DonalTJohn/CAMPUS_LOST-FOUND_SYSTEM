package com.lostfound.controller;

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
import java.util.List;

/**
 * Controller for browsing item listings with category and type filters.
 */
@WebServlet("/items")
public class ItemListServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ItemOperations itemService = new ItemService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String type = request.getParameter("type"); // LOST, FOUND, or ALL/null
        String categoryIdStr = request.getParameter("category");
        String location = request.getParameter("location");
        String keyword = request.getParameter("keyword");

        Integer categoryId = null;
        try {
            if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
                categoryId = Integer.parseInt(categoryIdStr);
            }
        } catch (NumberFormatException ignored) {}

        List<Item> items;
        if (keyword != null || categoryId != null || location != null || (type != null && !type.isEmpty())) {
            items = itemService.searchItems(keyword, categoryId, location, type, "ACTIVE", null);
        } else {
            items = itemService.getAllItems();
        }

        List<Category> categories = itemService.getAllCategories();

        request.setAttribute("items", items);
        request.setAttribute("categories", categories);
        request.setAttribute("selectedType", type != null ? type : "ALL");
        request.setAttribute("selectedCategory", categoryId);
        request.setAttribute("keyword", keyword);
        request.setAttribute("location", location);

        request.getRequestDispatcher("/items.jsp").forward(request, response);
    }
}
