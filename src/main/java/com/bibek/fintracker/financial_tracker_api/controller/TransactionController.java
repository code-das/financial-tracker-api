package com.bibek.fintracker.financial_tracker_api.controller;

import com.bibek.fintracker.financial_tracker_api.dto.TransactionDto;
import com.bibek.fintracker.financial_tracker_api.model.Transaction;
import com.bibek.fintracker.financial_tracker_api.model.User;
import com.bibek.fintracker.financial_tracker_api.repository.TransactionRepository;
import com.bibek.fintracker.financial_tracker_api.repository.UserRepository;
import com.bibek.fintracker.financial_tracker_api.service.CategorizationService;
import com.bibek.fintracker.financial_tracker_api.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired private TransactionService transactionService;
    @Autowired private CategorizationService categorizationService; // New service
    @Autowired private UserRepository userRepository; // To find the user
    @Autowired private TransactionRepository transactionRepository; // To get transactions

    @PostMapping("/upload")
    public ResponseEntity<String> uploadTransactions(@RequestParam("file") MultipartFile file, Principal principal) {
        transactionService.processAndSaveTransactions(file, principal.getName());
        return ResponseEntity.ok("File uploaded successfully. Processing will start shortly.");
    }

    // ** NEW ENDPOINT TO VIEW TRANSACTIONS **
    @GetMapping
    public ResponseEntity<List<TransactionDto>> getUserTransactions(Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        List<Transaction> transactions = transactionRepository.findByUser(user);
        List<TransactionDto> dtos = transactions.stream().map(this::convertToDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // ** NEW ENDPOINT TO TRIGGER CATEGORIZATION **
    @PostMapping("/categorize")
    public ResponseEntity<String> categorizeTransactions(Principal principal) {
        categorizationService.categorizeTransactionsForUser(principal.getName());
        return ResponseEntity.ok("Transactions categorized successfully.");
    }

    private TransactionDto convertToDto(Transaction transaction) {
        TransactionDto dto = new TransactionDto();
        dto.setId(transaction.getId());
        dto.setDate(transaction.getDate());
        dto.setDescription(transaction.getDescription());
        dto.setAmount(transaction.getAmount());
        dto.setCategory(transaction.getCategory());
        return dto;
    }
}