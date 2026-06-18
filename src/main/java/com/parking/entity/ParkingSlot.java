package com.parking.entity;

import com.parking.enums.SlotStatus;
import com.parking.enums.SlotType;
import jakarta.persistence.*;
import lombok.*;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long slotId;

    @Column(nullable = false)
    private String slotNumber;

    @Enumerated(EnumType.STRING)
    private SlotType slotType;

    @Enumerated(EnumType.STRING)
    private SlotStatus status = SlotStatus.AVAILABLE;

    private Integer floorNumber;

    @ManyToOne
    @JoinColumn(name = "lot_id")
    private ParkingLot parkingLot;
}