package com.lostfound.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Model class representing a Lost or Found item.
 * Supports complete item metadata, image paths, status lifecycle, and joined user/category details.
 */
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    private int itemId;
    private int userId;
    private int categoryId;
    private String itemName;
    private String itemType;       // 'LOST' or 'FOUND'
    private String description;
    private String location;
    private Date incidentDate;
    private String imagePath;
    private String status;         // 'PENDING', 'ACTIVE', 'CLAIMED', 'RECOVERED', 'REJECTED'
    private Timestamp createdAt;

    // Joined helper fields (not in items table directly, populated via JOINs for display)
    private String categoryName;
    private String reporterName;
    private String reporterEmail;
    private String reporterPhone;

    public Item() {
    }

    // Parameterized constructor for creation
    public Item(int userId, int categoryId, String itemName, String itemType, String description, 
                String location, Date incidentDate, String imagePath, String status) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.itemName = itemName;
        this.itemType = itemType;
        this.description = description;
        this.location = location;
        this.incidentDate = incidentDate;
        this.imagePath = imagePath;
        this.status = status != null ? status : "ACTIVE";
    }

    // Full constructor
    public Item(int itemId, int userId, int categoryId, String itemName, String itemType, 
                String description, String location, Date incidentDate, String imagePath, 
                String status, Timestamp createdAt) {
        this.itemId = itemId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.itemName = itemName;
        this.itemType = itemType;
        this.description = description;
        this.location = location;
        this.incidentDate = incidentDate;
        this.imagePath = imagePath;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(Date incidentDate) {
        this.incidentDate = incidentDate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getReporterEmail() {
        return reporterEmail;
    }

    public void setReporterEmail(String reporterEmail) {
        this.reporterEmail = reporterEmail;
    }

    public String getReporterPhone() {
        return reporterPhone;
    }

    public void setReporterPhone(String reporterPhone) {
        this.reporterPhone = reporterPhone;
    }

    // Helper methods
    public boolean isLost() {
        return "LOST".equalsIgnoreCase(this.itemType);
    }

    public boolean isFound() {
        return "FOUND".equalsIgnoreCase(this.itemType);
    }

    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(this.status);
    }

    @Override
    public String toString() {
        return "Item [itemId=" + itemId + ", itemName=" + itemName + ", itemType=" + itemType + 
               ", location=" + location + ", status=" + status + "]";
    }
}
