package com.bibek.fintracker.financial_tracker_api.repository;

import com.bibek.fintracker.financial_tracker_api.model.Transaction;
import com.bibek.fintracker.financial_tracker_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUser(User user);

    // Find only the transactions for a user that have no category yet
    List<Transaction> findByUserAndCategoryIsNull(User user);
}