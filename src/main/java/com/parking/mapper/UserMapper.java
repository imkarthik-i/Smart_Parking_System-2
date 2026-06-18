package com.parking.mapper;

import com.parking.dto.UserDTO;
import com.parking.entity.User;

public class UserMapper {

    public static User toEntity(UserDTO dto) {

        return User.builder()
                .userId(dto.getUserId())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(dto.getRole())
                .build();
    }

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