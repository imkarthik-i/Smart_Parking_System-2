package com.parking.repository;

import com.parking.entity.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingLotRepository
        extends JpaRepository<ParkingLot, Long> {
}