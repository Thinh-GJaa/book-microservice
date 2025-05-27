package com.book.inventory.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "suppliers", indexes = { @Index(name = "idx_supplier_name", columnList = "nameSupplier") })
public class Supplier extends VersionEntity {
    @Id
    @GeneratedValue(generator = "uuid-generator")
    @GenericGenerator(name = "uuid-generator", strategy = "uuid2")
    String supplierId;

    @Column(nullable = false, unique = true, length = 100)
    String nameSupplier;

    @Column(length = 255)
    String contactName; // Tên người liên hệ chính của nhà cung cấp

    @Column(length = 100, unique = true)
    String phoneNumber;

    @Column(length = 255, unique = true)
    String email;

    @Column(length = 500)
    String address;

    @Column(length = 1000)
    String description;

    @Column(nullable = false, updatable = false)
    LocalDateTime createdDate;

    @Column(nullable = false)
    LocalDateTime updatedDate;

    @OneToMany(mappedBy = "supplier")
    List<PurchaseOrder> purchaseOrders;

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
