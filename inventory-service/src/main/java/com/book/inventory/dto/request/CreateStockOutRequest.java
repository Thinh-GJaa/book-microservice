package com.book.inventory.dto.request;

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
public class CreateStockOutRequest {

    @NotBlank(message = "Warehouse ID is required")
    String warehouseId;

    @NotNull(message = "Stock out type is required")
    StockOutType type;

    @Size(max = 1000, message = "Note must not exceed 1000 characters")
    String note;

    @NotEmpty(message = "Stock out details are required")
    @Valid
    List<StockOutDetailRequest> details;
}
