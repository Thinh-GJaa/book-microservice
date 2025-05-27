package com.book.inventory.service.impl;

import com.book.inventory.dto.request.CreateSupplierRequest;
import com.book.inventory.dto.request.UpdateSupplierRequest;
import com.book.inventory.dto.response.SupplierResponse;
import com.book.inventory.entity.Supplier;
import com.book.inventory.exception.CustomException;
import com.book.inventory.exception.ErrorCode;
import com.book.inventory.mapper.SupplierMapper;
import com.book.inventory.repository.SupplierRepository;
import com.book.inventory.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SupplierServiceImpl implements SupplierService {

    SupplierRepository supplierRepository;
    SupplierMapper supplierMapper;

    @Override
    @CacheEvict(value = {"suppliers"}, allEntries = true)
    public SupplierResponse createSupplier(CreateSupplierRequest request) {

        if (supplierRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.SUPPLIER_EMAIL_ALREADY_EXISTS, request.getEmail());
        }

        if (supplierRepository.existsByNameSupplier(request.getNameSupplier())) {
            throw new CustomException(ErrorCode.SUPPLIER_NAME_ALREADY_EXISTS, request.getNameSupplier());
        }

        if (supplierRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new CustomException(ErrorCode.SUPPLIER_PHONE_ALREADY_EXISTS, request.getPhoneNumber());
        }

        Supplier supplier = supplierMapper.toSupplier(request);
        supplier = supplierRepository.save(supplier);
        return supplierMapper.toSupplierResponse(supplier);
    }

    @Override
    @Cacheable(value = "supplier", key = "#supplierId")
    public SupplierResponse getSupplierById(String supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new CustomException(ErrorCode.SUPPLIER_NOT_FOUND, supplierId));
        return supplierMapper.toSupplierResponse(supplier);
    }

    @Override
    @Caching(put = @CachePut(value = "supplier", key = "#request.supplierId"),
            evict = @CacheEvict(value = {"suppliers",}, allEntries = true))
    public SupplierResponse updateSupplier(UpdateSupplierRequest request) {
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new CustomException(ErrorCode.SUPPLIER_NOT_FOUND, request.getSupplierId()));

        if (request.getEmail() != null
                && !request.getEmail().equals(supplier.getEmail())
                && supplierRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.SUPPLIER_EMAIL_ALREADY_EXISTS, request.getEmail());
        }

        // Kiểm tra trùng tên nếu thay đổi và khác supplier hiện tại
        if (request.getNameSupplier() != null
                && !request.getNameSupplier().equals(supplier.getNameSupplier())
                && supplierRepository.existsByNameSupplier(request.getNameSupplier())) {
            throw new CustomException(ErrorCode.SUPPLIER_NAME_ALREADY_EXISTS, request.getNameSupplier());
        }

        // Kiểm tra trùng số điện thoại nếu thay đổi và khác supplier hiện tại
        if (request.getPhoneNumber() != null
                && !request.getPhoneNumber().equals(supplier.getPhoneNumber())
                && supplierRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new CustomException(ErrorCode.SUPPLIER_PHONE_ALREADY_EXISTS, request.getPhoneNumber());
        }

        supplierMapper.updateSupplier(supplier, request);
        supplier = supplierRepository.save(supplier);
        return supplierMapper.toSupplierResponse(supplier);
    }

    @Override
    @Cacheable(value = "products", key = "#keyword + '_' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort.toString()", condition = "#pageable.pageNumber < 2", unless = "#result.isEmpty()")
    public Page<SupplierResponse> searchSuppliers(String keyword, Pageable pageable) {
        Page<Supplier> suppliers = supplierRepository
                .findByNameSupplierContainingIgnoreCaseOrContactNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        keyword, keyword, keyword, pageable);
        return suppliers.map(supplierMapper::toSupplierResponse);
    }
}
