package com.book.paymentservice.enums;

public enum TransactionStatus {
    PENDING, // Đang chờ xử lý
    SUCCESS, // Thành công
    FAILED, // Thất bại
    EXPIRED, // Hết thời gian thanh toán
}