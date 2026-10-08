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
    <title>Student Dashboard | Campus Lost &amp; Found Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/includes/navbar.jsp" />

    <main class="main-content">
        <div class="container">
            
            <jsp:include page="/includes/alerts.jsp" />

            <!-- Welcome Header -->
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem; flex-wrap: wrap; gap: 1rem;">
                <div>
                    <h2>Hello, ${sessionScope.loggedUser.name} 👋</h2>
                    <p style="color: var(--text-muted);">Manage your reported lost/found items and track claims.</p>
                </div>
                <div style="display: flex; gap: 0.75rem;">
                    <a href="${pageContext.request.contextPath}/item/add?type=LOST" class="btn btn-primary btn-sm">
                        + Report Lost Item
                    </a>
                    <a href="${pageContext.request.contextPath}/item/add?type=FOUND" class="btn btn-success btn-sm">
                        + Report Found Item
                    </a>
                </div>
            </div>

            <!-- Stats Grid -->
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-icon primary">📋</div>
                    <div class="stat-info">
                        <h3>${myItems != null ? myItems.size() : 0}</h3>
                        <p>My Reported Items</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon warning">🏷️</div>
                    <div class="stat-info">
                        <h3>${myClaims != null ? myClaims.size() : 0}</h3>
                        <p>My Submitted Claims</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon info">🔔</div>
                    <div class="stat-info">
                        <h3>${unreadNotificationsCount != null ? unreadNotificationsCount : 0}</h3>
                        <p>Unread Alerts</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon success">📦</div>
                    <div class="stat-info">
                        <h3>${stats.totalItems != null ? stats.totalItems : 0}</h3>
                        <p>Campus Total Items</p>
                    </div>
                </div>
            </div>

            <!-- Two Column Section: My Recent Items & My Recent Claims -->
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 2rem; margin-bottom: 2.5rem;">
                <!-- My Items Preview -->
                <div class="table-container">
                    <div style="padding: 1.25rem; border-bottom: 1px solid var(--border-color); display: flex; justify-content: space-between; align-items: center;">
                        <h3 style="font-size: 1.15rem;">My Reported Items</h3>
                        <a href="${pageContext.request.contextPath}/my-items" style="font-size: 0.85rem; font-weight: 700;">View All →</a>
                    </div>
                    <c:choose>
                        <c:when test="${empty myItems}">
                            <div style="padding: 2.5rem; text-align: center; color: var(--text-muted);">
                                You haven't reported any items yet.
                            </div>
                        </c:when>
                        <c:otherwise>
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>Item</th>
                                        <th>Type</th>
                                        <th>Status</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${myItems}" end="4">
                                        <tr>
                                            <td>
                                                <strong>${item.itemName}</strong><br>
                                                <small style="color: var(--text-muted);">${item.categoryName}</small>
                                            </td>
                                            <td>
                                                <span class="status-pill ${item.itemType == 'LOST' ? 'status-rejected' : 'status-recovered'}">
                                                    ${item.itemType}
                                                </span>
                                            </td>
                                            <td>
                                                <span class="status-pill status-active">${item.status}</span>
                                            </td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/item/details?id=${item.itemId}" class="btn btn-secondary btn-sm">View</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- My Claims Preview -->
                <div class="table-container">
                    <div style="padding: 1.25rem; border-bottom: 1px solid var(--border-color); display: flex; justify-content: space-between; align-items: center;">
                        <h3 style="font-size: 1.15rem;">My Submitted Claims</h3>
                        <a href="${pageContext.request.contextPath}/claims/my" style="font-size: 0.85rem; font-weight: 700;">View All →</a>
                    </div>
                    <c:choose>
                        <c:when test="${empty myClaims}">
                            <div style="padding: 2.5rem; text-align: center; color: var(--text-muted);">
                                You have not submitted any item claims yet.
                            </div>
                        </c:when>
                        <c:otherwise>
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>Item</th>
                                        <th>Status</th>
                                        <th>Date</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="claim" items="${myClaims}" end="4">
                                        <tr>
                                            <td>
                                                <strong>${claim.itemName}</strong><br>
                                                <small style="color: var(--text-muted);">${claim.itemLocation}</small>
                                            </td>
                                            <td>
                                                <span class="status-pill ${claim.claimStatus == 'APPROVED' ? 'status-recovered' : (claim.claimStatus == 'REJECTED' ? 'status-rejected' : 'status-pending')}">
                                                    ${claim.claimStatus}
                                                </span>
                                            </td>
                                            <td>
                                                <small>${claim.claimDate}</small>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />
</body>
</html>
