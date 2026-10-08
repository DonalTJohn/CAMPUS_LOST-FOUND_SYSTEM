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
    <title>Admin Portal Dashboard | Campus Lost &amp; Found</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/includes/navbar.jsp" />

    <main class="main-content">
        <div class="container">
            
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem; flex-wrap: wrap; gap: 1rem;">
                <div>
                    <span class="status-pill status-active" style="margin-bottom: 0.5rem;">Administrator Control</span>
                    <h2>Campus Administration Hub</h2>
                    <p style="color: var(--text-muted);">Manage campus reports, moderate users, and review pending ownership claims.</p>
                </div>
                <div style="display: flex; gap: 0.75rem;">
                    <a href="${pageContext.request.contextPath}/admin/claims" class="btn btn-primary btn-sm">Review Pending Claims</a>
                    <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-secondary btn-sm">Manage Users</a>
                </div>
            </div>

            <jsp:include page="/includes/alerts.jsp" />

            <!-- Stats Grid -->
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-icon primary">📦</div>
                    <div class="stat-info">
                        <h3>${stats.totalItems != null ? stats.totalItems : 0}</h3>
                        <p>Total Items</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon warning">⏳</div>
                    <div class="stat-info">
                        <h3>${stats.pendingClaims != null ? stats.pendingClaims : 0}</h3>
                        <p>Pending Claims</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon danger">❓</div>
                    <div class="stat-info">
                        <h3>${stats.totalLost != null ? stats.totalLost : 0}</h3>
                        <p>Lost Items</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon success">🎁</div>
                    <div class="stat-info">
                        <h3>${stats.totalFound != null ? stats.totalFound : 0}</h3>
                        <p>Found Items</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon info">👥</div>
                    <div class="stat-info">
                        <h3>${stats.totalUsers != null ? stats.totalUsers : 0}</h3>
                        <p>Registered Users</p>
                    </div>
                </div>
            </div>

            <!-- Recent Claims For Verification -->
            <div style="margin-bottom: 2.5rem;">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem;">
                    <h3>Claims Awaiting Admin Action</h3>
                    <a href="${pageContext.request.contextPath}/admin/claims" style="font-weight: 700; font-size: 0.9rem;">View All Claims →</a>
                </div>

                <div class="table-container">
                    <c:choose>
                        <c:when test="${empty recentClaims}">
                            <div style="padding: 2.5rem; text-align: center; color: var(--text-muted);">
                                No claims recorded currently.
                            </div>
                        </c:when>
                        <c:otherwise>
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>Claim ID</th>
                                        <th>Item Name</th>
                                        <th>Claimant Name</th>
                                        <th>Contact Email</th>
                                        <th>Status</th>
                                        <th>Submission Date</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="claim" items="${recentClaims}">
                                        <tr>
                                            <td>#${claim.claimId}</td>
                                            <td><strong>${claim.itemName}</strong></td>
                                            <td>${claim.claimantName}</td>
                                            <td>${claim.claimantEmail}</td>
                                            <td>
                                                <span class="status-pill ${claim.claimStatus == 'APPROVED' ? 'status-recovered' : (claim.claimStatus == 'REJECTED' ? 'status-rejected' : 'status-pending')}">
                                                    ${claim.claimStatus}
                                                </span>
                                            </td>
                                            <td><small>${claim.claimDate}</small></td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/admin/claim/verify?id=${claim.claimId}" class="btn btn-primary btn-sm">
                                                    Verify / Review
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Moderation Shortcuts -->
            <div style="display: flex; gap: 1.5rem; flex-wrap: wrap;">
                <div style="flex: 1; min-width: 280px; background: white; padding: 1.5rem; border-radius: var(--radius-md); border: 1px solid var(--border-color);">
                    <h4 style="margin-bottom: 0.5rem;">📋 Item Moderation</h4>
                    <p style="color: var(--text-muted); font-size: 0.85rem; margin-bottom: 1rem;">View all reported items, update statuses to Recovered/Claimed, or delete fake reports.</p>
                    <a href="${pageContext.request.contextPath}/admin/items" class="btn btn-secondary btn-sm">Manage Items →</a>
                </div>

                <div style="flex: 1; min-width: 280px; background: white; padding: 1.5rem; border-radius: var(--radius-md); border: 1px solid var(--border-color);">
                    <h4 style="margin-bottom: 0.5rem;">👥 User Management</h4>
                    <p style="color: var(--text-muted); font-size: 0.85rem; margin-bottom: 1rem;">Review registered students/staff, block suspicious users, and check account statuses.</p>
                    <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-secondary btn-sm">Manage Users →</a>
                </div>
            </div>

        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />

</body>
</html>
