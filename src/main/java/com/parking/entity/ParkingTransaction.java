package com.parking.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.parking.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity representing a parking transaction capturing the entry and exit lifecycle.
 * <p>
 * Records the vehicle entry time, exit time, duration of stay, and the
 * current transaction status. Links a vehicle and a parking slot with
 * an optional billing record.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
@Entity
@Table(name = "parking_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingTransaction {

    /**
     * Unique identifier for the transaction.
     * Auto-generated using identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    /**
     * Timestamp of vehicle entry into the parking slot.
     */
    private LocalDateTime entryTime;

    /**
     * Timestamp of vehicle exit from the parking slot.
     */
    private LocalDateTime exitTime;

    /**
     * Duration of the parking stay in hours.
     */
    private Double duration;

    /**
     * Current transaction status (e.g., ACTIVE, COMPLETED).
     */
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    @JsonBackReference
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "slot_id")
    @JsonBackReference
    private ParkingSlot parkingSlot;

    @OneToOne(mappedBy = "transaction")
    @JsonIgnore
    private Billing billing;
}