package com.book.inventory.service.client;

import com.book.inventory.dto.response.OrderResponse;
import com.book.inventory.dto.response.ProductTitleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

@FeignClient(name = "order-service")
public interface OrderClient {

    @GetMapping(value = "/order/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    OrderResponse getOrderById(@PathVariable("orderId") String orderId);

}
