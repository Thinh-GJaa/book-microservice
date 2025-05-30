package com.book.bookservice.service;

import com.book.bookservice.dto.request.CreateProductRequest;
import com.book.bookservice.dto.request.UpdateProductRequest;
import com.book.bookservice.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface ProductService {

    ProductResponse getProductById(String productId);

    ProductResponse createProduct(CreateProductRequest productRequest);

    ProductResponse updateProduct(UpdateProductRequest productRequest);

    Page<ProductResponse> searchProducts(String keyword, Pageable pageable);

    Page<ProductResponse> getProductsByCategory(String categoryId, Pageable pageable);

    Page<ProductResponse> getProductsByAuthor(String authorId, Pageable pageable);

    Set<String> checkInvalidProductIds(Set<String> productIds);
}
