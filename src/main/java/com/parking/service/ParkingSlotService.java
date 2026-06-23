package com.parking.service;

import com.parking.entity.ParkingLot;
import com.parking.entity.ParkingSlot;
import com.parking.enums.SlotStatus;
import com.parking.enums.SlotType;

import java.util.List;

/**
 * Service interface for managing parking slot operations.
 * <p>
 * Defines business logic for slot management including
 * availability queries filtered by status, type, and lot.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
public interface ParkingSlotService {

    /**
     * Adds a new parking slot to the system.
     *
     * @param slot the parking slot entity to add
     * @return the created parking slot entity
     */
    ParkingSlot addSlot(ParkingSlot slot);

    /**
     * Retrieves all parking slots.
     *
     * @return list of all parking slots
     */
    List<ParkingSlot> getAllSlots();

    /**
     * Retrieves all currently available parking slots.
     *
     * @return list of available slots
     */
    List<ParkingSlot> getAvailableSlots();

    /**
     * Retrieves available slots filtered by slot type.
     *
     * @param slotType the type of slot to filter by
     * @return list of available slots of the specified type
     */
    List<ParkingSlot> getAvailableSlotsByType(SlotType slotType);

    /**
     * Retrieves a parking slot by its identifier.
     *
     * @param id the slot identifier
     * @return the parking slot entity
     * @throws com.parking.exception.ResourceNotFoundException if the slot is not found
     */
    ParkingSlot getSlot(Long id);

    /**
     * Updates an existing parking slot's information.
     *
     * @param id   the slot identifier
     * @param slot the updated slot data
     * @return the updated parking slot entity
     * @throws com.parking.exception.ResourceNotFoundException if the slot is not found
     */
    ParkingSlot updateSlot(Long id, ParkingSlot slot);

    /**
     * Deletes a parking slot by its identifier.
     *
     * @param id the slot identifier
     * @throws com.parking.exception.ResourceNotFoundException if the slot is not found
     */
    void deleteSlot(Long id);

    /**
     * Retrieves available slots for a specific lot and type.
     *
     * @param lot      the parent parking lot
     * @param slotType the type of slot required
     * @return list of matching available slots
     */
    List<ParkingSlot> getAvailableSlotsByLotAndType(ParkingLot lot, SlotType slotType);
}
