package com.book.orderservice.dto.response;


import com.book.orderservice.dto.request.OrderItem;
import com.book.orderservice.enums.OrderStatus;
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
    OrderStatus status;
    String warehouseId;

    List<OrderItem> orderItems;

}
