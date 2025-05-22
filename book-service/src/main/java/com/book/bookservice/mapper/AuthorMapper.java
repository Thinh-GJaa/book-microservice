package com.book.bookservice.mapper;

import com.book.bookservice.dto.request.CreateAuthorRequest;
import com.book.bookservice.dto.request.UpdateAuthorRequest;
import com.book.bookservice.dto.response.AuthorResponse;
import com.book.bookservice.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AuthorMapper {
    Author toAuthor(CreateAuthorRequest request);

    void updateAuthor(@MappingTarget Author author, UpdateAuthorRequest request);

    AuthorResponse toAuthorResponse(Author Author);

}
