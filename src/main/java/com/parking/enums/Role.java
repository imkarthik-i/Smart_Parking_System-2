package com.parking.enums;

/**
 * Enumeration of user roles for role-based access control.
 * <p>
 * Defines the available roles within the system:
 * <ul>
 *   <li>{@code ROLE_ADMIN} - Administrator with full system access</li>
 *   <li>{@code ROLE_CUSTOMER} - Regular customer with limited access</li>
 * </ul>
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
public enum Role {
    ROLE_ADMIN,
    ROLE_CUSTOMER
}