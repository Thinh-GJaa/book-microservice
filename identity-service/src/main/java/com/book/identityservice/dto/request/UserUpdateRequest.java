package com.book.identityservice.dto.request;

import java.time.LocalDate;
import java.util.List;

import com.book.identityservice.validator.ValidDob;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String password;
    String firstName;
    String lastName;

    @ValidDob(min = 18, message = "INVALID_DOB")
    LocalDate dob;

    List<String> roles;
}
