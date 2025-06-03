package com.book.bookservice.mapper;

import com.book.bookservice.dto.request.CreateProductRequest;
import com.book.bookservice.dto.request.UpdateProductRequest;
import com.book.bookservice.dto.response.ProductResponse;
import com.book.bookservice.dto.response.ProductTitleResponse;
import com.book.bookservice.entity.Product;

import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = ProductMapperHelper.class)
public interface ProductMapper {

    @Mapping(target = "authors", source = "authorIds", qualifiedByName = "authorIdsToAuthors")
    @Mapping(target = "category", source = "categoryId", qualifiedByName = "categoryIdToCategory")
    public Product toProduct(CreateProductRequest dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "authors", source = "authorIds", qualifiedByName = "authorIdsToAuthors")
    @Mapping(target = "category", source = "categoryId", qualifiedByName = "categoryIdToCategory")
    public void updateProduct(UpdateProductRequest dto, @MappingTarget Product product);

    @Mapping(target = "authors", source = "authors", qualifiedByName = "mapAuthors")
    @Mapping(target = "category", source = "category", qualifiedByName = "mapCategory")
    public ProductResponse toProductResponse(Product product);

    ProductTitleResponse toProductTitleResponse(Product product);


}
