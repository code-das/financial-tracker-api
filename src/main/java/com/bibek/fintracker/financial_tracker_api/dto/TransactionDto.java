package com.bibek.fintracker.financial_tracker_api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionDto {
    private Long id;
    private LocalDate date;
    private String description;
    private BigDecimal amount;
    private String category;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}