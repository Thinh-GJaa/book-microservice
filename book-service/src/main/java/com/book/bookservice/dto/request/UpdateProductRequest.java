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
public class UpdateProductRequest {

    @NotBlank(message = "Product Id cannot be blank")
    String productId;

    String title;

    String publisher;

    Integer publishYear;

    String isbn; // Định danh sách toàn cầu

    BigDecimal price;

    String language;

    String categoryId;

    Set<String> authorIds;
}
