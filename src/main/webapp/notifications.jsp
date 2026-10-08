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
    <title>Notifications &amp; Alerts | Campus Lost &amp; Found Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/includes/navbar.jsp" />

    <main class="main-content">
        <div class="container">
            
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem; flex-wrap: wrap; gap: 1rem;">
                <div>
                    <h2>Notifications &amp; Activity Alerts</h2>
                    <p style="color: var(--text-muted);">Stay updated on status changes, matches, and claim reviews.</p>
                </div>
                <c:if test="${not empty notifications}">
                    <a href="${pageContext.request.contextPath}/notification/read-all" class="btn btn-secondary btn-sm">
                        Mark All as Read
                    </a>
                </c:if>
            </div>

            <jsp:include page="/includes/alerts.jsp" />

            <div style="max-width: 800px; margin: 0 auto; display: flex; flex-direction: column; gap: 1rem;">
                <c:choose>
                    <c:when test="${empty notifications}">
                        <div style="background: white; border-radius: var(--radius-lg); padding: 4rem 2rem; text-align: center; border: 1px solid var(--border-color);">
                            <div style="font-size: 3rem; margin-bottom: 1rem;">🔕</div>
                            <h3>No Notifications</h3>
                            <p style="color: var(--text-muted);">You're all caught up! New alerts will show up here.</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="notif" items="${notifications}">
                            <div style="background: ${notif.read ? 'white' : 'var(--primary-light)'}; border: 1px solid ${notif.read ? 'var(--border-color)' : 'var(--border-focus)'}; border-radius: var(--radius-md); padding: 1.25rem; display: flex; justify-content: space-between; align-items: center; gap: 1rem; box-shadow: var(--shadow-sm);">
                                <div style="display: flex; align-items: flex-start; gap: 1rem;">
                                    <div style="font-size: 1.5rem; line-height: 1;">🔔</div>
                                    <div>
                                        <p style="font-weight: ${notif.read ? '500' : '700'}; color: var(--text-main); margin-bottom: 0.25rem;">
                                            ${notif.message}
                                        </p>
                                        <small style="color: var(--text-muted);">${notif.createdAt}</small>
                                    </div>
                                </div>
                                <c:if test="${!notif.read}">
                                    <a href="${pageContext.request.contextPath}/notification/read?id=${notif.notificationId}" class="btn btn-secondary btn-sm" style="white-space: nowrap;">
                                        Mark Read
                                    </a>
                                </c:if>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>

        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />

</body>
</html>
