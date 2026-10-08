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
    <title>My Profile | Campus Lost &amp; Found Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/includes/navbar.jsp" />

    <main class="main-content">
        <div class="container">
            
            <div style="max-width: 760px; margin: 0 auto;">
                <div style="margin-bottom: 2rem;">
                    <h2>User Account &amp; Profile</h2>
                    <p style="color: var(--text-muted);">Manage your personal details and account security.</p>
                </div>

                <jsp:include page="/includes/alerts.jsp" />

                <!-- Profile Details Form -->
                <div class="form-box" style="margin-bottom: 2rem; max-width: 100%;">
                    <h3 style="margin-bottom: 1.5rem; font-size: 1.25rem;">Personal Information</h3>
                    
                    <form action="${pageContext.request.contextPath}/profile" method="POST" class="needs-validation" novalidate>
                        <input type="hidden" name="action" value="updateDetails">

                        <div class="form-group">
                            <label for="username" class="form-label">Username (Immutable)</label>
                            <input type="text" id="username" class="form-control" value="${sessionScope.loggedUser.username}" disabled style="background-color: #e2e8f0;">
                        </div>

                        <div class="form-group">
                            <label for="name" class="form-label">Full Name</label>
                            <input type="text" id="name" name="name" class="form-control" value="${sessionScope.loggedUser.name}" required>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="email" class="form-label">Email Address</label>
                                <input type="email" id="email" name="email" class="form-control" value="${sessionScope.loggedUser.email}" required>
                            </div>
                            <div class="form-group">
                                <label for="phone" class="form-label">Phone Number</label>
                                <input type="tel" id="phone" name="phone" class="form-control" value="${sessionScope.loggedUser.phone}" required>
                            </div>
                        </div>

                        <div style="margin-top: 1.5rem;">
                            <button type="submit" class="btn btn-primary">Save Changes</button>
                        </div>
                    </form>
                </div>

                <!-- Password Change Form -->
                <div class="form-box" style="max-width: 100%;">
                    <h3 style="margin-bottom: 1.5rem; font-size: 1.25rem;">Security &amp; Password</h3>
                    
                    <form action="${pageContext.request.contextPath}/profile" method="POST" class="needs-validation" novalidate>
                        <input type="hidden" name="action" value="changePassword">

                        <div class="form-group">
                            <label for="currentPassword" class="form-label">Current Password</label>
                            <input type="password" id="currentPassword" name="currentPassword" class="form-control" required>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="newPassword" class="form-label">New Password</label>
                                <input type="password" id="newPassword" name="newPassword" class="form-control" placeholder="Min 8 chars, 1 digit" required>
                            </div>
                            <div class="form-group">
                                <label for="confirmPassword" class="form-label">Confirm New Password</label>
                                <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" required>
                            </div>
                        </div>

                        <div style="margin-top: 1.5rem;">
                            <button type="submit" class="btn btn-secondary">Update Password</button>
                        </div>
                    </form>
                </div>

            </div>

        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />

    <script src="${pageContext.request.contextPath}/js/validation.js"></script>
</body>
</html>
