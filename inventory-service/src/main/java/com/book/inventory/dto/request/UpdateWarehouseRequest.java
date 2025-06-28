package com.book.inventory.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Request for updating warehouse information")
public class UpdateWarehouseRequest {

    @Schema(description = "Warehouse ID", example = "5f6f2a1b-d9a4-4d4c-aeb7-f09366e91f75")
    @NotBlank(message = "Warehouse ID cannot be blank")
    String warehouseId;

    @Schema(description = "Warehouse name", example = "Central Warehouse")
    String warehouseName;

    @Schema(description = "Address", example = "456 Storage Ave")
    String address;

    @Schema(description = "Description", example = "Main storage for all products")
    String description;
}
