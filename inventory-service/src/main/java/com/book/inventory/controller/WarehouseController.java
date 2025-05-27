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

@RestController
@RequestMapping("/warehouses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WarehouseController {

    WarehouseService warehouseService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createWarehouse(
            @Valid @RequestBody CreateWarehouseRequest createWarehouseRequest) {
        ApiResponse<WarehouseResponse> apiResponse = ApiResponse.<WarehouseResponse>builder()
                .message("Create warehouse successfully")
                .data(warehouseService.createWarehouse(createWarehouseRequest))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{warehouseId}")
    public ResponseEntity<ApiResponse<?>> getWarehouseById(@PathVariable String warehouseId) {
        ApiResponse<WarehouseResponse> apiResponse = ApiResponse.<WarehouseResponse>builder()
                .message("Get warehouse successfully")
                .data(warehouseService.getWarehouseById(warehouseId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<?>> updateWarehouse(
            @Valid @RequestBody UpdateWarehouseRequest updateWarehouseRequest) {
        ApiResponse<WarehouseResponse> apiResponse = ApiResponse.<WarehouseResponse>builder()
                .message("Update warehouse successfully")
                .data(warehouseService.updateWarehouse(updateWarehouseRequest))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> searchWarehouse(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @PageableDefault(page = 0, size = 10, sort = "warehouseName", direction = Sort.Direction.ASC) Pageable pageable) {
        ApiResponse<Page<WarehouseResponse>> apiResponse = ApiResponse.<Page<WarehouseResponse>>builder()
                .message("Get warehouses successfully")
                .data(warehouseService.searchWarehouses(keyword, pageable))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

}
