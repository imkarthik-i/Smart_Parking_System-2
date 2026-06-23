package com.parking.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application.
 * <p>
 * Uses {@link RestControllerAdvice} to provide centralized exception
 * handling across all controllers. Maps various exception types to
 * appropriate HTTP status codes with standardized error response
 * format. Handles resource not found, slot unavailability, bad
 * credentials, validation errors, and generic exceptions.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link ResourceNotFoundException} and returns a 404 Not Found response.
     *
     * @param ex      the exception thrown
     * @param request the current HTTP request
     * @return a 404 response with error details
     */
    @ExceptionHandler(
            ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse>
    handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        ErrorResponse response =
                ErrorResponse.builder()
                        .timestamp(
                                LocalDateTime.now())
                        .status(404)
                        .error("Not Found")
                        .message(ex.getMessage())
                        .path(
                                request.getRequestURI())
                        .build();

        return new ResponseEntity<>(
                response,
                HttpStatus.NOT_FOUND);
    }

    /**
     * Handles {@link SlotNotAvailableException} and returns a 400 Bad Request response.
     *
     * @param ex      the exception thrown
     * @param request the current HTTP request
     * @return a 400 response with error details
     */
    @ExceptionHandler(
            SlotNotAvailableException.class)
    public ResponseEntity<ErrorResponse>
    handleSlotException(
            SlotNotAvailableException ex,
            HttpServletRequest request) {

        ErrorResponse response =
                ErrorResponse.builder()
                        .timestamp(
                                LocalDateTime.now())
                        .status(400)
                        .error("Bad Request")
                        .message(ex.getMessage())
                        .path(
                                request.getRequestURI())
                        .build();

        return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link BadCredentialsException} thrown during authentication failures.
     *
     * @param ex      the exception thrown
     * @param request the current HTTP request
     * @return a 401 Unauthorized response with error details
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse>
    handleBadCredentials(
            BadCredentialsException ex,
            HttpServletRequest request) {

        ErrorResponse response =
                ErrorResponse.builder()
                        .timestamp(
                                LocalDateTime.now())
                        .status(401)
                        .error("Unauthorized")
                        .message(ex.getMessage())
                        .path(
                                request.getRequestURI())
                        .build();

        return new ResponseEntity<>(
                response,
                HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles validation errors from {@code @Valid} annotated request bodies.
     *
     * @param ex the validation exception
     * @return a 400 response with field-specific validation error messages
     */
    @ExceptionHandler(
            MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>>
    handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String,String> errors =
                new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()));

        return new ResponseEntity<>(
                errors,
                HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all unhandled exceptions as a fallback.
     *
     * @param ex      the exception thrown
     * @param request the current HTTP request
     * @return a 500 Internal Server Error response with error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse>
    handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        ErrorResponse response =
                ErrorResponse.builder()
                        .timestamp(
                                LocalDateTime.now())
                        .status(500)
                        .error("Internal Server Error")
                        .message(ex.getMessage())
                        .path(
                                request.getRequestURI())
                        .build();

        return new ResponseEntity<>(
                response,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}