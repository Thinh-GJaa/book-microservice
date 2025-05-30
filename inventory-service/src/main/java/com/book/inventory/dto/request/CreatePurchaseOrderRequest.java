package com.book.inventory.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePurchaseOrderRequest {

    @NotBlank(message = "Supplier ID cannot be blank")
    String supplierId;

    @NotBlank(message = "Warehouse ID cannot be blank")
    String warehouseId;

    String note;

    @NotEmpty(message = "Details cannot be empty")
            @Valid
    List<PurchaseOrderDetailRequest> details;
}
