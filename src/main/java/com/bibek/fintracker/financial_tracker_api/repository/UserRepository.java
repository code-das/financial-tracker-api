package com.bibek.fintracker.financial_tracker_api.repository;

import com.bibek.fintracker.financial_tracker_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Find a user by their unique username
    Optional<User> findByUsername(String username);

    // Find a user by their unique email
    Optional<User> findByEmail(String email);
}