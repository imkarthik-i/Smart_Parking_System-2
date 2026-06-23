package com.parking.security;

import com.parking.entity.User;
import com.parking.enums.Role;
import com.parking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Utility component for retrieving security context information.
 * <p>
 * Provides convenient methods for accessing the currently authenticated
 * user's details, including username, full user entity, user ID, role,
 * and admin status checks. Used throughout controllers for authorization.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class SecurityHelper {

    private final UserRepository userRepository;

    /**
     * Retrieves the username of the currently authenticated user.
     *
     * @return the current username from the security context
     */
    public String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * Retrieves the full {@link User} entity for the currently authenticated user.
     *
     * @return the current user entity
     * @throws RuntimeException if the user is not found in the database
     */
    public User getCurrentUser() {
        String username = getCurrentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Retrieves the user ID of the currently authenticated user.
     *
     * @return the current user's ID
     */
    public Long getCurrentUserId() {
        return getCurrentUser().getUserId();
    }

    /**
     * Retrieves the role of the currently authenticated user.
     *
     * @return the current user's role
     */
    public Role getCurrentUserRole() {
        return getCurrentUser().getRole();
    }

    /**
     * Checks whether the currently authenticated user has admin privileges.
     *
     * @return {@code true} if the user is an admin, {@code false} otherwise
     */
    public boolean isAdmin() {
        return getCurrentUserRole() == Role.ROLE_ADMIN;
    }
}