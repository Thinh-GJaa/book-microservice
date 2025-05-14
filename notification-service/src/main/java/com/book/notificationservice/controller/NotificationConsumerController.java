package com.book.notificationservice.controller;


import com.book.event.NotificationEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationConsumerController {

    @KafkaListener(topics = "create-profile-topic")
    public void listenCreateProfileEvent(NotificationEvent event){
        log.info("Thinh, event: {}", event);
    }
}
