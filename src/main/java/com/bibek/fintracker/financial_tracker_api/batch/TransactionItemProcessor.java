package com.bibek.fintracker.financial_tracker_api.batch;

import com.bibek.fintracker.financial_tracker_api.model.Transaction;
import com.bibek.fintracker.financial_tracker_api.model.User;
import com.bibek.fintracker.financial_tracker_api.repository.UserRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class TransactionItemProcessor implements ItemProcessor<Transaction, Transaction> {

    private final UserRepository userRepository;
    private final String username;

    public TransactionItemProcessor(UserRepository userRepository, String username) {
        this.userRepository = userRepository;
        this.username = username;
    }

    @Override
    public Transaction process(final Transaction transaction) throws Exception {
        // Find the user from the database using the username passed into this processor
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        // Set the user on the transaction object
        transaction.setUser(user);

        // Return the completed transaction object, ready for the writer
        return transaction;
    }
}