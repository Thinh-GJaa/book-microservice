package com.book.bookservice.controller;

import com.book.bookservice.dto.response.ProductTitleResponse;
import com.book.bookservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.Set;

@RestController(value = "check-product-id-controller")
@RequestMapping
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "Product ID Validation", description = "APIs for validating and retrieving products by their IDs.")
public class CheckProductIdController {

    ProductService productService;

    @Operation(summary = "Check invalid product IDs", description = "Validate a set of product IDs and return the invalid ones. Requires authentication.")
    @PostMapping("/invalidate-ids")
    public ResponseEntity<?> checkProductIds(
            @Parameter(description = "Set of product IDs to validate", required = true) @RequestBody Set<String> productIds) {

        Set<String> invalidProductIds = productService.checkInvalidProductIds(productIds);

        return ResponseEntity.ok(invalidProductIds);
    }

    @Operation(summary = "Get products by IDs", description = "Retrieve product details by a set of product IDs. Requires authentication.")
    @PostMapping("/ids")
    public ResponseEntity<?> getProductsByIds(
            @Parameter(description = "Set of product IDs to retrieve", required = true) @RequestBody Set<String> productIds) {

        Set<ProductTitleResponse> products = productService.getProductsByIds(productIds);

        return ResponseEntity.ok(products);
    }
}
