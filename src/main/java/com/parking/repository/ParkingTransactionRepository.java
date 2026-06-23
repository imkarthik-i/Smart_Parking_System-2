package com.parking.repository;

import com.parking.entity.ParkingSlot;
import com.parking.entity.ParkingTransaction;
import com.parking.entity.User;
import com.parking.entity.Vehicle;
import com.parking.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link ParkingTransaction} entities.
 * <p>
 * Provides methods to query parking transactions by vehicle, status,
 * and user. Supports finding active transactions per vehicle and
 * checking slot occupancy status.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
public interface ParkingTransactionRepository
        extends JpaRepository<ParkingTransaction, Long> {

    /**
     * Finds all transactions for a specific vehicle.
     *
     * @param vehicle the vehicle entity
     * @return list of transactions for the vehicle
     */
    List<ParkingTransaction> findByVehicle(Vehicle vehicle);

    /**
     * Finds all transactions with a specific status.
     *
     * @param status the transaction status to filter by
     * @return list of transactions matching the status
     */
    List<ParkingTransaction> findByStatus(TransactionStatus status);

    /**
     * Finds the active transaction for a specific vehicle.
     *
     * @param vehicle the vehicle entity
     * @param status  the transaction status
     * @return an {@link Optional} containing the matching transaction, or empty
     */
    Optional<ParkingTransaction> findByVehicleAndStatus(
            Vehicle vehicle,
            TransactionStatus status
    );

    /**
     * Checks whether a parking slot has an active transaction.
     *
     * @param slot   the parking slot
     * @param status the transaction status
     * @return {@code true} if the slot has an active transaction, {@code false} otherwise
     */
    boolean existsByParkingSlotAndStatus(
            ParkingSlot slot,
            TransactionStatus status
    );

    /**
     * Finds all transactions for a user, ordered by entry time descending.
     *
     * @param user the user entity
     * @return list of transactions ordered by most recent first
     */
    @Query("SELECT t FROM ParkingTransaction t WHERE t.vehicle.user = :user ORDER BY t.entryTime DESC")
    List<ParkingTransaction> findByUser(@Param("user") User user);

    /**
     * Finds transactions for a user filtered by status, ordered by entry time descending.
     *
     * @param user   the user entity
     * @param status the transaction status to filter by
     * @return list of matching transactions
     */
    @Query("SELECT t FROM ParkingTransaction t WHERE t.vehicle.user = :user AND t.status = :status ORDER BY t.entryTime DESC")
    List<ParkingTransaction> findByUserAndStatus(@Param("user") User user, @Param("status") TransactionStatus status);
}