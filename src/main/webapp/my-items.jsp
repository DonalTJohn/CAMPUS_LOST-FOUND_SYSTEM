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
    <title>My Reported Items | Campus Lost &amp; Found Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/includes/navbar.jsp" />

    <main class="main-content">
        <div class="container">
            
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem; flex-wrap: wrap; gap: 1rem;">
                <div>
                    <h2>My Reported Items</h2>
                    <p style="color: var(--text-muted);">Manage all items you have reported as lost or found on campus.</p>
                </div>
                <div style="display: flex; gap: 0.75rem;">
                    <a href="${pageContext.request.contextPath}/item/add?type=LOST" class="btn btn-primary btn-sm">+ Report Lost</a>
                    <a href="${pageContext.request.contextPath}/item/add?type=FOUND" class="btn btn-success btn-sm">+ Report Found</a>
                </div>
            </div>

            <jsp:include page="/includes/alerts.jsp" />

            <div class="table-container">
                <c:choose>
                    <c:when test="${empty items}">
                        <div style="padding: 4rem 2rem; text-align: center; color: var(--text-muted);">
                            <div style="font-size: 3rem; margin-bottom: 1rem;">📝</div>
                            <h3>No Items Reported Yet</h3>
                            <p style="margin-bottom: 1.5rem;">You haven't submitted any lost or found reports.</p>
                            <a href="${pageContext.request.contextPath}/item/add?type=LOST" class="btn btn-primary">Report an Item Now</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Item Details</th>
                                    <th>Category</th>
                                    <th>Type</th>
                                    <th>Location</th>
                                    <th>Incident Date</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${items}">
                                    <tr>
                                        <td>
                                            <strong>${item.itemName}</strong>
                                        </td>
                                        <td>${item.categoryName}</td>
                                        <td>
                                            <span class="status-pill ${item.itemType == 'LOST' ? 'status-rejected' : 'status-recovered'}">
                                                ${item.itemType}
                                            </span>
                                        </td>
                                        <td>${item.location}</td>
                                        <td>${item.incidentDate}</td>
                                        <td>
                                            <span class="status-pill status-active">${item.status}</span>
                                        </td>
                                        <td>
                                            <div style="display: flex; gap: 0.5rem;">
                                                <a href="${pageContext.request.contextPath}/item/details?id=${item.itemId}" class="btn btn-secondary btn-sm">View</a>
                                                <a href="${pageContext.request.contextPath}/item/edit?id=${item.itemId}" class="btn btn-secondary btn-sm">Edit</a>
                                                <form action="${pageContext.request.contextPath}/item/delete" method="POST" onsubmit="return confirm('Delete this report?');" style="display:inline;">
                                                    <input type="hidden" name="id" value="${item.itemId}">
                                                    <input type="hidden" name="redirect" value="my">
                                                    <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                                                </form>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
            </div>

        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />

</body>
</html>
