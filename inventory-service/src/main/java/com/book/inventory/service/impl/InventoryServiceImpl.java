package com.book.inventory.service.impl;

import com.book.inventory.dto.request.*;
import com.book.inventory.dto.response.OrderItem;
import com.book.inventory.dto.response.OrderResponse;
import com.book.inventory.entity.*;
import com.book.inventory.exception.CustomException;
import com.book.inventory.exception.ErrorCode;
import com.book.inventory.repository.*;
import com.book.inventory.service.InventoryService;
import com.book.inventory.service.client.OrderClient;
import lombok.RequiredArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InventoryServiceImpl implements InventoryService {

    InventoryRepository inventoryRepository;
    WarehouseRepository warehouseRepository;

    OrderClient orderClient;

    /**
     * Lấy danh sách kho đã sắp xếp theo độ gần với địa chỉ nhận hàng (giả lập bằng
     * độ dài chuỗi).
     */
    private List<Warehouse> getNearestWarehouses(String address) {
        List<Warehouse> warehouses = warehouseRepository.findAll();
        warehouses.sort(Comparator.comparingInt(w -> Math.abs(w.getAddress().length() - address.length())));
        return warehouses;
    }

    public String simulateCheckAndReserveSingleWarehouse(CheckInventoryRequest request) {
        log.info("[INVENTORY SERVICE] Checking and reserving inventory for address: {}, items: {}",
                request.getAddress(), request.getItems());
        List<Warehouse> warehouses = getNearestWarehouses(request.getAddress());
        Set<String> notEnoughProducts = new HashSet<>();
        for (Warehouse warehouse : warehouses) {
            boolean allAvailable = true;
            List<Inventory> inventoriesToReserve = new ArrayList<>();
            notEnoughProducts.clear();
            for (CheckInventoryRequest.Item item : request.getItems()) {
                Optional<Inventory> invOpt = inventoryRepository
                        .findByProductIdAndWarehouse_WarehouseId(item.getProductId(), warehouse.getWarehouseId());
                if (invOpt.isPresent()) {
                    Inventory inv = invOpt.get();
                    int available = inv.getAvailableQuantity();
                    if (available < item.getQuantity()) {
                        log.warn(
                                "[INVENTORY SERVICE] Not enough available for product {} in warehouse {}. Available: {}, Required: {}",
                                item.getProductId(), warehouse.getWarehouseId(), available, item.getQuantity());
                        allAvailable = false;
                        notEnoughProducts.add(item.getProductId());
                        break;
                    } else {
                        inv.setAvailableQuantity(inv.getAvailableQuantity() - item.getQuantity());
                        inv.setReservedQuantity(inv.getReservedQuantity() + item.getQuantity());
                        inventoriesToReserve.add(inv);
                        log.info("[INVENTORY SERVICE] Reserved {} units of product {} in warehouse {}",
                                item.getQuantity(), item.getProductId(), warehouse.getWarehouseId());
                    }
                } else {
                    log.warn("[INVENTORY SERVICE] Inventory not found for product {} in warehouse {}",
                            item.getProductId(), warehouse.getWarehouseId());
                    allAvailable = false;
                    notEnoughProducts.add(item.getProductId());
                    break;
                }
            }
            if (allAvailable) {
                inventoryRepository.saveAll(inventoriesToReserve);
                log.info("[INVENTORY SERVICE] Successfully reserved inventory in warehouse {} for address {}",
                        warehouse.getWarehouseId(), request.getAddress());
                return warehouse.getWarehouseId();
            }
        }
        log.error("[INVENTORY SERVICE] Out of stock for products: {}", notEnoughProducts);
        throw new CustomException(ErrorCode.PRODUCT_OUT_OF_STOCK, String.join(",", notEnoughProducts));
    }

    @Override
    public void releaseInventory(String orderId) {
        log.info("[INVENTORY SERVICE] Start releasing inventory for order: {}", orderId);
        try {
            OrderResponse order = orderClient.getOrderById(orderId);
            String warehouseId = order.getWarehouse().getWarehouseId();
            List<Inventory> inventoriesToUpdate = new ArrayList<>();
            for (OrderItem item : order.getOrderItems()) {
                Optional<Inventory> invOpt = inventoryRepository.findByProductIdAndWarehouse_WarehouseId(
                        item.getProductId(), warehouseId);
                if (invOpt.isPresent()) {
                    Inventory inv = invOpt.get();
                    if (inv.getReservedQuantity() < item.getQuantity()) {
                        log.error(
                                "[INVENTORY SERVICE] Reserved quantity not enough for product {} in order {}. Reserved: {}, To release: {}",
                                item.getProductId(), orderId, inv.getReservedQuantity(), item.getQuantity());
                        continue;
                    }
                    inv.setReservedQuantity(inv.getReservedQuantity() - item.getQuantity());
                    inv.setAvailableQuantity(inv.getAvailableQuantity() + item.getQuantity());
                    inventoriesToUpdate.add(inv);
                    log.info("[INVENTORY SERVICE] Released {} units of product {} for order {}", item.getQuantity(),
                            item.getProductId(), orderId);
                } else {
                    log.error(
                            "[INVENTORY SERVICE] Inventory not found for product {} in warehouse {} when releasing order {}",
                            item.getProductId(), warehouseId, orderId);
                }
            }
            if (!inventoriesToUpdate.isEmpty()) {
                inventoryRepository.saveAll(inventoriesToUpdate);
            }
            log.info("[INVENTORY SERVICE] Released inventory for order: {}", orderId);
        } catch (Exception e) {
            log.error("[INVENTORY SERVICE] Error releasing inventory for order: {}", orderId, e);
        }
    }

}
