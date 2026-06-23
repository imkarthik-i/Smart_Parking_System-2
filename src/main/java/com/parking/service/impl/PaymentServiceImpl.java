package com.parking.service.impl;

import com.parking.dto.PaymentDTO;
import com.parking.entity.*;
import com.parking.enums.PaymentStatus;
import com.parking.exception.ResourceNotFoundException;
import com.parking.repository.*;
import com.parking.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of {@link PaymentService} for processing payments.
 * <p>
 * Handles payment creation against billing records with duplicate
 * payment protection and automatic billing status updates.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final BillingRepository billingRepository;
    private final PaymentRepository paymentRepository;

    /**
     * {@inheritDoc}
     * <p>
     * Validates that the billing record exists and has not already
     * been paid. Creates a new payment record with SUCCESS status
     * and updates the billing payment status to PAID.
     * </p>
     */
    @Override
    @Transactional
    public PaymentDTO makePayment(Long billingId, String method) {

        Billing billing = billingRepository.findById(billingId)
                .orElseThrow(() -> new ResourceNotFoundException("Billing not found"));

        if ("PAID".equals(billing.getPaymentStatus())) {
            throw new RuntimeException("Already paid");
        }

        Payment payment = Payment.builder()
                .amount(billing.getTotalAmount())
                .paymentMethod(method)
                .status(PaymentStatus.SUCCESS)
                .paymentTime(LocalDateTime.now())
                .billing(billing)
                .build();

        billing.setPaymentStatus("PAID");
        billingRepository.save(billing);

        paymentRepository.save(payment);

        return paymentRepository.findPaymentDTOById(payment.getPaymentId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found after save"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Payment getPaymentEntity(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PaymentDTO getPayment(Long paymentId) {
        return paymentRepository.findPaymentDTOById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAllPaymentDTOs();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PaymentDTO> getPaymentsByUser(User user) {
        return paymentRepository.findPaymentDTOsByUser(user);
    }
}
