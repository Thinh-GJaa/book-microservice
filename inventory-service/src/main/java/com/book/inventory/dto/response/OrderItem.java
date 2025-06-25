package com.book.inventory.dto.response;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItem {

    @NotBlank(message = "Product ID is required")
    String productId;

    @NotNull(message = "Quantity is required")
            @Min(1)
    int quantity;

    @NotNull(message = "Unit price is required")
            @Min(0)
    BigDecimal unitPrice;

}
