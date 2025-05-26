package com.book.bookservice.service.impl;

import com.book.bookservice.dto.request.CreateProductRequest;
import com.book.bookservice.dto.request.UpdateProductRequest;
import com.book.bookservice.dto.response.ProductResponse;
import com.book.bookservice.entity.Product;
import com.book.bookservice.exception.CustomException;
import com.book.bookservice.exception.ErrorCode;
import com.book.bookservice.mapper.ProductMapper;
import com.book.bookservice.repository.ProductRepository;
import com.book.bookservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;

    ProductMapper productMapper;

    @Override
    @Cacheable(value = "product", key = "#productId")
    public ProductResponse getProductById(String productId) {
        return productRepository.findById(productId)
                .map(productMapper::toProductResponse)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND, productId));
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CacheEvict(value = {
            "products",
            "products_by_category",
            "products_by_author"
    }, allEntries = true)
    public ProductResponse createProduct(CreateProductRequest productRequest) {
        if (productRepository.existsByIsbn(productRequest.getIsbn())) {
            throw new CustomException(ErrorCode.PRODUCT_ISBN_EXISTED, productRequest.getIsbn());
        }

        // Đã check id exist trong mapper
        return productMapper.toProductResponse(
                productRepository.save(productMapper.toProduct(productRequest)));
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Caching(put = @CachePut(value = "product", key = "#productRequest.productId"), evict = @CacheEvict(value = {
            "products",
            "products_by_category",
            "products_by_author"
    }, allEntries = true))
    public ProductResponse updateProduct(UpdateProductRequest productRequest) {

        Product product = productRepository.findById(productRequest.getProductId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND, productRequest.getProductId()));

        if (productRepository.existsByIsbn(productRequest.getIsbn())
                && !product.getIsbn().equals(productRequest.getIsbn())) {
            throw new CustomException(ErrorCode.PRODUCT_ISBN_EXISTED, productRequest.getIsbn());
        }

        productMapper.updateProduct(productRequest, product); // Đã check id exist trong mapper

        return productMapper.toProductResponse(productRepository.save(product));
    }

    @Override
    @Cacheable(value = "products", key = "#keyword + '_' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort.toString()", condition = "#pageable.pageNumber < 2", unless = "#result.isEmpty()")
    public Page<ProductResponse> searchProducts(String keyword, Pageable pageable) {
        Page<Product> products = productRepository.findByTitleContainingIgnoreCase(keyword, pageable);

        return products.map(productMapper::toProductResponse);
    }

    @Override
    @Cacheable(value = "products_by_category", key = "#categoryId + '_' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort.toString()", condition = "#pageable.pageNumber < 2", unless = "#result.isEmpty()")
    public Page<ProductResponse> getProductsByCategory(String categoryId, Pageable pageable) {
        Page<Product> products = productRepository.findAllByCategory_CategoryId(categoryId, pageable);

        return products.map(productMapper::toProductResponse);
    }

    @Override
    @Cacheable(value = "products_by_author", key = "#authorId + '_' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort.toString()", condition = "#pageable.pageNumber < 2", unless = "#result.isEmpty()")
    public Page<ProductResponse> getProductsByAuthor(String authorId, Pageable pageable) {
        Page<Product> products = productRepository.findAllByAuthors_AuthorId(authorId, pageable);

        return products.map(productMapper::toProductResponse);
    }
}
