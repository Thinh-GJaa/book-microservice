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
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Request for updating stock out status")
public class UpdateStockOutStatusRequest {

    @Schema(description = "Stock out ID", example = "5f6f2a1b-d9a4-4d4c-aeb7-f09366e91f75")
    @NotBlank(message = "Warehouse ID is required")
    String stockOutId;

    @Schema(description = "Status", example = "COMPLETED")
    @NotNull(message = "Status is required")
    StockOutStatus status;

}