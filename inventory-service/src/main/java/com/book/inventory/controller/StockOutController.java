package com.book.inventory.controller;

import com.book.inventory.dto.ApiResponse;
import com.book.inventory.dto.request.CreateStockOutRequest;
import com.book.inventory.dto.request.UpdateStockOutStatusRequest;
import com.book.inventory.dto.response.StockOutResponse;
import com.book.inventory.enums.StockOutStatus;
import com.book.inventory.enums.StockOutType;
import com.book.inventory.service.StockOutService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock-outs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StockOutController {

    StockOutService stockOutService;

    @PostMapping
    public ResponseEntity<ApiResponse<StockOutResponse>> createStockOut(
            @Valid @RequestBody CreateStockOutRequest request) {
        ApiResponse<StockOutResponse> response = ApiResponse.<StockOutResponse>builder()
                .data(stockOutService.createStockOut(request))
                .message("Stock out created successfully")
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{stockOutId}")
    public ResponseEntity<ApiResponse<StockOutResponse>> getStockOutById(
            @PathVariable String stockOutId) {
        ApiResponse<StockOutResponse> response = ApiResponse.<StockOutResponse>builder()
                .data(stockOutService.getStockOutById(stockOutId))
                .message("Get stock out successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/status")
    public ResponseEntity<ApiResponse<Void>> updateStockOutStatus(
            @Valid @RequestBody UpdateStockOutStatusRequest request) {
        stockOutService.updateStockOutStatus(request);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Update stock out status successfully")
                .build();
        return ResponseEntity.ok(response);
    }



    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<Page<StockOutResponse>>> searchStockOuts(
            @RequestParam(required = false) StockOutStatus status,
            @RequestParam(required = false) StockOutType type,
            @RequestParam(required = false) String warehouseId,
            @PageableDefault(page = 0, size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        ApiResponse<Page<StockOutResponse>> response = ApiResponse.<Page<StockOutResponse>>builder()
                .data(stockOutService.filterStockOuts(warehouseId, type, status, pageable))
                .message("Filter stock outs successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}
