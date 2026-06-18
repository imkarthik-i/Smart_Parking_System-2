package com.parking.security;

import com.parking.entity.User;
import com.parking.enums.Role;
import com.parking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityHelper {

    private final UserRepository userRepository;

    public String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public User getCurrentUser() {
        String username = getCurrentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Long getCurrentUserId() {
        return getCurrentUser().getUserId();
    }

    public Role getCurrentUserRole() {
        return getCurrentUser().getRole();
    }

    public boolean isAdmin() {
        return getCurrentUserRole() == Role.ROLE_ADMIN;
    }
}