package com.book.inventory.service.impl;

import com.book.inventory.dto.request.CreateStockOutRequest;
import com.book.inventory.dto.request.StockOutDetailRequest;
import com.book.inventory.dto.request.UpdateStockOutStatusRequest;
import com.book.inventory.dto.response.StockOutResponse;
import com.book.inventory.entity.*;
import com.book.inventory.enums.StockOutStatus;
import com.book.inventory.enums.StockOutType;
import com.book.inventory.exception.CustomException;
import com.book.inventory.exception.ErrorCode;
import com.book.inventory.mapper.StockOutMapper;
import com.book.inventory.repository.InventoryRepository;
import com.book.inventory.repository.StockOutRepository;
import com.book.inventory.repository.WarehouseRepository;
import com.book.inventory.service.StockOutService;
import com.book.inventory.service.client.BookClient;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class StockOutServiceImpl implements StockOutService {

    StockOutRepository stockOutRepository;
    InventoryRepository inventoryRepository;
    WarehouseRepository warehouseRepository;

    StockOutMapper stockOutMapper;

    BookClient bookClient;

    @Override
    @CacheEvict(value = "stockOuts", allEntries = true)
    public StockOutResponse createStockOut(CreateStockOutRequest request) {
        // Lấy danh sách productId từ request
        List<String> productIds = request.getDetails().stream()
                .map(StockOutDetailRequest::getProductId)
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

        // Lấy warehouse từ repository
        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new CustomException(ErrorCode.WAREHOUSE_NOT_FOUND, request.getWarehouseId()));

        // Tính tổng tiền
        BigDecimal totalAmount = request.getDetails().stream()
                .map(d -> d.getUnitPrice().multiply(BigDecimal.valueOf(d.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Tạo StockOut
        StockOut stockOut = new StockOut();
        stockOut.setWarehouse(warehouse);
        stockOut.setType(request.getType());
        stockOut.setNote(request.getNote());
        stockOut.setTotalAmount(totalAmount);
        stockOut.setStatus(StockOutStatus.PENDING);
        stockOut.setDetails(new ArrayList<>());

        // 1. Lưu StockOut để sinh ra stockOutId
        stockOut = stockOutRepository.save(stockOut);
        String stockOutId = stockOut.getStockOutId();

        // 2. Tạo từng StockOutDetail với id đầy đủ và set stockOut
        List<StockOutDetail> details = new ArrayList<>();
        for (StockOutDetailRequest detailReq : request.getDetails()) {
            StockOutDetailId id = StockOutDetailId.builder()
                    .stockOutId(stockOutId)
                    .productId(detailReq.getProductId())
                    .build();

            StockOutDetail detail = new StockOutDetail();
            detail.setId(id);
            detail.setStockOut(stockOut);
            detail.setQuantity(detailReq.getQuantity());
            detail.setUnitPrice(detailReq.getUnitPrice());
            details.add(detail);
        }

        // 3. Gán lại danh sách detail cho StockOut
        stockOut.setDetails(details);

        // 4. Lưu lại StockOut
        return stockOutMapper.toStockOutResponse(stockOutRepository.save(stockOut));
    }

    @Override
    @Cacheable(value = "stockOut", key = "#stockOutId")
    public StockOutResponse getStockOutById(String stockOutId) {
        StockOut stockOut = stockOutRepository.findById(stockOutId)
                .orElseThrow(() -> new CustomException(ErrorCode.STOCK_OUT_NOT_FOUND, stockOutId));
        return stockOutMapper.toStockOutResponse(stockOut);
    }

    @Override
    @Caching(
            put = @CachePut(value = "stockOut", key = "#req.stockOutId"),
            evict = @CacheEvict(value = "stockOuts", allEntries = true)
    )
    public StockOutResponse updateStockOutStatus(UpdateStockOutStatusRequest req) {
        String stockOutId = req.getStockOutId();
        StockOutStatus status = req.getStatus();

        StockOut stockOut = stockOutRepository.findById(stockOutId)
                .orElseThrow(() -> new CustomException(ErrorCode.STOCK_OUT_NOT_FOUND, stockOutId));

        StockOutStatus currentStatus = stockOut.getStatus();
        if (!isValidStatusTransition(currentStatus, status)) {
            throw new CustomException(ErrorCode.INVALID_STOCK_OUT_STATUS, status);
        }

        stockOut.setStatus(status);

        // Nếu trạng thái là COMPLETED thì mới cập nhật tồn kho
        if (status == StockOutStatus.COMPLETED) {
            updateInventory(stockOut);
        }

        StockOut savedStockOut = stockOutRepository.save(stockOut);
        return stockOutMapper.toStockOutResponse(savedStockOut);
    }

    @Override
    @Cacheable(value = "stockOuts", key = "#warehouseId + '_' + #type + '_' + #status + '_' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort.toString()", unless = "#result.isEmpty()")
    public Page<StockOutResponse> filterStockOuts(String warehouseId, StockOutType type, StockOutStatus status, Pageable pageable) {
        Page<StockOut> page = stockOutRepository.findAll((root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>(3);
            if (warehouseId != null) {
                predicates.add(cb.equal(root.get("warehouse").get("warehouseId"), warehouseId));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (type != null) {
                predicates.add(cb.equal(root.get("type"), type));
            }
            return predicates.isEmpty() ? cb.conjunction()
                    : cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        return page.map(stockOutMapper::toStockOutResponse);
    }


    private boolean isValidStatusTransition(StockOutStatus current, StockOutStatus next) {
        return switch (next) {
            case APPROVED, REJECTED -> current == StockOutStatus.PENDING;
            case COMPLETED -> current == StockOutStatus.APPROVED;
            case CANCELLED -> current == StockOutStatus.PENDING || current == StockOutStatus.APPROVED;
            default -> false;
        };
    }


    private void updateInventory(StockOut stockOut) {
        List<Inventory> inventories = new ArrayList<>();
        String warehouseId = stockOut.getWarehouse().getWarehouseId();
        StockOutType type = stockOut.getType();

        for (StockOutDetail detail : stockOut.getDetails()) {
            String productId = detail.getId().getProductId();
            Inventory inventory = inventoryRepository
                    .findByProductIdAndWarehouse_WarehouseId(productId, warehouseId)
                    .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND, productId));

            if (type == StockOutType.SALE) {
                // Trừ ở reservedQuantity
                if (inventory.getReservedQuantity() < detail.getQuantity()) {
                    throw new CustomException(ErrorCode.INSUFFICIENT_STOCK, productId);
                }
                inventory.setReservedQuantity(inventory.getReservedQuantity() - detail.getQuantity());
            } else {
                // Trừ ở availableQuantity
                if (inventory.getAvailableQuantity() < detail.getQuantity()) {
                    throw new CustomException(ErrorCode.INSUFFICIENT_STOCK, productId);
                }
                inventory.setAvailableQuantity(inventory.getAvailableQuantity() - detail.getQuantity());
            }
            inventories.add(inventory);
        }
        if (!inventories.isEmpty()) {
            inventoryRepository.saveAll(inventories);
        }
    }
}
