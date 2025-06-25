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

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateOrderRequest {

    @NotBlank(message = "Recipient name is required")
    String recipientName;

    @NotBlank(message = "Recipient phone is required")
    @ValidPhoneNumber
    String recipientPhone;

    @NotBlank(message = "Shipping address is required")
    String shippingAddress;

    @Size(max = 500, message = "Note cannot exceed 500 characters")
    String note;

    @NotNull(message = "Payment method is required")
    PaymentMethod paymentMethod;

    @NotEmpty(message = "Order items cannot be empty")
    @Valid
    List<OrderItem> orderItems;

}
