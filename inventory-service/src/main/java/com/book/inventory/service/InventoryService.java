package com.book.inventory.service;


import com.book.inventory.dto.request.CheckInventoryRequest;

public interface InventoryService {

    String simulateCheckAndReserveSingleWarehouse(CheckInventoryRequest request);

    void releaseInventory(String orderId);

}
