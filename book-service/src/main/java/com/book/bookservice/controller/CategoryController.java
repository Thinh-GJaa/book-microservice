package com.book.bookservice.controller;

import com.book.bookservice.dto.ApiResponse;
import com.book.bookservice.dto.request.CreateCategoryRequest;
import com.book.bookservice.dto.request.DeleteCategoryRequest;
import com.book.bookservice.dto.request.UpdateCategoryRequest;
import com.book.bookservice.dto.response.CategoryResponse;
import com.book.bookservice.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryController {

    CategoryService categoryService;

    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<?>> getCategoryById(@PathVariable String categoryId) {
        ApiResponse<CategoryResponse> apiResponse = ApiResponse.<CategoryResponse>builder()
                .message("Get category successfully")
                .data(categoryService.getCategoryById(categoryId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createCategory(
            @Valid @RequestBody CreateCategoryRequest createCategoryRequest) {
        ApiResponse<CategoryResponse> apiResponse = ApiResponse.<CategoryResponse>builder()
                .message("Create category successfully")
                .data(categoryService.createCategory(createCategoryRequest))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<?>> updateCategory(
            @Valid @RequestBody UpdateCategoryRequest updateCategoryRequest) {
        ApiResponse<CategoryResponse> apiResponse = ApiResponse.<CategoryResponse>builder()
                .message("Update category successfully")
                .data(categoryService.updateCategory(updateCategoryRequest))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<?>> deleteCategory(
            @Valid @RequestBody DeleteCategoryRequest deleteCategoryRequest) {
        categoryService.deleteCategory(deleteCategoryRequest);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message("Delete category successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> getCategories(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @PageableDefault(page = 0, size = 10, sort = "categoryName", direction = Sort.Direction.ASC) Pageable pageable) {
        ApiResponse<?> apiResponse = ApiResponse.<Object>builder()
                .message("Get categories successfully")
                .data(categoryService.getCategories(keyword, pageable))
                .build();
        return ResponseEntity.ok(apiResponse);
    }


}
