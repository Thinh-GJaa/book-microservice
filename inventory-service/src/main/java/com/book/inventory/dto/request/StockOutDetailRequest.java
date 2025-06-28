package com.book.inventory.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Detail item for a stock out request")
public class StockOutDetailRequest {

    @Schema(description = "Product ID", example = "5f6f2a1b-d9a4-4d4c-aeb7-f09366e91f75")
    @NotBlank(message = "Product ID is required")
    String productId;

    @Schema(description = "Quantity", example = "5")
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than 0")
    Integer quantity;

    @Schema(description = "Unit price", example = "50000.0")
    @NotNull(message = "Unit price is required")
    BigDecimal unitPrice;

}
