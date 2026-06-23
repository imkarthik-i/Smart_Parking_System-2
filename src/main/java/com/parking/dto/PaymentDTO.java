package com.parking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO for payment details associated with a billing record.
 * <p>
 * Provides a comprehensive view of a payment including billing
 * references, transaction details, and vehicle/slot information
 * for display purposes.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Payment details for a billing")
public class PaymentDTO {

    @Schema(description = "Payment ID (auto-generated)", example = "1")
    private Long paymentId;

    @Schema(description = "Associated billing ID", example = "1")
    private Long billingId;

    @Schema(description = "Associated transaction ID", example = "1")
    private Long transactionId;

    @Schema(description = "Amount paid", example = "100.0")
    private Double amount;

    @Schema(description = "Payment method used", example = "UPI", allowableValues = {"CASH", "UPI", "CARD"})
    private String paymentMethod;

    @Schema(description = "Payment status", example = "SUCCESS", allowableValues = {"PENDING", "SUCCESS", "FAILED"})
    private String status;

    @Schema(description = "Timestamp when payment was processed", example = "2025-01-15T12:35:00")
    private LocalDateTime paymentTime;

    @Schema(description = "Vehicle license plate number", example = "KA01AB1234")
    private String vehicleNumber;

    @Schema(description = "Type of vehicle", example = "CAR")
    private String vehicleType;

    @Schema(description = "Slot number used", example = "A-01")
    private String slotNumber;

    @Schema(description = "Parking lot name", example = "Downtown Parking")
    private String lotName;

    @Schema(description = "Vehicle entry timestamp", example = "2025-01-15T10:30:00")
    private LocalDateTime entryTime;

    @Schema(description = "Vehicle exit timestamp", example = "2025-01-15T12:30:00")
    private LocalDateTime exitTime;

    @Schema(description = "Duration of parking in hours", example = "2.0")
    private Double duration;

    /**
     * Constructs a comprehensive payment DTO with all display fields.
     *
     * @param paymentId     unique payment identifier
     * @param billingId     associated billing record identifier
     * @param transactionId associated parking transaction identifier
     * @param amount        amount paid
     * @param paymentMethod payment method used
     * @param status        payment status
     * @param paymentTime   timestamp of payment processing
     * @param vehicleNumber license plate number
     * @param vehicleType   type of vehicle
     * @param slotNumber    slot identifier used
     * @param lotName       parking lot name
     * @param entryTime     vehicle entry timestamp
     * @param exitTime      vehicle exit timestamp
     * @param duration      parking duration in hours
     */
    public PaymentDTO(Long paymentId, Long billingId, Long transactionId,
                      Double amount, String paymentMethod, String status,
                      LocalDateTime paymentTime,
                      String vehicleNumber, String vehicleType,
                      String slotNumber, String lotName,
                      LocalDateTime entryTime, LocalDateTime exitTime,
                      Double duration) {
        this.paymentId = paymentId;
        this.billingId = billingId;
        this.transactionId = transactionId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.paymentTime = paymentTime;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.slotNumber = slotNumber;
        this.lotName = lotName;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.duration = duration;
    }
}