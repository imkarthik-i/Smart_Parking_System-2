package com.parking.config;

import com.parking.entity.User;
import com.parking.enums.Role;
import com.parking.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for initializing seed data on application startup.
 * <p>
 * Creates a default admin account with username "admin" and a predefined
 * password if no admin user exists in the database. This ensures the
 * system has an initial administrative user for first-time setup.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
@Configuration
public class DataInitializer {

    /**
     * Creates a {@link CommandLineRunner} that checks for and creates
     * the default admin account if it does not already exist.
     *
     * @param userRepository   the user repository for persistence
     * @param passwordEncoder  the password encoder for secure hashing
     * @return a command-line runner that runs after application startup
     */
    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                User admin = User.builder()
                        .username("admin")
                        .email("admin@parking.com")
                        .password(passwordEncoder.encode("Admin@123"))
                        .role(Role.ROLE_ADMIN)
                        .status("ACTIVE")
                        .build();
                userRepository.save(admin);
            }
        };
    }
}
