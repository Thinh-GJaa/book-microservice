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
@Table(name = "warehouses", uniqueConstraints = { @UniqueConstraint(columnNames = "code") }, indexes = {
        @Index(name = "idx_warehouse_name", columnList = "name") })
public class Warehouse extends VersionEntity {
    @Id
    @GeneratedValue(generator = "uuid-generator")
    @GenericGenerator(name = "uuid-generator", strategy = "uuid2")
    String warehouseId;

    @Column(nullable = false, length = 255)
    String warehouseName; // Tên kho

    @Column(nullable = false, length = 500)
    String address; // Địa chỉ kho

    @Column(length = 1000)
    String description; // Mô tả thêm về kho, có thể null

    @Column(nullable = false)
    boolean active; // Trạng thái hoạt động, dùng kiểu nguyên thủy

    @Column(nullable = false, updatable = false)
    LocalDateTime createdDate; // Thời gian tạo bản ghi

    @Column(nullable = false)
    LocalDateTime updatedDate; // Thời gian cập nhật bản ghi gần nhất

    @OneToMany(mappedBy = "warehouse")
    List<Inventory> inventories; // Danh sách các bản ghi tồn kho liên kết với kho này

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