package com.bibek.fintracker.financial_tracker_api.service;

import com.bibek.fintracker.financial_tracker_api.model.Category;
import com.bibek.fintracker.financial_tracker_api.model.CategorizationRule;
import com.bibek.fintracker.financial_tracker_api.model.User;
import com.bibek.fintracker.financial_tracker_api.repository.CategoryRepository;
import com.bibek.fintracker.financial_tracker_api.repository.CategorizationRuleRepository;
import com.bibek.fintracker.financial_tracker_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RuleService {

    @Autowired private UserRepository userRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private CategorizationRuleRepository ruleRepository;

    public List<CategorizationRule> getRulesByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return ruleRepository.findByUser(user);
    }

    public CategorizationRule createRule(String keyword, Long categoryId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Category category = categoryRepository.findByIdAndUser(categoryId, user)
                .orElseThrow(() -> new SecurityException("Category not found or user does not have permission"));

        CategorizationRule newRule = new CategorizationRule();
        newRule.setKeyword(keyword);
        newRule.setCategory(category);
        newRule.setUser(user);
        return ruleRepository.save(newRule);
    }

    public void deleteRule(Long ruleId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        CategorizationRule rule = ruleRepository.findById(ruleId).orElseThrow(() -> new RuntimeException("Rule not found"));

        if (!rule.getUser().getId().equals(user.getId())) {
            throw new SecurityException("User does not have permission to delete this rule");
        }
        ruleRepository.deleteById(ruleId);
    }
}