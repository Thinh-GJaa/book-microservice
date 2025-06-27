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
public class UpdateAuthorRequest {

    @Schema(description = "Unique identifier of the author", example = "b3b7c9e2-8f2a-4c1a-9e2b-1a2b3c4d5e6f")
    @NotBlank(message = "Author ID cannot blank")
    String authorId;

    @Schema(description = "Full name of the author", example = "John Doe")
    String authorName;

    @Schema(description = "Gender of the author", example = "MALE")
    Gender gender;

    @Schema(description = "Birth date of the author", example = "1980-01-01")
    LocalDate birthDate;

    @Schema(description = "Nationality of the author", example = "American")
    String nationality;

    @Schema(description = "Short description or biography of the author", example = "Award-winning novelist and essayist.")
    String description;

}
