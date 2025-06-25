package com.book.paymentservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


import com.book.paymentservice.enums.PaymentMethod;
import com.book.paymentservice.enums.PaymentStatus;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "payments")
public class Payment extends VersionEntity {

    @Id
    @GeneratedValue(generator = "uuid-generator")
    @GenericGenerator(name = "uuid-generator", strategy = "uuid2")
    String paymentId;

    @Column(nullable = false, unique = true)
    String orderId;

    @Column(nullable = false)
    BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
            @Column(nullable = false)
    PaymentStatus status;

    @Enumerated(EnumType.STRING)
    PaymentMethod paymentMethod;

    @Column(nullable = false)
    LocalDateTime createdAt;

    @Column(nullable = false)
    LocalDateTime updatedAt;

    @Column(nullable = false)

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}