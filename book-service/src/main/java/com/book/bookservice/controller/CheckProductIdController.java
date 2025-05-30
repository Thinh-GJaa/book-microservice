package com.book.bookservice.controller;


import com.book.bookservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
