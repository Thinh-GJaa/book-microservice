package com.book.inventory.dto.response;

import com.book.inventory.enums.StockOutStatus;
import com.book.inventory.enums.StockOutType;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockOutResponse implements Serializable {
    String stockOutId;
    StockOutStatus status;
    StockOutType type;
    WarehouseResponse warehouse;
    String note;
    BigDecimal totalAmount;
    LocalDateTime createdDate;
    LocalDateTime updatedDate;
    List<StockOutDetailResponse> details;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class WarehouseResponse implements Serializable {
        String warehouseId;
        String warehouseName;
    }

}
