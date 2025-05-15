//package com.book.profileservice.consumer;
//
//import com.book.profileservice.dto.event.UpdateEmailEvent;
//import com.book.profileservice.parser.Parser;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class ProfileConsumer {
//
//    Parser parser;
//
//    @KafkaListener(topics = "update-email-topic")
//    public void listenUpdateProfileEvent(String eventJson){
//
//        UpdateEmailEvent event = parser.parseToEvent(eventJson, UpdateEmailEvent.class);
//
//
//
//        log.info("Thinh, event: {}", parser.parseToEvent(eventJson, UpdateEmailEvent.class));
//
//    }
//}
