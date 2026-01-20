-- ============================================================
-- SCRIPT TẠO DATABASE CHO BÀI TẬP ÁP DỤNG
-- Môn: Kiểm Thử Phần Mềm
-- Sinh viên: Trần Minh Nhật - 1150080068 - CNPM1
-- ============================================================

-- ============================================================
-- BƯỚC 1: Tạo database (chạy trong pgAdmin hoặc psql)
-- ============================================================
-- Nếu database đã tồn tại, xóa đi tạo lại 
-- DROP DATABASE IF EXISTS organization_db;

CREATE DATABASE organization_db
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Vietnamese_Vietnam.1258'
    LC_CTYPE = 'Vietnamese_Vietnam.1258'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

COMMENT ON DATABASE organization_db IS 'Database quản lý tổ chức - Bài tập áp dụng Kiểm Thử Phần Mềm';

-- ============================================================
-- BƯỚC 2: Kết nối vào database organization_db rồi chạy các lệnh sau
-- ============================================================

-- Tạo bảng TỔ CHỨC (ORGANIZATION)
CREATE TABLE IF NOT EXISTS organization (
    org_id SERIAL PRIMARY KEY,
    org_name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(12),
    email VARCHAR(100),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Ràng buộc
    CONSTRAINT uq_org_name UNIQUE (org_name),
    CONSTRAINT chk_org_name_length CHECK (LENGTH(org_name) >= 3),
    CONSTRAINT chk_phone_format CHECK (phone ~ '^[0-9]{9,12}$'),
    CONSTRAINT chk_email_format CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

-- Tạo bảng GIÁM ĐỐC (DIRECTOR)
CREATE TABLE IF NOT EXISTS director (
    director_id SERIAL PRIMARY KEY,
    org_id INTEGER NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(12),
    email VARCHAR(100),
    position VARCHAR(100) DEFAULT 'Giám đốc',
    start_date DATE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Khóa ngoại
    CONSTRAINT fk_director_org FOREIGN KEY (org_id) 
        REFERENCES organization(org_id) ON DELETE CASCADE ON UPDATE CASCADE,
    
    -- Ràng buộc
    CONSTRAINT chk_director_phone CHECK (phone ~ '^[0-9]{9,12}$'),
    CONSTRAINT chk_director_email CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

-- ============================================================
-- BƯỚC 3: Tạo các INDEX để tối ưu tìm kiếm
-- ============================================================

-- Index tìm kiếm tổ chức theo tên (không phân biệt hoa thường)
CREATE INDEX IF NOT EXISTS idx_org_name_lower ON organization (LOWER(org_name));

-- Index tìm giám đốc theo tổ chức
CREATE INDEX IF NOT EXISTS idx_director_org ON director (org_id);

-- Index tìm giám đốc theo tên
CREATE INDEX IF NOT EXISTS idx_director_name ON director (LOWER(full_name));

-- ============================================================
-- BƯỚC 4: Tạo TRIGGER cập nhật updated_date tự động
-- ============================================================

CREATE OR REPLACE FUNCTION update_updated_date()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_date = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_organization_update
    BEFORE UPDATE ON organization
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_date();

-- ============================================================
-- BƯỚC 5: Thêm DỮ LIỆU MẪU (Optional)
-- ============================================================

-- Dữ liệu mẫu cho bảng organization
INSERT INTO organization (org_name, address, phone, email) VALUES
('Công ty TNHH ABC', '123 Nguyễn Huệ, Quận 1, TP.HCM', '0901234567', 'contact@abc.com.vn'),
('Tập đoàn XYZ Việt Nam', '456 Lê Lợi, Quận 3, TP.HCM', '0912345678', 'info@xyz.vn'),
('Công ty Cổ phần DEF', '789 Trần Hưng Đạo, Quận 5, TP.HCM', '0923456789', 'support@def.com'),
('Doanh nghiệp GHI Tech', '321 Võ Văn Tần, Quận 3, TP.HCM', '0934567890', 'hello@ghitech.vn'),
('Tổ chức Phi lợi nhuận JKL', '654 Điện Biên Phủ, Bình Thạnh, TP.HCM', '0945678901', 'ngo@jkl.org');

-- Dữ liệu mẫu cho bảng director
INSERT INTO director (org_id, full_name, phone, email, position, start_date) VALUES
(1, 'Nguyễn Văn A', '0901111111', 'nguyenvana@abc.com.vn', 'Giám đốc điều hành', '2020-01-15'),
(2, 'Trần Thị B', '0912222222', 'tranthib@xyz.vn', 'Tổng Giám đốc', '2018-06-01'),
(3, 'Lê Văn C', '0923333333', 'levanc@def.com', 'Giám đốc', '2021-03-20'),
(4, 'Phạm Thị D', '0934444444', 'phamthid@ghitech.vn', 'CEO', '2019-09-10'),
(5, 'Hoàng Văn E', '0945555555', 'hoangvane@jkl.org', 'Giám đốc điều hành', '2022-01-01');

-- ============================================================
-- BƯỚC 6: VIEW để xem danh sách tổ chức và giám đốc
-- ============================================================

CREATE OR REPLACE VIEW vw_organization_director AS
SELECT 
    o.org_id,
    o.org_name AS "Tên tổ chức",
    o.address AS "Địa chỉ",
    o.phone AS "Điện thoại",
    o.email AS "Email",
    d.full_name AS "Giám đốc",
    d.position AS "Chức vụ",
    d.start_date AS "Ngày nhậm chức"
FROM organization o
LEFT JOIN director d ON o.org_id = d.org_id
ORDER BY o.org_name;

-- ============================================================
-- KIỂM TRA DỮ LIỆU
-- ============================================================

-- Xem tất cả tổ chức
-- SELECT * FROM organization;

-- Xem tất cả giám đốc
-- SELECT * FROM director;

-- Xem danh sách tổ chức và giám đốc
-- SELECT * FROM vw_organization_director;
