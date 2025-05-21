package com.book.bookservice.mapper;

import com.book.bookservice.dto.request.CreateAuthorRequest;
import com.book.bookservice.dto.request.CreateCategoryRequest;
import com.book.bookservice.dto.request.UpdateAuthorRequest;
import com.book.bookservice.dto.request.UpdateCategoryRequest;
import com.book.bookservice.dto.response.AuthorResponse;
import com.book.bookservice.dto.response.CategoryResponse;
import com.book.bookservice.entity.Author;
import com.book.bookservice.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    Author toAuthor(CreateAuthorRequest createAuthorRequest);

    Author updateAuthor(@MappingTarget Author author, UpdateAuthorRequest request);

    AuthorResponse toAuthorResponse(Author author);

}
