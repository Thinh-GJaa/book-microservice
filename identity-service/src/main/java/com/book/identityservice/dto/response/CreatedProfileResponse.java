package com.book.identityservice.dto.response;

import java.time.LocalDate;

import com.book.identityservice.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatedProfileResponse {
    String userId;
    String email;
    String firstName;
    String lastName;
    Gender gender;
    String phoneNumber;
    LocalDate dob;
    String address;
}
