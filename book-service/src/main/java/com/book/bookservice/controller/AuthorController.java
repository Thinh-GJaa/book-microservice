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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/author")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "Author", description = "APIs for managing authors, including creation, update, retrieval, and search operations.")
public class AuthorController {

        AuthorService authorService;

        @Operation(summary = "Get author by ID", description = "Retrieve detailed information of an author by their unique ID. Requires authentication.")
        @GetMapping("/{authorId}")
        public ResponseEntity<ApiResponse<?>> getAuthorById(
                        @Parameter(description = "Unique identifier of the author", required = true) @PathVariable String authorId) {
                ApiResponse<AuthorResponse> apiResponse = ApiResponse.<AuthorResponse>builder()
                                .message("Get author successfully")
                                .data(authorService.getAuthorById(authorId))
                                .build();
                return ResponseEntity.ok(apiResponse);
        }

        @Operation(summary = "Create a new author", description = "Create a new author with the provided information. Requires authentication.")
        @PostMapping
        public ResponseEntity<ApiResponse<?>> createAuthor(
                        @Parameter(description = "Author creation request body", required = true) @Valid @RequestBody CreateAuthorRequest createAuthorRequest) {
                ApiResponse<AuthorResponse> apiResponse = ApiResponse.<AuthorResponse>builder()
                                .message("Create author successfully")
                                .data(authorService.createAuthor(createAuthorRequest))
                                .build();
                return ResponseEntity.ok(apiResponse);
        }

        @Operation(summary = "Update an existing author", description = "Update the details of an existing author. Requires authentication.")
        @PatchMapping
        public ResponseEntity<ApiResponse<?>> updateAuthor(
                        @Parameter(description = "Author update request body", required = true) @Valid @RequestBody UpdateAuthorRequest updateAuthorRequest) {
                ApiResponse<AuthorResponse> apiResponse = ApiResponse.<AuthorResponse>builder()
                                .message("Update author successfully")
                                .data(authorService.updateAuthor(updateAuthorRequest))
                                .build();
                return ResponseEntity.ok(apiResponse);
        }

        @Operation(summary = "Search authors", description = "Search for authors by keyword with pagination support. Requires authentication.")
        @GetMapping("/search")
        public ResponseEntity<ApiResponse<?>> searchAuthors(
                        @Parameter(description = "Keyword to search for authors", required = false) @RequestParam(required = false, defaultValue = "") String keyword,
                        @Parameter(description = "Pagination and sorting information", required = false) @PageableDefault(page = 0, size = 10, sort = "authorName", direction = Sort.Direction.ASC) Pageable pageable) {
                ApiResponse<Page<AuthorResponse>> apiResponse = ApiResponse.<Page<AuthorResponse>>builder()
                                .message("Get authors successfully")
                                .data(authorService.searchAuthors(keyword, pageable))
                                .build();
                return ResponseEntity.ok(apiResponse);
        }

}
