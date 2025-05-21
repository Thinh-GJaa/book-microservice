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
public class CreateAuthorRequest {

    @NotBlank(message = "Author name cannot blank")
    String authorName;

    @NotNull(message = "Gender cannot null")
    Gender gender;

    @NotNull(message = "Birth day cannot null")
    LocalDate birthDate;

    @NotBlank(message = "Nationality cannot blank")
    String nationality;

    @NotBlank(message = "Description cannot blank")
    String description;


}
