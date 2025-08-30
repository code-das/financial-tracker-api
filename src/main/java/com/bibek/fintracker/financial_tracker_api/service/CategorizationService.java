package com.bibek.fintracker.financial_tracker_api.service;

import com.bibek.fintracker.financial_tracker_api.model.CategorizationRule;
import com.bibek.fintracker.financial_tracker_api.model.Transaction;
import com.bibek.fintracker.financial_tracker_api.model.User;
import com.bibek.fintracker.financial_tracker_api.repository.CategorizationRuleRepository;
import com.bibek.fintracker.financial_tracker_api.repository.TransactionRepository;
import com.bibek.fintracker.financial_tracker_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategorizationService {

    @Autowired private UserRepository userRepository;
    @Autowired private TransactionRepository transactionRepository;
    @Autowired private CategorizationRuleRepository ruleRepository;

    public void categorizeTransactionsForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CategorizationRule> rules = ruleRepository.findByUser(user);
        List<Transaction> uncategorizedTransactions = transactionRepository.findByUserAndCategoryIsNull(user);

        for (Transaction transaction : uncategorizedTransactions) {
            for (CategorizationRule rule : rules) {
                // Check if the transaction description contains the rule's keyword (case-insensitive)
                if (transaction.getDescription().toLowerCase().contains(rule.getKeyword().toLowerCase())) {
                    transaction.setCategory(rule.getCategory().getName());
                    // Apply the first matching rule and move to the next transaction
                    break;
                }
            }
        }

        // Save all the updated transactions back to the database
        transactionRepository.saveAll(uncategorizedTransactions);
    }
}