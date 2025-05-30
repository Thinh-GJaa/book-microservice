package com.book.inventory.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

@FeignClient(name = "book-service")
public interface BookClient {

    @PostMapping(value = "/books/invalidate-ids", produces = MediaType.APPLICATION_JSON_VALUE)
    Set<String> checkInvalidProductIds(@RequestBody Set<String> productIds);
}
