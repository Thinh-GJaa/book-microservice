package com.book.paymentservice.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.book.paymentservice.enums.PaymentMethod;
import com.book.paymentservice.enums.PaymentStatus;

@Data
public class PaymentResponse {
    private String paymentId;
    private String orderId;
    private BigDecimal amount;
    private PaymentStatus status;
    private PaymentMethod paymentMethod;
    private LocalDateTime createdAt;
}