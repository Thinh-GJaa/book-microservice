package com.book.paymentservice.consumer;

import com.book.paymentservice.dto.event.OrderEvent;
import com.book.paymentservice.parser.Parser;
import com.book.paymentservice.producer.PaymentProducer;
import com.book.paymentservice.service.PaymentService;
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
public class OrderConsumer {

    Parser parser;
    PaymentService paymentService;
    PaymentProducer paymentProducer;

    static final String ORDER_CREATED_EVENT = "order-created-event";

    @KafkaListener(topics = ORDER_CREATED_EVENT)
    public void orderCreatedConsume(String message) {
        try {
            log.info("[ORDER CONSUMER] Consumed message: {}", message);
            OrderEvent event = parser.parseToEvent(message, OrderEvent.class);
            paymentService.processPayment(event);
            log.info("[ORDER CONSUMER] Processed order created event for order: {}", event.getOrderId());
        } catch (Exception e) {
            log.error("[ORDER CONSUMER] Error processing order created event: {}", e.getMessage());
        }

    }
}
