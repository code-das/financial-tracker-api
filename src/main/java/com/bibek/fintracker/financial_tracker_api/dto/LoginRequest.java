package com.bibek.fintracker.financial_tracker_api.dto;

// We removed @Data
public class LoginRequest {
    private String username;
    private String password;

    // --- MANUALLY ADDED GETTERS AND SETTERS ---

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}