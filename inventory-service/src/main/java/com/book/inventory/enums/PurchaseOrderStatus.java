package com.book.inventory.enums;

public enum PurchaseOrderStatus {
    PENDING,        // Đơn hàng mới tạo, chưa xử lý
    APPROVED,       // Đơn hàng đã được phê duyệt
    REJECTED,       // Đơn hàng bị từ chối
    COMPLETED,      // Đơn hàng đã hoàn thành
    CANCELLED       // Đơn hàng đã bị hủy
}
