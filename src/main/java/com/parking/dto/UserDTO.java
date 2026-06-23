package com.parking.dto;

import com.parking.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO for user creation and update operations.
 * <p>
 * Carries all modifiable user fields including authentication
 * credentials and role assignment. Validation constraints ensure
 * data integrity at the API boundary.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User creation/update request payload")
public class UserDTO {

    @Schema(description = "User ID (auto-generated, not required for create)", example = "1")
    private Long userId;

    @NotBlank
    @Schema(description = "Unique username for the user", example = "john_doe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Email
    @Schema(description = "Email address of the user", example = "john@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Size(min = 6)
    @Schema(description = "Password (minimum 6 characters)", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @Schema(description = "Role assigned (always ROLE_CUSTOMER for new registrations)", example = "ROLE_CUSTOMER", allowableValues = {"ROLE_ADMIN", "ROLE_CUSTOMER"})
    private Role role;
}