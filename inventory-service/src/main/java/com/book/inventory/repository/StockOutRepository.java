package com.book.inventory.repository;

import com.book.inventory.entity.StockOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StockOutRepository extends JpaRepository<StockOut, String>, JpaSpecificationExecutor<StockOut> {
    // Các custom query methods sẽ được thêm khi cần
}
