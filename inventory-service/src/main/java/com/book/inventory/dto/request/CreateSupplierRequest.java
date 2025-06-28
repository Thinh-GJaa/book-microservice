package com.book.inventory.dto.request;

import com.book.inventory.validator.ValidPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Request for creating a new supplier")
public class CreateSupplierRequest {

    @Schema(description = "Supplier name", example = "ABC Company")
    @NotBlank(message = "Supplier name is required")
    String supplierName;

    @Schema(description = "Contact name", example = "John Doe")
    @NotBlank(message = "Contact name is required")
    String contactName;

    @Schema(description = "Phone number", example = "0987654321")
    @NotBlank(message = "Phone number is required")
    @ValidPhoneNumber
    String phoneNumber;

    @Schema(description = "Email", example = "abc@company.com")
    @Email
    @NotBlank(message = "Email is required")
    String email;

    @Schema(description = "Address", example = "123 Main St")
    @NotBlank(message = "Address is required")
    String address;

    @Schema(description = "Description", example = "Supplier of office equipment")
    @NotBlank(message = "Description is required")
    String description;
}
