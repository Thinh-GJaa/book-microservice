package com.book.inventory.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateWarehouseRequest {

    @NotBlank(message = "Warehouse ID cannot be blank")
    String warehouseId;

    String warehouseName;

    String address;

    String description;
}
