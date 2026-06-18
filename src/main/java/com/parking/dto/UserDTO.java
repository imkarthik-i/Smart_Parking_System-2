package com.parking.dto;

import com.parking.enums.Role;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long userId;

    @NotBlank
    private String username;

    @Email
    private String email;

    @Size(min = 6)
    private String password;

    private Role role;
}