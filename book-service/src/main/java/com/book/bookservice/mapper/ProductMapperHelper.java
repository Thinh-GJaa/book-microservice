package com.book.bookservice.mapper;

import com.book.bookservice.dto.response.ProductResponse;
import com.book.bookservice.entity.Author;
import com.book.bookservice.entity.Category;
import com.book.bookservice.exception.CustomException;
import com.book.bookservice.exception.ErrorCode;
import com.book.bookservice.repository.AuthorRepository;
import com.book.bookservice.repository.CategoryRepository;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProductMapperHelper {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Named("authorIdsToAuthors")
    public Set<Author> authorIdsToAuthors(Set<String> authorIds) {
        if (authorIds == null) return null;
        return authorIds.stream()
                .map(id -> authorRepository.findById(id).orElseThrow(
                        ()-> new CustomException(ErrorCode.AUTHOR_NOT_FOUND, id)))
                .collect(Collectors.toSet());
    }

    @Named("categoryIdToCategory")
    public Category categoryIdToCategory(String categoryId) {
        if (categoryId == null) return null;
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND, categoryId));
    }

    @Named("mapAuthors")
    public List<ProductResponse.AuthorInfo> mapAuthors(Set<Author> authors) {
        if (authors == null) return null;
        return authors.stream()
                .map(a -> ProductResponse.AuthorInfo.builder()
                        .authorId(a.getAuthorId())
                        .authorName(a.getAuthorName())
                        .build())
                .collect(Collectors.toList());
    }

    @Named("mapCategory")
    public ProductResponse.CategoryInfo mapCategory(Category category) {
        if (category == null) return null;
        return ProductResponse.CategoryInfo.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .build();
    }
}
