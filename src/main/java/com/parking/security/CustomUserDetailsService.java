package com.parking.security;

import com.parking.entity.User;
import com.parking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Custom implementation of Spring Security's {@link UserDetailsService}.
 * <p>
 * Loads user details from the database by username during authentication.
 * Converts the application's {@link User} entity into a Spring Security
 * {@link UserDetails} object with the user's role mapped as a granted authority.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Loads a user by their username and returns a Spring Security UserDetails object.
     *
     * @param username the username to look up
     * @return the UserDetails containing username, password, and granted authorities
     * @throws UsernameNotFoundException if the username is not found in the database
     */
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singleton(
                        new SimpleGrantedAuthority(user.getRole().name())
                )
        );
    }
}