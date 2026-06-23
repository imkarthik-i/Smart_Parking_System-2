package com.parking.enums;

/**
 * Enumeration of possible states for a parking reservation.
 * <p>
 * <ul>
 *   <li>{@code PENDING} - Reservation has been created but not yet confirmed</li>
 *   <li>{@code CONFIRMED} - Reservation has been confirmed and the slot is reserved</li>
 *   <li>{@code CANCELLED} - Reservation has been cancelled</li>
 * </ul>
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
public enum ReservationStatus {
    PENDING,
    CONFIRMED,
    CANCELLED
}