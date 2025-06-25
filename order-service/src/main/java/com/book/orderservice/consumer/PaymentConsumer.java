package com.book.orderservice.consumer;

import com.book.orderservice.dto.request.UpdateOrderStatusRequest;
import com.book.orderservice.enums.OrderStatus;
import com.book.orderservice.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentConsumer {

    OrderService orderService;

    static final String PAYMENT_SUCCESS_EVENT = "payment-success-event";
    static final String PAYMENT_FAIL_EVENT = "payment-fail-event";

    @KafkaListener(topics = PAYMENT_SUCCESS_EVENT)
    public void consumeOrderCreatedEvent(String orderId) {
        log.info("[ORDER SERVICE] Consuming payment success event (order created): {}. Order created successfully!",
                orderId);
        try {
            orderService.updateOrderStatus(UpdateOrderStatusRequest.builder()
                    .orderId(orderId)
                    .status(OrderStatus.PENDING)
                    .build());
        } catch (Exception e) {
            log.error("[ORDER SERVICE] Error processing payment success event (order created): {}", orderId, e);
        }
    }

    @KafkaListener(topics = PAYMENT_FAIL_EVENT)
    public void consumePaymentFailEvent(String orderId) {
        log.info("[ORDER SERVICE] Consuming payment fail event: {}", orderId);
        try {
            orderService.updateOrderStatus(UpdateOrderStatusRequest.builder()
                    .orderId(orderId)
                    .status(OrderStatus.FAILED)
                    .build());
        } catch (Exception e) {
            log.error("[ORDER SERVICE] Error processing payment fail event: {}", orderId, e);
        }
    }

}
