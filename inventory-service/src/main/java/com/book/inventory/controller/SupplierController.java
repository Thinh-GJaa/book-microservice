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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/suppliers")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Supplier", description = "APIs for supplier management.")
public class SupplierController {

    SupplierService supplierService;

    @Operation(summary = "Create supplier", description = "Register a new supplier.")
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createSupplier(
            @Parameter(description = "Supplier creation request body", required = true)
            @Valid @RequestBody CreateSupplierRequest createSupplierRequest) {
        ApiResponse<SupplierResponse> apiResponse = ApiResponse.<SupplierResponse>builder()
                .message("Create supplier successfully")
                .data(supplierService.createSupplier(createSupplierRequest))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Get supplier by ID", description = "Get supplier details by supplierId.")
    @GetMapping("/{supplierId}")
    public ResponseEntity<ApiResponse<?>> getSupplierById(
            @Parameter(description = "Supplier ID", required = true) @PathVariable String supplierId) {
        ApiResponse<SupplierResponse> apiResponse = ApiResponse.<SupplierResponse>builder()
                .message("Get supplier successfully")
                .data(supplierService.getSupplierById(supplierId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Update supplier", description = "Update supplier information.")
    @PatchMapping
    public ResponseEntity<ApiResponse<?>> updateSupplier(
            @Parameter(description = "Supplier update request body", required = true)
            @Valid @RequestBody UpdateSupplierRequest updateSupplierRequest) {
        ApiResponse<SupplierResponse> apiResponse = ApiResponse.<SupplierResponse>builder()
                .message("Update supplier successfully")
                .data(supplierService.updateSupplier(updateSupplierRequest))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Search suppliers", description = "Search suppliers by keyword with pagination.")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> searchSupplier(
            @Parameter(description = "Keyword to search suppliers")
            @RequestParam(required = false, defaultValue = "") String keyword,
            @Parameter(hidden = true) @PageableDefault(page = 0, size = 10, sort = "supplierName", direction = Sort.Direction.ASC) Pageable pageable) {
        ApiResponse<Page<SupplierResponse>> apiResponse = ApiResponse.<Page<SupplierResponse>>builder()
                .message("Get suppliers successfully")
                .data(supplierService.searchSuppliers(keyword, pageable))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
