<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    String currentUri = request.getRequestURI();
    request.setAttribute("currentUri", currentUri);
%>
<header class="navbar">
    <div class="navbar-container">
        <!-- Brand Logo -->
        <a href="${pageContext.request.contextPath}/" class="brand-logo">
            <div class="brand-icon">🔍</div>
            <span class="brand-text">Campus<span class="brand-accent">Lost&amp;Found</span></span>
        </a>

        <!-- Main Navigation Links -->
        <nav class="nav-wrapper">
            <ul class="nav-menu">
                <li>
                    <a href="${pageContext.request.contextPath}/items" class="nav-link ${currentUri.contains('/items') && !currentUri.contains('admin') ? 'active' : ''}">Browse Items</a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/item/add?type=LOST" class="nav-link ${currentUri.contains('type=LOST') ? 'active' : ''}">Report Lost</a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/item/add?type=FOUND" class="nav-link ${currentUri.contains('type=FOUND') ? 'active' : ''}">Report Found</a>
                </li>

                <c:choose>
                    <c:when test="${not empty sessionScope.loggedUser}">
                        <c:choose>
                            <c:when test="${sessionScope.loggedUser.role == 'ADMIN'}">
                                <li>
                                    <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link ${currentUri.contains('/admin/dashboard') ? 'active' : ''}">Admin Panel</a>
                                </li>
                                <li>
                                    <a href="${pageContext.request.contextPath}/admin/claims" class="nav-link ${currentUri.contains('/admin/claims') || currentUri.contains('/admin/verify-claim') ? 'active' : ''}">Verify Claims</a>
                                </li>
                                <li>
                                    <a href="${pageContext.request.contextPath}/admin/users" class="nav-link ${currentUri.contains('/admin/users') ? 'active' : ''}">Users</a>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li>
                                    <a href="${pageContext.request.contextPath}/dashboard" class="nav-link ${currentUri.endsWith('/dashboard') || currentUri.endsWith('/dashboard.jsp') ? 'active' : ''}">Dashboard</a>
                                </li>
                                <li>
                                    <a href="${pageContext.request.contextPath}/my-items" class="nav-link ${currentUri.contains('/my-items') ? 'active' : ''}">My Items</a>
                                </li>
                                <li>
                                    <a href="${pageContext.request.contextPath}/claims/my" class="nav-link ${currentUri.contains('/claims/my') ? 'active' : ''}">My Claims</a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                </c:choose>
            </ul>
        </nav>

        <!-- Right Side User Actions -->
        <div class="nav-actions">
            <c:choose>
                <c:when test="${empty sessionScope.loggedUser}">
                    <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-secondary btn-sm">Log In</a>
                    <a href="${pageContext.request.contextPath}/register.jsp" class="btn btn-primary btn-sm">Sign Up</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/notifications" class="nav-link nav-icon-link ${currentUri.contains('/notifications') ? 'active' : ''}" title="Notifications &amp; Alerts">
                        <span class="nav-icon">🔔</span>
                        <span class="nav-badge-text">Alerts</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/profile" class="nav-link nav-user-profile ${currentUri.contains('/profile') ? 'active' : ''}" title="Logged in as ${sessionScope.loggedUser.name}">
                        <span class="nav-avatar">👤</span>
                        <span class="user-display-name">${sessionScope.loggedUser.name}</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-secondary btn-sm btn-nav-logout">Log Out</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</header>
