<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="com.lostfound.service.ItemService, com.lostfound.service.ItemOperations, com.lostfound.model.Category, java.util.List" %>
<%
    if (session == null || session.getAttribute("loggedUser") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    if (request.getAttribute("categories") == null) {
        ItemOperations itemService = new ItemService();
        List<Category> categories = itemService.getAllCategories();
        request.setAttribute("categories", categories);
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Report Found Item | Campus Lost &amp; Found Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/includes/navbar.jsp" />

    <main class="main-content">
        <div class="container">
            
            <div class="form-box">
                <div style="margin-bottom: 2rem;">
                    <span class="status-pill status-recovered" style="margin-bottom: 0.5rem;">Found Item Report</span>
                    <h2>Report a Found Item</h2>
                    <p style="color: var(--text-muted); font-size: 0.9rem;">
                        Thank you for helping fellow students and campus members recover their property.
                    </p>
                </div>

                <jsp:include page="/includes/alerts.jsp" />

                <form action="${pageContext.request.contextPath}/item/add" method="POST" enctype="multipart/form-data" class="needs-validation" novalidate>
                    <input type="hidden" name="itemType" value="FOUND">

                    <div class="form-group">
                        <label for="itemName" class="form-label">Item Title</label>
                        <input type="text" id="itemName" name="itemName" class="form-control" 
                               placeholder="e.g. Black Fossil Leather Wallet, Student ID Card" required>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="categoryId" class="form-label">Category</label>
                            <select id="categoryId" name="categoryId" class="form-control" required>
                                <option value="">-- Select Category --</option>
                                <c:forEach var="cat" items="${categories}">
                                    <option value="${cat.categoryId}">${cat.categoryName}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="incidentDate" class="form-label">Date Found</label>
                            <input type="date" id="incidentDate" name="incidentDate" class="form-control" required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="location" class="form-label">Where Was It Found?</label>
                        <input type="text" id="location" name="location" class="form-control" 
                               placeholder="e.g. Science Block Lab 304 bench" required>
                    </div>

                    <div class="form-group">
                        <label for="description" class="form-label">Item Description</label>
                        <textarea id="description" name="description" rows="4" class="form-control" 
                                  placeholder="Describe the item general appearance (avoid mentioning private sensitive details like security PINs/exact card numbers so the true owner can claim it by proof)..." required></textarea>
                    </div>

                    <div class="form-group">
                        <label for="imageFile" class="form-label">Upload Photo (Recommended)</label>
                        <input type="file" id="imageFile" name="imageFile" class="form-control" accept="image/jpeg,image/png,image/jpg">
                        <small style="color: var(--text-muted); font-size: 0.8rem;">Supported formats: JPG, JPEG, PNG (Max 5MB)</small>
                    </div>

                    <div style="margin-top: 2rem; display: flex; gap: 1rem;">
                        <button type="submit" class="btn btn-success" style="flex: 1;">Publish Found Item Listing</button>
                        <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary">Cancel</a>
                    </div>
                </form>
            </div>

        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />

    <script src="${pageContext.request.contextPath}/js/validation.js"></script>
</body>
</html>
