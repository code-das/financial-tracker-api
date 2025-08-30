package com.bibek.fintracker.financial_tracker_api.dto;

public class CreateRuleRequest {
    private String keyword;
    private Long categoryId;

    // Getters and Setters
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}