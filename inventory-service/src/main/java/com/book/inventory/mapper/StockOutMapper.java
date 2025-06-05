package com.book.inventory.mapper;

import com.book.inventory.dto.request.CreateStockOutRequest;
import com.book.inventory.dto.request.StockOutDetailRequest;
import com.book.inventory.dto.response.StockOutDetailResponse;
import com.book.inventory.dto.response.StockOutResponse;
import com.book.inventory.entity.StockOut;
import com.book.inventory.entity.StockOutDetail;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface StockOutMapper {

    @Mapping(target = "details", source = "details", qualifiedByName = "mapStockOutDetail")
    StockOutResponse toStockOutResponse(StockOut stockOut);


    @Named("mapStockOutDetail")
    public default List<StockOutDetailResponse> mapStockOutDetail(List<StockOutDetail> details) {

        return details.stream()
                .map(detail ->
                        StockOutDetailResponse.builder()
                                .productId(detail.getId().getProductId())
                                .quantity(detail.getQuantity())
                                .unitPrice(detail.getUnitPrice())
                                .build()
                ).toList();

    }
}
