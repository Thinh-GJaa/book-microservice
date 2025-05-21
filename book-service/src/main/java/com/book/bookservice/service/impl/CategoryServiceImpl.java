package com.book.bookservice.service.impl;

import com.book.bookservice.dto.request.CreateCategoryRequest;
import com.book.bookservice.dto.request.DeleteCategoryRequest;
import com.book.bookservice.dto.request.UpdateCategoryRequest;
import com.book.bookservice.dto.response.CategoryResponse;
import com.book.bookservice.entity.Category;
import com.book.bookservice.exception.CustomException;
import com.book.bookservice.exception.ErrorCode;
import com.book.bookservice.mapper.CategoryMapper;
import com.book.bookservice.repository.CategoryRepository;
import com.book.bookservice.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    @Override
    public CategoryResponse getCategoryById(String categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND, categoryId));

        return categoryMapper.toCategoryResponse(category);

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public CategoryResponse createCategory(CreateCategoryRequest createCategoryRequest) {
        try {

            Category category = categoryMapper.toCategory(createCategoryRequest);

            category = categoryRepository.save(category);

            return categoryMapper.toCategoryResponse(category);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.CATEGORY_NAME_ALREADY_EXISTS, createCategoryRequest.getCategoryName());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public CategoryResponse updateCategory(UpdateCategoryRequest updateCategoryRequest) {
        try {
            Category category = categoryRepository.findById(updateCategoryRequest.getCategoryId())
                    .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND, updateCategoryRequest.getCategoryId()));

            category = categoryMapper.updateCategory(category, updateCategoryRequest);
            category = categoryRepository.save(category);

            return categoryMapper.toCategoryResponse(category);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.CATEGORY_NAME_ALREADY_EXISTS, updateCategoryRequest.getCategoryName());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public void deleteCategory(DeleteCategoryRequest deleteCategoryRequest) {

        Category category = categoryRepository.findById(deleteCategoryRequest.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND, deleteCategoryRequest.getCategoryId()));

        categoryRepository.delete(category);
    }


    @Override
    @Cacheable(value = "categories", key = "#keyword + '_' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort.toString()"
            , condition = "#pageable.pageNumber < 2"
            , unless = "#result.isEmpty()")
    public Page<CategoryResponse> getCategories(String keyword, Pageable pageable) {
        Page<Category> categories = categoryRepository
                .findByCategoryNameContainingIgnoreCase(keyword, pageable);

        return categories.map(categoryMapper::toCategoryResponse);
    }


}
