package com.book.bookservice.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse implements Serializable {

    String productId;

    String title;

    String publisher;

    Integer publishYear;

    String isbn; // Định danh sách toàn cầu

    BigDecimal price;

    String language;

    LocalDateTime createDate;

    LocalDateTime updateDate;

    CategoryInfo category;

    List<AuthorInfo> authors;


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthorInfo implements Serializable {
        String authorId;
        String authorName;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryInfo implements Serializable{
        String categoryId;
        String categoryName;
    }
}
