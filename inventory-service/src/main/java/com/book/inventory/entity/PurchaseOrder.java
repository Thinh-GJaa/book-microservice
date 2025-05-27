package com.book.inventory.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import com.book.inventory.enums.PurchaseOrderStatus;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder extends VersionEntity {
    @Id
    @GeneratedValue(generator = "uuid-generator")
    @GenericGenerator(name = "uuid-generator", strategy = "uuid2")
    String purchaseOrderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    Supplier supplier;

    @Column(length = 1000)
    String note;

    @Column(nullable = false)
    LocalDateTime orderDate;

    @Column(precision = 18, scale = 2)
    BigDecimal totalAmount;

    @Column(nullable = false, updatable = false)
    LocalDateTime createdDate;

    @Column(nullable = false)
    LocalDateTime updatedDate;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    PurchaseOrderStatus status;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    List<PurchaseOrderDetail> details;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = LocalDateTime.now();
    }
}
