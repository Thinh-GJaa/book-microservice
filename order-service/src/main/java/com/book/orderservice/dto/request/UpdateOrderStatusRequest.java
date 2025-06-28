package com.book.orderservice.dto.request;

import com.book.orderservice.enums.OrderStatus;
import com.book.orderservice.enums.PaymentMethod;
import com.book.orderservice.service.OrderService;
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
@Schema(description = "Request to update order status")
public class UpdateOrderStatusRequest {

    @Schema(description = "Order ID", example = "7b2a2e3c-9f92-4c73-a8be-1db4d17f3a3c")
    @NotBlank(message = "Order ID cannot be blank")
    String orderId;

    @Schema(description = "Order status", example = "DELIVERED")
    @NotNull(message = "Status cannot be null")
    OrderStatus status;

}
