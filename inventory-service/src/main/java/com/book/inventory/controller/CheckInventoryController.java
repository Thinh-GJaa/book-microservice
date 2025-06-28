package com.book.inventory.controller;

import com.book.inventory.dto.request.CheckInventoryRequest;
import com.book.inventory.service.InventoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/check-inventory")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Inventory", description = "APIs for inventory checking.")
public class CheckInventoryController {

    InventoryService inventoryService;

    @Operation(summary = "Check inventory", description = "Check and reserve inventory for a request.")
    @PostMapping
    public ResponseEntity<?> checkInventory(
            @Parameter(description = "Inventory check request body", required = true)
            @Valid @RequestBody CheckInventoryRequest request) {
        String warehouseId = inventoryService.simulateCheckAndReserveSingleWarehouse(request);
        return ResponseEntity.ok(warehouseId);
    }

}
