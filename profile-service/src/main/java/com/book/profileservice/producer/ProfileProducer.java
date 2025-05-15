package com.book.profileservice.producer;


import com.book.profileservice.dto.event.UpdateEmailEvent;
import com.book.profileservice.parser.Parser;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class ProfileProducer {

    KafkaTemplate<String, String> kafkaTemplate;

    Parser parser;

    public void updateEmail(String userId, String email ){

        UpdateEmailEvent event = UpdateEmailEvent.builder()
                .userId(userId)
                .email(email)
                .build();

        kafkaTemplate.send("update-email-topic", parser.parseToJson(event));

    }

}
