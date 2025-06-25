package com.book.inventory.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "inventories", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "warehouse_id", "product_id" }) }, indexes = {
                @Index(name = "idx_product_id", columnList = "product_id") })
public class Inventory extends VersionEntity {
    @Id
    @GeneratedValue(generator = "uuid-generator")
    @GenericGenerator(name = "uuid-generator", strategy = "uuid2")
    String inventoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    Warehouse warehouse;

    @Column(name = "product_id", nullable = false)
    String productId;

    // Số lượng còn có thể bán
    @Column(name = "available_quantity", nullable = false)
    int availableQuantity;

    // Số lượng khách mua nhưng chưa giao
    @Column(name = "reserved_quantity", nullable = false)
    int reservedQuantity;

    @Column(nullable = false, updatable = false)
    LocalDateTime createdDate;

    @Column(nullable = false)
    LocalDateTime updatedDate;

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
