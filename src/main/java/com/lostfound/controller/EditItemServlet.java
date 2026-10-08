package com.lostfound.controller;

import com.lostfound.exception.InvalidInputException;
import com.lostfound.exception.ItemNotFoundException;
import com.lostfound.model.Category;
import com.lostfound.model.Item;
import com.lostfound.model.User;
import com.lostfound.service.ItemOperations;
import com.lostfound.service.ItemService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

/**
 * Controller for editing reported items.
 */
@WebServlet("/item/edit")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1,
    maxFileSize = 1024 * 1024 * 5,
    maxRequestSize = 1024 * 1024 * 10
)
public class EditItemServlet extends HttpServlet {

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
        String idStr = request.getParameter("id");

        try {
            int itemId = Integer.parseInt(idStr);
            Item item = itemService.getItemById(itemId);

            if (!user.isAdmin() && item.getUserId() != user.getUserId()) {
                request.setAttribute("errorMessage", "You do not have permission to edit this item.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }

            List<Category> categories = itemService.getAllCategories();
            request.setAttribute("item", item);
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/edit-item.jsp").forward(request, response);

        } catch (Exception e) {
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
        String itemName = request.getParameter("itemName");
        String categoryIdStr = request.getParameter("categoryId");
        String description = request.getParameter("description");
        String location = request.getParameter("location");
        String incidentDateStr = request.getParameter("incidentDate");
        String status = request.getParameter("status");

        try {
            int itemId = Integer.parseInt(itemIdStr);
            Item item = itemService.getItemById(itemId);

            int categoryId = Integer.parseInt(categoryIdStr);
            Date incidentDate = Date.valueOf(incidentDateStr);

            item.setItemName(itemName);
            item.setCategoryId(categoryId);
            item.setDescription(description);
            item.setLocation(location);
            item.setIncidentDate(incidentDate);
            if (status != null && !status.isEmpty()) {
                item.setStatus(status);
            }

            // Handle optional new image replacement
            Part filePart = request.getPart("imageFile");
            if (filePart != null && filePart.getSize() > 0) {
                String submittedFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                String ext = submittedFileName.substring(submittedFileName.lastIndexOf('.')).toLowerCase();
                if (ext.equals(".jpg") || ext.equals(".jpeg") || ext.equals(".png")) {
                    String uploadDir = getServletContext().getRealPath("/uploads");
                    File uploadDirFile = new File(uploadDir);
                    if (!uploadDirFile.exists()) uploadDirFile.mkdirs();
                    String uniqueFileName = UUID.randomUUID().toString() + ext;
                    filePart.write(uploadDir + File.separator + uniqueFileName);
                    item.setImagePath("uploads/" + uniqueFileName);
                }
            }

            itemService.updateItem(item, user.getUserId(), user.isAdmin());
            response.sendRedirect(request.getContextPath() + "/item/details?id=" + itemId);

        } catch (InvalidInputException | ItemNotFoundException e) {
            request.setAttribute("errorMessage", e.getMessage());
            doGet(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error updating item: " + e.getMessage());
            doGet(request, response);
        }
    }
}
