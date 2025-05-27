package com.book.inventory.repository;

import com.book.inventory.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, String> {

    boolean existsByEmail(String email);

    boolean existsByNameSupplier(String nameSupplier);

    boolean existsByPhoneNumber(String phoneNumber);

    Page<Supplier> findByNameSupplierContainingIgnoreCaseOrContactNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String nameSupplier, String contactName, String email, Pageable pageable);
}
