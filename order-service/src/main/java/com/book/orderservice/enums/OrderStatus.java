package com.book.orderservice.enums;

public enum OrderStatus {
    CREATED, // Đã tạo
    PENDING, // Chờ xử lý
    CONFIRMED, // Đã xác nhận
    PROCESSING, // Đang xử lý
    SHIPPING, // Đang giao hàng
    DELIVERED,  // Đã giao hàng
    CANCELLED, // Đã hủy
    FAILED, // Đã thất bại
    REFUNDED // Đã hoàn trả
}