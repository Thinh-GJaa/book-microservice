package com.book.bookservice.dto.response;

import com.book.bookservice.enums.Gender;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorResponse implements Serializable {

    String authorId;

    String authorName;

    Gender gender;

    LocalDate birthDate;

    String nationality;

    String description;

}
