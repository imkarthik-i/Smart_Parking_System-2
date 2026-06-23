package com.parking.repository;

import com.parking.entity.ParkingSlot;
import com.parking.entity.Reservation;
import com.parking.entity.User;
import com.parking.entity.Vehicle;
import com.parking.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository interface for managing {@link Reservation} entities.
 * <p>
 * Provides methods for querying reservations by vehicle, status,
 * parking slot, and user. Includes custom JPQL queries for
 * user-specific and status-filtered retrieval.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
public interface ReservationRepository
        extends JpaRepository<Reservation, Long> {

    /**
     * Finds all reservations for a specific vehicle.
     *
     * @param vehicle the vehicle entity
     * @return list of reservations for the vehicle
     */
    List<Reservation> findByVehicle(Vehicle vehicle);

    /**
     * Finds all reservations with a specific status.
     *
     * @param status the reservation status to filter by
     * @return list of reservations matching the status
     */
    List<Reservation> findByStatus(ReservationStatus status);

    /**
     * Checks if a parking slot has any active reservations with the given status.
     *
     * @param slot   the parking slot
     * @param status the reservation status to check
     * @return {@code true} if such a reservation exists, {@code false} otherwise
     */
    boolean existsByParkingSlotAndStatus(ParkingSlot slot, ReservationStatus status);

    /**
     * Finds all reservations for a specific parking slot.
     *
     * @param slot the parking slot
     * @return list of reservations for the slot
     */
    List<Reservation> findByParkingSlot(ParkingSlot slot);

    /**
     * Finds all reservations made by a specific user, ordered by reservation time descending.
     *
     * @param user the user entity
     * @return list of reservations ordered by most recent first
     */
    @Query("SELECT r FROM Reservation r WHERE r.vehicle.user = :user ORDER BY r.reservationTime DESC")
    List<Reservation> findByUser(@Param("user") User user);

    /**
     * Finds reservations by user filtered by status, ordered by start time ascending.
     *
     * @param user   the user entity
     * @param status the reservation status to filter by
     * @return list of matching reservations ordered by start time
     */
    @Query("SELECT r FROM Reservation r WHERE r.vehicle.user = :user AND r.status = :status ORDER BY r.startTime ASC")
    List<Reservation> findByUserAndStatus(@Param("user") User user, @Param("status") ReservationStatus status);
}