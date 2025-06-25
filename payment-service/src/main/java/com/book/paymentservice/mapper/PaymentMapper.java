package com.book.paymentservice.mapper;

import com.book.paymentservice.dto.event.OrderEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.book.paymentservice.entity.Payment;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PaymentMapper {


    @Mapping(target = "status", constant = "PENDING")
    Payment toPayment(OrderEvent orderEvent);

}