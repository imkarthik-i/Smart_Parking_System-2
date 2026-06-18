package com.parking.controller;

import com.parking.dto.*;
import com.parking.entity.User;
import com.parking.enums.Role;
import com.parking.repository.UserRepository;
import com.parking.security.SecurityHelper;
import com.parking.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "BearerAuth")
@Tag(
	    name = "User Management APIs",
	    description = "APIs for creating, updating, fetching and deleting users"
	)
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SecurityHelper securityHelper;

    @PostMapping
    public UserResponseDTO createUser(@Valid @RequestBody UserDTO dto) {
        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .build();

        User saved = userRepository.save(user);

        return new UserResponseDTO(
                saved.getUserId(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getRole()
        );
    }

    @GetMapping("/me")
    public UserResponseDTO getMyProfile() {
        User u = securityHelper.getCurrentUser();
        return new UserResponseDTO(
                u.getUserId(),
                u.getUsername(),
                u.getEmail(),
                u.getRole()
        );
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        if (!securityHelper.isAdmin()) {
            throw new RuntimeException("Only admins can view all users");
        }

        return userRepository.findAll()
                .stream()
                .map(u -> new UserResponseDTO(
                        u.getUserId(),
                        u.getUsername(),
                        u.getEmail(),
                        u.getRole()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUser(@PathVariable Long id) {
        if (!securityHelper.isAdmin() && !securityHelper.getCurrentUserId().equals(id)) {
            throw new RuntimeException("You can only view your own profile");
        }

        User u = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserResponseDTO(
                u.getUserId(),
                u.getUsername(),
                u.getEmail(),
                u.getRole()
        );
    }

    @PutMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable Long id, @RequestBody UserResponseDTO dto) {
        if (!securityHelper.isAdmin()) {
            throw new RuntimeException("Only admins can update users");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }

        User updated = userRepository.save(user);

        return new UserResponseDTO(
                updated.getUserId(),
                updated.getUsername(),
                updated.getEmail(),
                updated.getRole()
        );
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        if (!securityHelper.isAdmin()) {
            throw new RuntimeException("Only admins can delete users");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);

        return "User deleted successfully";
    }
}