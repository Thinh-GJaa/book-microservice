package com.book.inventory.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Detail item for a purchase order request")
public class PurchaseOrderDetailRequest implements Serializable {

        @Schema(description = "Product ID", example = "5f6f2a1b-d9a4-4d4c-aeb7-f09366e91f75")
        @NotBlank(message = "Product ID cannot be blank")
        String productId;

        @Schema(description = "Quantity", example = "10")
        @NotNull(message = "Quantity cannot be null")
        @Min(1)
        int quantity;

        @Schema(description = "Unit price", example = "100000.0")
        @NotNull(message = "Unit price cannot be null")
        @DecimalMin(value = "0", message = "Unit price must be greater than or equal to 0")
        BigDecimal unitPrice;

}
