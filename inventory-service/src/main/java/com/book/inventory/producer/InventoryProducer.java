package com.book.inventory.producer;


import com.book.inventory.dto.event.LowInventoryEvent;
import com.book.inventory.parser.Parser;
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

public class InventoryProducer {

    KafkaTemplate<String, String> kafkaTemplate;

    Parser parser;

    public void lowInventoryNotification(LowInventoryEvent event) {

        // Tạo sự kiện thông báo tồn kho thấp
        String eventJson = parser.parseToJson(event);

        // Gửi sự kiện đến Kafka topic
        kafkaTemplate.send("low-inventory-topic", eventJson);

        log.info("Sent low inventory notification: {}", eventJson);
    }


}
