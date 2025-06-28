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
@Schema(description = "Request for creating a new warehouse")
public class CreateWarehouseRequest {

    @Schema(description = "Warehouse name", example = "Central Warehouse")
    @NotBlank(message = "Warehouse name cannot be blank")
    String warehouseName;

    @Schema(description = "Address", example = "456 Storage Ave")
    @NotBlank(message = "Address cannot be blank")
    String address;

    @Schema(description = "Description", example = "Main storage for all products")
    @NotBlank(message = "Description cannot be blank")
    String description;
}
