package com.parking.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.parking.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing a system user.
 * <p>
 * Stores authentication credentials, role-based access control information,
 * and the user's operational status. Each user can own multiple vehicles.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    /**
     * Unique identifier for the user.
     * Auto-generated using identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    /**
     * Unique username for login authentication.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Unique email address of the user.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Encrypted password for authentication.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Role assigned to the user for authorization (e.g., ADMIN, CUSTOMER).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /**
     * Current status of the user account (e.g., ACTIVE, INACTIVE).
     * Defaults to "ACTIVE".
     */
    @Column(nullable = false)
    private String status = "ACTIVE";

    /**
     * Timestamp of account creation. Set once and not updatable.
     */
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate() {
        if (createdDate == null) {
            createdDate = LocalDateTime.now();
        }
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Vehicle> vehicles;
}
