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
public class UpdateCategoryRequest {

    @Schema(description = "Unique identifier of the category", example = "b3b7c9e2-8f2a-4c1a-9e2b-1a2b3c4d5e6f")
    @NotBlank(message = "Category ID cannot be blank")
    String categoryId;

    @Schema(description = "Name of the book category", example = "Science Fiction")
    @NotBlank(message = "Category name cannot be blank")
    String categoryName;

}
