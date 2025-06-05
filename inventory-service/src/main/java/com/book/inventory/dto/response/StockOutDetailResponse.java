package com.book.inventory.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StockOutDetailResponse implements Serializable {
    String productId;
    Integer quantity;
    BigDecimal unitPrice;
}
