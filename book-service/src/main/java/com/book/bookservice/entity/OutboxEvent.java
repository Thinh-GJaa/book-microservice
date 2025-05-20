package com.book.bookservice.entity;

import com.book.identityservice.enums.OutboxEventStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "outbox_event")
public class OutboxEvent {

    @Id
    String id; // Định danh duy nhất cho mỗi sự kiện (UUID hoặc chuỗi bất kỳ)

    String aggregateType; // Loại aggregate (ví dụ: "ORDER", "USER"), dùng làm tên topic Kafka

    String aggregateId; // Định danh của aggregate liên quan (ví dụ: mã đơn hàng, mã người dùng)

    String eventType; // Loại sự kiện (ví dụ: "ORDER_CREATED", "USER_UPDATED")

    @Lob
    String payload; // Dữ liệu chi tiết của sự kiện, thường là chuỗi JSON

    @Builder.Default
    LocalDateTime createdAt = LocalDateTime.now(); // Thời điểm tạo sự kiện

    @Enumerated(EnumType.STRING)
    @Builder.Default
    OutboxEventStatus status = OutboxEventStatus.PENDING; // Trạng thái xử lý sự kiện (PENDING, SENT, FAILED)

}
