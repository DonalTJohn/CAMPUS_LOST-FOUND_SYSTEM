<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    if (session == null || session.getAttribute("loggedUser") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    com.lostfound.model.User user = (com.lostfound.model.User) session.getAttribute("loggedUser");
    if (!user.isAdmin()) {
        response.sendRedirect(request.getContextPath() + "/dashboard");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Verify Claim #${claim.claimId} | Admin Control</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/includes/navbar.jsp" />

    <main class="main-content">
        <div class="container">
            
            <div style="margin-bottom: 1.5rem;">
                <a href="${pageContext.request.contextPath}/admin/claims" style="font-weight: 700;">← Back to Claims Queue</a>
            </div>

            <jsp:include page="/includes/alerts.jsp" />

            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 2rem;">
                
                <!-- Left: Item & Claimant Details -->
                <div>
                    <!-- Item Info Card -->
                    <div style="background: white; border-radius: var(--radius-lg); border: 1px solid var(--border-color); padding: 2rem; margin-bottom: 1.5rem; box-shadow: var(--shadow-sm);">
                        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem;">
                            <span class="status-pill status-active">Item Details</span>
                            <a href="${pageContext.request.contextPath}/item/details?id=${claim.itemId}" target="_blank" style="font-size: 0.85rem; font-weight: 700;">Full Listing ↗</a>
                        </div>
                        <h3 style="margin-bottom: 0.5rem;">${claim.itemName}</h3>
                        <p style="color: var(--text-muted); font-size: 0.9rem;">📍 Location: ${claim.itemLocation}</p>
                    </div>

                    <!-- Claimant Info Card -->
                    <div style="background: white; border-radius: var(--radius-lg); border: 1px solid var(--border-color); padding: 2rem; box-shadow: var(--shadow-sm);">
                        <h4 style="margin-bottom: 1rem;">Claimant Information</h4>
                        <div style="font-size: 0.9rem; line-height: 1.8;">
                            <div><strong>Name:</strong> ${claim.claimantName}</div>
                            <div><strong>Email:</strong> ${claim.claimantEmail}</div>
                            <div><strong>Phone:</strong> ${claim.claimantPhone}</div>
                            <div><strong>Submission Time:</strong> ${claim.claimDate}</div>
                            <div>
                                <strong>Status:</strong> 
                                <span class="status-pill ${claim.claimStatus == 'APPROVED' ? 'status-recovered' : (claim.claimStatus == 'REJECTED' ? 'status-rejected' : 'status-pending')}">
                                    ${claim.claimStatus}
                                </span>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Right: Submitted Proof & Verification Decision Form -->
                <div>
                    <!-- Ownership Proof Card -->
                    <div style="background: white; border-radius: var(--radius-lg); border: 1px solid var(--border-color); padding: 2rem; margin-bottom: 1.5rem; box-shadow: var(--shadow-sm);">
                        <h4 style="margin-bottom: 1rem; color: #1e293b;">Submitted Proof of Ownership</h4>
                        <div style="background: #f8fafc; border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 1.25rem; font-size: 0.95rem; line-height: 1.7; color: #334155;">
                            ${claim.proofDescription}
                        </div>
                    </div>

                    <!-- Existing Verification Remarks if any -->
                    <c:if test="${not empty verification}">
                        <div style="background: #eff6ff; border: 1px solid #bfdbfe; border-radius: var(--radius-lg); padding: 1.5rem; margin-bottom: 1.5rem;">
                            <h4 style="color: #1e40af; margin-bottom: 0.5rem;">Previous Verification Record</h4>
                            <p style="font-size: 0.9rem; margin-bottom: 0.5rem;"><strong>Decision:</strong> ${verification.decision}</p>
                            <p style="font-size: 0.9rem; margin-bottom: 0.5rem;"><strong>Remarks:</strong> ${verification.remarks}</p>
                            <small style="color: #60a5fa;">Verified by: ${verification.adminName} on ${verification.verifiedAt}</small>
                        </div>
                    </c:if>

                    <!-- Verification Review Form -->
                    <div style="background: white; border-radius: var(--radius-lg); border: 1px solid var(--border-color); padding: 2rem; box-shadow: var(--shadow-sm);">
                        <h4 style="margin-bottom: 1.25rem;">Administrator Verification &amp; Decision</h4>
                        
                        <form action="${pageContext.request.contextPath}/admin/claim/verify" method="POST" class="needs-validation" novalidate>
                            <input type="hidden" name="claimId" value="${claim.claimId}">

                            <div class="form-group">
                                <label for="remarks" class="form-label">Verification Remarks / Notes</label>
                                <textarea id="remarks" name="remarks" rows="4" class="form-control" 
                                          placeholder="Enter review notes (e.g. Student presented valid ID card matching name; serial number matched...)" required></textarea>
                            </div>

                            <div class="form-group">
                                <label for="decision" class="form-label">Decision</label>
                                <select id="decision" name="decision" class="form-control" required>
                                    <option value="APPROVED">APPROVE CLAIM (Item will be marked Claimed)</option>
                                    <option value="REJECTED">REJECT CLAIM (Insufficient or incorrect proof)</option>
                                </select>
                            </div>

                            <div style="margin-top: 1.5rem; display: flex; gap: 1rem;">
                                <button type="submit" class="btn btn-primary" style="flex: 1;">Submit Verification Decision</button>
                                <a href="${pageContext.request.contextPath}/admin/claims" class="btn btn-secondary">Cancel</a>
                            </div>
                        </form>
                    </div>

                </div>

            </div>

        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />

    <script src="${pageContext.request.contextPath}/js/validation.js"></script>
</body>
</html>
