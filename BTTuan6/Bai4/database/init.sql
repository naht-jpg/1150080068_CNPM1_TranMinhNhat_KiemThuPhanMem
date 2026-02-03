-- Tạo database và bảng cho Organization Unit
CREATE DATABASE organization_db;

\c organization_db;

CREATE TABLE organization_unit (
    unit_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

-- Dữ liệu mẫu
INSERT INTO organization_unit (name, description) VALUES ('IT Department', 'Information Technology Department');
INSERT INTO organization_unit (name, description) VALUES ('HR Department', 'Human Resources Department');
