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
@Schema(description = "Request for updating supplier information")
public class UpdateSupplierRequest {

    @Schema(description = "Supplier ID", example = "5f6f2a1b-d9a4-4d4c-aeb7-f09366e91f75")
    @NotBlank(message = "Supplier ID is required")
    String supplierId;

    @Schema(description = "Supplier name", example = "ABC Company")
    String supplierName;
    @Schema(description = "Contact name", example = "John Doe")
    String contactName;
    @Schema(description = "Phone number", example = "0987654321")
    String phoneNumber;
    @Schema(description = "Email", example = "abc@company.com")
    String email;
    @Schema(description = "Address", example = "123 Main St")
    String address;
    @Schema(description = "Description", example = "Supplier of office equipment")
    String description;
}
