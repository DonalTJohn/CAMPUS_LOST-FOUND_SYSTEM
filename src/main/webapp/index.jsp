<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="com.lostfound.service.ItemService, com.lostfound.service.ItemOperations, com.lostfound.model.Item, com.lostfound.model.Category, java.util.List, java.util.Map" %>
<%
    ItemOperations itemService = new ItemService();
    Map<String, Integer> stats = itemService.getDashboardStatistics();
    List<Item> recentItems = itemService.getAllItems();
    if (recentItems.size() > 6) {
        recentItems = recentItems.subList(0, 6);
    }
    List<Category> categories = itemService.getAllCategories();
    request.setAttribute("stats", stats);
    request.setAttribute("recentItems", recentItems);
    request.setAttribute("categories", categories);
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Campus Lost &amp; Found Portal | Smart Campus Recovery System</title>
    <meta name="description" content="Report, search, and reclaim lost and found items across the campus with our automated matching system.">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script>
        const contextPath = "${pageContext.request.contextPath}";
    </script>
</head>
<body>

    <jsp:include page="/includes/navbar.jsp" />

    <main class="main-content">
        <div class="container">
            
            <jsp:include page="/includes/alerts.jsp" />

            <!-- Hero Section -->
            <section class="hero">
                <span class="hero-tag">🎓 Official University Portal</span>
                <h1 class="hero-title">Misplaced something on campus? <br><span class="brand-accent">We've got you covered.</span></h1>
                <p class="hero-subtitle">
                    The fastest way to report, search, and reclaim lost belongings across campus lecture halls, libraries, cafeterias, and student dorms.
                </p>
                <div class="hero-cta-group">
                    <a href="${pageContext.request.contextPath}/item/add?type=LOST" class="btn btn-primary btn-lg">
                        <span>📢</span> Report Lost Item
                    </a>
                    <a href="${pageContext.request.contextPath}/item/add?type=FOUND" class="btn btn-success btn-lg">
                        <span>🤝</span> Report Found Item
                    </a>
                    <a href="${pageContext.request.contextPath}/items" class="btn btn-secondary btn-lg">
                        <span>🔍</span> Browse Listings
                    </a>
                </div>
            </section>

            <!-- Metrics Overview -->
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-icon primary">📦</div>
                    <div class="stat-info">
                        <h3>${stats.totalItems != null ? stats.totalItems : 0}</h3>
                        <p>Total Items Reported</p>
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
                    <div class="stat-icon info">✨</div>
                    <div class="stat-info">
                        <h3>${stats.resolvedItems != null ? stats.resolvedItems : 0}</h3>
                        <p>Reclaimed &amp; Resolved</p>
                    </div>
                </div>
            </div>

            <!-- Live AJAX Search Filter -->
            <section class="search-widget">
                <h3 style="margin-bottom: 1rem; font-size: 1.25rem;">🔍 Instant Live Search (AJAX)</h3>
                <form id="ajaxSearchForm" class="search-grid">
                    <div class="form-group" style="margin-bottom:0;">
                        <label class="form-label" for="ajaxKeyword">Keyword or Name</label>
                        <input type="text" id="ajaxKeyword" class="form-control" placeholder="e.g. Laptop charger, Student ID, Wallet...">
                    </div>
                    <div class="form-group" style="margin-bottom:0;">
                        <label class="form-label" for="ajaxCategory">Category</label>
                        <select id="ajaxCategory" class="form-control">
                            <option value="">All Categories</option>
                            <c:forEach var="cat" items="${categories}">
                                <option value="${cat.categoryId}">${cat.categoryName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group" style="margin-bottom:0;">
                        <label class="form-label" for="ajaxLocation">Location</label>
                        <input type="text" id="ajaxLocation" class="form-control" placeholder="e.g. Library, Lab 304">
                    </div>
                    <div class="form-group" style="margin-bottom:0;">
                        <label class="form-label" for="ajaxType">Type</label>
                        <select id="ajaxType" class="form-control">
                            <option value="">All Types</option>
                            <option value="LOST">Lost</option>
                            <option value="FOUND">Found</option>
                        </select>
                    </div>
                    <div>
                        <button type="submit" class="btn btn-primary" style="height: 44px; width: 100%;">Filter</button>
                    </div>
                </form>
            </section>

            <!-- Dynamic Search & Recent Listings Grid -->
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
                <div>
                    <h2>Recent Campus Listings</h2>
                    <p style="color: var(--text-muted); font-size: 0.9rem;" id="resultsCount">Displaying latest reports</p>
                </div>
                <a href="${pageContext.request.contextPath}/items" class="btn btn-secondary btn-sm">View All Listings →</a>
            </div>

            <div class="cards-grid" id="searchResultsGrid">
                <c:forEach var="item" items="${recentItems}">
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

        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />

    <script src="${pageContext.request.contextPath}/js/search-ajax.js"></script>
</body>
</html>
