package com.book.paymentservice.service;

import com.book.paymentservice.dto.event.OrderEvent;
import lombok.experimental.FieldDefaults;
import static lombok.AccessLevel.PRIVATE;

public interface PaymentService {

    void processPayment(OrderEvent request);

//    PaymentResponse getPaymentByOrderId(String orderId);

//    PaymentResponse refundPayment(String orderId);
}
