<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign In | Campus Lost &amp; Found Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/includes/navbar.jsp" />

    <main class="main-content">
        <div class="container">
            <div class="auth-box">
                <div style="text-align: center; margin-bottom: 2rem;">
                    <div style="font-size: 2.5rem; margin-bottom: 0.5rem;">🔐</div>
                    <h2>Welcome Back</h2>
                    <p style="color: var(--text-muted); font-size: 0.9rem;">Sign in to access your reports, claims, and alerts.</p>
                </div>

                <jsp:include page="/includes/alerts.jsp" />

                <c:if test="${not empty param.logout}">
                    <div class="alert alert-info">
                        <span>👋</span>
                        <div>You have been logged out successfully.</div>
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/login" method="POST" class="needs-validation" novalidate>
                    <div class="form-group">
                        <label for="identifier" class="form-label">Username or Email</label>
                        <input type="text" id="identifier" name="identifier" class="form-control" 
                               value="${identifier != null ? identifier : ''}" placeholder="e.g. john.doe@campus.edu" required>
                    </div>

                    <div class="form-group">
                        <label for="password" class="form-label">Password</label>
                        <input type="password" id="password" name="password" class="form-control" 
                               placeholder="Enter your password" required>
                    </div>

                    <div style="margin-top: 1.75rem;">
                        <button type="submit" class="btn btn-primary" style="width: 100%; padding: 0.85rem;">
                            Sign In to Account
                        </button>
                    </div>
                </form>

                <div style="text-align: center; margin-top: 1.5rem; padding-top: 1.5rem; border-top: 1px solid var(--border-color); font-size: 0.9rem;">
                    Don't have an account? <a href="${pageContext.request.contextPath}/register.jsp" style="font-weight: 700;">Create Account</a>
                </div>

                <!-- Academic Demo Helper Badge -->
                <div style="margin-top: 1.5rem; background: #f8fafc; border: 1px dashed #cbd5e1; border-radius: var(--radius-sm); padding: 0.85rem; font-size: 0.8rem; color: var(--text-muted);">
                    <strong>💡 Demo Credentials:</strong><br>
                    • Admin: <code>admin</code> / <code>admin123</code><br>
                    • Student: <code>johndoe</code> / <code>student123</code>
                </div>
            </div>
        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />

    <script src="${pageContext.request.contextPath}/js/validation.js"></script>
</body>
</html>
