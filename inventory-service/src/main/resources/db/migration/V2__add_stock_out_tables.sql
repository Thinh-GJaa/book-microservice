-- V2__add_stock_out_tables.sql: Thêm bảng stock_outs và stock_out_details

CREATE TABLE stock_outs (
    stock_out_id VARCHAR(36) PRIMARY KEY,
    warehouse_id VARCHAR(36) NOT NULL,
    note VARCHAR(1000),
    total_amount DECIMAL(19,2) NOT NULL,
    type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_date DATETIME NOT NULL,
    updated_date DATETIME NOT NULL,
    version BIGINT,
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(warehouse_id),
    INDEX idx_warehouse_id (warehouse_id),
    INDEX idx_status (status),
    INDEX idx_type (type)
);

CREATE TABLE stock_out_details (
    stock_out_id VARCHAR(36) NOT NULL,
    product_id VARCHAR(36) NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(19,2),
    version BIGINT,
    PRIMARY KEY (stock_out_id, product_id),
    FOREIGN KEY (stock_out_id) REFERENCES stock_outs(stock_out_id) ON DELETE CASCADE,
    INDEX idx_product_id (product_id)
);
