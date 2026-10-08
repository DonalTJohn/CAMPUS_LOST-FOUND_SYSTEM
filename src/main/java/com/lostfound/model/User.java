package com.lostfound.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model class representing a User (Student, Staff, or Admin) in the Campus Lost & Found Portal.
 * Demonstrates OOP Encapsulation with private fields and public accessors.
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private int userId;
    private String name;
    private String username;
    private String email;
    private String password;
    private String phone;
    private String role;     // 'USER' or 'ADMIN'
    private String status;   // 'ACTIVE' or 'BLOCKED'
    private Timestamp createdAt;

    // Default Constructor
    public User() {
    }

    // Parameterized Constructor (for registration / creation)
    public User(String name, String username, String email, String password, String phone, String role, String status) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role != null ? role : "USER";
        this.status = status != null ? status : "ACTIVE";
    }

    // Full Parameterized Constructor (retrieved from database)
    public User(int userId, String name, String username, String email, String password, String phone, String role, String status, Timestamp createdAt) {
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    // Helper Business Methods
    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(this.role);
    }

    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(this.status);
    }

    /**
     * Represents user data in a simple JSON structure.
     * Satisfies Phase 3 requirement: Represent user data format using JSON.
     */
    public String toJson() {
        return "{\n" +
                "  \"userId\": " + userId + ",\n" +
                "  \"name\": \"" + (name != null ? name.replace("\"", "\\\"") : "") + "\",\n" +
                "  \"username\": \"" + (username != null ? username.replace("\"", "\\\"") : "") + "\",\n" +
                "  \"email\": \"" + (email != null ? email.replace("\"", "\\\"") : "") + "\",\n" +
                "  \"phone\": \"" + (phone != null ? phone.replace("\"", "\\\"") : "") + "\",\n" +
                "  \"role\": \"" + (role != null ? role : "USER") + "\",\n" +
                "  \"status\": \"" + (status != null ? status : "ACTIVE") + "\"\n" +
                "}";
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", name=" + name + ", username=" + username + 
               ", email=" + email + ", role=" + role + ", status=" + status + "]";
    }
}
