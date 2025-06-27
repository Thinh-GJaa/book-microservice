package com.book.bookservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProductRequest {

    @Schema(description = "Title of the book", example = "The Great Gatsby")
    @NotBlank(message = "Book title cannot be blank")
    String title;

    @Schema(description = "Publisher of the book", example = "Scribner")
    @NotBlank(message = "Book publisher cannot be blank")
    String publisher;

    @Schema(description = "Year the book was published", example = "1925")
    @NotNull(message = "Book publish year cannot be null")
    Integer publishYear;

    @Schema(description = "International Standard Book Number (ISBN)", example = "978-3-16-148410-0")
    @NotBlank(message = "Book isbn cannot be blank")
    String isbn; // Định danh sách toàn cầu

    @Schema(description = "Price of the book", example = "19.99")
    @NotNull(message = "Book price cannot be null")
    BigDecimal price;

    @Schema(description = "Language of the book", example = "English")
    @NotBlank(message = "Book language cannot be blank")
    String language;

    @Schema(description = "Category ID of the book", example = "b3b7c9e2-8f2a-4c1a-9e2b-1a2b3c4d5e6f")
    @NotBlank(message = "Book categoryId cannot be blank")
    String categoryId;

    @Schema(description = "Set of author IDs for the book", example = "[\"b3b7c9e2-8f2a-4c1a-9e2b-1a2b3c4d5e6f\", \"a1b2c3d4-5678-90ab-cdef-1234567890ab\"]")
    @NotEmpty(message = "Book must have at least one author")
    Set<String> authorIds;
}
