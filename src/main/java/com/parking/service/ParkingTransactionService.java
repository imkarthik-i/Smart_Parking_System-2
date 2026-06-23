package com.parking.service;

import com.parking.entity.ParkingTransaction;
import com.parking.entity.User;

import java.util.List;

/**
 * Service interface for managing parking transactions.
 * <p>
 * Defines business logic for vehicle entry, exit processing,
 * and transaction history retrieval.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
public interface ParkingTransactionService {

    /**
     * Records a vehicle entry into a parking slot and starts a new transaction.
     *
     * @param vehicleNumber the license plate of the entering vehicle
     * @param slotId        the parking slot identifier
     * @return the created parking transaction entity
     * @throws com.parking.exception.ResourceNotFoundException if vehicle or slot not found
     * @throws com.parking.exception.SlotNotAvailableException if the slot is occupied
     */
    ParkingTransaction vehicleEntry(String vehicleNumber, Long slotId);

    /**
     * Processes a vehicle exit, completing the active transaction.
     *
     * @param transactionId the active transaction identifier
     * @return the completed parking transaction entity
     * @throws com.parking.exception.ResourceNotFoundException if transaction not found
     */
    ParkingTransaction vehicleExit(Long transactionId);

    /**
     * Retrieves a transaction by its identifier.
     *
     * @param id the transaction identifier
     * @return the parking transaction entity
     * @throws com.parking.exception.ResourceNotFoundException if transaction not found
     */
    ParkingTransaction getTransaction(Long id);

    /**
     * Retrieves all transactions for a specific user.
     *
     * @param user the user entity
     * @return list of transactions belonging to the user
     */
    List<ParkingTransaction> getTransactionsByUser(User user);

    /**
     * Retrieves all parking transactions.
     *
     * @return list of all transactions
     */
    List<ParkingTransaction> getAllTransactions();
}