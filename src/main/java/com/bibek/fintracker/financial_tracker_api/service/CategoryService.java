package com.bibek.fintracker.financial_tracker_api.service;

import com.bibek.fintracker.financial_tracker_api.model.Category;
import com.bibek.fintracker.financial_tracker_api.model.User;
import com.bibek.fintracker.financial_tracker_api.repository.CategoryRepository;
import com.bibek.fintracker.financial_tracker_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

    public Category createCategory(String categoryName, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Category newCategory = new Category();
        newCategory.setName(categoryName);
        newCategory.setUser(user);
        return categoryRepository.save(newCategory);
    }

    public List<Category> getCategoriesByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return categoryRepository.findByUser(user);
    }

    public void deleteCategory(Long categoryId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Security check: Make sure the user owns this category
        if (!category.getUser().getId().equals(user.getId())) {
            throw new SecurityException("User does not have permission to delete this category");
        }
        categoryRepository.deleteById(categoryId);
    }
}