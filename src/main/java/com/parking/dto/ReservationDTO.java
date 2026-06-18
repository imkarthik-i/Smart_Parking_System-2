package com.parking.dto;

import com.parking.enums.ReservationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

    private Long reservationId;

    @NotNull(message = "Vehicle id is required")
    private Long vehicleId;

    @NotNull(message = "Slot id is required")
    private Long slotId;

    private ReservationStatus status;

    private LocalDateTime reservationTime;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String slotNumber;

    private String vehicleNumber;
}