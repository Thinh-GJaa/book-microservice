package com.book.bookservice.dto.request;

import com.book.bookservice.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateAuthorRequest {

    @Schema(description = "Full name of the author", example = "John Doe")
    @NotBlank(message = "Author name cannot blank")
    String authorName;

    @Schema(description = "Gender of the author", example = "MALE")
    @NotNull(message = "Gender cannot null")
    Gender gender;

    @Schema(description = "Birth date of the author", example = "1980-01-01")
    @NotNull(message = "Birth day cannot null")
    LocalDate birthDate;

    @Schema(description = "Nationality of the author", example = "American")
    @NotBlank(message = "Nationality cannot blank")
    String nationality;

    @Schema(description = "Short description or biography of the author", example = "Award-winning novelist and essayist.")
    @NotBlank(message = "Description cannot blank")
    String description;

}
