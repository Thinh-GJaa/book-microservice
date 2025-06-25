package com.book.orderservice.entity;

import jakarta.persistence.Version;

public abstract class VersionEntity {

    @Version
    private Long version; // Hỗ trợ optimistic locking để tránh xung đột khi cập nhật dữ liệu
}
