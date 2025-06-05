package com.book.inventory.dto.request;

import com.book.inventory.enums.StockOutStatus;
import com.book.inventory.enums.StockOutType;
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
public class UpdateStockOutStatusRequest {

    @NotBlank(message = "Warehouse ID is required")
    String stockOutId;

    @NotNull(message = "Status is required")
    StockOutStatus status;

}