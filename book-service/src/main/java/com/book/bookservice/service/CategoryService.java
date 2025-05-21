package com.book.bookservice.service;

import com.book.bookservice.dto.request.CreateCategoryRequest;
import com.book.bookservice.dto.request.DeleteCategoryRequest;
import com.book.bookservice.dto.request.UpdateCategoryRequest;
import com.book.bookservice.dto.response.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    CategoryResponse getCategoryById(String categoryId);

    CategoryResponse createCategory(CreateCategoryRequest createCategoryRequest);

    CategoryResponse updateCategory(UpdateCategoryRequest updateCategoryRequest);

    void deleteCategory(DeleteCategoryRequest deleteCategoryRequest);

    Page<CategoryResponse> getCategories(String keyword, Pageable pageable);
}
