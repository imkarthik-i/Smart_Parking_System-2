package com.parking.enums;

/**
 * Enumeration of possible states for a parking transaction.
 * <p>
 * <ul>
 *   <li>{@code ACTIVE} - Vehicle is currently parked and the transaction is ongoing</li>
 *   <li>{@code COMPLETED} - Vehicle has exited and the transaction is finished</li>
 * </ul>
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
public enum TransactionStatus {
    ACTIVE,
    COMPLETED
}