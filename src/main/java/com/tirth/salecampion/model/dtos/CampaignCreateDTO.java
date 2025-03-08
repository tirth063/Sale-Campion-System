package com.tirth.salecampion.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for creating a new campaign
 */
@Data
@Schema(description = "Request object for creating a new campaign")
public class CampaignCreateDTO {
    @NotBlank(message = "Title is required")
    @Schema(description = "Campaign title", example = "Summer Sale 2024")
    private String title;

    @NotNull(message = "Start date is required")
    @Schema(description = "Campaign start date", example = "2024-06-01")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Schema(description = "Campaign end date", example = "2024-06-15")
    private LocalDate endDate;

    @NotEmpty(message = "At least one product discount is required")
    @Schema(description = "List of product discounts")
    private List<CampaignDiscountDTO> discounts;
}

