package com.bibek.fintracker.financial_tracker_api.repository;

import com.bibek.fintracker.financial_tracker_api.model.Category;
import com.bibek.fintracker.financial_tracker_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUser(User user);

    Optional<Category> findByIdAndUser(Long id, User user);
}