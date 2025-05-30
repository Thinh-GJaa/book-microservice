package com.book.inventory.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrderDetailRequest implements Serializable {

        @NotBlank(message = "Product ID cannot be blank")
        String productId;

        @NotNull(message = "Quantity cannot be null")
        @Min(1)
        int quantity;

        @NotNull(message = "Unit price cannot be null")
        @DecimalMin(value = "0", message = "Unit price must be greater than or equal to 0")
        BigDecimal unitPrice;

}
