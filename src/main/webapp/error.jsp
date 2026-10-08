<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error | Campus Lost &amp; Found Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/includes/navbar.jsp" />

    <main class="main-content">
        <div class="container">
            
            <div style="background: white; border-radius: var(--radius-lg); border: 1px solid var(--border-color); padding: 4rem 2rem; max-width: 600px; margin: 3rem auto; text-align: center; box-shadow: var(--shadow-md);">
                <div style="font-size: 3.5rem; margin-bottom: 1rem;">⚠️</div>
                <h2 style="margin-bottom: 0.75rem;">Something Went Wrong</h2>
                
                <p style="color: var(--text-muted); margin-bottom: 2rem;">
                    <c:choose>
                        <c:when test="${not empty errorMessage}">
                            ${errorMessage}
                        </c:when>
                        <c:when test="${not empty exception}">
                            ${exception.message}
                        </c:when>
                        <c:otherwise>
                            The requested page or resource could not be loaded. Please return to the homepage or try again later.
                        </c:otherwise>
                    </c:choose>
                </p>

                <div style="display: flex; gap: 1rem; justify-content: center;">
                    <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Go to Homepage</a>
                    <a href="${pageContext.request.contextPath}/items" class="btn btn-secondary">Browse Items</a>
                </div>
            </div>

        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />

</body>
</html>
