package com.book.orderservice.dto.request;

import com.book.orderservice.enums.PaymentMethod;
import com.book.orderservice.validator.ValidPhoneNumber;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Request to create a new order")
public class CreateOrderRequest {

    @Schema(description = "Recipient name", example = "Nguyen Van A")
    @NotBlank(message = "Recipient name is required")
    String recipientName;

    @Schema(description = "Recipient phone", example = "0912345678")
    @NotBlank(message = "Recipient phone is required")
    @ValidPhoneNumber
    String recipientPhone;

    @Schema(description = "Shipping address", example = "123 Main St, Hanoi")
    @NotBlank(message = "Shipping address is required")
    String shippingAddress;

    @Schema(description = "Order note", example = "Giao giờ hành chính")
    @Size(max = 500, message = "Note cannot exceed 500 characters")
    String note;

    @Schema(description = "Payment method", example = "MOMO")
    @NotNull(message = "Payment method is required")
    PaymentMethod paymentMethod;

    @Schema(description = "List of order items")
    @NotEmpty(message = "Order items cannot be empty")
    @Valid
    List<OrderItem> orderItems;

}
