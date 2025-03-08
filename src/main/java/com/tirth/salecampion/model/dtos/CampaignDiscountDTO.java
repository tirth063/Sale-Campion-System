package com.tirth.salecampion.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Product discount information")
public class CampaignDiscountDTO {
    @NotNull(message = "Product ID is required")
    @Schema(description = "Product ID", example = "1")
    private Long productId;

    @Min(0) @Max(100)
    @Schema(description = "Discount percentage", example = "25")
    private Integer discount;
}
