package com.book.inventory.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "purchase_order_details", indexes = {
        @Index(name = "idx_purchase_order_detail_product_id", columnList = "product_id") })
public class PurchaseOrderDetail extends VersionEntity {
    @Id
    @GeneratedValue(generator = "uuid-generator")
    @GenericGenerator(name = "uuid-generator", strategy = "uuid2")
    String purchaseOrderDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id", nullable = false)
    PurchaseOrder purchaseOrder;

    @Column(name = "product_id", nullable = false)
    String productId;

    @Column(nullable = false)
    int quantity;

    @Column(precision = 18, scale = 2, nullable = false)
    BigDecimal unitPrice;

}
