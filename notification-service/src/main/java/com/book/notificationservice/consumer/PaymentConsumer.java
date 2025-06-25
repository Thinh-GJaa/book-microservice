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
public class PaymentConsumer {

    EmailService emailService;
    Parser parser;

    ProfileClient profileClient;

    static final String PAYMENT_SUCCESS_EVENT = "payment-success-event";

    @KafkaListener(topics = PAYMENT_SUCCESS_EVENT)
    public void listenPaymentSuccessEvent(String eventJson) {
        log.info("[PAYMENT CONSUMER] Received payment success event: {}", eventJson);
        // Parse the event JSON to a PaymentEvent object
        OrderEvent event = parser.parseToEvent(eventJson, OrderEvent.class);

        String email = profileClient.getEmailByUserId(event.getUserId());

        emailService.send(
                email,
                "Order created successfully",
                String.format("Your order with ID %s has been created successfully. Total amount: %s",
                        event.getOrderId(),
                        event.getTotalAmount()));

        log.info("[PAYMENT CONSUMER] Sent payment success email to: {}", email);
    }

}
