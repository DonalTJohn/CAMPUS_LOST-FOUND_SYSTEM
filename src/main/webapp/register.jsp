<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Account | Campus Lost &amp; Found Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/includes/navbar.jsp" />

    <main class="main-content">
        <div class="container">
            <div class="auth-box" style="max-width: 540px;">
                <div style="text-align: center; margin-bottom: 2rem;">
                    <div style="font-size: 2.5rem; margin-bottom: 0.5rem;">🎓</div>
                    <h2>Student &amp; Staff Registration</h2>
                    <p style="color: var(--text-muted); font-size: 0.9rem;">Join the campus network to report and recover lost belongings.</p>
                </div>

                <jsp:include page="/includes/alerts.jsp" />

                <form action="${pageContext.request.contextPath}/register" method="POST" class="needs-validation" novalidate>
                    
                    <div class="form-group">
                        <label for="name" class="form-label">Full Name</label>
                        <input type="text" id="name" name="name" class="form-control" 
                               value="${name != null ? name : ''}" placeholder="e.g. John Doe" required>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="username" class="form-label">Username</label>
                            <input type="text" id="username" name="username" class="form-control" 
                                   value="${username != null ? username : ''}" placeholder="e.g. jdoe24" required>
                        </div>
                        <div class="form-group">
                            <label for="phone" class="form-label">Phone Number</label>
                            <input type="tel" id="phone" name="phone" class="form-control" 
                                   value="${phone != null ? phone : ''}" placeholder="10-15 digits" required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="email" class="form-label">Campus Email</label>
                        <input type="email" id="email" name="email" class="form-control" 
                               value="${email != null ? email : ''}" placeholder="e.g. j.doe@campus.edu" required>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="password" class="form-label">Password</label>
                            <input type="password" id="password" name="password" class="form-control" 
                                   placeholder="Min 8 chars, 1 number" required>
                        </div>
                        <div class="form-group">
                            <label for="confirmPassword" class="form-label">Confirm Password</label>
                            <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" 
                                   placeholder="Re-enter password" required>
                        </div>
                    </div>

                    <div style="margin-top: 1.75rem;">
                        <button type="submit" class="btn btn-primary" style="width: 100%; padding: 0.85rem;">
                            Complete Registration
                        </button>
                    </div>
                </form>

                <div style="text-align: center; margin-top: 1.5rem; padding-top: 1.5rem; border-top: 1px solid var(--border-color); font-size: 0.9rem;">
                    Already have an account? <a href="${pageContext.request.contextPath}/login.jsp" style="font-weight: 700;">Sign In</a>
                </div>
            </div>
        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />

    <script src="${pageContext.request.contextPath}/js/validation.js"></script>
</body>
</html>
