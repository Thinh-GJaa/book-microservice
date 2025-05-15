package com.book.identityservice.consumer;

import com.book.identityservice.dto.event.UpdateEmailEvent;
import com.book.identityservice.parser.Parser;
import com.book.identityservice.service.UserService;
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
public class ProfileConsumer {

    Parser parser;

    UserService userService;

    @KafkaListener(topics = "update-email-topic")
    public void listenUpdateProfileEvent(String eventJson){

        UpdateEmailEvent event = parser.parseToEvent(eventJson, UpdateEmailEvent.class);

        userService.updateEmailEvent(event);

        log.info("Thinh, event: {}", parser.parseToEvent(eventJson, UpdateEmailEvent.class));

    }
}
