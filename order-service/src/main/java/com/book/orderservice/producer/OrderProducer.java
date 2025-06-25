package com.book.orderservice.producer;

import com.book.orderservice.dto.event.OrderEvent;
import com.book.orderservice.entity.Order;
import com.book.orderservice.mapper.OrderMapper;
import com.book.orderservice.parser.Parser;
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

public class OrderProducer {

    KafkaTemplate<String, String> kafkaTemplate;
    OrderMapper orderMapper;
    Parser parser;

    static final String ORDER_CREATED_EVENT = "order-created-event";
    static final String ORDER_CONFIRMED_EVENT = "order-confirmed-event";

    public void sendCreatedOrderEvent(Order order) {

        OrderEvent event = orderMapper.toOrderEvent(order);

        String orderJson = parser.parseToJson(event);

        kafkaTemplate.send(ORDER_CREATED_EVENT, orderJson);

        log.info("[ORDER PRODUCER] Sent created order event: {}", orderJson);
    }


    public void sendOrderConfirmedEvent(Order order) {

        OrderEvent event = orderMapper.toOrderEvent(order);

        String orderJson = parser.parseToJson(event);

        kafkaTemplate.send(ORDER_CONFIRMED_EVENT, orderJson);

        log.info("[ORDER PRODUCER] Sent order confirmed event: {}", orderJson);

    }

}
