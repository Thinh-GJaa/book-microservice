package com.book.orderservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Order item information")
public class OrderItem implements Serializable {

    @Schema(description = "Product ID", example = "7b2a2e3c-9f92-4c73-a8be-1db4d17f3a3c")
    @NotBlank(message = "Product ID is required")
    String productId;

    @Schema(description = "Quantity of the product", example = "2")
    @NotNull(message = "Quantity is required")
            @Min(1)
    int quantity;

    @Schema(description = "Unit price of the product", example = "19.99")
    @NotNull(message = "Unit price is required")
            @Min(0)
    BigDecimal unitPrice;

}
