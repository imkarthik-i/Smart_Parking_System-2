package com.parking.exception;

import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO for standardized error responses returned by the API.
 * <p>
 * Provides a consistent error format across all exception handlers,
 * including timestamp, HTTP status code, error type, detailed message,
 * and the request path that caused the error.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {

    /**
     * Timestamp when the error occurred.
     */
    private LocalDateTime timestamp;

    /**
     * HTTP status code of the error response.
     */
    private int status;

    /**
     * Error type description (e.g., "Not Found", "Bad Request").
     */
    private String error;

    /**
     * Detailed error message explaining the cause.
     */
    private String message;

    /**
     * The URI path that produced the error.
     */
    private String path;
}