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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "Category", description = "APIs for managing book categories, including creation, update, deletion, retrieval, and search operations.")
public class CategoryController {

        CategoryService categoryService;

        @Operation(summary = "Get category by ID", description = "Retrieve detailed information of a category by its unique ID. Requires authentication.")
        @GetMapping("/{categoryId}")
        public ResponseEntity<ApiResponse<?>> getCategoryById(
                        @Parameter(description = "Unique identifier of the category", required = true) @PathVariable String categoryId) {
                ApiResponse<CategoryResponse> apiResponse = ApiResponse.<CategoryResponse>builder()
                                .message("Get category successfully")
                                .data(categoryService.getCategoryById(categoryId))
                                .build();
                return ResponseEntity.ok(apiResponse);
        }

        @Operation(summary = "Create a new category", description = "Create a new book category with the provided information. Requires authentication.")
        @PostMapping
        public ResponseEntity<ApiResponse<?>> createCategory(
                        @Parameter(description = "Category creation request body", required = true) @Valid @RequestBody CreateCategoryRequest createCategoryRequest) {
                ApiResponse<CategoryResponse> apiResponse = ApiResponse.<CategoryResponse>builder()
                                .message("Create category successfully")
                                .data(categoryService.createCategory(createCategoryRequest))
                                .build();
                return ResponseEntity.ok(apiResponse);
        }

        @Operation(summary = "Update an existing category", description = "Update the details of an existing book category. Requires authentication.")
        @PutMapping
        public ResponseEntity<ApiResponse<?>> updateCategory(
                        @Parameter(description = "Category update request body", required = true) @Valid @RequestBody UpdateCategoryRequest updateCategoryRequest) {
                ApiResponse<CategoryResponse> apiResponse = ApiResponse.<CategoryResponse>builder()
                                .message("Update category successfully")
                                .data(categoryService.updateCategory(updateCategoryRequest))
                                .build();
                return ResponseEntity.ok(apiResponse);
        }

        @Operation(summary = "Delete a category", description = "Delete a book category by its unique ID. Requires authentication.")
        @DeleteMapping
        public ResponseEntity<ApiResponse<?>> deleteCategory(
                        @Parameter(description = "Category deletion request body", required = true) @Valid @RequestBody DeleteCategoryRequest deleteCategoryRequest) {
                categoryService.deleteCategory(deleteCategoryRequest);
                ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                                .message("Delete category successfully")
                                .build();
                return ResponseEntity.ok(apiResponse);
        }

        @Operation(summary = "Search categories", description = "Search for book categories by keyword with pagination support. Requires authentication.")
        @GetMapping("/search")
        public ResponseEntity<ApiResponse<?>> getCategories(
                        @Parameter(description = "Keyword to search for categories", required = false) @RequestParam(required = false, defaultValue = "") String keyword,
                        @Parameter(description = "Pagination and sorting information", required = false) @PageableDefault(page = 0, size = 10, sort = "categoryName", direction = Sort.Direction.ASC) Pageable pageable) {
                ApiResponse<?> apiResponse = ApiResponse.<Object>builder()
                                .message("Get categories successfully")
                                .data(categoryService.getCategories(keyword, pageable))
                                .build();
                return ResponseEntity.ok(apiResponse);
        }

}
