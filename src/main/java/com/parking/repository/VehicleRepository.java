package com.parking.repository;

import com.parking.entity.User;
import com.parking.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

/**
 * Repository interface for managing {@link Vehicle} entities.
 * <p>
 * Provides lookup methods for vehicle registration numbers,
 * owner-based searches, and user-specific vehicle listings.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    /**
     * Finds a vehicle by its unique registration number.
     *
     * @param vehicleNumber the license plate to search for
     * @return an {@link Optional} containing the vehicle if found, or empty
     */
    Optional<Vehicle> findByVehicleNumber(String vehicleNumber);

    /**
     * Checks whether a vehicle with the given registration number exists.
     *
     * @param vehicleNumber the license plate to check
     * @return {@code true} if a vehicle with the number exists, {@code false} otherwise
     */
    boolean existsByVehicleNumber(String vehicleNumber);

    /**
     * Finds all vehicles registered under a specific owner name.
     *
     * @param ownerName the name of the owner
     * @return list of vehicles owned by the given name
     */
    List<Vehicle> findByOwnerName(String ownerName);

    /**
     * Finds all vehicles associated with a specific user.
     *
     * @param user the user entity whose vehicles to retrieve
     * @return list of vehicles owned by the user
     */
    List<Vehicle> findByUser(User user);
}