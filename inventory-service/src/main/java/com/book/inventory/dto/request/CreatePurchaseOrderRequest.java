package com.book.inventory.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request for creating a new purchase order")
public class CreatePurchaseOrderRequest {

    @Schema(description = "Supplier ID", example = "5f6f2a1b-d9a4-4d4c-aeb7-f09366e91f75")
    @NotBlank(message = "Supplier ID cannot be blank")
    String supplierId;

    @Schema(description = "Warehouse ID", example = "5f6f2a1b-gt56-4d4c-aeb7-f09366e91f75")
    @NotBlank(message = "Warehouse ID cannot be blank")
    String warehouseId;

    @Schema(description = "Note", example = "Urgent order for restocking")
    String note;

    @Schema(description = "List of purchase order details")
    @NotEmpty(message = "Details cannot be empty")
    @Valid
    List<PurchaseOrderDetailRequest> details;
}
