package com.parking.enums;

/**
 * Enumeration of possible states for a parking slot.
 * <p>
 * <ul>
 *   <li>{@code AVAILABLE} - Slot is free and ready for use</li>
 *   <li>{@code RESERVED} - Slot has been booked for a future time</li>
 *   <li>{@code OCCUPIED} - Slot is currently in use by a vehicle</li>
 * </ul>
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
public enum SlotStatus {
    AVAILABLE,
    RESERVED,
    OCCUPIED
}