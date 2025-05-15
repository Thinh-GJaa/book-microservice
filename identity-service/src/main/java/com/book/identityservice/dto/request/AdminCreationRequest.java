package com.book.identityservice.dto.request;

import com.book.identityservice.enums.Gender;
import com.book.identityservice.validator.ValidDob;
import com.book.identityservice.validator.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminCreationRequest {

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    String email;

    @ValidPassword
    String password;

    @NotBlank(message = "Firstname is required")
    String firstName;

    @NotBlank(message = "Lastname is required")
    String lastName;

    @NotNull(message = "Gender is required")
    Gender gender;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^0(3|5|7|8|9)\\d{8}$", message = "Phone number invalid")
    String phoneNumber;

    @ValidDob(min = 18)
    LocalDate dob;

    @NotBlank(message = "Address is required")
    String address;
}
