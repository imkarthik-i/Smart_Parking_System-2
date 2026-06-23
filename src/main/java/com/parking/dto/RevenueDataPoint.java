package com.parking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * DTO representing a single monthly data point for revenue trend analysis.
 * <p>
 * Used in dashboard charts to display historical revenue and
 * occupancy trends over time.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Monthly revenue data point for trend charts")
public class RevenueDataPoint {

    @Schema(description = "Month label", example = "Jan")
    private String month;

    @Schema(description = "Total revenue for the month", example = "5000.0")
    private double revenue;

    @Schema(description = "Occupancy percentage for the month", example = "75.5")
    private double occupancy;
}
