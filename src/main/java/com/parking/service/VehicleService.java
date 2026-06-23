package com.parking.service;

import com.parking.entity.User;
import com.parking.entity.Vehicle;

import java.util.List;

/**
 * Service interface for managing vehicle operations.
 * <p>
 * Defines business logic for vehicle registration, lookup by
 * registration number, user-specific retrieval, and vehicle
 * profile updates.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
public interface VehicleService {

    /**
     * Registers a new vehicle in the system.
     *
     * @param vehicle the vehicle entity to persist
     * @return the persisted vehicle entity
     */
    Vehicle saveVehicle(Vehicle vehicle);

    /**
     * Retrieves a vehicle by its identifier.
     *
     * @param id the vehicle identifier
     * @return the vehicle entity
     * @throws com.parking.exception.ResourceNotFoundException if the vehicle is not found
     */
    Vehicle getVehicle(Long id);

    /**
     * Retrieves a vehicle by its registration number.
     *
     * @param vehicleNumber the license plate number
     * @return the vehicle entity
     * @throws com.parking.exception.ResourceNotFoundException if the vehicle is not found
     */
    Vehicle getByVehicleNumber(String vehicleNumber);

    /**
     * Retrieves all registered vehicles.
     *
     * @return list of all vehicles
     */
    List<Vehicle> getAllVehicles();

    /**
     * Retrieves all vehicles belonging to a specific user.
     *
     * @param user the user entity
     * @return list of vehicles owned by the user
     */
    List<Vehicle> getVehiclesByUser(User user);

    /**
     * Updates an existing vehicle's information.
     *
     * @param id      the vehicle identifier
     * @param vehicle the updated vehicle data
     * @return the updated vehicle entity
     * @throws com.parking.exception.ResourceNotFoundException if the vehicle is not found
     */
    Vehicle updateVehicle(Long id, Vehicle vehicle);

    /**
     * Deletes a vehicle by its identifier.
     *
     * @param id the vehicle identifier
     * @throws com.parking.exception.ResourceNotFoundException if the vehicle is not found
     */
    void deleteVehicle(Long id);
}
