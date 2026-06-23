package com.parking.entity;

import com.parking.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity representing a payment transaction for parking billing.
 * <p>
 * Records the payment amount, method used, status, and timestamp.
 * Each payment is linked to exactly one billing record.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    /**
     * Unique identifier for the payment record.
     * Auto-generated using identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    /**
     * Amount paid for the parking service.
     */
    private Double amount;

    /**
     * Payment method used (e.g., CASH, UPI, CARD).
     */
    private String paymentMethod;

    /**
     * Current status of the payment (e.g., PAID, PENDING, FAILED).
     */
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    /**
     * Timestamp when the payment was processed.
     */
    private LocalDateTime paymentTime;

    @OneToOne
    @JoinColumn(name = "billing_id", unique = true)
    private Billing billing;
}