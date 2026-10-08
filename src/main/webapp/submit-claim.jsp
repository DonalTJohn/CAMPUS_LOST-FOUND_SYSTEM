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
    <title>Submit Ownership Claim | Campus Lost &amp; Found Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/includes/navbar.jsp" />

    <main class="main-content">
        <div class="container">
            
            <div class="form-box">
                <div style="margin-bottom: 2rem;">
                    <span class="status-pill status-pending" style="margin-bottom: 0.5rem;">Claim Verification Request</span>
                    <h2>Claim Item: ${item.itemName}</h2>
                    <p style="color: var(--text-muted); font-size: 0.9rem;">
                        To prevent fraudulent claims, please provide verifiable proof of ownership that only the rightful owner would know.
                    </p>
                </div>

                <jsp:include page="/includes/alerts.jsp" />

                <!-- Summary of item being claimed -->
                <div style="background: #f8fafc; border-radius: var(--radius-md); padding: 1.25rem; margin-bottom: 2rem; border: 1px solid var(--border-color);">
                    <h4 style="margin-bottom: 0.5rem;">Item Overview</h4>
                    <p style="font-size: 0.9rem; color: var(--text-muted); margin-bottom: 0.5rem;">${item.description}</p>
                    <div style="font-size: 0.85rem; color: var(--text-muted);">
                        <span>📍 Found Location: <strong>${item.location}</strong></span> | 
                        <span>📅 Date: <strong>${item.incidentDate}</strong></span>
                    </div>
                </div>

                <form action="${pageContext.request.contextPath}/claim/submit" method="POST" class="needs-validation" novalidate>
                    <input type="hidden" name="itemId" value="${item.itemId}">

                    <div class="form-group">
                        <label for="proofDescription" class="form-label">Ownership Proof &amp; Verification Details</label>
                        <textarea id="proofDescription" name="proofDescription" rows="5" class="form-control" 
                                  placeholder="Provide specific details such as: serial number, distinctive contents inside wallet, phone wallpaper/passcode hint, receipt details, scratches, brand name..." required></textarea>
                        <small style="color: var(--text-muted); font-size: 0.8rem;">
                            Your proof will be strictly reviewed by the Campus Administration before the item is handed over.
                        </small>
                    </div>

                    <div style="margin-top: 2rem; display: flex; gap: 1rem;">
                        <button type="submit" class="btn btn-primary" style="flex: 1;">Submit Claim for Verification</button>
                        <a href="${pageContext.request.contextPath}/item/details?id=${item.itemId}" class="btn btn-secondary">Cancel</a>
                    </div>
                </form>
            </div>

        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />

    <script src="${pageContext.request.contextPath}/js/validation.js"></script>
</body>
</html>
