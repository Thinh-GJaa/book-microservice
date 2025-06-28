package com.book.bookservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCategoryRequest {

    @Schema(description = "Name of the book category", example = "Science Fiction")
    @NotBlank(message = "Category name cannot be blank")
    String categoryName;

}
