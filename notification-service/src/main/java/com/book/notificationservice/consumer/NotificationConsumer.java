package com.book.notificationservice.consumer;

import com.book.notificationservice.dto.event.NotificationEvent;
import com.book.notificationservice.parser.Parser;
import com.book.notificationservice.service.EmailService;
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
public class NotificationConsumer {

    Parser parser;

    EmailService emailService;

    @KafkaListener(topics = "create-profile-topic")
    public void listenCreateProfileEvent(String eventJson){

        NotificationEvent event = parser.parseToEvent(eventJson, NotificationEvent.class);

        emailService.send(event.getRecipient(), event. getSubject(), event.getBody());

        log.info("Thinh, event: {}", parser.parseToEvent(eventJson, NotificationEvent.class));

    }
}
