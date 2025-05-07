package com.book.identityservice.dto.response;

import java.time.LocalDate;

import com.book.identityservice.enums.Gender;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
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
