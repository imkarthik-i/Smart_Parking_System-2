package com.parking.service.impl;

import com.parking.entity.User;
import com.parking.entity.Vehicle;
import com.parking.exception.ResourceNotFoundException;
import com.parking.repository.VehicleRepository;
import com.parking.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link VehicleService} for managing vehicle operations.
 * <p>
 * Handles vehicle registration, lookup by various criteria, profile
 * updates, and deletion. Validates vehicle existence before operations.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vehicle getVehicle(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id : " + id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vehicle getByVehicleNumber(String vehicleNumber) {
        return vehicleRepository.findByVehicleNumber(vehicleNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with number : " + vehicleNumber));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Vehicle> getVehiclesByUser(User user) {
        return vehicleRepository.findByUser(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vehicle updateVehicle(Long id, Vehicle updated) {
        Vehicle vehicle = getVehicle(id);
        vehicle.setVehicleNumber(updated.getVehicleNumber());
        vehicle.setVehicleType(updated.getVehicleType());
        vehicle.setOwnerName(updated.getOwnerName());
        vehicle.setMobileNumber(updated.getMobileNumber());
        return vehicleRepository.save(vehicle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteVehicle(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id : " + id));
        vehicleRepository.delete(vehicle);
    }
}
