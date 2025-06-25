package com.book.notificationservice.service.client;

import com.book.notificationservice.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profile-service")
public interface ProfileClient {

    @GetMapping(value = "/profile/internal/users/email/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getEmailByUserId(@PathVariable String userId);

}
