package com.lostfound.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model class representing an Admin verification review of a claim.
 */
public class ClaimVerification implements Serializable {

    private static final long serialVersionUID = 1L;

    private int verificationId;
    private int claimId;
    private int adminId;
    private String remarks;
    private String decision;      // 'APPROVED', 'REJECTED'
    private Timestamp verifiedAt;

    // Joined fields
    private String adminName;

    public ClaimVerification() {
    }

    public ClaimVerification(int claimId, int adminId, String remarks, String decision) {
        this.claimId = claimId;
        this.adminId = adminId;
        this.remarks = remarks;
        this.decision = decision;
    }

    public ClaimVerification(int verificationId, int claimId, int adminId, String remarks, String decision, Timestamp verifiedAt) {
        this.verificationId = verificationId;
        this.claimId = claimId;
        this.adminId = adminId;
        this.remarks = remarks;
        this.decision = decision;
        this.verifiedAt = verifiedAt;
    }

    public int getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(int verificationId) {
        this.verificationId = verificationId;
    }

    public int getClaimId() {
        return claimId;
    }

    public void setClaimId(int claimId) {
        this.claimId = claimId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public Timestamp getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(Timestamp verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    @Override
    public String toString() {
        return "ClaimVerification [verificationId=" + verificationId + ", claimId=" + claimId + 
               ", decision=" + decision + ", adminId=" + adminId + "]";
    }
}
