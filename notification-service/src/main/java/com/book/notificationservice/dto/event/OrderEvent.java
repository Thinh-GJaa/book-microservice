package com.book.notificationservice.dto.event;


import com.book.notificationservice.enums.PaymentMethod;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderEvent {

    String orderId;
    String userId;
    String recipientName;
    String recipientPhone;
    String shippingAddress;
    String note;
    LocalDateTime createdAt;
    BigDecimal totalAmount;
    PaymentMethod paymentMethod;

    List<OrderItem> orderItems;

}
