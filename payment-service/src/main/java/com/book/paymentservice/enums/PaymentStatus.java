package com.book.paymentservice.enums;

public enum PaymentStatus {
    PENDING, // Đang chờ xử lý
    COMPLETED, // Đã hoàn thành
    FAILED, // Thất bại
    REFUND_FAILED, // Hoàn tiền thất bại
    REFUNDED, // Đã hoàn tiền
    CANCELLED, // Đã hủy
    EXPIRED, // Đã hết hạn

}