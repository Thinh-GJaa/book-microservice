package com.book.inventory.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse implements Serializable {
    String orderId;
    String userId;
    String recipientName;
    String recipientPhone;
    String shippingAddress;
    String note;
    String paymentMethod;
    String orderStatus;

    WarehouseDTO warehouse;

    List<OrderItem> orderItems;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class WarehouseDTO implements Serializable {
        String warehouseId;
        String warehouseName;
    }

}
