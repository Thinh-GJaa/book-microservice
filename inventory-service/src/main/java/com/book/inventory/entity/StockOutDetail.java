package com.book.inventory.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "stock_out_details")
public class StockOutDetail extends VersionEntity {
    @EmbeddedId
    StockOutDetailId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("stockOutId")
    @JoinColumn(name = "stock_out_id", nullable = false)
    StockOut stockOut;

    @Column(nullable = false)
    int quantity;

    @Column(name = "unit_price", precision = 18, scale = 2)
    BigDecimal unitPrice;
}
