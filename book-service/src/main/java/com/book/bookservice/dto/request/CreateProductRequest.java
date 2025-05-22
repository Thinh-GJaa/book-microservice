package com.book.bookservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProductRequest {

    @NotBlank(message = "Book title cannot be blank")
    String title;

    @NotBlank(message = "Book publisher cannot be blank")
    String publisher;

    @NotNull(message = "Book publish year cannot be null")
    Integer publishYear;

    @NotBlank(message = "Book isbn cannot be blank")
    String isbn; // Định danh sách toàn cầu

    @NotNull(message = "Book price cannot be null")
    BigDecimal price;

    @NotBlank(message = "Book language cannot be blank")
    String language;

    @NotBlank(message = "Book categoryId cannot be blank")
    String categoryId;

    @NotEmpty(message = "Book must have at least one author")
    Set<String> authorIds;
}
