package com.book.notificationservice.consumer;

import com.book.notificationservice.dto.event.OrderEvent;
import com.book.notificationservice.parser.Parser;
import com.book.notificationservice.service.EmailService;
import com.book.notificationservice.service.client.ProfileClient;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class OrderConsumer {

    EmailService emailService;
    Parser parser;
    ProfileClient profileClient;

    static final String ORDER_CONFIRMED_EVENT = "order-confirmed-event";

    @KafkaListener(topics = ORDER_CONFIRMED_EVENT)
    public void listenOrderConfirmEvent(String eventJson) {
        log.info("[PAYMENT CONSUMER] Received payment success event: {}", eventJson);
        // Parse the event JSON to a PaymentEvent object
        OrderEvent event = parser.parseToEvent(eventJson, OrderEvent.class);

        String email = profileClient.getEmailByUserId(event.getUserId());

        emailService.send(
                email,
                "Order confirmation",
                String.format("Your order with ID %s has been confirmed. Total amount: %s",
                        event.getOrderId(),
                        event.getTotalAmount()));

        log.info("[PAYMENT CONSUMER] Sent confirmation email to user: {}", event.getUserId());

    }

}
