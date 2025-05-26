package com.book.bookservice.service;

import com.book.bookservice.dto.request.CreateAuthorRequest;
import com.book.bookservice.dto.request.UpdateAuthorRequest;
import com.book.bookservice.dto.response.AuthorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorService {
    AuthorResponse createAuthor(CreateAuthorRequest request);

    AuthorResponse getAuthorById(String authorId);

    AuthorResponse updateAuthor(UpdateAuthorRequest request);

    Page<AuthorResponse> searchAuthors(String keyword, Pageable pageable);
}
