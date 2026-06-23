package com.parking.dto;

import com.parking.enums.SlotStatus;
import com.parking.enums.SlotType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO for parking slot information.
 * <p>
 * Captures slot-level details including its location within a lot,
 * slot number, type, current status, and floor. Used for both
 * slot management and availability queries.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Parking slot information")
public class ParkingSlotDTO {

    @Schema(description = "Slot ID (auto-generated)", example = "1")
    private Long slotId;

    @Schema(description = "ID of the parking lot this slot belongs to", example = "1")
    private Long lotId;

    @Schema(description = "Name of the parking lot", example = "Downtown Parking")
    private String lotName;

    @NotBlank(message = "Slot number is required")
    @Schema(description = "Slot identifier within the parking lot", example = "A-01", requiredMode = Schema.RequiredMode.REQUIRED)
    private String slotNumber;

    @NotNull(message = "Slot type is required")
    @Schema(description = "Type of slot", example = "CAR", allowableValues = {"CAR", "BIKE", "EV"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private SlotType slotType;

    @Schema(description = "Current status of the slot", example = "AVAILABLE", allowableValues = {"AVAILABLE", "RESERVED", "OCCUPIED"})
    private SlotStatus status;

    @Schema(description = "Floor number where the slot is located", example = "1")
    private Integer floorNumber;
}