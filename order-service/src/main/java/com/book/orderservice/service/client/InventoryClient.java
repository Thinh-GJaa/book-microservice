package com.book.orderservice.service.client;

import com.book.orderservice.dto.request.CheckInventoryRequest;
import com.book.orderservice.dto.response.ProductTitleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @PostMapping(value = "/inventory/check-inventory", produces = MediaType.APPLICATION_JSON_VALUE)
    String checkAndReserveInventory(@RequestBody CheckInventoryRequest request);

}
