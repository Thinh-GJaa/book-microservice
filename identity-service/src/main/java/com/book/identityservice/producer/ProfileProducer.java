package com.book.identityservice.producer;

import com.book.identityservice.dto.event.NotificationEvent;
import com.book.identityservice.parser.Parser;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileProducer {

    KafkaTemplate<String, String> kafkaTemplate;

    Parser parser;

    public void createProfile(String userName, String email) {
        NotificationEvent event = NotificationEvent.builder()
                .channel("Email")
                .recipient(email)
                .subject("Welcome to microservice (ThinhGJaa)")
                .body("Hello, " + userName)
                .build();

        kafkaTemplate.send("create-profile-event", parser.parseToJson(event));
    }
}
