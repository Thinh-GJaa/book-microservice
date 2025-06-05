package com.book.inventory.service;

import com.book.inventory.dto.request.CreateStockOutRequest;
import com.book.inventory.dto.request.UpdateStockOutStatusRequest;
import com.book.inventory.dto.response.StockOutResponse;
import com.book.inventory.enums.StockOutStatus;
import com.book.inventory.enums.StockOutType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StockOutService {

    StockOutResponse createStockOut(CreateStockOutRequest request);

    StockOutResponse getStockOutById(String stockOutId);

    StockOutResponse updateStockOutStatus(UpdateStockOutStatusRequest req);

    Page<StockOutResponse> filterStockOuts(
            String warehouseId, StockOutType type, StockOutStatus status, Pageable pageable);
}
