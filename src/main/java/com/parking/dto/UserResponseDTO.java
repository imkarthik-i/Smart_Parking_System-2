package com.parking.dto;

import com.parking.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long userId;
    private String username;
    private String email;
    private Role role;
}