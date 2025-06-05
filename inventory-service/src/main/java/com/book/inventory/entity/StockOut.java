package com.book.inventory.entity;

import com.book.inventory.enums.StockOutType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.book.inventory.enums.StockOutStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "stock_outs")
public class StockOut extends VersionEntity {
    @Id
    @GeneratedValue(generator = "uuid-generator")
    @GenericGenerator(name = "uuid-generator", strategy = "uuid2")
    String stockOutId;

    @Column(length = 1000)
    String note;

    @Column(precision = 18, scale = 2, nullable = false)
    @Builder.Default
    BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(nullable = false, updatable = false)
    LocalDateTime createdDate;

    @Column(nullable = false)
    LocalDateTime updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    Warehouse warehouse;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    StockOutType type;

    @Column(name = "status", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    StockOutStatus status = StockOutStatus.PENDING;

    @OneToMany(mappedBy = "stockOut", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<StockOutDetail> details = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
        this.status = StockOutStatus.PENDING;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = LocalDateTime.now();
    }
}
