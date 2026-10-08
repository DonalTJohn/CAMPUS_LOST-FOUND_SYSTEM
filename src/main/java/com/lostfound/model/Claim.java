package com.lostfound.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model class representing a Claim submitted for a found or lost item.
 */
public class Claim implements Serializable {

    private static final long serialVersionUID = 1L;

    private int claimId;
    private int itemId;
    private int claimantId;
    private String proofDescription;
    private String claimStatus;     // 'PENDING', 'APPROVED', 'REJECTED'
    private Timestamp claimDate;

    // Joined helper fields for easy JSP rendering
    private String itemName;
    private String itemType;
    private String itemLocation;
    private String claimantName;
    private String claimantEmail;
    private String claimantPhone;

    public Claim() {
    }

    public Claim(int itemId, int claimantId, String proofDescription, String claimStatus) {
        this.itemId = itemId;
        this.claimantId = claimantId;
        this.proofDescription = proofDescription;
        this.claimStatus = claimStatus != null ? claimStatus : "PENDING";
    }

    public Claim(int claimId, int itemId, int claimantId, String proofDescription, String claimStatus, Timestamp claimDate) {
        this.claimId = claimId;
        this.itemId = itemId;
        this.claimantId = claimantId;
        this.proofDescription = proofDescription;
        this.claimStatus = claimStatus;
        this.claimDate = claimDate;
    }

    public int getClaimId() {
        return claimId;
    }

    public void setClaimId(int claimId) {
        this.claimId = claimId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getClaimantId() {
        return claimantId;
    }

    public void setClaimantId(int claimantId) {
        this.claimantId = claimantId;
    }

    public String getProofDescription() {
        return proofDescription;
    }

    public void setProofDescription(String proofDescription) {
        this.proofDescription = proofDescription;
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }

    public Timestamp getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(Timestamp claimDate) {
        this.claimDate = claimDate;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    public String getClaimantName() {
        return claimantName;
    }

    public void setClaimantName(String claimantName) {
        this.claimantName = claimantName;
    }

    public String getClaimantEmail() {
        return claimantEmail;
    }

    public void setClaimantEmail(String claimantEmail) {
        this.claimantEmail = claimantEmail;
    }

    public String getClaimantPhone() {
        return claimantPhone;
    }

    public void setClaimantPhone(String claimantPhone) {
        this.claimantPhone = claimantPhone;
    }

    public boolean isPending() {
        return "PENDING".equalsIgnoreCase(this.claimStatus);
    }

    public boolean isApproved() {
        return "APPROVED".equalsIgnoreCase(this.claimStatus);
    }

    public boolean isRejected() {
        return "REJECTED".equalsIgnoreCase(this.claimStatus);
    }

    @Override
    public String toString() {
        return "Claim [claimId=" + claimId + ", itemId=" + itemId + ", claimantId=" + claimantId + 
               ", claimStatus=" + claimStatus + "]";
    }
}
