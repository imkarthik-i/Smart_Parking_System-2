package com.parking.repository;

import com.parking.entity.ParkingLot;
import com.parking.entity.ParkingSlot;
import com.parking.enums.SlotStatus;
import com.parking.enums.SlotType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link ParkingSlot} entities.
 * <p>
 * Provides comprehensive slot querying capabilities including
 * status-based filtering, type-based lookups, floor searches,
 * and lot-specific availability checks.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
public interface ParkingSlotRepository
        extends JpaRepository<ParkingSlot, Long> {

    /**
     * Finds a slot by its slot number across all lots.
     *
     * @param slotNumber the slot identifier
     * @return an {@link Optional} containing the slot if found, or empty
     */
    Optional<ParkingSlot> findBySlotNumber(String slotNumber);

    /**
     * Finds a slot by its number within a specific parking lot.
     *
     * @param slotNumber the slot identifier
     * @param parkingLot the parent parking lot
     * @return an {@link Optional} containing the slot if found, or empty
     */
    Optional<ParkingSlot> findBySlotNumberAndParkingLot(String slotNumber, ParkingLot parkingLot);

    /**
     * Checks whether a slot number already exists within a specific lot.
     *
     * @param slotNumber the slot identifier to check
     * @param parkingLot the parent parking lot
     * @return {@code true} if the slot exists, {@code false} otherwise
     */
    boolean existsBySlotNumberAndParkingLot(String slotNumber, ParkingLot parkingLot);

    /**
     * Finds all slots with a specific status.
     *
     * @param status the slot status to filter by
     * @return list of slots matching the given status
     */
    List<ParkingSlot> findByStatus(SlotStatus status);

    /**
     * Finds all slots of a specific type.
     *
     * @param slotType the slot type to filter by
     * @return list of slots matching the given type
     */
    List<ParkingSlot> findBySlotType(SlotType slotType);

    /**
     * Finds slots matching both status and type criteria.
     *
     * @param status   the slot status
     * @param slotType the slot type
     * @return list of slots matching both criteria
     */
    List<ParkingSlot> findByStatusAndSlotType(
            SlotStatus status,
            SlotType slotType);

    /**
     * Finds all slots on a specific floor.
     *
     * @param floorNumber the floor number
     * @return list of slots on the given floor
     */
    List<ParkingSlot> findByFloorNumber(Integer floorNumber);

    /**
     * Counts slots with a specific status.
     *
     * @param status the status to count
     * @return the count of slots with the given status
     */
    long countByStatus(SlotStatus status);

    /**
     * Counts slots within a specific lot and status.
     *
     * @param parkingLot the parent parking lot
     * @param status     the slot status
     * @return the count of matching slots
     */
    long countByParkingLotAndStatus(ParkingLot parkingLot, SlotStatus status);

    /**
     * Finds all slots belonging to a specific parking lot.
     *
     * @param parkingLot the parent parking lot
     * @return list of slots in the given lot
     */
    List<ParkingSlot> findByParkingLot(ParkingLot parkingLot);

    /**
     * Finds slots in a specific lot matching both type and status.
     *
     * @param parkingLot the parent parking lot
     * @param slotType   the slot type
     * @param status     the slot status
     * @return list of matching slots
     */
    List<ParkingSlot> findByParkingLotAndSlotTypeAndStatus(ParkingLot parkingLot, SlotType slotType, SlotStatus status);

    /**
     * Finds slots in a specific lot with a given status.
     *
     * @param parkingLot the parent parking lot
     * @param status     the slot status
     * @return list of matching slots
     */
    List<ParkingSlot> findByParkingLotAndStatus(ParkingLot parkingLot, SlotStatus status);
}