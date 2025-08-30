package com.bibek.fintracker.financial_tracker_api.dto;

// We removed @Data and @AllArgsConstructor
public class AuthResponse {
    private String accessToken;

    // --- MANUALLY ADDED CONSTRUCTOR ---
    public AuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    // --- MANUALLY ADDED GETTER AND SETTER ---
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}