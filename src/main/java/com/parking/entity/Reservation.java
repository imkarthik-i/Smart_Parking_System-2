package com.parking.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.parking.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity representing a slot reservation made by a vehicle owner.
 * <p>
 * Tracks the reservation window (start and end time), the current
 * status of the reservation, and associates the reservation with
 * a specific vehicle and parking slot.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    /**
     * Unique identifier for the reservation.
     * Auto-generated using identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    /**
     * Timestamp when the reservation was created.
     */
    private LocalDateTime reservationTime;

    /**
     * Scheduled start time for the reservation.
     */
    private LocalDateTime startTime;

    /**
     * Scheduled end time for the reservation.
     */
    private LocalDateTime endTime;

    /**
     * Current status of the reservation (e.g., ACTIVE, COMPLETED, CANCELLED).
     */
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    @JsonBackReference
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "slot_id")
    private ParkingSlot parkingSlot;
}