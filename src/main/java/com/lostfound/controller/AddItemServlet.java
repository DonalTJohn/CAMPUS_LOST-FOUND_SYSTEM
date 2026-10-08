package com.lostfound.controller;

import com.lostfound.exception.InvalidInputException;
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
 * Controller handling reporting of Lost or Found items.
 * Uses @MultipartConfig for secure image file upload.
 */
@WebServlet("/item/add")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
    maxFileSize = 1024 * 1024 * 5,      // 5 MB
    maxRequestSize = 1024 * 1024 * 10   // 10 MB
)
public class AddItemServlet extends HttpServlet {

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

        String type = request.getParameter("type");
        List<Category> categories = itemService.getAllCategories();
        request.setAttribute("categories", categories);

        if ("FOUND".equalsIgnoreCase(type)) {
            request.getRequestDispatcher("/report-found.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/report-lost.jsp").forward(request, response);
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

        String itemName = request.getParameter("itemName");
        String categoryIdStr = request.getParameter("categoryId");
        String itemType = request.getParameter("itemType");
        String description = request.getParameter("description");
        String location = request.getParameter("location");
        String incidentDateStr = request.getParameter("incidentDate");

        int categoryId = 0;
        try {
            if (categoryIdStr != null) categoryId = Integer.parseInt(categoryIdStr);
        } catch (NumberFormatException ignored) {}

        Date incidentDate = null;
        try {
            if (incidentDateStr != null && !incidentDateStr.trim().isEmpty()) {
                incidentDate = Date.valueOf(incidentDateStr.trim());
            }
        } catch (IllegalArgumentException ignored) {}

        // Handle Image Upload
        String imagePath = null;
        try {
            Part filePart = request.getPart("imageFile");
            if (filePart != null && filePart.getSize() > 0) {
                String submittedFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                String fileExtension = "";
                int dotIndex = submittedFileName.lastIndexOf('.');
                if (dotIndex > 0) {
                    fileExtension = submittedFileName.substring(dotIndex).toLowerCase();
                }

                if (!fileExtension.equals(".jpg") && !fileExtension.equals(".jpeg") && !fileExtension.equals(".png")) {
                    throw new InvalidInputException("Only JPG, JPEG, and PNG image formats are allowed.");
                }

                // Resolve real upload directory path
                String uploadDir = getServletContext().getRealPath("/uploads");
                File uploadDirFile = new File(uploadDir);
                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs();
                }

                String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
                String fullSavePath = uploadDir + File.separator + uniqueFileName;
                filePart.write(fullSavePath);

                imagePath = "uploads/" + uniqueFileName;
            }
        } catch (InvalidInputException e) {
            forwardWithError(request, response, itemType, e.getMessage());
            return;
        } catch (Exception e) {
            forwardWithError(request, response, itemType, "Image upload failed: " + e.getMessage());
            return;
        }

        try {
            Item item = new Item(
                user.getUserId(),
                categoryId,
                itemName,
                itemType,
                description,
                location,
                incidentDate,
                imagePath,
                "ACTIVE"
            );

            boolean success = itemService.reportItem(item);
            if (success) {
                response.sendRedirect(request.getContextPath() + "/item/details?id=" + item.getItemId() + "&created=true");
            } else {
                forwardWithError(request, response, itemType, "Failed to submit item report. Please try again.");
            }
        } catch (InvalidInputException e) {
            forwardWithError(request, response, itemType, e.getMessage());
        } catch (Exception e) {
            forwardWithError(request, response, itemType, "An unexpected error occurred: " + e.getMessage());
        }
    }

    private void forwardWithError(HttpServletRequest request, HttpServletResponse response, String itemType, String error) 
            throws ServletException, IOException {
        request.setAttribute("errorMessage", error);
        request.setAttribute("categories", itemService.getAllCategories());
        if ("FOUND".equalsIgnoreCase(itemType)) {
            request.getRequestDispatcher("/report-found.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/report-lost.jsp").forward(request, response);
        }
    }
}
