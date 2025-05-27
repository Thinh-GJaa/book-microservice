package com.book.inventory.service;

import com.book.inventory.dto.request.CreateSupplierRequest;
import com.book.inventory.dto.request.UpdateSupplierRequest;
import com.book.inventory.dto.response.SupplierResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SupplierService {
    SupplierResponse createSupplier(CreateSupplierRequest request);

    SupplierResponse getSupplierById(String supplierId);

    SupplierResponse updateSupplier(UpdateSupplierRequest request);

    Page<SupplierResponse> searchSuppliers(String keyword, Pageable pageable);
}
