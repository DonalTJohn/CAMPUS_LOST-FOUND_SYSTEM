<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Browse Campus Items | Campus Lost &amp; Found Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script>
        const contextPath = "${pageContext.request.contextPath}";
    </script>
</head>
<body>

    <jsp:include page="/includes/navbar.jsp" />

    <main class="main-content">
        <div class="container">
            
            <div style="margin-bottom: 2rem; display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 1rem;">
                <div>
                    <h2>Campus Items Directory</h2>
                    <p style="color: var(--text-muted);">Explore all active lost and found items on campus.</p>
                </div>
                <div style="display: flex; gap: 0.75rem;">
                    <a href="${pageContext.request.contextPath}/item/add?type=LOST" class="btn btn-primary btn-sm">+ Report Lost</a>
                    <a href="${pageContext.request.contextPath}/item/add?type=FOUND" class="btn btn-success btn-sm">+ Report Found</a>
                </div>
            </div>

            <jsp:include page="/includes/alerts.jsp" />

            <!-- Filter Section -->
            <section class="search-widget">
                <form action="${pageContext.request.contextPath}/items" method="GET" class="search-grid">
                    <div class="form-group" style="margin-bottom:0;">
                        <label class="form-label" for="keyword">Keyword</label>
                        <input type="text" id="keyword" name="keyword" class="form-control" 
                               value="${keyword != null ? keyword : ''}" placeholder="Search title or description...">
                    </div>

                    <div class="form-group" style="margin-bottom:0;">
                        <label class="form-label" for="category">Category</label>
                        <select id="category" name="category" class="form-control">
                            <option value="">All Categories</option>
                            <c:forEach var="cat" items="${categories}">
                                <option value="${cat.categoryId}" ${selectedCategory == cat.categoryId ? 'selected' : ''}>
                                    ${cat.categoryName}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group" style="margin-bottom:0;">
                        <label class="form-label" for="location">Location</label>
                        <input type="text" id="location" name="location" class="form-control" 
                               value="${location != null ? location : ''}" placeholder="e.g. Library, Cafeteria">
                    </div>

                    <div class="form-group" style="margin-bottom:0;">
                        <label class="form-label" for="type">Item Type</label>
                        <select id="type" name="type" class="form-control">
                            <option value="ALL" ${selectedType == 'ALL' ? 'selected' : ''}>All Types</option>
                            <option value="LOST" ${selectedType == 'LOST' ? 'selected' : ''}>Lost Items</option>
                            <option value="FOUND" ${selectedType == 'FOUND' ? 'selected' : ''}>Found Items</option>
                        </select>
                    </div>

                    <div>
                        <button type="submit" class="btn btn-primary" style="height: 44px; width: 100%;">Search</button>
                    </div>
                </form>
            </section>

            <!-- Items Grid -->
            <c:choose>
                <c:when test="${empty items}">
                    <div style="background: white; border-radius: var(--radius-lg); padding: 4rem 2rem; text-align: center; border: 1px solid var(--border-color);">
                        <div style="font-size: 3.5rem; margin-bottom: 1rem;">🔍</div>
                        <h3>No Items Found</h3>
                        <p style="color: var(--text-muted); margin-bottom: 1.5rem;">There are currently no items matching your criteria.</p>
                        <a href="${pageContext.request.contextPath}/items" class="btn btn-secondary">Clear Filters</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="cards-grid">
                        <c:forEach var="item" items="${items}">
                            <div class="item-card">
                                <div class="item-img-container">
                                    <span class="card-type-badge ${item.itemType == 'LOST' ? 'badge-lost' : 'badge-found'}">
                                        ${item.itemType}
                                    </span>
                                    <span class="badge-status">${item.status}</span>
                                    <c:choose>
                                        <c:when test="${not empty item.imagePath}">
                                            <img src="${pageContext.request.contextPath}/${item.imagePath}" alt="${item.itemName}" class="item-img">
                                        </c:when>
                                        <c:otherwise>
                                            <div class="item-img-placeholder">${item.itemType == 'LOST' ? '❓' : '🎁'}</div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="item-card-body">
                                    <span class="item-category-tag">${item.categoryName}</span>
                                    <h4 class="item-card-title">${item.itemName}</h4>
                                    <p class="item-card-desc">${item.description}</p>
                                    
                                    <div class="item-meta-list">
                                        <div class="item-meta-item">
                                            <span>📍</span>
                                            <span>${item.location}</span>
                                        </div>
                                        <div class="item-meta-item">
                                            <span>📅</span>
                                            <span>${item.incidentDate}</span>
                                        </div>
                                    </div>
                                </div>
                                <div class="item-card-footer">
                                    <a href="${pageContext.request.contextPath}/item/details?id=${item.itemId}" class="btn btn-primary btn-sm">
                                        View Details →
                                    </a>
                                    <c:if test="${item.itemType == 'FOUND' && item.status == 'ACTIVE'}">
                                        <a href="${pageContext.request.contextPath}/claim/submit?itemId=${item.itemId}" class="btn btn-secondary btn-sm">Claim</a>
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>

        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />

</body>
</html>
