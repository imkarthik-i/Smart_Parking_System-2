package com.parking.service;

import com.parking.entity.ParkingLot;

import java.util.List;

/**
 * Service interface for managing parking lot operations.
 * <p>
 * Defines business logic for creating, updating, and managing
 * parking lot facilities and their configurations.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
public interface ParkingLotService {

    /**
     * Creates a new parking lot with the given configuration.
     *
     * @param lot the parking lot entity to create
     * @return the created parking lot entity
     */
    ParkingLot createLot(ParkingLot lot);

    /**
     * Retrieves a parking lot by its identifier.
     *
     * @param id the parking lot identifier
     * @return the parking lot entity
     * @throws com.parking.exception.ResourceNotFoundException if the lot is not found
     */
    ParkingLot getLot(Long id);

    /**
     * Retrieves all parking lots in the system.
     *
     * @return list of all parking lots
     */
    List<ParkingLot> getAllLots();

    /**
     * Updates an existing parking lot's configuration.
     *
     * @param id  the parking lot identifier
     * @param lot the updated parking lot data
     * @return the updated parking lot entity
     * @throws com.parking.exception.ResourceNotFoundException if the lot is not found
     */
    ParkingLot updateLot(Long id, ParkingLot lot);

    /**
     * Deletes a parking lot by its identifier.
     *
     * @param id the parking lot identifier
     * @throws com.parking.exception.ResourceNotFoundException if the lot is not found
     */
    void deleteLot(Long id);
}
