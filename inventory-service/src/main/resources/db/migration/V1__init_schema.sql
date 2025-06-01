-- V1__init_schema.sql: Tạo schema cho inventory-service

CREATE TABLE warehouses (
    warehouse_id VARCHAR(36) PRIMARY KEY,
    warehouse_name VARCHAR(100) NOT NULL,
    address VARCHAR(500) NOT NULL,
    description VARCHAR(1000),
    active BOOLEAN NOT NULL,
    created_date DATETIME NOT NULL,
    updated_date DATETIME NOT NULL,
    version BIGINT,
    INDEX idx_warehouse_name (warehouse_name)
);

CREATE TABLE suppliers (
    supplier_id VARCHAR(36) PRIMARY KEY,
    supplier_name VARCHAR(100) NOT NULL UNIQUE,
    contact_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    address VARCHAR(500) NOT NULL,
    description VARCHAR(1000),
    created_date DATETIME NOT NULL,
    updated_date DATETIME NOT NULL,
    version BIGINT,
    INDEX idx_supplier_name (supplier_name)
);

CREATE TABLE purchase_orders (
    purchase_order_id VARCHAR(36) PRIMARY KEY,
    order_code VARCHAR(50) NOT NULL UNIQUE,
    supplier_id VARCHAR(36) NOT NULL,
    warehouse_id VARCHAR(36) NOT NULL,
    order_date DATETIME NOT NULL,
    total_amount DECIMAL(19,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    notes VARCHAR(1000),
    created_date DATETIME NOT NULL,
    updated_date DATETIME NOT NULL,
    version BIGINT,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id),
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(warehouse_id),
    INDEX idx_order_code (order_code),
    INDEX idx_supplier_id (supplier_id),
    INDEX idx_warehouse_id (warehouse_id)
);

CREATE TABLE purchase_order_details (
    detail_id VARCHAR(36) PRIMARY KEY,
    purchase_order_id VARCHAR(36) NOT NULL,
    product_id VARCHAR(36) NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(19,2) NOT NULL,
    total_price DECIMAL(19,2) NOT NULL,
    notes VARCHAR(500),
    created_date DATETIME NOT NULL,
    version BIGINT,
    FOREIGN KEY (purchase_order_id) REFERENCES purchase_orders(purchase_order_id),
    INDEX idx_purchase_order_id (purchase_order_id),
    INDEX idx_product_id (product_id)
);

CREATE TABLE inventory (
    inventory_id VARCHAR(36) PRIMARY KEY,
    product_id VARCHAR(36) NOT NULL,
    warehouse_id VARCHAR(36) NOT NULL,
    quantity INT NOT NULL,
    last_checked DATETIME NOT NULL,
    notes VARCHAR(500),
    created_date DATETIME NOT NULL,
    updated_date DATETIME NOT NULL,
    version BIGINT,
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(warehouse_id),
    UNIQUE KEY uk_product_warehouse (product_id, warehouse_id),
    INDEX idx_product_id (product_id),
    INDEX idx_warehouse_id (warehouse_id)
);
