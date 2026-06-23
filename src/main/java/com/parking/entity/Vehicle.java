package com.parking.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Entity representing a vehicle registered in the system.
 * <p>
 * Each vehicle is associated with a user and can have multiple
 * reservations and parking transactions. Vehicles are uniquely
 * identified by their registration number.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    /**
     * Unique identifier for the vehicle record.
     * Auto-generated using identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vehicleId;

    /**
     * Unique vehicle registration number (license plate).
     */
    @Column(nullable = false, unique = true)
    private String vehicleNumber;

    /**
     * Type of vehicle (e.g., CAR, BIKE, EV).
     */
    private String vehicleType;

    /**
     * Name of the vehicle owner.
     */
    private String ownerName;

    /**
     * Mobile contact number of the vehicle owner.
     */
    private String mobileNumber;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @OneToMany(
            mappedBy = "vehicle",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    private List<Reservation> reservations;

    @OneToMany(
            mappedBy = "vehicle",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    private List<ParkingTransaction> transactions;
}