package com.parking.service;

import com.parking.entity.Reservation;
import com.parking.entity.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for managing parking reservations.
 * <p>
 * Defines business logic for creating reservations (immediate
 * or scheduled), cancelling existing reservations, and
 * retrieving reservation information.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
public interface ReservationService {

    /**
     * Creates an immediate reservation for a vehicle at a parking slot.
     *
     * @param vehicleId the identifier of the vehicle
     * @param slotId    the identifier of the parking slot
     * @return the created reservation entity
     * @throws com.parking.exception.ResourceNotFoundException if vehicle or slot not found
     * @throws com.parking.exception.SlotNotAvailableException if the slot is not available
     */
    Reservation createReservation(Long vehicleId, Long slotId);

    /**
     * Creates a scheduled reservation with a specific time window.
     *
     * @param vehicleId the identifier of the vehicle
     * @param slotId    the identifier of the parking slot
     * @param startTime the scheduled start time
     * @param endTime   the scheduled end time
     * @return the created reservation entity
     * @throws com.parking.exception.ResourceNotFoundException if vehicle or slot not found
     * @throws com.parking.exception.SlotNotAvailableException if the slot is not available
     */
    Reservation createReservation(Long vehicleId, Long slotId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * Cancels an existing reservation.
     *
     * @param reservationId the reservation identifier
     * @return the cancelled reservation entity
     * @throws com.parking.exception.ResourceNotFoundException if the reservation is not found
     */
    Reservation cancelReservation(Long reservationId);

    /**
     * Retrieves a reservation by its identifier.
     *
     * @param id the reservation identifier
     * @return the reservation entity
     * @throws com.parking.exception.ResourceNotFoundException if the reservation is not found
     */
    Reservation getReservationById(Long id);

    /**
     * Retrieves all reservations in the system.
     *
     * @return list of all reservations
     */
    List<Reservation> getAllReservations();

    /**
     * Retrieves all reservations made by a specific user.
     *
     * @param user the user entity
     * @return list of reservations belonging to the user
     */
    List<Reservation> getReservationsByUser(User user);
}