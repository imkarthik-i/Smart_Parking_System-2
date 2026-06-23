package com.parking.exception;

/**
 * Exception thrown when a requested resource is not found in the system.
 * <p>
 * Used throughout service and controller layers when database lookups
 * fail to find an entity by its identifier. Maps to a 404 HTTP response
 * via the {@link GlobalExceptionHandler}.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
public class ResourceNotFoundException
        extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with a descriptive message.
     *
     * @param message the detail message explaining which resource was not found
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}