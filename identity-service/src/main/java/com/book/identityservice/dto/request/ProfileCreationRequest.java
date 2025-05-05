package com.book.identityservice.dto.request;

import java.time.LocalDate;

import com.book.identityservice.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileCreationRequest {
    String userId;
    String email;
    String firstName;
    String lastName;
    Gender gender;
    String phoneNumber;
    LocalDate dob;
    String address;
}
