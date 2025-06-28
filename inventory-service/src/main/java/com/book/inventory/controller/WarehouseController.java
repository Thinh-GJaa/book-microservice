package com.book.inventory.controller;

import com.book.inventory.dto.ApiResponse;
import com.book.inventory.dto.request.CreateWarehouseRequest;
import com.book.inventory.dto.request.UpdateWarehouseRequest;
import com.book.inventory.dto.response.WarehouseResponse;
import com.book.inventory.service.WarehouseService;
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
@RequestMapping("/warehouses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Warehouse", description = "APIs for warehouse management.")
public class WarehouseController {

    WarehouseService warehouseService;

    @Operation(summary = "Create warehouse", description = "Register a new warehouse.")
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createWarehouse(
            @Parameter(description = "Warehouse creation request body", required = true)
            @Valid @RequestBody CreateWarehouseRequest createWarehouseRequest) {
        ApiResponse<WarehouseResponse> apiResponse = ApiResponse.<WarehouseResponse>builder()
                .message("Create warehouse successfully")
                .data(warehouseService.createWarehouse(createWarehouseRequest))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Get warehouse by ID", description = "Get warehouse details by warehouseId.")
    @GetMapping("/{warehouseId}")
    public ResponseEntity<ApiResponse<?>> getWarehouseById(
            @Parameter(description = "Warehouse ID", required = true) @PathVariable String warehouseId) {
        ApiResponse<WarehouseResponse> apiResponse = ApiResponse.<WarehouseResponse>builder()
                .message("Get warehouse successfully")
                .data(warehouseService.getWarehouseById(warehouseId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Update warehouse", description = "Update warehouse information.")
    @PatchMapping
    public ResponseEntity<ApiResponse<?>> updateWarehouse(
            @Parameter(description = "Warehouse update request body", required = true)
            @Valid @RequestBody UpdateWarehouseRequest updateWarehouseRequest) {
        ApiResponse<WarehouseResponse> apiResponse = ApiResponse.<WarehouseResponse>builder()
                .message("Update warehouse successfully")
                .data(warehouseService.updateWarehouse(updateWarehouseRequest))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Search warehouses", description = "Search warehouses by keyword with pagination.")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> searchWarehouse(
            @Parameter(description = "Keyword to search warehouses")
            @RequestParam(required = false, defaultValue = "") String keyword,
            @Parameter(hidden = true) @PageableDefault(page = 0, size = 10, sort = "warehouseName", direction = Sort.Direction.ASC) Pageable pageable) {
        ApiResponse<Page<WarehouseResponse>> apiResponse = ApiResponse.<Page<WarehouseResponse>>builder()
                .message("Get warehouses successfully")
                .data(warehouseService.searchWarehouses(keyword, pageable))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

}
