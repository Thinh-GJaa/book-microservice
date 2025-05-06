package com.book.profileservice.dto.response;


import com.book.profileservice.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

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
