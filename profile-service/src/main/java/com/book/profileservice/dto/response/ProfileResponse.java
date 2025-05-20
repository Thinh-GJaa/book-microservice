package com.book.profileservice.dto.response;


import com.book.profileservice.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileResponse implements Serializable {
    String userId;
    String email;
    String phoneNumber;
    String firstName;
    String lastName;
    Gender gender;
    String address;
    LocalDate dob;

}
