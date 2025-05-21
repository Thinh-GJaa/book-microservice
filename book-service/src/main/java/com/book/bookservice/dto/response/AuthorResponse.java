package com.book.bookservice.dto.response;

import com.book.bookservice.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthorResponse {

    String authorId;

    String authorName;

    Gender gender;

    LocalDate birthDate;

    String nationality;

    String description;

}
