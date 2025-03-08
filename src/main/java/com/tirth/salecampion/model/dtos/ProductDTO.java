package com.tirth.salecampion.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;


@Data
public class ProductDTO {

    @NotBlank(message = "Product title is required")
    private String title;

    @NotBlank(message = "Product description is required")
    private String description;

    @Positive(message = "MRP must be positive")
    @Schema(description = "Maximum Retail Price")
    @NotBlank
    private BigDecimal mrp;

    @Positive(message = "Current price must be positive")
    @Schema(description = "Current selling price")
    @NotBlank
    private BigDecimal currentPrice;

    @PositiveOrZero(message = "Discount cannot be negative")
    @Schema(description = "Current discount percentage", example = "25")
    @Min(0) @Max(100)
    private Integer discount;

    @PositiveOrZero(message = "Inventory cannot be negative")
    @Schema(description = "Available inventory count")
    @NotBlank
    private Integer inventory;

    @PreUpdate
    private void validatePrice() {
        if (currentPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Current price cannot be negative");
        }
    }
}
