package com.book.paymentservice.dto.request;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import com.book.paymentservice.enums.PaymentMethod;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    @NotNull(message = "Order ID is required")
    private String orderId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
}