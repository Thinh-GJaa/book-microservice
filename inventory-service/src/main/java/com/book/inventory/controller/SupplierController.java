package com.book.inventory.controller;

import com.book.inventory.dto.ApiResponse;
import com.book.inventory.dto.request.CreateSupplierRequest;
import com.book.inventory.dto.request.UpdateSupplierRequest;
import com.book.inventory.dto.response.SupplierResponse;
import com.book.inventory.service.SupplierService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/suppliers")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SupplierController {

    SupplierService supplierService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createSupplier(
            @Valid @RequestBody CreateSupplierRequest createSupplierRequest) {
        ApiResponse<SupplierResponse> apiResponse = ApiResponse.<SupplierResponse>builder()
                .message("Create supplier successfully")
                .data(supplierService.createSupplier(createSupplierRequest))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{supplierId}")
    public ResponseEntity<ApiResponse<?>> getSupplierById(@PathVariable String supplierId) {
        ApiResponse<SupplierResponse> apiResponse = ApiResponse.<SupplierResponse>builder()
                .message("Get supplier successfully")
                .data(supplierService.getSupplierById(supplierId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<?>> updateSupplier(
            @Valid @RequestBody UpdateSupplierRequest updateSupplierRequest) {
        ApiResponse<SupplierResponse> apiResponse = ApiResponse.<SupplierResponse>builder()
                .message("Update supplier successfully")
                .data(supplierService.updateSupplier(updateSupplierRequest))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> searchSupplier(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @PageableDefault(page = 0, size = 10, sort = "supplierName", direction = Sort.Direction.ASC) Pageable pageable) {
        ApiResponse<Page<SupplierResponse>> apiResponse = ApiResponse.<Page<SupplierResponse>>builder()
                .message("Get suppliers successfully")
                .data(supplierService.searchSuppliers(keyword, pageable))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
