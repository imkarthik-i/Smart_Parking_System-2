package com.parking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * DTO representing a single data point for slot utilization visualization.
 * <p>
 * Used in dashboard charts to display the distribution of slot
 * statuses (available, occupied, reserved) with associated colors
 * for rendering.
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
@Schema(description = "Slot utilization data point for pie/donut charts")
public class SlotUtilizationData {

    @Schema(description = "Category name", example = "Available")
    private String name;

    @Schema(description = "Count for this category", example = "45")
    private long value;

    @Schema(description = "Hex color code for visualization", example = "#10b981")
    private String color;
}
