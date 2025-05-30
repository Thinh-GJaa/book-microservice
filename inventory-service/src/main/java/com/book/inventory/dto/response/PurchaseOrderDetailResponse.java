package com.book.inventory.dto.response;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrderDetailResponse implements Serializable {
    String productId;
    int quantity;
    BigDecimal unitPrice;
}
