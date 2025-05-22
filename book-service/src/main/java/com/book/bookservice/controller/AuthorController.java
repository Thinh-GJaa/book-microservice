package com.book.bookservice.controller;

import com.book.bookservice.dto.ApiResponse;

import com.book.bookservice.dto.request.CreateAuthorRequest;
import com.book.bookservice.dto.request.UpdateAuthorRequest;
import com.book.bookservice.dto.response.AuthorResponse;
import com.book.bookservice.service.AuthorService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/author")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthorController {

    AuthorService authorService;

    @GetMapping("/{authorId}")
    public ResponseEntity<ApiResponse<?>> getAuthorById(@PathVariable String authorId) {
        ApiResponse<AuthorResponse> apiResponse = ApiResponse.<AuthorResponse>builder()
                .message("Get author successfully")
                .data(authorService.getAuthorById(authorId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createAuthor(
            @Valid @RequestBody CreateAuthorRequest createAuthorRequest) {
        ApiResponse<AuthorResponse> apiResponse = ApiResponse.<AuthorResponse>builder()
                .message("Create author successfully")
                .data(authorService.createAuthor(createAuthorRequest))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<?>> updateAuthor(
            @Valid @RequestBody UpdateAuthorRequest updateAuthorRequest) {
        ApiResponse<AuthorResponse> apiResponse = ApiResponse.<AuthorResponse>builder()
                .message("Update author successfully")
                .data(authorService.updateAuthor(updateAuthorRequest))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> searchAuthors(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @PageableDefault(page = 0, size = 10, sort = "authorName", direction = Sort.Direction.ASC) Pageable pageable) {
        ApiResponse<Page<AuthorResponse>> apiResponse = ApiResponse.<Page<AuthorResponse>>builder()
                .message("Get authors successfully")
                .data(authorService.searchAuthors(keyword, pageable))
                .build();
        return ResponseEntity.ok(apiResponse);
    }


}
