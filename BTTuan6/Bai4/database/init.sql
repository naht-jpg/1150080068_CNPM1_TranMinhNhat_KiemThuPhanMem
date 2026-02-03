-- Tạo database và bảng cho Organization Unit
CREATE DATABASE organization_db;

\c organization_db;

-- Xóa bảng cũ nếu tồn tại
DROP TABLE IF EXISTS organization_unit;

-- Tạo bảng organization_unit
-- id: SERIAL (auto-increment cho internal use)
-- unit_id: VARCHAR(20), UNIQUE (optional, user input)
-- name: VARCHAR(100), NOT NULL (bắt buộc)
-- description: VARCHAR(255), optional
CREATE TABLE organization_unit (
    id SERIAL PRIMARY KEY,
    unit_id VARCHAR(20) UNIQUE,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255)
);

-- Dữ liệu mẫu
INSERT INTO organization_unit (unit_id, name, description) VALUES ('DEPT_001', 'IT Department', 'Information Technology Department');
INSERT INTO organization_unit (unit_id, name, description) VALUES ('DEPT_002', 'HR Department', 'Human Resources Department');
INSERT INTO organization_unit (unit_id, name, description) VALUES ('DEPT_003', 'Finance', 'Finance and Accounting Department');
