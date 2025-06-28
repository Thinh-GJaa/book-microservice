package com.book.inventory.dto.request;

import com.book.inventory.enums.PurchaseOrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request for updating purchase order status")
public class UpdatePurchaseOrderStatusRequest {

    @Schema(description = "Purchase Order ID", example = "5f6f2a1b-d9a4-4d4c-aeb7-f09366e91f75")
    @NotBlank(message = "Purchase Order ID cannot be blank")
    String purchaseOrderId;

    @Schema(description = "Status", example = "COMPLETED")
    @NotNull(message = "Status cannot be null")
    PurchaseOrderStatus status;

}
