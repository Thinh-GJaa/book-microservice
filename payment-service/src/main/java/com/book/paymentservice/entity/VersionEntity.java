package com.book.paymentservice.entity;

import jakarta.persistence.*;

public abstract class VersionEntity {

    @Version
    private Long version; // Hỗ trợ optimistic locking để tránh xung đột khi cập nhật dữ liệu
}
