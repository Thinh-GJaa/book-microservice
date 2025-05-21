package com.book.bookservice.mapper;

import com.book.bookservice.dto.request.CreateCategoryRequest;
import com.book.bookservice.dto.request.UpdateCategoryRequest;
import com.book.bookservice.dto.response.CategoryResponse;
import com.book.bookservice.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CreateCategoryRequest request);

    Category updateCategory(@MappingTarget Category category, UpdateCategoryRequest request);

    CategoryResponse toCategoryResponse(Category category);

}
