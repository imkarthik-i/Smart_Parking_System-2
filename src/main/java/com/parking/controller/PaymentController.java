package com.parking.controller;

import com.parking.entity.Payment;
import com.parking.entity.User;
import com.parking.repository.UserRepository;
import com.parking.security.SecurityHelper;
import com.parking.service.PaymentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Payment APIs", description = "Payment processing system")
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final SecurityHelper securityHelper;
    private final UserRepository userRepository;

    @PostMapping("/pay/{billingId}")
    public Payment pay(
            @PathVariable Long billingId,
            @RequestParam String method) {
        return paymentService.makePayment(billingId, method);
    }

    @GetMapping("/my")
    public List<Payment> getMyPayments() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return paymentService.getPaymentsByUser(user);
    }

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    public Payment get(@PathVariable Long id) {
        return paymentService.getPayment(id);
    }
}
