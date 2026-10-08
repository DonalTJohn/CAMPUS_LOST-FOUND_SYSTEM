<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    if (session == null || session.getAttribute("loggedUser") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Item Report | Campus Lost &amp; Found Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/includes/navbar.jsp" />

    <main class="main-content">
        <div class="container">
            
            <div class="form-box">
                <div style="margin-bottom: 2rem;">
                    <h2>Edit Item Report</h2>
                    <p style="color: var(--text-muted); font-size: 0.9rem;">Update information or status for ${item.itemName}.</p>
                </div>

                <jsp:include page="/includes/alerts.jsp" />

                <form action="${pageContext.request.contextPath}/item/edit" method="POST" enctype="multipart/form-data" class="needs-validation" novalidate>
                    <input type="hidden" name="itemId" value="${item.itemId}">

                    <div class="form-group">
                        <label for="itemName" class="form-label">Item Title</label>
                        <input type="text" id="itemName" name="itemName" class="form-control" value="${item.itemName}" required>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="categoryId" class="form-label">Category</label>
                            <select id="categoryId" name="categoryId" class="form-control" required>
                                <c:forEach var="cat" items="${categories}">
                                    <option value="${cat.categoryId}" ${item.categoryId == cat.categoryId ? 'selected' : ''}>
                                        ${cat.categoryName}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="incidentDate" class="form-label">Incident Date</label>
                            <input type="date" id="incidentDate" name="incidentDate" class="form-control" value="${item.incidentDate}" required>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="location" class="form-label">Location</label>
                            <input type="text" id="location" name="location" class="form-control" value="${item.location}" required>
                        </div>

                        <div class="form-group">
                            <label for="status" class="form-label">Status</label>
                            <select id="status" name="status" class="form-control">
                                <option value="ACTIVE" ${item.status == 'ACTIVE' ? 'selected' : ''}>ACTIVE</option>
                                <option value="CLAIMED" ${item.status == 'CLAIMED' ? 'selected' : ''}>CLAIMED</option>
                                <option value="RECOVERED" ${item.status == 'RECOVERED' ? 'selected' : ''}>RECOVERED</option>
                                <option value="REJECTED" ${item.status == 'REJECTED' ? 'selected' : ''}>REJECTED</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="description" class="form-label">Item Description</label>
                        <textarea id="description" name="description" rows="4" class="form-control" required>${item.description}</textarea>
                    </div>

                    <div class="form-group">
                        <label for="imageFile" class="form-label">Replace Photo (Optional)</label>
                        <input type="file" id="imageFile" name="imageFile" class="form-control" accept="image/jpeg,image/png,image/jpg">
                    </div>

                    <div style="margin-top: 2rem; display: flex; gap: 1rem;">
                        <button type="submit" class="btn btn-primary" style="flex: 1;">Save Changes</button>
                        <a href="${pageContext.request.contextPath}/item/details?id=${item.itemId}" class="btn btn-secondary">Cancel</a>
                    </div>
                </form>
            </div>

        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />

    <script src="${pageContext.request.contextPath}/js/validation.js"></script>
</body>
</html>
