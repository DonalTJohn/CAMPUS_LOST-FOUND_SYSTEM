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
    <title>Claims Verification Queue | Admin Control</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/includes/navbar.jsp" />

    <main class="main-content">
        <div class="container">
            
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem; flex-wrap: wrap; gap: 1rem;">
                <div>
                    <h2>Submitted Claims &amp; Proof Queue</h2>
                    <p style="color: var(--text-muted);">Review student ownership proofs, add verification remarks, and decide approvals.</p>
                </div>
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-secondary btn-sm">← Admin Dashboard</a>
            </div>

            <jsp:include page="/includes/alerts.jsp" />

            <div class="table-container">
                <c:choose>
                    <c:when test="${empty claims}">
                        <div style="padding: 4rem 2rem; text-align: center; color: var(--text-muted);">
                            <div style="font-size: 3rem; margin-bottom: 1rem;">✅</div>
                            <h3>No Claims Found</h3>
                            <p>All claims have been processed or no claims have been submitted yet.</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Claim ID</th>
                                    <th>Item Name</th>
                                    <th>Claimant</th>
                                    <th>Contact</th>
                                    <th>Proof Excerpt</th>
                                    <th>Status</th>
                                    <th>Submitted Date</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="claim" items="${claims}">
                                    <tr>
                                        <td>#${claim.claimId}</td>
                                        <td>
                                            <strong>${claim.itemName}</strong><br>
                                            <small style="color: var(--text-muted);">${claim.itemLocation}</small>
                                        </td>
                                        <td>${claim.claimantName}</td>
                                        <td>
                                            ${claim.claimantEmail}<br>
                                            <small style="color: var(--text-muted);">${claim.claimantPhone}</small>
                                        </td>
                                        <td style="max-width: 250px;">
                                            <span style="display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; font-size: 0.85rem; color: var(--text-muted);">
                                                ${claim.proofDescription}
                                            </span>
                                        </td>
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
    </main>

    <jsp:include page="/includes/footer.jsp" />

</body>
</html>
