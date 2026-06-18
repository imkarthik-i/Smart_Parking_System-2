package com.parking.repository;

import com.parking.entity.User;
import com.parking.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByVehicleNumber(String vehicleNumber);

    boolean existsByVehicleNumber(String vehicleNumber);

    List<Vehicle> findByOwnerName(String ownerName);

    List<Vehicle> findByUser(User user);
}