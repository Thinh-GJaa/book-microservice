package com.book.identityservice.dto.response;

import java.time.LocalDate;

import com.book.identityservice.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileResponse {
    String userId;
    String email;
    String phoneNumber;
    String firstName;
    String lastName;
    Gender gender;
    String address;
    LocalDate dob;

}
