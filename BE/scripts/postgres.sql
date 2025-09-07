CREATE DATABASE "cvconnect-core-service";

CREATE TABLE IF NOT EXISTS attach_file (
    id BIGSERIAL PRIMARY KEY,
    original_filename VARCHAR(255) NOT NULL,
    base_filename VARCHAR(255) NOT NULL,
    extension VARCHAR(50) NOT NULL,
    filename VARCHAR(255) NOT NULL,
    format VARCHAR(100) NULL ,
    resource_type VARCHAR(100) NOT NULL,
    secure_url VARCHAR(500) NOT NULL,
    type VARCHAR(100) NOT NULL,
    url VARCHAR(500) NOT NULL,
    public_id VARCHAR(255) NOT NULL,
    folder VARCHAR(255),


    is_active BOOLEAN DEFAULT TRUE,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    created_by VARCHAR(100),
    updated_by VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS organization (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    logo_id BIGINT,
    cover_photo_id BIGINT,
    website VARCHAR(255),
    staff_count_from INT,
    staff_count_to INT,


    is_active BOOLEAN DEFAULT TRUE,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    created_by VARCHAR(100),
    updated_by VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS industry (
    id BIGSERIAL PRIMARY KEY,

    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,

    is_active BOOLEAN DEFAULT TRUE,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    created_by VARCHAR(100),
    updated_by VARCHAR(100)
);

INSERT INTO industry (code, name, created_by) VALUES
('IT', 'Công nghệ thông tin', 'admin'),
('EDU', 'Giáo dục & Đào tạo', 'admin'),
('FIN', 'Tài chính - Ngân hàng', 'admin'),
('INS', 'Bảo hiểm', 'admin'),
('HEA', 'Y tế & Chăm sóc sức khỏe', 'admin'),
('REA', 'Bất động sản', 'admin'),
('CON', 'Xây dựng', 'admin'),
('MAN', 'Sản xuất - Chế biến', 'admin'),
('TRA', 'Thương mại - Bán lẻ', 'admin'),
('AGR', 'Nông nghiệp & Thủy sản', 'admin'),
('LOG', 'Giao thông vận tải & Logistics', 'admin'),
('ENE', 'Năng lượng', 'admin'),
('TEL', 'Viễn thông', 'admin'),
('MED', 'Truyền thông & Giải trí', 'admin'),
('TOU', 'Du lịch & Khách sạn', 'admin'),
('LAW', 'Luật & Dịch vụ pháp lý', 'admin'),
('HR',  'Nhân sự & Tuyển dụng', 'admin'),
('FOO', 'Thực phẩm & Đồ uống', 'admin'),
('FAS', 'Thời trang & Mỹ phẩm', 'admin'),
('PUB', 'Dịch vụ công (Chính phủ, Hành chính)', 'admin'),
('AUT', 'Ô tô & Công nghiệp phụ trợ', 'admin'),
('AVI', 'Hàng không & Vũ trụ', 'admin'),
('MAR', 'Hàng hải & Đóng tàu', 'admin'),
('ELE', 'Điện tử & Cơ điện tử', 'admin'),
('MEC', 'Cơ khí & Chế tạo máy', 'admin'),
('MIN', 'Khai khoáng & Khoáng sản', 'admin');

CREATE TABLE IF NOT EXISTS organization_industry (
    id BIGSERIAL PRIMARY KEY,

    org_id INT NOT NULL,
    industry_id INT NOT NULL,

    is_active BOOLEAN DEFAULT TRUE,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),

    UNIQUE (org_id, industry_id),
    FOREIGN KEY (org_id) REFERENCES organization (id) ON DELETE CASCADE,
    FOREIGN KEY (industry_id) REFERENCES industry (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS organization_address (
    id BIGSERIAL PRIMARY KEY,

    org_id BIGINT NOT NULL,
    is_headquarter BOOLEAN DEFAULT FALSE,
    province VARCHAR(150) NOT NULL,  -- Tỉnh/Thành phố
    district VARCHAR(150), -- Quận/Huyện
    ward VARCHAR(150), -- Xã/Phường
    detail_address VARCHAR(255) NOT NULL,

    is_active BOOLEAN DEFAULT TRUE,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),

    FOREIGN KEY (org_id) REFERENCES organization (id) ON DELETE CASCADE
);
