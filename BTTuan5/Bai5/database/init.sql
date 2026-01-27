-- Tạo database (chạy riêng trong pgAdmin hoặc psql)
-- CREATE DATABASE bai5_db;

-- Tạo bảng customers
CREATE TABLE IF NOT EXISTS customers (
    id SERIAL PRIMARY KEY,
    customer_id VARCHAR(10) UNIQUE NOT NULL,
    full_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(12) NOT NULL,
    address VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    birth_date DATE,
    gender VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index cho tìm kiếm nhanh
CREATE INDEX idx_customer_id ON customers(customer_id);
CREATE INDEX idx_email ON customers(email);
