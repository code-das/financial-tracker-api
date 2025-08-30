package com.bibek.fintracker.financial_tracker_api.controller;

import com.bibek.fintracker.financial_tracker_api.dto.CreateRuleRequest;
import com.bibek.fintracker.financial_tracker_api.dto.RuleDto;
import com.bibek.fintracker.financial_tracker_api.model.CategorizationRule;
import com.bibek.fintracker.financial_tracker_api.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rules")
public class RuleController {

    @Autowired
    private RuleService ruleService;

    @PostMapping
    public ResponseEntity<RuleDto> createRule(@RequestBody CreateRuleRequest request, Principal principal) {
        CategorizationRule newRule = ruleService.createRule(request.getKeyword(), request.getCategoryId(), principal.getName());
        return ResponseEntity.ok(convertToDto(newRule));
    }

    @GetMapping
    public ResponseEntity<List<RuleDto>> getUserRules(Principal principal) {
        List<CategorizationRule> rules = ruleService.getRulesByUsername(principal.getName());
        List<RuleDto> ruleDtos = rules.stream().map(this::convertToDto).collect(Collectors.toList());
        return ResponseEntity.ok(ruleDtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable Long id, Principal principal) {
        ruleService.deleteRule(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

    private RuleDto convertToDto(CategorizationRule rule) {
        RuleDto dto = new RuleDto();
        dto.setId(rule.getId());
        dto.setKeyword(rule.getKeyword());
        dto.setCategoryId(rule.getCategory().getId());
        dto.setCategoryName(rule.getCategory().getName());
        return dto;
    }
}