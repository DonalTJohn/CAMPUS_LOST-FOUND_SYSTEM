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
    <title>Manage Users | Campus Lost &amp; Found Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/includes/navbar.jsp" />

    <main class="main-content">
        <div class="container">
            
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem; flex-wrap: wrap; gap: 1rem;">
                <div>
                    <h2>Campus User Directory</h2>
                    <p style="color: var(--text-muted);">Manage student &amp; staff permissions, active status, and account security.</p>
                </div>
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-secondary btn-sm">← Admin Dashboard</a>
            </div>

            <jsp:include page="/includes/alerts.jsp" />

            <div class="table-container">
                <table class="table">
                    <thead>
                        <tr>
                            <th>User ID</th>
                            <th>Name</th>
                            <th>Username</th>
                            <th>Email</th>
                            <th>Phone</th>
                            <th>Role</th>
                            <th>Status</th>
                            <th>Joined Date</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="u" items="${users}">
                            <tr>
                                <td>#${u.userId}</td>
                                <td><strong>${u.name}</strong></td>
                                <td><code>${u.username}</code></td>
                                <td>${u.email}</td>
                                <td>${u.phone}</td>
                                <td>
                                    <span class="status-pill ${u.role == 'ADMIN' ? 'status-active' : 'status-recovered'}">
                                        ${u.role}
                                    </span>
                                </td>
                                <td>
                                    <span class="status-pill ${u.status == 'ACTIVE' ? 'status-recovered' : 'status-blocked'}">
                                        ${u.status}
                                    </span>
                                </td>
                                <td><small>${u.createdAt}</small></td>
                                <td>
                                    <c:if test="${u.userId != sessionScope.loggedUser.userId}">
                                        <form action="${pageContext.request.contextPath}/admin/user/status" method="POST" style="display:inline;">
                                            <input type="hidden" name="userId" value="${u.userId}">
                                            <c:choose>
                                                <c:when test="${u.status == 'ACTIVE'}">
                                                    <input type="hidden" name="status" value="BLOCKED">
                                                    <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Block user ${u.name}?');">
                                                        Block
                                                    </button>
                                                </c:when>
                                                <c:otherwise>
                                                    <input type="hidden" name="status" value="ACTIVE">
                                                    <button type="submit" class="btn btn-success btn-sm">
                                                        Unblock
                                                    </button>
                                                </c:otherwise>
                                            </c:choose>
                                        </form>
                                    </c:if>
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
