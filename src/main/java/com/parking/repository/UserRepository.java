package com.parking.repository;

import com.parking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link User} entities.
 * <p>
 * Provides data access methods for user authentication and
 * duplicate validation. Supports lookup by email and username.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email address.
     *
     * @param email the email to search for
     * @return an {@link Optional} containing the user if found, or empty
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds a user by their username.
     *
     * @param username the username to search for
     * @return an {@link Optional} containing the user if found, or empty
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks whether a user with the given email already exists.
     *
     * @param email the email to check
     * @return {@code true} if a user with the email exists, {@code false} otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Checks whether a user with the given username already exists.
     *
     * @param username the username to check
     * @return {@code true} if a user with the username exists, {@code false} otherwise
     */
    boolean existsByUsername(String username);
}