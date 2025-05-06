package com.book.identityservice.repository.httpclient;


import com.book.identityservice.dto.ApiResponse;
import com.book.identityservice.dto.request.ProfileCreationRequest;
import com.book.identityservice.dto.response.CreatedProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@FeignClient(name = "profile-service")
public interface ProfileClient {

    @PostMapping(value = "/profile/internal/users", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<CreatedProfileResponse> createProfile(@RequestBody ProfileCreationRequest request);
}
