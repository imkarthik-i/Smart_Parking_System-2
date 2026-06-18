package com.parking.controller;

import com.parking.entity.Billing;
import com.parking.entity.User;
import com.parking.enums.Role;
import com.parking.repository.UserRepository;
import com.parking.security.SecurityHelper;
import com.parking.service.BillingService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Billing APIs", description = "Billing generation and retrieval")
@RestController
@RequestMapping("/api/billing")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;
    private final SecurityHelper securityHelper;
    private final UserRepository userRepository;

    @PostMapping("/generate/{transactionId}")
    public Billing generateBill(@PathVariable Long transactionId) {
        if (!securityHelper.isAdmin()) {
            throw new RuntimeException("Only admins can generate bills");
        }
        return billingService.generateBill(transactionId);
    }

    @GetMapping("/my")
    public List<Billing> getMyBills() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return billingService.getBillsByUser(user);
    }

    @GetMapping
    public List<Billing> getAllBills() {
        return billingService.getAllBills();
    }

    @GetMapping("/{id}")
    public Billing getBill(@PathVariable Long id) {
        return billingService.getBill(id);
    }
}