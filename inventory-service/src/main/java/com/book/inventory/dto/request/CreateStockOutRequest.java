package com.book.inventory.dto.request;

import com.book.inventory.enums.StockOutType;
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
@Schema(description = "Request for creating a new stock out")
public class CreateStockOutRequest {

    @Schema(description = "Warehouse ID", example = "5f6f2a1b-d9a4-4d4c-aeb7-f09366e91f75")
    @NotBlank(message = "Warehouse ID is required")
    String warehouseId;

    @Schema(description = "Stock out type", example = "SALE")
    @NotNull(message = "Stock out type is required")
    StockOutType type;

    @Schema(description = "Note", example = "Stock out for customer order")
    @Size(max = 1000, message = "Note must not exceed 1000 characters")
    String note;

    @Schema(description = "List of stock out details")
    @NotEmpty(message = "Stock out details are required")
    @Valid
    List<StockOutDetailRequest> details;
}
