package com.book.identityservice.dto.request;

import com.book.identityservice.enums.Gender;
import com.book.identityservice.validator.ValidDob;
import com.book.identityservice.validator.ValidPassword;
import com.book.identityservice.validator.ValidPhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Request for creating a new admin")
public class AdminCreationRequest {

    @Schema(description = "Admin email", example = "admin@gmail.com")
    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    String email;

    @Schema(description = "Admin password", example = "Admin@123456")
    @NotBlank(message = "Password is required")
    @ValidPassword
    String password;

    @Schema(description = "First name", example = "Alice")
    @NotBlank(message = "Firstname is required")
    String firstName;

    @Schema(description = "Last name", example = "Smith")
    @NotBlank(message = "Lastname is required")
    String lastName;

    @Schema(description = "Gender", example = "FEMALE")
    @NotNull(message = "Gender is required")
    Gender gender;

    @Schema(description = "Phone number", example = "0987654321")
    @NotBlank(message = "Phone number is required")
    @ValidPhoneNumber
    String phoneNumber;

    @Schema(description = "Date of birth", example = "1990-05-15")
    @ValidDob(min = 18)
    LocalDate dob;

    @Schema(description = "Address", example = "456 Admin Ave")
    @NotBlank(message = "Address is required")
    String address;
}
