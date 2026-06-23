package com.parking.entity;

import com.parking.enums.SlotStatus;
import com.parking.enums.SlotType;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing an individual parking slot within a parking lot.
 * <p>
 * Each slot has a unique combination of slot number and lot identifier.
 * Tracks the slot type, current availability status, and floor location.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
@Entity
@Table(
    name = "parking_slots",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"slot_number", "lot_id"}
    )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingSlot {

    /**
     * Unique identifier for the parking slot.
     * Auto-generated using identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long slotId;

    /**
     * Display identifier or label for the slot within its lot.
     */
    @Column(nullable = false)
    private String slotNumber;

    /**
     * Type of the slot indicating the compatible vehicle type (e.g., CAR, BIKE, EV).
     */
    @Enumerated(EnumType.STRING)
    private SlotType slotType;

    /**
     * Current availability status of the slot (e.g., AVAILABLE, OCCUPIED, RESERVED).
     * Defaults to AVAILABLE.
     */
    @Enumerated(EnumType.STRING)
    private SlotStatus status = SlotStatus.AVAILABLE;

    /**
     * Floor level within the parking lot where this slot is located.
     */
    private Integer floorNumber;

    @ManyToOne
    @JoinColumn(name = "lot_id")
    private ParkingLot parkingLot;
}