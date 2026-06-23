package com.parking.mapper;

import com.parking.dto.UserDTO;
import com.parking.entity.User;

/**
 * Mapper utility for converting between {@link User} entities and
 * {@link UserDTO} objects.
 * <p>
 * Provides static methods for bidirectional mapping, excluding
 * sensitive fields like password when converting to DTO.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
public class UserMapper {

    /**
     * Converts a {@link UserDTO} to a {@link User} entity.
     *
     * @param dto the DTO containing user data
     * @return a User entity with mapped fields
     */
    public static User toEntity(UserDTO dto) {

        return User.builder()
                .userId(dto.getUserId())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(dto.getRole())
                .build();
    }

    /**
     * Converts a {@link User} entity to a {@link UserDTO}.
     * The password field is intentionally set to null for security.
     *
     * @param user the user entity
     * @return a UserDTO with mapped fields (password excluded)
     */
    public static UserDTO toDTO(User user) {

        return new UserDTO(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                null,
                user.getRole()
        );
    }
}