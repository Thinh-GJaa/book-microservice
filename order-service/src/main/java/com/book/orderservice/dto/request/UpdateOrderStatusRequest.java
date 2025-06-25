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

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateOrderStatusRequest {

    @NotBlank(message = "Order ID cannot be blank")
    String orderId;

    @NotNull(message = "Status cannot be null")
    OrderStatus status;

}
