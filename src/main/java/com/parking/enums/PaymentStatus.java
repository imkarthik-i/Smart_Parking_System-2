package com.parking.enums;

/**
 * Enumeration of possible states for a payment transaction.
 * <p>
 * <ul>
 *   <li>{@code PENDING} - Payment is initiated but not yet processed</li>
 *   <li>{@code SUCCESS} - Payment has been completed successfully</li>
 *   <li>{@code FAILED} - Payment processing has failed</li>
 * </ul>
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
public enum PaymentStatus {
    PENDING,
    SUCCESS,
    FAILED
}