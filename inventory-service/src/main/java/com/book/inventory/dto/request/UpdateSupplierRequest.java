package com.book.inventory.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateSupplierRequest {

    @NotBlank(message = "Supplier ID is required")
    String supplierId;

    String supplierName;
    String contactName;
    String phoneNumber;
    String email;
    String address;
    String description;
}
