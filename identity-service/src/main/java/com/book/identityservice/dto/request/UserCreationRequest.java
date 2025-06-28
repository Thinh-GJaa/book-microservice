package com.book.identityservice.dto.request;

import java.time.LocalDate;

import com.book.identityservice.enums.Gender;
import com.book.identityservice.validator.ValidDob;
import com.book.identityservice.validator.ValidPassword;
import com.book.identityservice.validator.ValidPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request for creating a new user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @Schema(description = "User email", example = "user@gmail.com")
    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    String email;

    @Schema(description = "User password", example = "Abc@123456")
    @NotBlank(message = "Password is required")
    @ValidPassword
    String password;

    @Schema(description = "First name", example = "John")
    @NotBlank(message = "Firstname is required")
    String firstName;

    @Schema(description = "Last name", example = "Doe")
    @NotBlank(message = "Lastname is required")
    String lastName;

    @Schema(description = "Gender", example = "MALE")
    @NotNull(message = "Gender is required")
    Gender gender;

    @Schema(description = "Phone number", example = "0923456789")
    @NotBlank(message = "Phone number is required")
    @ValidPhoneNumber
    String phoneNumber;

    @Schema(description = "Date of birth", example = "2000-01-01")
    @ValidDob(min = 18)
    LocalDate dob;

    @Schema(description = "Address", example = "123 Main St")
    @NotBlank(message = "Address is required")
    String address;
}
