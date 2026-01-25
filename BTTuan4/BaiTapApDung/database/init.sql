-- ============================================================
-- SCRIPT TẠO DATABASE CHO BÀI TẬP ÁP DỤNG
-- Môn: Kiểm Thử Phần Mềm
-- Sinh viên: Trần Minh Nhật - 1150080068 - CNPM1
-- ============================================================

-- ============================================================
-- BƯỚC 1: Tạo database (chạy trong pgAdmin hoặc psql)
-- ============================================================

CREATE DATABASE organization_db
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- ============================================================
-- BƯỚC 2: Kết nối vào database organization_db rồi chạy lệnh sau
-- ============================================================

-- Bảng ORGANIZATION
CREATE TABLE IF NOT EXISTS organization (
    org_id SERIAL PRIMARY KEY,
    org_name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(12),
    email VARCHAR(100),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT uq_org_name UNIQUE (org_name),
    CONSTRAINT chk_org_name_length CHECK (LENGTH(org_name) >= 3),
    CONSTRAINT chk_phone_format CHECK (phone IS NULL OR phone ~ '^[0-9]{9,12}$'),
    CONSTRAINT chk_email_format CHECK (email IS NULL OR email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);
