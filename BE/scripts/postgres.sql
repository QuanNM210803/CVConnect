-- CREATE DATABASE "cvconnect-core-service";
--
-- CREATE TABLE IF NOT EXISTS attach_file (
--     id BIGSERIAL PRIMARY KEY,
--     original_filename VARCHAR(255) NOT NULL,
--     base_filename VARCHAR(255) NOT NULL,
--     extension VARCHAR(50) NOT NULL,
--     filename VARCHAR(255) NOT NULL,
--     format VARCHAR(100) NULL ,
--     resource_type VARCHAR(100) NOT NULL,
--     secure_url VARCHAR(500) NOT NULL,
--     type VARCHAR(100) NOT NULL,
--     url VARCHAR(500) NOT NULL,
--     public_id VARCHAR(255) NOT NULL,
--     folder VARCHAR(255),
--
--
--     is_active BOOLEAN DEFAULT TRUE,
--     is_deleted BOOLEAN DEFAULT FALSE,
--     created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP WITHOUT TIME ZONE,
--     created_by VARCHAR(100),
--     updated_by VARCHAR(100)
-- );
--
-- CREATE TABLE IF NOT EXISTS organization (
--     id BIGSERIAL PRIMARY KEY,
--     name VARCHAR(255) NOT NULL,
--     description TEXT,
--     logo_id BIGINT,
--     cover_photo_id BIGINT,
--     website VARCHAR(255),
--     staff_count_from INT,
--     staff_count_to INT,
--
--
--     is_active BOOLEAN DEFAULT TRUE,
--     is_deleted BOOLEAN DEFAULT FALSE,
--     created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP WITHOUT TIME ZONE,
--     created_by VARCHAR(100),
--     updated_by VARCHAR(100)
-- );
--
-- CREATE TABLE IF NOT EXISTS industry (
--     id BIGSERIAL PRIMARY KEY,
--
--     code VARCHAR(50) UNIQUE NOT NULL,
--     name VARCHAR(255) NOT NULL,
--     description TEXT,
--
--     is_active BOOLEAN DEFAULT TRUE,
--     is_deleted BOOLEAN DEFAULT FALSE,
--     created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP WITHOUT TIME ZONE,
--     created_by VARCHAR(100),
--     updated_by VARCHAR(100)
-- );
--
-- INSERT INTO industry (code, name, created_by) VALUES
-- ('IT', 'Công nghệ thông tin', 'admin'),
-- ('EDU', 'Giáo dục & Đào tạo', 'admin'),
-- ('FIN', 'Tài chính - Ngân hàng', 'admin'),
-- ('INS', 'Bảo hiểm', 'admin'),
-- ('HEA', 'Y tế & Chăm sóc sức khỏe', 'admin'),
-- ('REA', 'Bất động sản', 'admin'),
-- ('CON', 'Xây dựng', 'admin'),
-- ('MAN', 'Sản xuất - Chế biến', 'admin'),
-- ('TRA', 'Thương mại - Bán lẻ', 'admin'),
-- ('AGR', 'Nông nghiệp & Thủy sản', 'admin'),
-- ('LOG', 'Giao thông vận tải & Logistics', 'admin'),
-- ('ENE', 'Năng lượng', 'admin'),
-- ('TEL', 'Viễn thông', 'admin'),
-- ('MED', 'Truyền thông & Giải trí', 'admin'),
-- ('TOU', 'Du lịch & Khách sạn', 'admin'),
-- ('LAW', 'Luật & Dịch vụ pháp lý', 'admin'),
-- ('HR',  'Nhân sự & Tuyển dụng', 'admin'),
-- ('FOO', 'Thực phẩm & Đồ uống', 'admin'),
-- ('FAS', 'Thời trang & Mỹ phẩm', 'admin'),
-- ('PUB', 'Dịch vụ công (Chính phủ, Hành chính)', 'admin'),
-- ('AUT', 'Ô tô & Công nghiệp phụ trợ', 'admin'),
-- ('AVI', 'Hàng không & Vũ trụ', 'admin'),
-- ('MAR', 'Hàng hải & Đóng tàu', 'admin'),
-- ('ELE', 'Điện tử & Cơ điện tử', 'admin'),
-- ('MEC', 'Cơ khí & Chế tạo máy', 'admin'),
-- ('MIN', 'Khai khoáng & Khoáng sản', 'admin');
--
-- CREATE TABLE IF NOT EXISTS organization_industry (
--     id BIGSERIAL PRIMARY KEY,
--
--     org_id INT NOT NULL,
--     industry_id INT NOT NULL,
--
--     is_active BOOLEAN DEFAULT TRUE,
--     is_deleted BOOLEAN DEFAULT FALSE,
--     created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP WITHOUT TIME ZONE,
--     created_by VARCHAR(100),
--     updated_by VARCHAR(100),
--
--     UNIQUE (org_id, industry_id),
--     FOREIGN KEY (org_id) REFERENCES organization (id) ON DELETE CASCADE,
--     FOREIGN KEY (industry_id) REFERENCES industry (id) ON DELETE CASCADE
-- );
--
-- CREATE TABLE IF NOT EXISTS organization_address (
--     id BIGSERIAL PRIMARY KEY,
--
--     org_id BIGINT NOT NULL,
--     is_headquarter BOOLEAN DEFAULT FALSE,
--     province VARCHAR(150) NOT NULL,  -- Tỉnh/Thành phố
--     district VARCHAR(150), -- Quận/Huyện
--     ward VARCHAR(150), -- Xã/Phường
--     detail_address VARCHAR(255) NOT NULL,
--
--     is_active BOOLEAN DEFAULT TRUE,
--     is_deleted BOOLEAN DEFAULT FALSE,
--     created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP WITHOUT TIME ZONE,
--     created_by VARCHAR(100),
--     updated_by VARCHAR(100),
--
--     FOREIGN KEY (org_id) REFERENCES organization (id) ON DELETE CASCADE
-- );
--
-- CREATE TABLE IF NOT EXISTS process_type (
--     id BIGSERIAL PRIMARY KEY,
--
--     code VARCHAR(50) UNIQUE NOT NULL,
--     name VARCHAR(255) NOT NULL,
--     sort_order INT DEFAULT 0,
--     is_default BOOLEAN DEFAULT FALSE,
--
--     is_active BOOLEAN DEFAULT TRUE,
--     is_deleted BOOLEAN DEFAULT FALSE,
--     created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP WITHOUT TIME ZONE,
--     created_by VARCHAR(100),
--     updated_by VARCHAR(100)
-- );
--
-- insert into process_type (code, name, sort_order, is_default, created_by) values
-- ('APPLY', 'Ứng tuyển', 1, true, 'admin'),
-- ('SCAN_CV', 'Lọc hồ sơ', 2, true, 'admin'),
-- ('CONTEST', 'Thi tuyển', 3, true, 'admin'),
-- ('INTERVIEW', 'Phỏng vấn', 4, true, 'admin'),
-- ('OFFER', 'Đề nghị làm việc', 5, true, 'admin'),
-- ('ONBOARD', 'Onboard', 6, true, 'admin');

-- CREATE TABLE IF NOT EXISTS level (
--     id BIGSERIAL PRIMARY KEY,
--
--     code VARCHAR(50) UNIQUE NOT NULL,
--     name VARCHAR(255) NOT NULL,
--     is_default BOOLEAN DEFAULT FALSE,
--
--     is_active BOOLEAN DEFAULT TRUE,
--     is_deleted BOOLEAN DEFAULT FALSE,
--     created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP WITHOUT TIME ZONE,
--     created_by VARCHAR(100),
--     updated_by VARCHAR(100)
-- );
--
-- insert into level (code, name, is_default, created_by) values
-- ('INTERN', 'Thực tập sinh', true, 'admin'),
-- ('STAFF', 'Nhân viên', true, 'admin'),
-- ('LEADER', 'Trưởng nhóm', true, 'admin'),
-- ('DEPARTMENT_HEAD', 'Trưởng/Phó phòng', true, 'admin'),
-- ('MANAGER', 'Quản lý', true, 'admin'),
-- ('BRANCH_HEAD', 'Trưởng/Phó chi nhánh', true, 'admin'),
-- ('VICE_DIRECTOR', 'Phó Giám đốc', true, 'admin'),
-- ('DIRECTOR', 'Giám đốc', true, 'admin');

-- CREATE TABLE IF NOT EXISTS department (
--     id BIGSERIAL PRIMARY KEY,
--
--     code VARCHAR(50) NOT NULL,
--     name VARCHAR(255) NOT NULL,
--     org_id BIGINT NOT NULL,
--
--     is_active BOOLEAN DEFAULT TRUE,
--     is_deleted BOOLEAN DEFAULT FALSE,
--     created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP WITHOUT TIME ZONE,
--     created_by VARCHAR(100),
--     updated_by VARCHAR(100),
--
--     FOREIGN KEY (org_id) REFERENCES organization (id) ON DELETE CASCADE,
--     CONSTRAINT uq_department_org_code UNIQUE (org_id, code)
-- );

-- CREATE TABLE IF NOT EXISTS position (
--     id BIGSERIAL PRIMARY KEY,
--
--     code VARCHAR(50) NOT NULL,
--     name VARCHAR(255) NOT NULL,
--     department_id BIGINT NOT NULL,
--
--     is_active BOOLEAN DEFAULT TRUE,
--     is_deleted BOOLEAN DEFAULT FALSE,
--     created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP WITHOUT TIME ZONE,
--     created_by VARCHAR(100),
--     updated_by VARCHAR(100),
--
--     FOREIGN KEY (department_id) REFERENCES department (id) ON DELETE CASCADE,
--     CONSTRAINT uq_position_department_code UNIQUE (department_id, code)
-- );
--
-- CREATE TABLE IF NOT EXISTS position_level (
--     id BIGSERIAL PRIMARY KEY,
--
--     name VARCHAR(255) NOT NULL,
--     position_id BIGINT NOT NULL,
--     level_id BIGINT NOT NULL,
--
--     is_active BOOLEAN DEFAULT TRUE,
--     is_deleted BOOLEAN DEFAULT FALSE,
--     created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP WITHOUT TIME ZONE,
--     created_by VARCHAR(100),
--     updated_by VARCHAR(100),
--
--     FOREIGN KEY (position_id) REFERENCES position (id) ON DELETE CASCADE,
--     FOREIGN KEY (level_id) REFERENCES level (id) ON DELETE CASCADE
-- );
--
-- CREATE TABLE IF NOT EXISTS position_process (
--     id BIGSERIAL PRIMARY KEY,
--
--     name VARCHAR(255) NOT NULL,
--     position_id BIGINT NOT NULL,
--     process_type_id BIGINT NOT NULL,
--     sort_order INT NOT NULL ,
--
--     is_active BOOLEAN DEFAULT TRUE,
--     is_deleted BOOLEAN DEFAULT FALSE,
--     created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP WITHOUT TIME ZONE,
--     created_by VARCHAR(100),
--     updated_by VARCHAR(100),
--
--     FOREIGN KEY (position_id) REFERENCES position (id) ON DELETE CASCADE,
--     FOREIGN KEY (process_type_id) REFERENCES process_type (id) ON DELETE CASCADE
-- );

-- CREATE TABLE IF NOT EXISTS industry_sub (
--     id BIGSERIAL PRIMARY KEY,
--
--     code VARCHAR(50) UNIQUE NOT NULL,
--     name VARCHAR(255) NOT NULL,
--     industry_id BIGINT NOT NULL,
--
--     is_active BOOLEAN DEFAULT TRUE,
--     is_deleted BOOLEAN DEFAULT FALSE,
--     created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP WITHOUT TIME ZONE,
--     created_by VARCHAR(100),
--     updated_by VARCHAR(100),
--
--     FOREIGN KEY (industry_id) REFERENCES industry (id) ON DELETE CASCADE
-- );
--
-- CREATE TABLE IF NOT EXISTS job_ad (
--     id BIGSERIAL PRIMARY KEY,
--
--     code VARCHAR(50) UNIQUE NOT NULL,
--     title VARCHAR(255) NOT NULL,
--     org_id BIGINT NOT NULL,
--     position_id BIGINT NOT NULL,
--     position_level_id BIGINT NOT NULL,
--     work_location_id BIGINT NOT NULL,
--     job_type VARCHAR(100) NOT NULL ,
--     due_date TIMESTAMP WITHOUT TIME ZONE NOT NULL ,
--     quantity INT DEFAULT 1,
--     salary_type VARCHAR(100) NOT NULL ,
--     salary_from INT,
--     salary_to INT,
--     currency_type VARCHAR(50) NOT NULL ,
--     keyword VARCHAR(255),
--     description TEXT,
--     requirement TEXT,
--     benefit TEXT,
--     hr_contact_id BIGINT NOT NULL,
--     job_ad_status VARCHAR(100) NOT NULL,
--     is_public BOOLEAN DEFAULT TRUE,
--     is_auto_send_email BOOLEAN DEFAULT FALSE,
--     email_template_id BIGINT,
--
--     is_active BOOLEAN DEFAULT TRUE,
--     is_deleted BOOLEAN DEFAULT FALSE,
--     created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP WITHOUT TIME ZONE,
--     created_by VARCHAR(100),
--     updated_by VARCHAR(100),
--
--     FOREIGN KEY (org_id) REFERENCES organization (id) ON DELETE CASCADE,
--     FOREIGN KEY (position_id) REFERENCES position (id) ON DELETE CASCADE,
--     FOREIGN KEY (position_level_id) REFERENCES position_level (id) ON DELETE CASCADE,
--     FOREIGN KEY (work_location_id) REFERENCES organization_address (id) ON DELETE CASCADE
-- );
--
-- ALTER TABLE job_ad
-- DROP COLUMN IF EXISTS work_location_id;
--
-- CREATE TABLE IF NOT EXISTS job_ad_work_location (
--     id BIGSERIAL PRIMARY KEY,
--
--     job_ad_id BIGINT NOT NULL,
--     work_location_id BIGINT NOT NULL,
--
--     is_active BOOLEAN DEFAULT TRUE,
--     is_deleted BOOLEAN DEFAULT FALSE,
--     created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP WITHOUT TIME ZONE,
--     created_by VARCHAR(100),
--     updated_by VARCHAR(100),
--
--     FOREIGN KEY (job_ad_id) REFERENCES job_ad (id) ON DELETE CASCADE,
--     FOREIGN KEY (work_location_id) REFERENCES organization_address (id) ON DELETE CASCADE
-- );
--
-- CREATE TABLE IF NOT EXISTS job_ad_industry_sub (
--     id BIGSERIAL PRIMARY KEY,
--
--     industry_sub_id BIGINT NOT NULL,
--     job_ad_id BIGINT NOT NULL,
--
--     is_active BOOLEAN DEFAULT TRUE,
--     is_deleted BOOLEAN DEFAULT FALSE,
--     created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP WITHOUT TIME ZONE,
--     created_by VARCHAR(100),
--     updated_by VARCHAR(100),
--
--     FOREIGN KEY (industry_sub_id) REFERENCES industry_sub (id) ON DELETE CASCADE,
--     FOREIGN KEY (job_ad_id) REFERENCES job_ad (id) ON DELETE CASCADE,
--     UNIQUE (industry_sub_id, job_ad_id)
-- );
--
-- CREATE TABLE IF NOT EXISTS job_ad_process (
--     id BIGSERIAL PRIMARY KEY,
--
--     name VARCHAR(255) NOT NULL,
--     sort_order INT NOT NULL,
--     job_ad_id BIGINT NOT NULL,
--     process_type_id BIGINT NOT NULL,
--
--     is_active BOOLEAN DEFAULT TRUE,
--     is_deleted BOOLEAN DEFAULT FALSE,
--     created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP WITHOUT TIME ZONE,
--     created_by VARCHAR(100),
--     updated_by VARCHAR(100),
--
--     FOREIGN KEY (job_ad_id) REFERENCES job_ad (id) ON DELETE CASCADE,
--     FOREIGN KEY (process_type_id) REFERENCES process_type (id) ON DELETE CASCADE
-- );
--
-- INSERT INTO industry_sub (code, name, industry_id, created_by) VALUES
-- -- IT (industry_id = 1)
-- ('IT_AI', 'AI / Trí tuệ nhân tạo', 1, 'admin'),
-- ('IT_ML', 'Machine Learning Engineer', 1, 'admin'),
-- ('IT_Data', 'Data Engineer / Kỹ sư dữ liệu', 1, 'admin'),
-- ('IT_BigData', 'Big Data', 1, 'admin'),
-- ('IT_Backend', 'Backend Developer', 1, 'admin'),
-- ('IT_Frontend', 'Frontend Developer', 1, 'admin'),
-- ('IT_FullStack', 'Full Stack Developer', 1, 'admin'),
-- ('IT_DevOps', 'DevOps / SRE', 1, 'admin'),
-- ('IT_Security', 'Cybersecurity / Bảo mật', 1, 'admin'),
-- ('IT_Cloud', 'Cloud Engineer / Kỹ sư đám mây', 1, 'admin'),
--
-- -- Giáo dục & Đào tạo (industry_id = 2)
-- ('EDU_Teacher', 'Giáo viên (mầm non / tiểu học / THCS / THPT)', 2, 'admin'),
-- ('EDU_Trainer', 'Giảng viên đào tạo / Trainer', 2, 'admin'),
-- ('EDU_Elearning', 'Chuyên viên E-learning / Thiết kế khóa học số', 2, 'admin'),
-- ('EDU_Admin', 'Quản lý giáo dục / Ban giám hiệu', 2, 'admin'),
-- ('EDU_Tutor', 'Gia sư / Dạy kèm', 2, 'admin'),
-- ('EDU_Counsel', 'Tư vấn học đường / Tư vấn du học', 2, 'admin'),
--
-- -- Tài chính – Ngân hàng (industry_id = 3)
-- ('FIN_Analyst', 'Chuyên viên phân tích tài chính', 3, 'admin'),
-- ('FIN_Accountant', 'Kế toán – Kiểm toán', 3, 'admin'),
-- ('FIN_Risk', 'Chuyên viên quản trị rủi ro / Risk', 3, 'admin'),
-- ('FIN_Credit', 'Chuyên viên tín dụng', 3, 'admin'),
-- ('FIN_FinTech', 'Fintech / Chuyển đổi số tài chính', 3, 'admin'),
-- ('FIN_Fund', 'Quản lý quỹ / Đầu tư', 3, 'admin'),
--
-- -- 4. INS - Bảo hiểm
-- ('INS_Agent', 'Nhân viên tư vấn bảo hiểm', 4, 'admin'),
-- ('INS_Claims', 'Chuyên viên bồi thường', 4, 'admin'),
-- ('INS_Underwriter', 'Chuyên viên thẩm định bảo hiểm', 4, 'admin'),
-- ('INS_Actuary', 'Chuyên viên định phí bảo hiểm (Actuary)', 4, 'admin'),
-- ('INS_Customer', 'Chăm sóc khách hàng bảo hiểm', 4, 'admin'),
-- ('INS_Risk', 'Quản trị rủi ro bảo hiểm', 4, 'admin'),
--
-- -- Y tế & Chăm sóc sức khỏe (industry_id = 5)
-- ('HEA_Doctor', 'Bác sĩ chuyên khoa', 5, 'admin'),
-- ('HEA_Nurse', 'Y tá / Điều dưỡng', 5, 'admin'),
-- ('HEA_Pharm', 'Dược sĩ / Dược học', 5, 'admin'),
-- ('HEA_Admin', 'Quản trị bệnh viện / Hành chính y tế', 5, 'admin'),
-- ('HEA_Lab', 'Kỹ thuật viên xét nghiệm / Lab', 5, 'admin'),
-- ('HEA_Public', 'Chuyên viên y tế cộng đồng', 5, 'admin'),
--
-- -- Bất động sản (industry_id = 6)
-- ('REA_Agent', 'Nhân viên môi giới bất động sản', 6, 'admin'),
-- ('REA_Appraisal', 'Thẩm định giá bất động sản', 6, 'admin'),
-- ('REA_Property', 'Quản lý bất động sản / Bảo trì', 6, 'admin'),
-- ('REA_Dev', 'Phát triển dự án bất động sản', 6, 'admin'),
-- ('REA_Sales', 'Kinh doanh bất động sản', 6, 'admin'),
--
-- -- 7. CON - Xây dựng
-- ('CON_Arch', 'Kiến trúc sư', 7, 'admin'),
-- ('CON_CivilEng', 'Kỹ sư xây dựng dân dụng', 7, 'admin'),
-- ('CON_Struct', 'Kỹ sư kết cấu', 7, 'admin'),
-- ('CON_MEP', 'Kỹ sư MEP (Điện, Nước, Điều hòa)', 7, 'admin'),
-- ('CON_Site', 'Chỉ huy công trường', 7, 'admin'),
-- ('CON_QS', 'Chuyên viên dự toán / QS', 7, 'admin'),
-- ('CON_Worker', 'Công nhân xây dựng', 7, 'admin'),
--
-- -- Sản xuất – Chế biến (industry_id = 8)
-- ('MAN_Production', 'Công nhân sản xuất / Vận hành máy', 8, 'admin'),
-- ('MAN_Quality', 'Kiểm định chất lượng / QC', 8, 'admin'),
-- ('MAN_Maintenance', 'Bảo trì / Bảo dưỡng máy móc', 8, 'admin'),
-- ('MAN_Process', 'Kỹ thuật quy trình sản xuất', 8, 'admin'),
-- ('MAN_Planner', 'Kế hoạch sản xuất / Scheduling', 8, 'admin');
--
-- INSERT INTO industry_sub (code, name, industry_id, created_by) VALUES
-- -- 9. TRA - Thương mại & Bán lẻ
-- ('TRA_RetailMgr', 'Quản lý bán lẻ / Chuỗi cửa hàng', 9, 'admin'),
-- ('TRA_Sales', 'Nhân viên bán hàng', 9, 'admin'),
-- ('TRA_Cashier', 'Thu ngân', 9, 'admin'),
-- ('TRA_Merch', 'Merchandiser / Trưng bày sản phẩm', 9, 'admin'),
-- ('TRA_Buyer', 'Chuyên viên mua hàng', 9, 'admin'),
-- ('TRA_Ecom', 'Thương mại điện tử', 9, 'admin'),
--
-- -- 10. AGR - Nông nghiệp & Thủy sản
-- ('AGR_FarmMgr', 'Quản lý trang trại', 10, 'admin'),
-- ('AGR_AgriTech', 'Kỹ sư nông nghiệp công nghệ cao', 10, 'admin'),
-- ('AGR_Aqua', 'Kỹ sư nuôi trồng thủy sản', 10, 'admin'),
-- ('AGR_Crop', 'Nhà nghiên cứu cây trồng', 10, 'admin'),
-- ('AGR_Vet', 'Bác sĩ thú y', 10, 'admin'),
-- ('AGR_Agro', 'Chế biến nông sản', 10, 'admin'),
--
-- -- 11. LOG - Giao thông vận tải & Logistics
-- ('LOG_Supply', 'Chuyên viên Supply Chain', 11, 'admin'),
-- ('LOG_Forward', 'Nhân viên giao nhận / Forwarder', 11, 'admin'),
-- ('LOG_Warehouse', 'Quản lý kho', 11, 'admin'),
-- ('LOG_Driver', 'Tài xế vận tải', 11, 'admin'),
-- ('LOG_Fleet', 'Quản lý đội xe', 11, 'admin'),
-- ('LOG_Courier', 'Nhân viên giao hàng', 11, 'admin'),
--
-- -- 12. ENE - Năng lượng
-- ('ENE_Solar', 'Kỹ sư năng lượng mặt trời', 12, 'admin'),
-- ('ENE_Wind', 'Kỹ sư năng lượng gió', 12, 'admin'),
-- ('ENE_OilGas', 'Kỹ sư dầu khí', 12, 'admin'),
-- ('ENE_Power', 'Kỹ sư điện lực', 12, 'admin'),
-- ('ENE_Safety', 'Kỹ sư an toàn năng lượng', 12, 'admin'),
--
-- -- 13. TEL - Viễn thông
-- ('TEL_Network', 'Kỹ sư mạng viễn thông', 13, 'admin'),
-- ('TEL_Tech', 'Kỹ thuật viên viễn thông', 13, 'admin'),
-- ('TEL_Support', 'Chăm sóc khách hàng viễn thông', 13, 'admin'),
-- ('TEL_Radio', 'Kỹ sư phát sóng / Radio', 13, 'admin'),
-- ('TEL_Mobile', 'Kỹ sư mạng di động', 13, 'admin'),
--
-- -- 14. MED - Truyền thông & Giải trí
-- ('MED_Journalist', 'Nhà báo / Phóng viên', 14, 'admin'),
-- ('MED_Editor', 'Biên tập viên', 14, 'admin'),
-- ('MED_Marketing', 'Chuyên viên truyền thông marketing', 14, 'admin'),
-- ('MED_PR', 'Quan hệ công chúng (PR)', 14, 'admin'),
-- ('MED_Content', 'Content Creator', 14, 'admin'),
-- ('MED_Producer', 'Nhà sản xuất chương trình', 14, 'admin'),
--
-- -- 15. TOU - Du lịch & Khách sạn
-- ('TOU_TourGuide', 'Hướng dẫn viên du lịch', 15, 'admin'),
-- ('TOU_Travel', 'Chuyên viên điều hành tour', 15, 'admin'),
-- ('TOU_HotelMgr', 'Quản lý khách sạn', 15, 'admin'),
-- ('TOU_Reception', 'Lễ tân khách sạn', 15, 'admin'),
-- ('TOU_FnB', 'Dịch vụ F&B (Ẩm thực - Nhà hàng khách sạn)', 15, 'admin'),
-- ('TOU_Event', 'Tổ chức sự kiện du lịch', 15, 'admin'),
--
-- -- 16. LAW - Luật & Dịch vụ pháp lý
-- ('LAW_Lawyer', 'Luật sư', 16, 'admin'),
-- ('LAW_Notary', 'Công chứng viên', 16, 'admin'),
-- ('LAW_Legal', 'Chuyên viên pháp chế', 16, 'admin'),
-- ('LAW_Judge', 'Thẩm phán / Tòa án', 16, 'admin'),
-- ('LAW_Paralegal', 'Trợ lý luật sư', 16, 'admin'),
--
-- -- 17. HR - Nhân sự & Tuyển dụng
-- ('HR_Recruiter', 'Chuyên viên tuyển dụng', 17, 'admin'),
-- ('HR_Generalist', 'Nhân sự tổng hợp', 17, 'admin'),
-- ('HR_Payroll', 'Chuyên viên tiền lương', 17, 'admin'),
-- ('HR_LnD', 'Đào tạo & Phát triển (L&D)', 17, 'admin'),
-- ('HR_HRMgr', 'Quản lý nhân sự', 17, 'admin'),
--
-- -- 18. FOO - Thực phẩm & Đồ uống
-- ('FOO_Chef', 'Đầu bếp', 18, 'admin'),
-- ('FOO_Baker', 'Thợ làm bánh', 18, 'admin'),
-- ('FOO_Bartender', 'Pha chế / Bartender', 18, 'admin'),
-- ('FOO_FnBMgr', 'Quản lý F&B', 18, 'admin'),
-- ('FOO_Quality', 'Kiểm định chất lượng thực phẩm', 18, 'admin'),
-- ('FOO_Prod', 'Sản xuất & chế biến thực phẩm', 18, 'admin');
--
-- INSERT INTO industry_sub (code, name, industry_id, created_by) VALUES
-- -- 19. FAS - Thời trang & Mỹ phẩm
-- ('FAS_Designer', 'Nhà thiết kế thời trang', 19, 'admin'),
-- ('FAS_Stylist', 'Stylist / Tư vấn phong cách', 19, 'admin'),
-- ('FAS_Model', 'Người mẫu thời trang', 19, 'admin'),
-- ('FAS_Makeup', 'Chuyên viên trang điểm', 19, 'admin'),
-- ('FAS_Beauty', 'Chuyên viên chăm sóc sắc đẹp', 19, 'admin'),
-- ('FAS_Retail', 'Nhân viên bán hàng mỹ phẩm / thời trang', 19, 'admin'),
--
-- -- 20. PUB - Dịch vụ công (Chính phủ, Hành chính)
-- ('PUB_Admin', 'Cán bộ hành chính công', 20, 'admin'),
-- ('PUB_Policy', 'Chuyên viên hoạch định chính sách', 20, 'admin'),
-- ('PUB_Diplomat', 'Cán bộ ngoại giao', 20, 'admin'),
-- ('PUB_Auditor', 'Thanh tra / Kiểm toán nhà nước', 20, 'admin'),
-- ('PUB_Civil', 'Công chức / Viên chức', 20, 'admin'),
--
-- -- 21. AUT - Ô tô & Công nghiệp phụ trợ
-- ('AUT_Design', 'Kỹ sư thiết kế ô tô', 21, 'admin'),
-- ('AUT_Mechanic', 'Kỹ thuật viên sửa chữa ô tô', 21, 'admin'),
-- ('AUT_Prod', 'Sản xuất, lắp ráp ô tô', 21, 'admin'),
-- ('AUT_Sales', 'Kinh doanh & bán hàng ô tô', 21, 'admin'),
-- ('AUT_AfterSale', 'Dịch vụ sau bán hàng / Bảo hành', 21, 'admin'),
-- ('AUT_Supply', 'Chuỗi cung ứng linh kiện ô tô', 21, 'admin'),
--
-- -- 22. AVI - Hàng không & Vũ trụ
-- ('AVI_Pilot', 'Phi công', 22, 'admin'),
-- ('AVI_Cabin', 'Tiếp viên hàng không', 22, 'admin'),
-- ('AVI_Airport', 'Nhân viên mặt đất', 22, 'admin'),
-- ('AVI_ATC', 'Kiểm soát không lưu', 22, 'admin'),
-- ('AVI_AeroEng', 'Kỹ sư hàng không / vũ trụ', 22, 'admin'),
-- ('AVI_Maintenance', 'Bảo dưỡng tàu bay', 22, 'admin'),
--
-- -- 23. MAR - Hàng hải & Đóng tàu
-- ('MAR_Captain', 'Thuyền trưởng / Sĩ quan boong', 23, 'admin'),
-- ('MAR_Sailor', 'Thuỷ thủ', 23, 'admin'),
-- ('MAR_Engineer', 'Kỹ sư hàng hải / Máy tàu', 23, 'admin'),
-- ('MAR_Dock', 'Công nhân đóng tàu', 23, 'admin'),
-- ('MAR_Logistics', 'Giao nhận hàng hải', 23, 'admin'),
--
-- -- 24. ELE - Điện tử & Cơ điện tử
-- ('ELE_Engineer', 'Kỹ sư điện tử', 24, 'admin'),
-- ('ELE_Tech', 'Kỹ thuật viên điện tử', 24, 'admin'),
-- ('ELE_Embedded', 'Kỹ sư nhúng / IoT', 24, 'admin'),
-- ('ELE_Chip', 'Thiết kế vi mạch / Chip design', 24, 'admin'),
-- ('ELE_Test', 'Kiểm thử phần cứng điện tử', 24, 'admin'),
--
-- -- 25. MEC - Cơ khí & Chế tạo máy
-- ('MEC_Design', 'Kỹ sư thiết kế cơ khí', 25, 'admin'),
-- ('MEC_Turner', 'Thợ tiện / Phay / Gia công cơ khí', 25, 'admin'),
-- ('MEC_CNC', 'Kỹ thuật viên CNC', 25, 'admin'),
-- ('MEC_Maintenance', 'Bảo trì cơ khí', 25, 'admin'),
-- ('MEC_Welder', 'Thợ hàn', 25, 'admin'),
--
-- -- 26. MIN - Khai khoáng & Khoáng sản
-- ('MIN_Geo', 'Kỹ sư địa chất', 26, 'admin'),
-- ('MIN_Mining', 'Kỹ sư khai thác mỏ', 26, 'admin'),
-- ('MIN_Safety', 'Chuyên viên an toàn mỏ', 26, 'admin'),
-- ('MIN_Processing', 'Chế biến khoáng sản', 26, 'admin'),
-- ('MIN_Survey', 'Khảo sát địa chất', 26, 'admin');
--
-- CREATE TABLE IF NOT EXISTS candidate_info_apply (
--     id BIGSERIAL PRIMARY KEY,
--
--     candidate_id BIGINT NOT NULL,
--     full_name VARCHAR(255) NOT NULL,
--     email VARCHAR(255) NOT NULL,
--     phone VARCHAR(50),
--     cv_file_id BIGINT NOT NULL,
--     cover_letter TEXT,
--
--     is_active BOOLEAN DEFAULT TRUE,
--     is_deleted BOOLEAN DEFAULT FALSE,
--     created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP WITHOUT TIME ZONE,
--     created_by VARCHAR(100),
--     updated_by VARCHAR(100),
--
--     FOREIGN KEY (cv_file_id) REFERENCES attach_file (id) ON DELETE CASCADE
-- );
--
-- CREATE TABLE IF NOT EXISTS job_ad_candidate (
--     id BIGSERIAL PRIMARY KEY,
--
--     job_ad_id BIGINT NOT NULL,
--     candidate_info_id BIGINT NOT NULL,
--     apply_date TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
--
--     candidate_status VARCHAR(100) NOT NULL,
--     eliminate_reason_type TEXT,
--     eliminate_reason_detail TEXT,
--
--     is_active BOOLEAN DEFAULT TRUE,
--     is_deleted BOOLEAN DEFAULT FALSE,
--     created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP WITHOUT TIME ZONE,
--     created_by VARCHAR(100),
--     updated_by VARCHAR(100),
--
--     FOREIGN KEY (job_ad_id) REFERENCES job_ad (id) ON DELETE CASCADE,
--     FOREIGN KEY (candidate_info_id) REFERENCES candidate_info_apply (id) ON DELETE CASCADE
-- );
--
-- CREATE TABLE IF NOT EXISTS job_ad_process_candidate (
--     id BIGSERIAL PRIMARY KEY,
--
--     job_ad_process_id BIGINT NOT NULL,
--     job_ad_candidate_id BIGINT NOT NULL,
--     action_date TIMESTAMP WITHOUT TIME ZONE,
--     is_current_process BOOLEAN DEFAULT FALSE,
--     note TEXT,
--
--     is_active BOOLEAN DEFAULT TRUE,
--     is_deleted BOOLEAN DEFAULT FALSE,
--     created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP WITHOUT TIME ZONE,
--     created_by VARCHAR(100),
--     updated_by VARCHAR(100),
--
--     FOREIGN KEY (job_ad_process_id) REFERENCES job_ad_process (id) ON DELETE CASCADE,
--     FOREIGN KEY (job_ad_candidate_id) REFERENCES job_ad_candidate (id) ON DELETE CASCADE
-- );
--
-- alter table job_ad_candidate
-- add column if not exists onboard_date TIMESTAMP WITHOUT TIME ZONE;
--
-- alter table job_ad
-- add column if not exists is_remote BOOLEAN DEFAULT FALSE;

-- alter table job_ad
-- drop column position_level_id;
--
-- drop table position_level;
--
-- CREATE TABLE IF NOT EXISTS job_ad_level (
--     id BIGSERIAL PRIMARY KEY,
--
--     job_ad_id BIGINT NOT NULL,
--     level_id BIGINT NOT NULL,
--
--     is_active BOOLEAN DEFAULT TRUE,
--     is_deleted BOOLEAN DEFAULT FALSE,
--     created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP WITHOUT TIME ZONE,
--     created_by VARCHAR(100),
--     updated_by VARCHAR(100),
--
--     FOREIGN KEY (job_ad_id) REFERENCES job_ad (id) ON DELETE CASCADE,
--     FOREIGN KEY (level_id) REFERENCES level (id) ON DELETE CASCADE
-- );
--
-- alter table job_ad
-- add column is_all_level BOOLEAN DEFAULT FALSE;
--
-- alter table industry_sub
-- drop column industry_id;
--
-- alter table job_ad_industry_sub
-- rename column industry_sub_id to career_id;
--
-- ALTER TABLE industry_sub RENAME TO careers;
--
-- alter table job_ad_industry_sub rename to job_ad_career;

CREATE TABLE IF NOT EXISTS candidate_summary_hr (
    id BIGSERIAL PRIMARY KEY,

    level VARCHAR(100),
    skill TEXT,
    org_id BIGINT NOT NULL,
    candidate_info_id BIGINT NOT NULL ,
    hr_contact_id BIGINT NOT NULL,

    is_active BOOLEAN DEFAULT TRUE,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),

    FOREIGN KEY (org_id) REFERENCES organization (id) ON DELETE CASCADE,
    FOREIGN KEY (candidate_info_id) REFERENCES candidate_info_apply (id) ON DELETE CASCADE
);

drop table candidate_summary_hr;

CREATE TABLE IF NOT EXISTS candidate_summary_org (
    id BIGSERIAL PRIMARY KEY,

    skill TEXT,
    level_id BIGINT NOT NULL ,
    org_id BIGINT NOT NULL,
    candidate_info_id BIGINT NOT NULL ,

    is_active BOOLEAN DEFAULT TRUE,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),

    FOREIGN KEY (org_id) REFERENCES organization (id) ON DELETE CASCADE,
    FOREIGN KEY (candidate_info_id) REFERENCES candidate_info_apply (id) ON DELETE CASCADE,
    FOREIGN KEY (level_id) REFERENCES level (id) ON DELETE CASCADE
);

update level
set code = 'FRESHER', name = 'Fresher'
where code = 'STAFF';

update level
set code = 'JUNIOR', name = 'Junior'
where code = 'LEADER';

update level
set code = 'JUNIOR_PLUS', name = 'Junior+'
where code = 'DEPARTMENT_HEAD';

update level
set code = 'MIDDLE', name = 'Middle'
where code = 'MANAGER';

update level
set code = 'MIDDLE_PLUS', name = 'Middle+'
where code = 'BRANCH_HEAD';

update level
set code = 'SENIOR', name = 'Senior'
where code = 'VICE_DIRECTOR';

update level
set code = 'LEADER', name = 'Leader'
where code = 'DIRECTOR';
