package com.book.inventory.dto.request;

import com.book.inventory.enums.PurchaseOrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePurchaseOrderStatusRequest {

    @NotBlank(message = "Purchase Order ID cannot be blank")
    String purchaseOrderId;

    @NotNull(message = "Status cannot be null")
    PurchaseOrderStatus status;

}
