package com.parking.exception;

/**
 * Exception thrown when a requested parking slot is not available.
 * <p>
 * Occurs when attempting to reserve or occupy a slot that is already
 * occupied or reserved. Maps to a 400 Bad Request HTTP response via
 * the {@link GlobalExceptionHandler}.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
public class SlotNotAvailableException
        extends RuntimeException {

    /**
     * Constructs a new SlotNotAvailableException with a descriptive message.
     *
     * @param message the detail message explaining why the slot is unavailable
     */
    public SlotNotAvailableException(
            String message) {

        super(message);
    }
}