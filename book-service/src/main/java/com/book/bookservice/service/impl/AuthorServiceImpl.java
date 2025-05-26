package com.book.bookservice.service.impl;

import com.book.bookservice.dto.request.CreateAuthorRequest;
import com.book.bookservice.dto.request.UpdateAuthorRequest;
import com.book.bookservice.dto.response.AuthorResponse;
import com.book.bookservice.entity.Author;
import com.book.bookservice.exception.CustomException;
import com.book.bookservice.exception.ErrorCode;
import com.book.bookservice.mapper.AuthorMapper;
import com.book.bookservice.repository.AuthorRepository;
import com.book.bookservice.service.AuthorService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthorServiceImpl implements AuthorService {

    AuthorRepository authorRepository;
    AuthorMapper authorMapper;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public AuthorResponse createAuthor(CreateAuthorRequest request) {

        Author author = authorMapper.toAuthor(request);

        author = authorRepository.save(author);

        return authorMapper.toAuthorResponse(author);
    }

    @Override
    @Cacheable(value = "author", key = "#authorId")
    public AuthorResponse getAuthorById(String authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new CustomException(ErrorCode.AUTHOR_NOT_FOUND, authorId));

        return authorMapper.toAuthorResponse(author);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CachePut(value = "author", key = "#request.authorId")
    public AuthorResponse updateAuthor(UpdateAuthorRequest request) {
        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new CustomException(ErrorCode.AUTHOR_NOT_FOUND, request.getAuthorId()));

        authorMapper.updateAuthor(author, request);
        author = authorRepository.save(author);

        return authorMapper.toAuthorResponse(author);
    }

    @Override
    @Cacheable(value = "authors", key = "#keyword + '_' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort.toString()"
            , condition = "#pageable.pageNumber < 2"
            , unless = "#result.isEmpty()")
    public Page<AuthorResponse> searchAuthors(String keyword, Pageable pageable) {
        Page<Author> authors = authorRepository
                .findByAuthorNameContainingIgnoreCaseOrNationalityContainingIgnoreCaseOrDescriptionContainingIgnoreCase
                        (keyword, keyword, keyword, pageable);

        return authors.map(authorMapper::toAuthorResponse);
    }
}
