-- V1__create_order_tables.sql
CREATE TABLE IF NOT EXISTS orders (
    order_id VARCHAR(255) PRIMARY KEY,
    customer_id VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    order_date TIMESTAMP NOT NULL,
    last_updated_date TIMESTAMP NOT NULL,
    total_amount DECIMAL(19, 4) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    payment_method_details VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS order_items (
    order_item_id VARCHAR(255) PRIMARY KEY,
    order_id VARCHAR(255) NOT NULL,
    product_id VARCHAR(255) NOT NULL,
    captured_product_name VARCHAR(255) NOT NULL,
    captured_unit_price DECIMAL(19, 4) NOT NULL,
    quantity INT NOT NULL,
    line_item_total DECIMAL(19, 4) NOT NULL,
    CONSTRAINT fk_order_id FOREIGN KEY (order_id) REFERENCES orders (order_id)
);