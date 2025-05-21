package com.book.bookservice.dto.request;

import com.book.bookservice.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateAuthorRequest {

    @NotBlank(message = "Author ID cannot blank")
    String authorId;

    String authorName;

    Gender gender;

    LocalDate birthDate;

    String nationality;

    String description;

}
