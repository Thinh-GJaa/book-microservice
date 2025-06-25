package com.book.inventory.consumer;

import com.book.inventory.service.InventoryService;
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

    InventoryService inventoryService;

    static final String PAYMENT_FAIL_EVENT = "payment-fail-event";

    @KafkaListener(topics = PAYMENT_FAIL_EVENT)
    public void consumePaymentFailEvent(String orderId) {
        log.info("[INVENTORY CONSUMER] Consuming payment fail event: {}", orderId);
        try {
            inventoryService.releaseInventory(orderId);
            log.info("[INVENTORY CONSUMER] Successfully released inventory for order ID: {}", orderId);
        } catch (Exception e) {
            log.error("[INVENTORY CONSUMER] Error processing payment fail event: {}", orderId, e);
        }
    }

}
