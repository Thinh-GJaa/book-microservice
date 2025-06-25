package com.book.inventory.controller;

import com.book.inventory.dto.request.CheckInventoryRequest;
import com.book.inventory.service.InventoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/check-inventory")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CheckInventoryController {

    InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<?> checkInventory(@Valid @RequestBody CheckInventoryRequest request) {
        String warehouseId = inventoryService.simulateCheckAndReserveSingleWarehouse(request);
        return ResponseEntity.ok(warehouseId);
    }

}
