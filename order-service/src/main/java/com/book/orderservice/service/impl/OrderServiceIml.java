package com.book.orderservice.service.impl;

import com.book.orderservice.dto.request.OrderItem;
import com.book.orderservice.dto.request.UpdateOrderStatusRequest;
import com.book.orderservice.enums.OrderStatus;
import com.book.orderservice.exception.CustomException;
import com.book.orderservice.exception.ErrorCode;
import com.book.orderservice.dto.request.CreateOrderRequest;
import com.book.orderservice.dto.response.OrderResponse;
import com.book.orderservice.entity.Order;
import com.book.orderservice.entity.OrderItemId;
import com.book.orderservice.mapper.OrderMapper;
import com.book.orderservice.producer.OrderProducer;
import com.book.orderservice.repository.OrderRepository;
import com.book.orderservice.service.OrderService;
import com.book.orderservice.service.client.BookClient;
import com.book.orderservice.service.client.InventoryClient;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceIml implements OrderService {

    OrderRepository orderRepository;

    OrderMapper orderMapper;

    BookClient bookClient;
    InventoryClient inventoryClient;

    OrderProducer orderProducer;

    @Override
    public void createOrder(CreateOrderRequest request) {
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();

        // Map request sang entity Order (chưa có orderItems)
        Order order = orderMapper.toOrder(request);

        // Lấy danh sách productId từ request
        Set<String> productIds = request.getOrderItems().stream()
                .map(OrderItem::getProductId)
                .collect(Collectors.toSet());

        // Kiểm tra tính hợp lệ của productId
        Set<String> invalidProductIds = bookClient.checkInvalidProductIds(productIds);

        if (!invalidProductIds.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_PRODUCT_ID, "Invalid product IDs: " + invalidProductIds);
        }

        String warehouseId = inventoryClient.checkAndReserveInventory(
                orderMapper.toCheckInventoryRequest(request));

        order.setWarehouseId(warehouseId);
        order.setUserId(userId);

        // Xử lý tạo OrderItem đúng chuẩn @EmbeddedId
        List<com.book.orderservice.entity.OrderItem> orderItemEntities = new ArrayList<>();
        for (OrderItem item : request.getOrderItems()) {
            OrderItemId orderItemId = new OrderItemId();
            // orderId sẽ được set sau khi order được lưu (có id)
            orderItemId.setProductId(item.getProductId());

            com.book.orderservice.entity.OrderItem entityItem = new com.book.orderservice.entity.OrderItem();
            entityItem.setId(orderItemId);
            entityItem.setOrder(order); // Gán order entity
            entityItem.setQuantity(item.getQuantity());
            entityItem.setUnitPrice(item.getUnitPrice());

            orderItemEntities.add(entityItem);
        }
        order.setOrderItems(orderItemEntities);

        // Lưu order, Hibernate sẽ tự lưu orderItems nhờ cascade = ALL
        order = orderRepository.save(order);

        orderProducer.sendCreatedOrderEvent(order);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "order", key = "#orderId")
    public OrderResponse getOrderById(String orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException(ErrorCode.ORDER_NOT_FOUND, orderId));

        return orderMapper.toOrderResponse(order);
    }

    @Override
    @CachePut(value = "order", key = "#request.orderId")
    public OrderResponse updateOrderStatus(UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(request.getOrderId()).orElseThrow(
                () -> new CustomException(ErrorCode.ORDER_NOT_FOUND, request.getOrderId()));

        OrderStatus currentStatus = order.getStatus();
        OrderStatus newStatus = request.getStatus();

        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new CustomException(ErrorCode.INVALID_ORDER_STATUS, currentStatus, newStatus);
        }

        order.setStatus(newStatus);
        order = orderRepository.save(order);

        if (newStatus == OrderStatus.CONFIRMED)
            orderProducer.sendOrderConfirmedEvent(order);

        return orderMapper.toOrderResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrders(String warehouseId, OrderStatus status, Pageable pageable) {
        Page<Order> page = orderRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>(3);
            if (warehouseId != null) {
                predicates.add(cb.equal(root.get("warehouseId"), warehouseId));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return predicates.isEmpty() ? cb.conjunction()
                    : cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
        return page.map(orderMapper::toOrderResponse);
    }

    private boolean isValidStatusTransition(OrderStatus current, OrderStatus next) {
        return switch (next) {
            case PENDING -> current == OrderStatus.CREATED;
            case CONFIRMED -> current == OrderStatus.PENDING;
            case PROCESSING -> current == OrderStatus.CONFIRMED;
            case SHIPPING -> current == OrderStatus.PROCESSING;
            case DELIVERED -> current == OrderStatus.SHIPPING;
            case CANCELLED ->
                current == OrderStatus.CREATED || current == OrderStatus.PENDING || current == OrderStatus.CONFIRMED
                        || current == OrderStatus.PROCESSING || current == OrderStatus.SHIPPING;
            case FAILED, REFUNDED -> current == OrderStatus.DELIVERED;
            default -> false;
        };
    }
}
