package com.book.inventory.scheduling;

import com.book.inventory.dto.event.LowInventoryEvent;
import com.book.inventory.dto.response.ProductTitleResponse;
import com.book.inventory.entity.Inventory;
import com.book.inventory.entity.Warehouse;
import com.book.inventory.producer.InventoryProducer;
import com.book.inventory.repository.InventoryRepository;
import com.book.inventory.repository.WarehouseRepository;
import com.book.inventory.service.client.BookClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryMonitorScheduler {

    final InventoryRepository inventoryRepository;
    final WarehouseRepository warehouseRepository;
    final InventoryProducer inventoryProducer;
    final BookClient bookClient;

    // Cấu hình ngưỡng cảnh báo
    final static int LOW_THRESHOLD = 50; // Ngưỡng cảnh báo thấp
    final static String TIME_ZONE = "Asia/Ho_Chi_Minh";

    // Chạy vào 9:00 AM và 3:00 PM mỗi ngày
//    @Scheduled(cron = "0 0 9,15 * * *", zone = TIME_ZONE)
    @Scheduled(cron = "0 */1 * * * *", zone = TIME_ZONE)
    public void checkLowInventoryByWarehouse() {
        log.info("Starting scheduled inventory check at {}", LocalDateTime.now());

        List<Warehouse> warehouses = warehouseRepository.findAll();
        log.info("Found {} warehouses to check", warehouses.size());

        for (Warehouse warehouse : warehouses) {
            try {
                // Kiểm tra tồn kho thấp
                List<Inventory> lowInventoryItems = inventoryRepository
                        .findByWarehouse_WarehouseIdAndAvailableQuantityLessThan(
                                warehouse.getWarehouseId(), LOW_THRESHOLD);

                if (!lowInventoryItems.isEmpty()) {
                    sendLowInventoryAlert(warehouse, lowInventoryItems);
                } else {
                    log.info("No low inventory items found in warehouse {}", warehouse.getWarehouseId());
                }
            } catch (Exception e) {
                log.error("Error checking inventory for warehouse {}: ", warehouse.getWarehouseId(), e);
            }
        }
    }

    private void sendLowInventoryAlert(Warehouse warehouse, List<Inventory> items) {
        log.info("Found {} items with low inventory in warehouse {}",
                items.size(), warehouse.getWarehouseId());

        // Lấy thông tin tên sản phẩm từ book-service
        Set<String> productIds = items.stream()
                .map(Inventory::getProductId)
                .collect(Collectors.toSet());

        Map<String, String> productTitles = bookClient.getProductTitlesByIds(productIds).stream()
                .collect(Collectors.toMap(
                        ProductTitleResponse::getProductId,
                        ProductTitleResponse::getTitle));

        LowInventoryEvent event = LowInventoryEvent.builder()
                .warehouseId(warehouse.getWarehouseId())
                .warehouseName(warehouse.getWarehouseName())
                .timestamp(LocalDateTime.now())
                .items(items.stream()
                        .map(item -> LowInventoryEvent.InventoryItem.builder()
                                .productId(item.getProductId())
                                .title(productTitles.getOrDefault(item.getProductId(), "Unknown Product"))
                                .availableQuantity(item.getAvailableQuantity())
                                .build())
                        .collect(Collectors.toList()))
                .build();

        try {
            inventoryProducer.lowInventoryNotification(event);
            log.debug("Successfully sent alert for warehouse {} with {} items",
                    warehouse.getWarehouseId(), items.size());
        } catch (Exception e) {
            log.error("Failed to send alert for warehouse {}: ", warehouse.getWarehouseId(), e);
        }
    }
}
