package com.book.profileservice.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Parser {

    ObjectMapper objectMapper;

    public String parseToJson(Object event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing event to JSON", e);
        }
    }

    public <T> T parseToEvent(String eventJson, Class<T> eventType) {
        try {
            return objectMapper.readValue(eventJson, eventType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid event data for " + eventType.getSimpleName());
        }
    }
}