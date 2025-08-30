package com.bibek.fintracker.financial_tracker_api.controller;

import com.bibek.fintracker.financial_tracker_api.dto.CategoryDto;
import com.bibek.fintracker.financial_tracker_api.model.Category;
import com.bibek.fintracker.financial_tracker_api.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto, Principal principal) {
        Category newCategory = categoryService.createCategory(categoryDto.getName(), principal.getName());
        return ResponseEntity.ok(convertToDto(newCategory));
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getUserCategories(Principal principal) {
        List<Category> categories = categoryService.getCategoriesByUsername(principal.getName());
        List<CategoryDto> categoryDtos = categories.stream().map(this::convertToDto).collect(Collectors.toList());
        return ResponseEntity.ok(categoryDtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id, Principal principal) {
        categoryService.deleteCategory(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

    private CategoryDto convertToDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }
}