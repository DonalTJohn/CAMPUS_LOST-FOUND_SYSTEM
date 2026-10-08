<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    if (session == null || session.getAttribute("loggedUser") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    com.lostfound.model.User user = (com.lostfound.model.User) session.getAttribute("loggedUser");
    if (!user.isAdmin()) {
        response.sendRedirect(request.getContextPath() + "/dashboard");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>All Campus Items | Admin Control</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/includes/navbar.jsp" />

    <main class="main-content">
        <div class="container">
            
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem; flex-wrap: wrap; gap: 1rem;">
                <div>
                    <h2>Campus Items Moderation</h2>
                    <p style="color: var(--text-muted);">Manage, approve, change statuses, and moderate all campus item reports.</p>
                </div>
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-secondary btn-sm">← Admin Dashboard</a>
            </div>

            <jsp:include page="/includes/alerts.jsp" />

            <!-- Filters -->
            <section class="search-widget" style="padding: 1.25rem;">
                <form action="${pageContext.request.contextPath}/admin/items" method="GET" style="display: flex; gap: 1rem; flex-wrap: wrap; align-items: center;">
                    <div style="flex: 1; min-width: 200px;">
                        <input type="text" name="keyword" class="form-control" value="${keyword != null ? keyword : ''}" placeholder="Filter by title / keyword...">
                    </div>
                    <div>
                        <select name="type" class="form-control">
                            <option value="">All Types</option>
                            <option value="LOST" ${filterType == 'LOST' ? 'selected' : ''}>LOST</option>
                            <option value="FOUND" ${filterType == 'FOUND' ? 'selected' : ''}>FOUND</option>
                        </select>
                    </div>
                    <div>
                        <select name="status" class="form-control">
                            <option value="">All Statuses</option>
                            <option value="ACTIVE" ${filterStatus == 'ACTIVE' ? 'selected' : ''}>ACTIVE</option>
                            <option value="CLAIMED" ${filterStatus == 'CLAIMED' ? 'selected' : ''}>CLAIMED</option>
                            <option value="RECOVERED" ${filterStatus == 'RECOVERED' ? 'selected' : ''}>RECOVERED</option>
                            <option value="REJECTED" ${filterStatus == 'REJECTED' ? 'selected' : ''}>REJECTED</option>
                        </select>
                    </div>
                    <div>
                        <button type="submit" class="btn btn-primary btn-sm">Filter</button>
                        <a href="${pageContext.request.contextPath}/admin/items" class="btn btn-secondary btn-sm">Reset</a>
                    </div>
                </form>
            </section>

            <!-- Table of Items -->
            <div class="table-container">
                <table class="table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Item Title</th>
                            <th>Category</th>
                            <th>Type</th>
                            <th>Reporter</th>
                            <th>Location</th>
                            <th>Date</th>
                            <th>Current Status</th>
                            <th>Change Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${items}">
                            <tr>
                                <td>#${item.itemId}</td>
                                <td>
                                    <strong>${item.itemName}</strong>
                                </td>
                                <td>${item.categoryName}</td>
                                <td>
                                    <span class="status-pill ${item.itemType == 'LOST' ? 'status-rejected' : 'status-recovered'}">
                                        ${item.itemType}
                                    </span>
                                </td>
                                <td>
                                    ${item.reporterName}<br>
                                    <small style="color: var(--text-muted);">${item.reporterPhone}</small>
                                </td>
                                <td>${item.location}</td>
                                <td><small>${item.incidentDate}</small></td>
                                <td>
                                    <span class="status-pill status-active">${item.status}</span>
                                </td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/admin/item/status" method="POST" style="display: flex; gap: 0.25rem;">
                                        <input type="hidden" name="action" value="updateStatus">
                                        <input type="hidden" name="itemId" value="${item.itemId}">
                                        <select name="status" class="form-control" style="padding: 0.25rem 0.5rem; font-size: 0.8rem; width: auto;">
                                            <option value="ACTIVE" ${item.status == 'ACTIVE' ? 'selected' : ''}>ACTIVE</option>
                                            <option value="CLAIMED" ${item.status == 'CLAIMED' ? 'selected' : ''}>CLAIMED</option>
                                            <option value="RECOVERED" ${item.status == 'RECOVERED' ? 'selected' : ''}>RECOVERED</option>
                                            <option value="REJECTED" ${item.status == 'REJECTED' ? 'selected' : ''}>REJECTED</option>
                                        </select>
                                        <button type="submit" class="btn btn-secondary btn-sm" style="padding: 0.25rem 0.5rem; font-size: 0.75rem;">Set</button>
                                    </form>
                                </td>
                                <td>
                                    <div style="display: flex; gap: 0.35rem;">
                                        <a href="${pageContext.request.contextPath}/item/details?id=${item.itemId}" class="btn btn-secondary btn-sm" target="_blank">View</a>
                                        <form action="${pageContext.request.contextPath}/item/delete" method="POST" onsubmit="return confirm('Delete this item?');" style="display: inline;">
                                            <input type="hidden" name="id" value="${item.itemId}">
                                            <input type="hidden" name="redirect" value="admin">
                                            <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />

</body>
</html>
