package com.bibek.fintracker.financial_tracker_api.repository;

import com.bibek.fintracker.financial_tracker_api.model.CategorizationRule;
import com.bibek.fintracker.financial_tracker_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategorizationRuleRepository extends JpaRepository<CategorizationRule, Long> {

    List<CategorizationRule> findByUser(User user);
}