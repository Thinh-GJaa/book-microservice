-- Payment table
CREATE TABLE payments (
    payment_id VARCHAR(36) PRIMARY KEY,
    order_id VARCHAR(36) NOT NULL UNIQUE,
    total_amount DECIMAL(18,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    payment_method VARCHAR(20),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version BIGINT
);

-- PaymentTransaction table
CREATE TABLE payment_transactions (
    payment_transaction_id VARCHAR(36) PRIMARY KEY,
    payment_id VARCHAR(36) NOT NULL,
    type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version BIGINT,
    CONSTRAINT fk_payment_transaction_payment
        FOREIGN KEY (payment_id) REFERENCES payments(payment_id)
); 