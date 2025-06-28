package com.book.bookservice.controller;

import com.book.bookservice.dto.ApiResponse;
import com.book.bookservice.dto.request.CreateProductRequest;
import com.book.bookservice.dto.request.UpdateProductRequest;
import com.book.bookservice.dto.response.ProductResponse;
import com.book.bookservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "Product", description = "APIs for managing products (books), including creation, update, retrieval, and search operations.")
public class ProductController {

        ProductService productService;

        @Operation(summary = "Get product by ID", description = "Retrieve detailed information of a product by its unique ID. Requires authentication.")
        @GetMapping("/{productId}")
        public ResponseEntity<ApiResponse<?>> getProductById(
                        @Parameter(description = "Unique identifier of the product", required = true) @PathVariable String productId) {
                ApiResponse<ProductResponse> apiResponse = ApiResponse.<ProductResponse>builder()
                                .message("Get product successfully")
                                .data(productService.getProductById(productId))
                                .build();
                return ResponseEntity.ok(apiResponse);
        }

        @Operation(summary = "Create a new product", description = "Create a new product (book) with the provided information. Requires authentication.")
        @PostMapping
        public ResponseEntity<ApiResponse<?>> createProduct(
                        @Parameter(description = "Product creation request body", required = true) @Valid @RequestBody CreateProductRequest createProductRequest) {
                ApiResponse<ProductResponse> apiResponse = ApiResponse.<ProductResponse>builder()
                                .message("Create product successfully")
                                .data(productService.createProduct(createProductRequest))
                                .build();
                return ResponseEntity.ok(apiResponse);
        }

        @Operation(summary = "Update an existing product", description = "Update the details of an existing product (book). Requires authentication.")
        @PatchMapping
        public ResponseEntity<ApiResponse<?>> updateProduct(
                        @Parameter(description = "Product update request body", required = true) @Valid @RequestBody UpdateProductRequest updateProductRequest) {
                ApiResponse<ProductResponse> apiResponse = ApiResponse.<ProductResponse>builder()
                                .message("Update product successfully")
                                .data(productService.updateProduct(updateProductRequest))
                                .build();
                return ResponseEntity.ok(apiResponse);
        }

        @Operation(summary = "Search products", description = "Search for products (books) by keyword with pagination support. Requires authentication.")
        @GetMapping(value = "/search")
        public ResponseEntity<ApiResponse<?>> searchProducts(
                        @Parameter(description = "Keyword to search for products", required = false) @RequestParam(required = false, defaultValue = "") String keyword,
                        @Parameter(description = "Pagination and sorting information", required = false) @PageableDefault(page = 0, size = 10, sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
                ApiResponse<Page<ProductResponse>> apiResponse = ApiResponse.<Page<ProductResponse>>builder()
                                .message("Get products successfully")
                                .data(productService.searchProducts(keyword, pageable))
                                .build();
                return ResponseEntity.ok(apiResponse);
        }

        @Operation(summary = "Get products by category", description = "Retrieve products (books) by category ID with pagination support. Requires authentication.")
        @GetMapping("/by-category/{categoryId}")
        public ResponseEntity<ApiResponse<?>> getProductsByCategory(
                        @Parameter(description = "Unique identifier of the category", required = true) @PathVariable String categoryId,
                        @Parameter(description = "Pagination and sorting information", required = false) @PageableDefault(page = 0, size = 10, sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
                ApiResponse<Page<ProductResponse>> apiResponse = ApiResponse.<Page<ProductResponse>>builder()
                                .message("Get products by category successfully")
                                .data(productService.getProductsByCategory(categoryId, pageable))
                                .build();
                return ResponseEntity.ok(apiResponse);
        }

        @Operation(summary = "Get products by author", description = "Retrieve products (books) by author ID with pagination support. Requires authentication.")
        @GetMapping("/by-author/{authorId}")
        public ResponseEntity<ApiResponse<?>> getProductsByAuthor(
                        @Parameter(description = "Unique identifier of the author", required = true) @PathVariable String authorId,
                        @Parameter(description = "Pagination and sorting information", required = false) @PageableDefault(page = 0, size = 10, sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
                ApiResponse<Page<ProductResponse>> apiResponse = ApiResponse.<Page<ProductResponse>>builder()
                                .message("Get products by author successfully")
                                .data(productService.getProductsByAuthor(authorId, pageable))
                                .build();
                return ResponseEntity.ok(apiResponse);
        }

}
