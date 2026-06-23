package com.parking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Entity representing a parking lot facility.
 * <p>
 * Contains configuration details such as total capacity and
 * slot distribution by vehicle type. A parking lot consists
 * of multiple parking slots.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
@Entity
@Table(name = "parking_lots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingLot {

    /**
     * Unique identifier for the parking lot.
     * Auto-generated using identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lotId;

    /**
     * Name or label of the parking lot.
     */
    private String lotName;

    /**
     * Physical address or location description of the parking lot.
     */
    private String location;

    /**
     * Total number of parking slots available in this lot.
     */
    private Integer totalSlots;

    /**
     * Number of slots designated for cars.
     */
    private Integer carSlots;

    /**
     * Number of slots designated for bikes.
     */
    private Integer bikeSlots;

    /**
     * Number of slots designated for electric vehicles (EVs).
     */
    private Integer evSlots;

    @OneToMany(mappedBy = "parkingLot",
            cascade = CascadeType.ALL)
    private List<ParkingSlot> parkingSlots;
}