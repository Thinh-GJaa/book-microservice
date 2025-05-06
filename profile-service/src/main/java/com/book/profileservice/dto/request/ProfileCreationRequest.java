package com.book.profileservice.dto.request;

import com.book.profileservice.enums.Gender;
import com.book.profileservice.validator.ValidDob;
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
public class ProfileCreationRequest {

    @NotBlank(message = "UserId is required")
    String userId;

    @Email(message = "Email invalid")
    @NotBlank(message = "Email is required")
    String email;

    @NotBlank(message = "FirstName is required")
    String firstName;

    @NotBlank(message = "LastName is required")
    String lastName;

    @NotNull(message = "Gender is required")
    Gender gender;

    @NotNull(message = "PhoneNumber is required")
    String phoneNumber;

    @ValidDob(min = 18)
    LocalDate dob;

    @NotBlank(message = "Address is required")
    String address;
}
