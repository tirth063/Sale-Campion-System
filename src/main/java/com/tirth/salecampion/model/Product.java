package com.tirth.salecampion.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * Represents a product in the system with its pricing and inventory information.
 */
@Entity
@Table(name = "products_tbl")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Product entity containing product details")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product title is required")
    @Schema(description = "Product title")
    private String title;

    @Schema(description = "Product description")
    private String description;

    @Positive(message = "MRP must be positive")
    @Schema(description = "Maximum Retail Price")
    private BigDecimal mrp;

    @Positive(message = "Current price must be positive")
    @Schema(description = "Current selling price")
    private BigDecimal currentPrice;

    @PositiveOrZero(message = "Discount cannot be negative")
    @Schema(description = "Current discount percentage")
    private Integer discount;

    @PositiveOrZero(message = "Inventory cannot be negative")
    @Schema(description = "Available inventory count")
    private Integer inventory;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @PreUpdate
    private void validatePrice() {
        if (currentPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Current price cannot be negative");
        }
    }
}