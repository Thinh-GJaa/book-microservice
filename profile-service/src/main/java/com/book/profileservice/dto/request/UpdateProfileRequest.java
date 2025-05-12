package com.book.profileservice.dto.request;

import com.book.profileservice.enums.Gender;
import com.book.profileservice.validator.ValidDob;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProfileRequest {

    @Email(message = "Email invalid")
    String email;

    String firstName;

    String lastName;

    Gender gender;

    @Pattern(regexp = "^0(3|5|7|8|9)\\d{8}$", message = "Phone number invalid")
    String phoneNumber;

    @ValidDob(min = 18)
    LocalDate dob;

    String address;
}
