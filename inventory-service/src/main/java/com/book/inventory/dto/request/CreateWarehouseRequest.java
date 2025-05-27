package com.book.inventory.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateWarehouseRequest {

    @NotBlank(message = "Warehouse name cannot be blank")
    String warehouseName;

    @NotBlank(message = "Address cannot be blank")
    String address;

    @NotBlank(message = "Description cannot be blank")
    String description;
}
