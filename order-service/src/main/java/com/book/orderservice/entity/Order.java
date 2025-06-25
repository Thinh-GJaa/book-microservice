package com.book.orderservice.entity;

import com.book.orderservice.enums.OrderStatus;
import com.book.orderservice.enums.PaymentMethod;
import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order extends VersionEntity {

    @Id
    @GeneratedValue(generator = "uuid-generator")
    @GenericGenerator(name = "uuid-generator", strategy = "uuid2")
    String orderId;

    @Column(nullable = false)
    String userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    OrderStatus status;

    @Column(nullable = false)
    BigDecimal totalAmount;

    @Column(nullable = false)
    String recipientName;

    @Column(nullable = false)
    String recipientPhone;

    @Column(nullable = false)
    String shippingAddress;

    @Column
    String note;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    PaymentMethod paymentMethod;

    @Column(nullable = false)
    String warehouseId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItem> orderItems = new ArrayList<>();

    @Column(nullable = false)
    LocalDateTime createdAt;

    @Column
    LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = OrderStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}