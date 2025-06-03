package com.book.inventory.dto.event;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LowInventoryEvent implements Serializable {
    String warehouseId;
    String warehouseName;
    LocalDateTime timestamp;
    List<InventoryItem> items;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class InventoryItem {
        String productId;
        String title;
        int availableQuantity;
    }

}
