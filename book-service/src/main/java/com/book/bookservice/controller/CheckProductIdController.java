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

import java.util.Set;

@RestController(value = "check-product-id-controller")
@RequestMapping
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CheckProductIdController {

    ProductService productService;

    @PostMapping("/invalidate-ids")
    public ResponseEntity<?> checkProductIds(@RequestBody Set<String> productIds) {

        Set<String> InvalidProductIds = productService.checkInvalidProductIds(productIds);
        
        return ResponseEntity.ok(InvalidProductIds);
    }


    @GetMapping("/ids")
    public ResponseEntity<?> getProductsByIds(@RequestBody Set<String> productIds) {
        log.info("Fetching products for IDs: {}", productIds);

        Set<ProductTitleResponse> products = productService.getProductsByIds(productIds);

        return ResponseEntity.ok(products);
    }
}
