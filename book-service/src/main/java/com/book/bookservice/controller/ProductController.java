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

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductController {

    ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<?>> getProductById(@PathVariable String productId) {
        ApiResponse<ProductResponse> apiResponse = ApiResponse.<ProductResponse>builder()
                .message("Get product successfully")
                .data(productService.getProductById(productId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createProduct(
            @Valid @RequestBody CreateProductRequest createProductRequest) {
        ApiResponse<ProductResponse> apiResponse = ApiResponse.<ProductResponse>builder()
                .message("Create product successfully")
                .data(productService.createProduct(createProductRequest))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<?>> updateProduct(
            @Valid @RequestBody UpdateProductRequest updateProductRequest) {
        ApiResponse<ProductResponse> apiResponse = ApiResponse.<ProductResponse>builder()
                .message("Update product successfully")
                .data(productService.updateProduct(updateProductRequest))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<ApiResponse<?>> searchProducts(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @PageableDefault(page = 0, size = 10, sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
        ApiResponse<Page<ProductResponse>> apiResponse = ApiResponse.<Page<ProductResponse>>builder()
                .message("Get products successfully")
                .data(productService.searchProducts(keyword, pageable))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<ApiResponse<?>> getProductsByCategory(
            @PathVariable String categoryId,
            @PageableDefault(page = 0, size = 10, sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
        ApiResponse<Page<ProductResponse>> apiResponse = ApiResponse.<Page<ProductResponse>>builder()
                .message("Get products by category successfully")
                .data(productService.getProductsByCategory(categoryId, pageable))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/by-author/{authorId}")
    public ResponseEntity<ApiResponse<?>> getProductsByAuthor(
            @PathVariable String authorId,
            @PageableDefault(page = 0, size = 10, sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
        ApiResponse<Page<ProductResponse>> apiResponse = ApiResponse.<Page<ProductResponse>>builder()
                .message("Get products by author successfully")
                .data(productService.getProductsByAuthor(authorId, pageable))
                .build();
        return ResponseEntity.ok(apiResponse);
    }


}
