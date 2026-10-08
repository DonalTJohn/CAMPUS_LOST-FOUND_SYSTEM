<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${item.itemName} | Campus Lost &amp; Found Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/includes/navbar.jsp" />

    <main class="main-content">
        <div class="container">
            
            <div style="margin-bottom: 1.5rem;">
                <a href="${pageContext.request.contextPath}/items" style="font-weight: 700;">← Back to Items Directory</a>
            </div>

            <jsp:include page="/includes/alerts.jsp" />

            <!-- Main Item Details Box -->
            <div style="background: white; border-radius: var(--radius-lg); border: 1px solid var(--border-color); overflow: hidden; box-shadow: var(--shadow-md); display: grid; grid-template-columns: 1fr 1.2fr; gap: 0;">
                
                <!-- Image Side -->
                <div style="background: #f1f5f9; display: flex; align-items: center; justify-content: center; min-height: 380px; position: relative; overflow: hidden;">
                    <span class="card-type-badge ${item.itemType == 'LOST' ? 'badge-lost' : 'badge-found'}" style="top: 1.5rem; left: 1.5rem;">
                        ${item.itemType}
                    </span>
                    <c:choose>
                        <c:when test="${not empty item.imagePath}">
                            <img src="${pageContext.request.contextPath}/${item.imagePath}" alt="${item.itemName}" style="width: 100%; height: 100%; object-fit: cover;">
                        </c:when>
                        <c:otherwise>
                            <div style="text-align: center; color: #94a3b8;">
                                <div style="font-size: 5rem; margin-bottom: 0.5rem;">${item.itemType == 'LOST' ? '❓' : '🎁'}</div>
                                <p>No Photo Provided</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Info Side -->
                <div style="padding: 2.5rem; display: flex; flex-direction: column;">
                    <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 1rem;">
                        <span class="item-category-tag" style="font-size: 0.85rem;">${item.categoryName}</span>
                        <span class="status-pill status-active">${item.status}</span>
                    </div>

                    <h1 style="font-size: 2rem; margin-bottom: 1rem;">${item.itemName}</h1>

                    <p style="color: var(--text-muted); line-height: 1.7; margin-bottom: 1.5rem; font-size: 1rem;">
                        ${item.description}
                    </p>

                    <div style="background: #f8fafc; border-radius: var(--radius-md); padding: 1.25rem; margin-bottom: 1.75rem; border: 1px solid var(--border-color);">
                        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; font-size: 0.9rem;">
                            <div>
                                <span style="color: var(--text-muted);">📍 Location:</span><br>
                                <strong>${item.location}</strong>
                            </div>
                            <div>
                                <span style="color: var(--text-muted);">📅 Incident Date:</span><br>
                                <strong>${item.incidentDate}</strong>
                            </div>
                            <div>
                                <span style="color: var(--text-muted);">👤 Reported By:</span><br>
                                <strong>${item.reporterName}</strong>
                            </div>
                            <div>
                                <span style="color: var(--text-muted);">🕒 Posted At:</span><br>
                                <strong>${item.createdAt}</strong>
                            </div>
                        </div>
                    </div>

                    <!-- Action Buttons -->
                    <div style="margin-top: auto; display: flex; gap: 1rem; flex-wrap: wrap;">
                        <c:if test="${item.itemType == 'FOUND' && item.status == 'ACTIVE'}">
                            <c:choose>
                                <c:when test="${not empty sessionScope.loggedUser && sessionScope.loggedUser.userId != item.userId}">
                                    <a href="${pageContext.request.contextPath}/claim/submit?itemId=${item.itemId}" class="btn btn-primary btn-lg">
                                        🏷️ Submit Ownership Claim
                                    </a>
                                </c:when>
                                <c:when test="${empty sessionScope.loggedUser}">
                                    <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-primary btn-lg">
                                        Log In to Claim This Item
                                    </a>
                                </c:when>
                            </c:choose>
                        </c:if>

                        <c:if test="${isOwner || isAdmin}">
                            <a href="${pageContext.request.contextPath}/item/edit?id=${item.itemId}" class="btn btn-secondary">
                                ✏️ Edit
                            </a>
                            <form action="${pageContext.request.contextPath}/item/delete" method="POST" onsubmit="return confirm('Are you sure you want to delete this item report?');" style="display: inline;">
                                <input type="hidden" name="id" value="${item.itemId}">
                                <input type="hidden" name="redirect" value="${isAdmin ? 'admin' : 'my'}">
                                <button type="submit" class="btn btn-danger">🗑️ Delete</button>
                            </form>
                        </c:if>
                    </div>

                </div>
            </div>

            <!-- Potential Matches Section -->
            <c:if test="${not empty potentialMatches}">
                <div class="potential-matches-box">
                    <div class="match-header">
                        <div class="match-icon">🎯</div>
                        <div>
                            <h3 style="color: #065f46; font-size: 1.35rem;">Potential Match Found!</h3>
                            <p style="color: #047857; font-size: 0.9rem;">
                                Our matching algorithm found corresponding ${item.itemType == 'LOST' ? 'FOUND' : 'LOST'} items with similar keywords, category, location, or date.
                            </p>
                        </div>
                    </div>

                    <div class="cards-grid">
                        <c:forEach var="match" items="${potentialMatches}">
                            <div class="item-card" style="background: white;">
                                <div class="item-img-container">
                                    <span class="card-type-badge ${match.itemType == 'LOST' ? 'badge-lost' : 'badge-found'}">
                                        ${match.itemType}
                                    </span>
                                    <c:choose>
                                        <c:when test="${not empty match.imagePath}">
                                            <img src="${pageContext.request.contextPath}/${match.imagePath}" alt="${match.itemName}" class="item-img">
                                        </c:when>
                                        <c:otherwise>
                                            <div class="item-img-placeholder">${match.itemType == 'LOST' ? '❓' : '🎁'}</div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="item-card-body">
                                    <span class="item-category-tag">${match.categoryName}</span>
                                    <h4 class="item-card-title">${match.itemName}</h4>
                                    <p class="item-card-desc">${match.description}</p>
                                    <div class="item-meta-list">
                                        <div class="item-meta-item">
                                            <span>📍</span>
                                            <span>${match.location}</span>
                                        </div>
                                        <div class="item-meta-item">
                                            <span>📅</span>
                                            <span>${match.incidentDate}</span>
                                        </div>
                                    </div>
                                </div>
                                <div class="item-card-footer">
                                    <a href="${pageContext.request.contextPath}/item/details?id=${match.itemId}" class="btn btn-primary btn-sm">
                                        View Match →
                                    </a>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </c:if>

        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />

</body>
</html>
