package com.book.paymentservice.producer;


import com.book.paymentservice.dto.event.OrderEvent;
import com.book.paymentservice.parser.Parser;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PaymentProducer {
    Parser parser;
    KafkaTemplate<String, String> kafkaTemplate;

    static String PAYMENT_SUCCESS_EVENT = "payment-success-event";
    static String PAYMENT_FAIL_EVENT= "payment-fail-event";

    public void sendPaymentSuccessEvent(OrderEvent event) {
        kafkaTemplate.send(PAYMENT_SUCCESS_EVENT, parser.parseToJson(event));
        log.info("[PAYMENT PRODUCER] Payment success event sent for order: {}", event.getOrderId());

    }

    public void sendPaymentFailEvent(OrderEvent event) {
        kafkaTemplate.send(PAYMENT_FAIL_EVENT, parser.parseToJson(event));
        log.info("[PAYMENT PRODUCER] Payment fail event sent for order: {}", event.getOrderId());
    }

}
