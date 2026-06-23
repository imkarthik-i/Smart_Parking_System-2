package com.parking.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing a billing record associated with a parking transaction.
 * <p>
 * Stores the hourly rate, computed total amount, and payment status.
 * Each billing record is linked to exactly one parking transaction.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
@Entity
@Table(name = "billing")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Billing {

    /**
     * Unique identifier for the billing record.
     * Auto-generated using identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billingId;

    /**
     * Rate charged per hour of parking.
     */
    private Double ratePerHour;

    /**
     * Total amount calculated for the parking duration.
     */
    private Double totalAmount;

    /**
     * Payment status of this billing record (e.g., PENDING, PAID).
     */
    private String paymentStatus;

    @OneToOne
    @JoinColumn(name = "transaction_id")
    private ParkingTransaction transaction;
}