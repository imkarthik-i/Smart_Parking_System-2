package com.parking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "parking_lots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lotId;

    private String lotName;

    private String location;

    private Integer totalSlots;

    @OneToMany(mappedBy = "parkingLot",
            cascade = CascadeType.ALL)
    private List<ParkingSlot> parkingSlots;
}