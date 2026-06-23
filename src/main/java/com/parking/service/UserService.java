package com.parking.service;

import com.parking.entity.User;

import java.util.List;

/**
 * Service interface for managing user operations.
 * <p>
 * Defines business logic for user CRUD, account activation
 * and deactivation, and user profile updates.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
public interface UserService {

    /**
     * Saves a new user to the system.
     *
     * @param user the user entity to persist
     * @return the persisted user entity
     */
    User saveUser(User user);

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the user identifier
     * @return the user entity
     * @throws com.parking.exception.ResourceNotFoundException if the user is not found
     */
    User getUserById(Long id);

    /**
     * Retrieves all registered users.
     *
     * @return list of all users
     */
    List<User> getAllUsers();

    /**
     * Deletes a user by their identifier.
     *
     * @param id the user identifier
     * @throws com.parking.exception.ResourceNotFoundException if the user is not found
     */
    void deleteUser(Long id);

    /**
     * Activates a user account.
     *
     * @param id the user identifier
     * @return the updated user entity with ACTIVE status
     * @throws com.parking.exception.ResourceNotFoundException if the user is not found
     */
    User activateUser(Long id);

    /**
     * Deactivates a user account.
     *
     * @param id the user identifier
     * @return the updated user entity with INACTIVE status
     * @throws com.parking.exception.ResourceNotFoundException if the user is not found
     */
    User deactivateUser(Long id);

    /**
     * Updates the profile information of an existing user.
     *
     * @param id      the user identifier
     * @param updated the updated user data
     * @return the updated user entity
     * @throws com.parking.exception.ResourceNotFoundException if the user is not found
     */
    User updateUser(Long id, User updated);
}
