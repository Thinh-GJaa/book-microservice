package com.book.orderservice.service;


import com.book.orderservice.dto.request.CreateOrderRequest;
import com.book.orderservice.dto.request.UpdateOrderStatusRequest;
import com.book.orderservice.dto.response.OrderResponse;
import com.book.orderservice.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    void createOrder(CreateOrderRequest request);

    OrderResponse getOrderById(String orderId);

    OrderResponse updateOrderStatus(UpdateOrderStatusRequest request);

    Page<OrderResponse> getOrders(String warehouseId, OrderStatus status, Pageable pageable);

}
