package com.book.inventory.service.impl;

import com.book.inventory.dto.request.*;
import com.book.inventory.dto.response.*;
import com.book.inventory.entity.*;
import com.book.inventory.enums.PurchaseOrderStatus;
import com.book.inventory.exception.CustomException;
import com.book.inventory.exception.ErrorCode;
import com.book.inventory.mapper.PurchaseOrderMapper;
import com.book.inventory.repository.*;
import com.book.inventory.service.PurchaseOrderService;
import com.book.inventory.service.client.BookClient;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    PurchaseOrderRepository purchaseOrderRepository;
    PurchaseOrderMapper purchaseOrderMapper;
    BookClient bookClient;
    InventoryRepository inventoryRepository; // Thêm dòng này

    @Override
    @Transactional
    @CacheEvict(value = "purchaseOrders", allEntries = true)
    public PurchaseOrderResponse createPurchaseOrder(CreatePurchaseOrderRequest request) {

        // Lấy danh sách productId từ request
        List<String> productIds = request.getDetails().stream()
                .map(PurchaseOrderDetailRequest::getProductId)
                .toList();

        // Kiểm tra xem có productId nào bị trùng lặp không
        Set<String> uniqueProductIds = new HashSet<>(productIds);
        if (uniqueProductIds.size() < productIds.size()) {
            throw new CustomException(ErrorCode.DUPLICATE_PRODUCT_ID_IN_ORDER, "Duplicate productId in order details");
        }

        // Kiểm tra xem tất cả productId có hợp lệ không
        Set<String> invalidProductIds = bookClient.checkInvalidProductIds(uniqueProductIds);
        if (!invalidProductIds.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_PRODUCT_ID, "Invalid productId(s): " + invalidProductIds);
        }

        // Trong mapper này có kiểm tra xem supplierId có tồn tại không
        // và tính toán tổng số tiền từ chi tiết đơn hàng
        PurchaseOrder purchaseOrder = purchaseOrderMapper.toPurchaseOrder(request);

        PurchaseOrder parentOrder = purchaseOrder;

        // Đảm bảo set purchaseOrder cho từng detail
        purchaseOrder.getDetails().forEach(detail -> detail.setPurchaseOrder(parentOrder));

        // Lưu purchaseOrder, Hibernate sẽ tự lưu luôn details
        purchaseOrder = purchaseOrderRepository.save(purchaseOrder);

        return purchaseOrderMapper.toPurchaseOrderResponse(purchaseOrder);
    }

    @Override
    @Cacheable(value = "purchaseOrder", key = "#purchaseOrderId")
    public PurchaseOrderResponse getPurchaseOrderById(String purchaseOrderId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new CustomException(ErrorCode.PURCHASE_ORDER_NOT_FOUND,
                        purchaseOrderId));
        return purchaseOrderMapper.toPurchaseOrderResponse(purchaseOrder);
    }

    @Override
    @Transactional
    @Caching(put = @CachePut(value = "purchaseOrder", key = "#request.purchaseOrderId"), evict = @CacheEvict(value = "purchaseOrders", allEntries = true))
    public PurchaseOrderResponse updatePurchaseOrderStatus(UpdatePurchaseOrderStatusRequest request) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(request.getPurchaseOrderId())
                .orElseThrow(
                        () -> new CustomException(ErrorCode.PURCHASE_ORDER_NOT_FOUND, request.getPurchaseOrderId()));

        PurchaseOrderStatus currentStatus = purchaseOrder.getStatus();
        PurchaseOrderStatus newStatus = request.getStatus();

        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new CustomException(ErrorCode.INVALID_ORDER_STATUS, newStatus);
        }

        if (newStatus == PurchaseOrderStatus.COMPLETED) {
            updateInventoryAfterPurchaseOrderCompletion(purchaseOrder);
        }

        purchaseOrder.setStatus(newStatus);

        return purchaseOrderMapper.toPurchaseOrderResponse(purchaseOrderRepository.save(purchaseOrder));
    }

    private boolean isValidStatusTransition(PurchaseOrderStatus current, PurchaseOrderStatus next) {
        return switch (next) {
            case APPROVED, REJECTED -> current == PurchaseOrderStatus.PENDING;
            case COMPLETED -> current == PurchaseOrderStatus.APPROVED;
            case CANCELLED -> current == PurchaseOrderStatus.PENDING || current == PurchaseOrderStatus.APPROVED;
            default -> false;
        };
    }

    @Override
    @Cacheable(value = "purchaseOrders", key = "#warehouseId + '_' + #supplierId + '_' + #status + '_' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort.toString()", unless = "#result.isEmpty()")
    public Page<PurchaseOrderResponse> getPurchaseOrders(String warehouseId, String supplierId,
            PurchaseOrderStatus status, Pageable pageable) {
        Page<PurchaseOrder> page = purchaseOrderRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>(3);
            if (warehouseId != null) {
                predicates.add(cb.equal(root.get("warehouse").get("warehouseId"), warehouseId));
            }
            if (supplierId != null) {
                predicates.add(cb.equal(root.get("supplier").get("supplierId"), supplierId));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return predicates.isEmpty() ? cb.conjunction()
                    : cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
        return page.map(purchaseOrderMapper::toPurchaseOrderResponse);
    }

    private static final int INVENTORY_WARNING_THRESHOLD = 10; // hoặc lấy từ config

    private void updateInventoryAfterPurchaseOrderCompletion(PurchaseOrder purchaseOrder) {
        List<Inventory> inventoriesToSave = new ArrayList<>();
        purchaseOrder.getDetails().forEach(detail -> {
            String productId = detail.getProductId();
            Warehouse warehouse = detail.getPurchaseOrder().getWarehouse();
            String warehouseId = warehouse.getWarehouseId();

            Inventory inventory = inventoryRepository
                    .findByProductIdAndWarehouse_WarehouseId(productId, warehouseId)
                    .orElse(null);

            if (inventory == null) {
                inventory = Inventory.builder()
                        .productId(productId)
                        .warehouse(warehouse)
                        .availableQuantity(detail.getQuantity())
                        .reservedQuantity(0)
                        .build();
            } else {
                inventory.setAvailableQuantity(inventory.getAvailableQuantity() + detail.getQuantity());
            }
            inventoriesToSave.add(inventory);

        });
        if (!inventoriesToSave.isEmpty()) {
            inventoryRepository.saveAll(inventoriesToSave);
        }
    }



}
