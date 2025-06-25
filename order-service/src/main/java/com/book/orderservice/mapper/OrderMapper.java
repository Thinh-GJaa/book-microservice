package com.book.orderservice.mapper;

import com.book.orderservice.dto.event.OrderEvent;
import com.book.orderservice.dto.request.CheckInventoryRequest;
import com.book.orderservice.dto.request.CreateOrderRequest;
import com.book.orderservice.dto.response.OrderResponse;
import com.book.orderservice.entity.Order;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "status", constant = "CREATED")
    @Mapping(target = "totalAmount", source = "request", qualifiedByName = "calculateTotalAmount")
    Order toOrder(CreateOrderRequest request);

    @Mapping(target = "orderItems", source = "orderItems", qualifiedByName = "orderItemListToDto")
    OrderResponse toOrderResponse(Order order);

    @Mapping(target = "orderItems", source = "orderItems", qualifiedByName = "orderItemListToDto")
    OrderEvent toOrderEvent(Order order);

    @Mapping(target = "address", source = "shippingAddress")
    @Mapping(target = "items", source = "orderItems")
    CheckInventoryRequest toCheckInventoryRequest(CreateOrderRequest request);

    @Named("calculateTotalAmount")
    default BigDecimal calculateTotalAmount(CreateOrderRequest request) {
        return request.getOrderItems().stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Named("orderItemListToDto")
    default com.book.orderservice.dto.request.OrderItem orderItemToDto(com.book.orderservice.entity.OrderItem entity) {
        return com.book.orderservice.dto.request.OrderItem.builder()
                .productId(entity.getId().getProductId())
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .build();
    }

}
