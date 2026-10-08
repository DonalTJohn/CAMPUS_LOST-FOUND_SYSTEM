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
    <title>My Ownership Claims | Campus Lost &amp; Found Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/includes/navbar.jsp" />

    <main class="main-content">
        <div class="container">
            
            <div style="margin-bottom: 2rem;">
                <h2>My Submitted Claims</h2>
                <p style="color: var(--text-muted);">Track the approval and verification status of your item claims.</p>
            </div>

            <jsp:include page="/includes/alerts.jsp" />

            <div class="table-container">
                <c:choose>
                    <c:when test="${empty claims}">
                        <div style="padding: 4rem 2rem; text-align: center; color: var(--text-muted);">
                            <div style="font-size: 3rem; margin-bottom: 1rem;">🏷️</div>
                            <h3>No Claims Submitted</h3>
                            <p style="margin-bottom: 1.5rem;">You haven't made any claims on found items.</p>
                            <a href="${pageContext.request.contextPath}/items?type=FOUND" class="btn btn-primary">Browse Found Items</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Item Name</th>
                                    <th>Location</th>
                                    <th>My Proof Description</th>
                                    <th>Status</th>
                                    <th>Submitted Date</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="claim" items="${claims}">
                                    <tr>
                                        <td>
                                            <strong>${claim.itemName}</strong>
                                        </td>
                                        <td>${claim.itemLocation}</td>
                                        <td style="max-width: 300px;">
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
                                            <a href="${pageContext.request.contextPath}/item/details?id=${claim.itemId}" class="btn btn-secondary btn-sm">
                                                Item Info
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
