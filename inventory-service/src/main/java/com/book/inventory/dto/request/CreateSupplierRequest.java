package com.book.inventory.dto.request;

import com.book.inventory.validator.ValidPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateSupplierRequest {

    @NotBlank(message = "Supplier name is required")
    String supplierName;

    @NotBlank(message = "Contact name is required")
    String contactName;

    @NotBlank(message = "Phone number is required")
    @ValidPhoneNumber
    String phoneNumber;

    @Email
    @NotBlank(message = "Email is required")
    String email;

    @NotBlank(message = "Address is required")
    String address;

    @NotBlank(message = "Description is required")
    String description;
}
