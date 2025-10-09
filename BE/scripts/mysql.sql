# CREATE DATABASE IF NOT EXISTS `cvconnect-user-service`;
#
# CREATE TABLE IF NOT EXISTS `cvconnect-user-service`.users (
#     `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
#
#     `username` VARCHAR(100) NOT NULL UNIQUE ,
#     `password` VARCHAR(255) NOT NULL,
#     `email` VARCHAR(255) NOT NULL UNIQUE,
#     `full_name` VARCHAR(255) NOT NULL,
#     `phone_number` VARCHAR(20),
#     `address` VARCHAR(255),
#     `date_of_birth` DATE,
#     `avatar_url` VARCHAR(255),
#     `is_email_verified` TINYINT(1) DEFAULT 0,
#     `access_method` VARCHAR(255) NOT NULL,
#
#     `is_active` TINYINT(1) DEFAULT 1,
#     `is_deleted` TINYINT(1) DEFAULT 0,
#     `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
#     `updated_at` DATETIME,
#     `created_by` VARCHAR(100),
#     `updated_by` VARCHAR(100),
#
#     INDEX idx_username (username),
#     INDEX idx_email (email)
# );
#
# CREATE TABLE IF NOT EXISTS `cvconnect-user-service`.roles (
#     `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
#
#     `code` VARCHAR(100) NOT NULL UNIQUE,
#     `name` VARCHAR(255) NOT NULL UNIQUE,
#     `member_type` VARCHAR(255) NOT NULL,
#
#     `is_active` TINYINT(1) DEFAULT 1,
#     `is_deleted` TINYINT(1) DEFAULT 0,
#     `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
#     `updated_at` DATETIME,
#     `created_by` VARCHAR(100),
#     `updated_by` VARCHAR(100)
# );
#
# CREATE TABLE IF NOT EXISTS `cvconnect-user-service`.menus (
#     `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
#
#     `code` VARCHAR(100) NOT NULL UNIQUE,
#     `label` VARCHAR(255) NOT NULL UNIQUE,
#     `icon` VARCHAR(255),
#     `url` VARCHAR(255),
#     `parent_id` BIGINT DEFAULT NULL,
#     `sort_order` INT NOT NULL DEFAULT 1,
#
#     `is_active` TINYINT(1) DEFAULT 1,
#     `is_deleted` TINYINT(1) DEFAULT 0,
#     `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
#     `updated_at` DATETIME,
#     `created_by` VARCHAR(100),
#     `updated_by` VARCHAR(100)
# );
#
# CREATE TABLE IF NOT EXISTS `cvconnect-user-service`.role_user (
#     `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
#
#     `user_id` BIGINT NOT NULL,
#     `role_id` BIGINT NOT NULL,
#
#     `is_active` TINYINT(1) DEFAULT 1,
#     `is_deleted` TINYINT(1) DEFAULT 0,
#     `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
#     `updated_at` DATETIME,
#     `created_by` VARCHAR(100),
#     `updated_by` VARCHAR(100),
#     FOREIGN KEY (role_id) REFERENCES `cvconnect-user-service`.roles(id) ON DELETE CASCADE ON UPDATE CASCADE,
#     FOREIGN KEY (user_id) REFERENCES `cvconnect-user-service`.users(id) ON DELETE CASCADE ON UPDATE CASCADE,
#     UNIQUE (role_id, user_id)
# );
#
# CREATE TABLE IF NOT EXISTS `cvconnect-user-service`.role_menu (
#     `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
#
#     `role_id` BIGINT NOT NULL,
#     `menu_id` BIGINT NOT NULL,
#     `permission` VARCHAR(255) NULL,
#
#     `is_active` TINYINT(1) DEFAULT 1,
#     `is_deleted` TINYINT(1) DEFAULT 0,
#     `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
#     `updated_at` DATETIME,
#     `created_by` VARCHAR(100),
#     `updated_by` VARCHAR(100),
#     FOREIGN KEY (role_id) REFERENCES `cvconnect-user-service`.roles(id) ON DELETE CASCADE ON UPDATE CASCADE,
#     FOREIGN KEY (menu_id) REFERENCES `cvconnect-user-service`.menus(id) ON DELETE CASCADE ON UPDATE CASCADE,
#     UNIQUE (role_id, menu_id)
# );
#
# CREATE TABLE IF NOT EXISTS `cvconnect-user-service`.candidates (
#     `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
#
#     `user_id` BIGINT NOT NULL UNIQUE,
#
#     `is_active` TINYINT(1) DEFAULT 1,
#     `is_deleted` TINYINT(1) DEFAULT 0,
#     `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
#     `updated_at` DATETIME,
#     `created_by` VARCHAR(100),
#     `updated_by` VARCHAR(100),
#     FOREIGN KEY (user_id) REFERENCES `cvconnect-user-service`.users(id) ON DELETE CASCADE ON UPDATE CASCADE
# );
#
# CREATE TABLE IF NOT EXISTS `cvconnect-user-service`.org_members (
#     `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
#
#     `user_id` BIGINT NOT NULL UNIQUE,
#     `org_id` BIGINT NOT NULL,
#
#     `is_active` TINYINT(1) DEFAULT 1,
#     `is_deleted` TINYINT(1) DEFAULT 0,
#     `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
#     `updated_at` DATETIME,
#     `created_by` VARCHAR(100),
#     `updated_by` VARCHAR(100),
#     FOREIGN KEY (user_id) REFERENCES `cvconnect-user-service`.users(id) ON DELETE CASCADE ON UPDATE CASCADE
# );
#
# CREATE TABLE IF NOT EXISTS `cvconnect-user-service`.management_members (
#     `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
#
#     `user_id` BIGINT NOT NULL UNIQUE,
#
#     `is_active` TINYINT(1) DEFAULT 1,
#     `is_deleted` TINYINT(1) DEFAULT 0,
#     `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
#     `updated_at` DATETIME,
#     `created_by` VARCHAR(100),
#     `updated_by` VARCHAR(100),
#     FOREIGN KEY (user_id) REFERENCES `cvconnect-user-service`.users(id) ON DELETE CASCADE ON UPDATE CASCADE
# );

# insert into `cvconnect-user-service`.users(username, password, email, full_name, phone_number, address, date_of_birth, avatar_url, is_email_verified, access_method, created_by)
# values ('admin', '$2a$10$//aPySVETPhRYx/6xFKev.4S81w7Oq6zs44rnl9aeNe.u7W7GdFaq', 'nnmhqn2003@gmail.com', 'System Administrator', '0123456789', '123 Main St, City, Country', null, null, 1, 'LOCAL', 'anonymous');
# insert into `cvconnect-user-service`.management_members(id, user_id, created_by)
# values (1, 1, 'anonymous');
#
# insert into `cvconnect-user-service`.roles(code, name, member_type, created_by)
# values ('SYSTEM_ADMIN', 'Quản trị hệ thống', 'MANAGEMENT', 'admin'),
#        ('CANDIDATE', 'Ứng viên', 'CANDIDATE', 'admin'),
#        ('ORG_ADMIN', 'Quản trị tổ chức', 'ORGANIZATION', 'admin'),
#        ('HR', 'Nhân viên tuyển dụng', 'ORGANIZATION', 'admin'),
#        ('INTERVIEWER', 'Người phỏng vấn', 'ORGANIZATION', 'admin');
#
# insert into `cvconnect-user-service`.role_user(user_id, role_id, created_by)
# values (1,1, 'admin');

# insert into `cvconnect-user-service`.menus(id, code, label, icon, url, parent_id, sort_order, created_by) values
#  (1, 'REPORT', 'Báo cáo', 'material-symbols:dashboard-2-outline', null, null, 1, 'admin')
# , (2, 'ORG', 'Tổ chức', 'material-symbols:person-play', '/organizations', null, 2, 'admin')
# , (3, 'USER', 'Người dùng', 'material-symbols:account-circle-full', '/users', null, 3, 'admin')
# ,(4, 'SETUP', 'Thiết lập', 'material-symbols:settings-b-roll-outline', null, null, 4, 'admin')
# ,(5, 'USER_GROUP', 'Nhóm người dùng', null, '/user-group', 4, 1, 'admin')
# , (6, 'CV_TEMPLATE', 'Mẫu CV', null, '/cv-templates', 4, 2, 'admin')
# ;
# insert into `cvconnect-user-service`.menus(id, code, label, icon, url, parent_id, sort_order, created_by) values
#  (7, 'REPORT_ORG', 'Tổ chức tham gia', null, '/report/org', 1, 1, 'admin')
# , (8, 'REPORT_JOB_POST', 'Tin tuyển dụng', null, '/report/job-post', 1, 2, 'admin')
# , (9, 'REPORT_CANDIDATE', 'Ứng viên', null, '/report/candidate', 1, 3, 'admin')
# ;
#
# insert into `cvconnect-user-service`.role_menu(role_id, menu_id, permission, created_by) values
# (1, 1, null, 'admin')
# , (1, 2, 'VIEW,ADD,UPDATE,DELETE,EXPORT', 'admin')
# , (1, 3, 'VIEW,ADD,UPDATE,DELETE,EXPORT', 'admin')
# , (1, 4, null, 'admin')
# , (1, 5, 'VIEW,ADD,UPDATE,DELETE,EXPORT', 'admin')
# , (1, 6, 'VIEW,ADD,UPDATE,DELETE,EXPORT', 'admin');
# insert into `cvconnect-user-service`.role_menu(role_id, menu_id, permission, created_by) values
#  (1, 7, 'VIEW,ADD,UPDATE,DELETE,EXPORT', 'admin')
# , (1, 8, 'VIEW,ADD,UPDATE,DELETE,EXPORT', 'admin')
# , (1, 9, 'VIEW,ADD,UPDATE,DELETE,EXPORT', 'admin');
#
# delete from `cvconnect-user-service`.menus where id = 7;
# update `cvconnect-user-service`.menus set icon = 'mdi:circle-medium' where icon is null;
# UPDATE `cvconnect-user-service`.menus
# SET url = CONCAT('/admin-system', url)
# WHERE url IS NOT NULL;
#
# update `cvconnect-user-service`.menus
# set sort_order = 1
# where id = 9;
# update `cvconnect-user-service`.menus
# set sort_order = 2
# where id = 8;
#
# alter table `cvconnect-user-service`.users
# change avatar_url avatar_id BIGINT NULL ;
#
# UPDATE `cvconnect-user-service`.menus
# SET url = REPLACE(url, '/admin-system/', '/system-admin/')
# WHERE url LIKE '/admin-system/%';
#
# insert into `cvconnect-user-service`.menus(id, code, label, icon, url, parent_id, sort_order, created_by) values
#  (10, 'PROCESS_TYPE', 'Vòng tuyển dụng', 'mdi:circle-medium', '/system-admin/process-type', 4, 3, 'admin');
#
# insert into `cvconnect-user-service`.role_menu (role_id, menu_id, permission, created_by) values
#  (1, 10, 'VIEW,ADD,UPDATE,DELETE,EXPORT', 'admin');

# alter table `cvconnect-user-service`.menus
# add column for_member_type VARCHAR(100) after sort_order;
# alter table `cvconnect-user-service`.menus
# add column is_show TINYINT(1) DEFAULT 1 after for_member_type;
#
# update `cvconnect-user-service`.menus
# set label = 'Danh sách tổ chức'
# where code = 'ORG';
# update `cvconnect-user-service`.menus
# set label = 'Người dùng hệ thống'
# where code = 'USER';
# update `cvconnect-user-service`.menus
# set for_member_type = 'MANAGEMENT'
# where id > 0;
# update `cvconnect-user-service`.menus
# set for_member_type = null
# where code in ('REPORT', 'SETUP');
#
# insert into `cvconnect-user-service`.menus(id, code, label, icon, url, parent_id, sort_order, for_member_type, is_show, created_by) values
# (11, 'CATEGORY', 'Danh mục', 'material-symbols-light:category-outline-rounded', null, null, 5, null, 1, 'admin'),
# (12, 'LEVEL', 'Cấp bậc', 'mdi:circle-medium', '/system-admin/level', 11, 1, 'MANAGEMENT', 1, 'admin'),
# (13, 'INDUSTRY', 'Ngành nghề', 'mdi:circle-medium', '/system-admin/industry', 11, 2, 'MANAGEMENT', 1, 'admin'),
# (14, 'ORG_MEMBER', 'Thành viên tổ chức', 'material-symbols:account-circle-full', '/org-admin/org-member', null, 2, 'ORGANIZATION', 1, 'admin'),
# (15, 'ORG_ADDRESS', 'Địa điểm làm việc', 'mdi:circle-medium', '/org-admin/org-address', 4, 1, 'ORGANIZATION', 1, 'admin'),
# (16, 'ORG_INDUSTRY', 'Ngành nghề tổ chức', 'mdi:circle-medium', '/org-admin/org-industry', 4, 2, 'ORGANIZATION', 1, 'admin'),
# (17, 'DEPARTMENT', 'Phòng ban', 'mdi:circle-medium', '/org-admin/department', 11, 1, 'ORGANIZATION', 1, 'admin'),
# (18, 'POSITION', 'Vị trí công việc', 'mdi:circle-medium', '/org-admin/position', 11, 2, 'ORGANIZATION', 1, 'admin');
#
# insert into `cvconnect-user-service`.role_menu(role_id, menu_id, permission, created_by) values
# (1, 11, null, 'admin'),
# (1, 12, 'VIEW,ADD,UPDATE,DELETE,EXPORT', 'admin'),
# (1, 13, 'VIEW,ADD,UPDATE,DELETE,EXPORT', 'admin'),
# (3,1, null, 'admin'),
# (3, 14, 'VIEW,ADD,UPDATE,DELETE,EXPORT', 'admin'),
# (3, 15, 'VIEW,ADD,UPDATE,DELETE,EXPORT', 'admin'),
# (3, 16, 'VIEW,ADD,UPDATE,DELETE,EXPORT', 'admin'),
# (3, 17, 'VIEW,ADD,UPDATE,DELETE,EXPORT', 'admin'),
# (3, 18, 'VIEW,ADD,UPDATE,DELETE,EXPORT', 'admin');
#
# insert into `cvconnect-user-service`.role_menu(role_id, menu_id, permission, created_by) values
# (3, 4, null, 'admin'),
# (3, 11, null, 'admin');
#
# update `cvconnect-user-service`.menus
# set icon = 'material-symbols:category-outline-rounded'
# where code = 'CATEGORY';
#
# update `cvconnect-user-service`.menus
# set label = 'Danh sách doanh nghiệp'
# where code = 'ORG';
# update `cvconnect-user-service`.menus
# set label = 'Thành viên doanh nghiệp'
# where code = 'ORG_MEMBER';
# update `cvconnect-user-service`.menus
# set label = 'Ngành nghề doanh nghiệp'
# where code = 'ORG_INDUSTRY';

update `cvconnect-user-service`.menus
set label = 'Thành viên'
where code = 'ORG_MEMBER';

update `cvconnect-user-service`.menus
set label = 'Lĩnh vực'
where code = 'INDUSTRY';

update `cvconnect-user-service`.menus
set parent_id = 11, sort_order = 3
where code = 'PROCESS_TYPE';

update `cvconnect-user-service`.menus
set sort_order = 10 * sort_order
where parent_id is null;

update `cvconnect-user-service`.menus
set code = 'REPORT_JOB_AD', url = '/system-admin/report/job-ad'
where code = 'REPORT_JOB_POST';

delete from `cvconnect-user-service`.menus
where code = 'ORG_INDUSTRY';

update `cvconnect-user-service`.menus
set sort_order = 100
where code = 'ORG_MEMBER';

alter table `cvconnect-user-service`.menus drop index label;

insert into `cvconnect-user-service`.menus(id, code, label, icon, url, parent_id, sort_order, for_member_type, is_show, created_by) values
(19, 'ORG_INFO', 'Thông tin chung', 'ri:info-card-line', '/org-info', null, 5, 'ORGANIZATION', 1, 'admin'),
(20, 'REPORT_ORG_CANDIDATE', 'Ứng viên', 'mdi:circle-medium', '/org/report/candidate', 10, 1, 'ORGANIZATION', 1, 'admin'),
(21, 'REPORT_ORG_JOB_AD', 'Tin tuyển dụng', 'mdi:circle-medium', '/org/report/job-ad', 10, 2, 'ORGANIZATION', 1, 'admin'),
(22, 'ORG_CANDIDATE', 'Ứng viên', 'material-symbols:person-pin-outline', '/org/candidate', null, 25, 'ORGANIZATION', 1, 'admin'),
(23, 'ORG_JOB_AD', 'Tin tuyển dụng', 'hugeicons:job-search', '/org/job-ad', null, 33, 'ORGANIZATION', 1, 'admin'),
(24, 'ORG_ONBOARD', 'Danh sách onboard', 'material-symbols:person-check-outline-rounded', '/org/onboard', null, 37, 'ORGANIZATION', 1, 'admin'),
(25, 'EMAIL_TEMPLATE', 'Mẫu Email', 'material-symbols:mail-asterisk-outline-sharp', '/org-admin/email-template', 40, 2, 'ORGANIZATION', 1, 'admin'),
(26, 'ORG_CALENDAR', 'Lịch', 'material-symbols:calendar-month-outline-rounded', '/org/calendar', null, 75, 'ORGANIZATION', 1, 'admin');

update `cvconnect-user-service`.menus
set parent_id = 1
where code in ('REPORT_ORG_CANDIDATE', 'REPORT_ORG_JOB_AD');

update `cvconnect-user-service`.menus
set parent_id = 4
where code in ('EMAIL_TEMPLATE');

update `cvconnect-user-service`.menus
set icon = 'mdi:circle-medium'
where code in ('EMAIL_TEMPLATE');


update `cvconnect-user-service`.menus
set label = 'Vị trí tuyển dụng'
where code = 'POSITION';

CREATE TABLE IF NOT EXISTS `cvconnect-user-service`.invite_join_org (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,

    `user_id` BIGINT NOT NULL,
    `role_id` BIGINT NOT NULL,
    `org_id` BIGINT NOT NULL,
    `status` VARCHAR(100) NOT NULL ,

    `is_active` TINYINT(1) DEFAULT 1,
    `is_deleted` TINYINT(1) DEFAULT 0,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME,
    `created_by` VARCHAR(100),
    `updated_by` VARCHAR(100),

    FOREIGN KEY (role_id) REFERENCES `cvconnect-user-service`.roles(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES `cvconnect-user-service`.users(id) ON DELETE CASCADE ON UPDATE CASCADE
);

alter table `cvconnect-user-service`.invite_join_org
add column `token` VARCHAR(255) NOT NULL UNIQUE after org_id;

#----------------------------------------------------------------------------------------------------------------------#

# create database if not exists `cvconnect-notify-service`;
# CREATE TABLE IF NOT EXISTS `cvconnect-notify-service`.email_config (
#     `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
#
#     `host` VARCHAR(255) NOT NULL,
#     `port` INT NOT NULL,
#     `email` VARCHAR(255) NOT NULL,
#     `password` VARCHAR(255) NOT NULL,
#     `is_ssl` TINYINT(1) DEFAULT 0,
#     `protocol` VARCHAR(50) DEFAULT 'smtp',
#     `org_id` BIGINT UNIQUE,
#
#     `is_active` TINYINT(1) DEFAULT 1,
#     `is_deleted` TINYINT(1) DEFAULT 0,
#     `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
#     `updated_at` DATETIME,
#     `created_by` VARCHAR(100),
#     `updated_by` VARCHAR(100)
# );
#
# CREATE TABLE IF NOT EXISTS `cvconnect-notify-service`.placeholders (
#     `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
#
#     `code` VARCHAR(100) NOT NULL UNIQUE,
#     `label` VARCHAR(255) NOT NULL UNIQUE,
#     `description` VARCHAR(500),
#     `member_type_used` VARCHAR(255),
#
#     `is_active` TINYINT(1) DEFAULT 1,
#     `is_deleted` TINYINT(1) DEFAULT 0,
#     `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
#     `updated_at` DATETIME,
#     `created_by` VARCHAR(100),
#     `updated_by` VARCHAR(100)
# );
#
# CREATE TABLE IF NOT EXISTS `cvconnect-notify-service`.email_templates (
#     `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
#
#     `code` VARCHAR(100) NOT NULL UNIQUE,
#     `name` VARCHAR(255) NOT NULL,
#     `subject` VARCHAR(255) NOT NULL,
#     `body` TEXT NOT NULL,
#     `org_id` BIGINT,
#
#     `is_active` TINYINT(1) DEFAULT 1,
#     `is_deleted` TINYINT(1) DEFAULT 0,
#     `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
#     `updated_at` DATETIME,
#     `created_by` VARCHAR(100),
#     `updated_by` VARCHAR(100)
# );
#
# CREATE TABLE IF NOT EXISTS `cvconnect-notify-service`.email_template_placeholder (
#     `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
#
#     email_template_id BIGINT NOT NULL,
#     placeholder_id BIGINT NOT NULL,
#
#     `is_active` TINYINT(1) DEFAULT 1,
#     `is_deleted` TINYINT(1) DEFAULT 0,
#     `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
#     `updated_at` DATETIME,
#     `created_by` VARCHAR(100),
#     `updated_by` VARCHAR(100),
#     UNIQUE(email_template_id, placeholder_id),
#     FOREIGN KEY (email_template_id) REFERENCES `cvconnect-notify-service`.email_templates(id) ON DELETE CASCADE ON UPDATE CASCADE,
#     FOREIGN KEY (placeholder_id) REFERENCES `cvconnect-notify-service`.placeholders(id) ON DELETE CASCADE ON UPDATE CASCADE
# );
#
# CREATE TABLE IF NOT EXISTS `cvconnect-notify-service`.email_logs (
#     `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
#
#     `message_id` VARCHAR(255) UNIQUE ,
#     `reply_message_id` VARCHAR(255),
#     `email_group` VARCHAR(100),
#     `sender` VARCHAR(255) NOT NULL,
#     `recipients` TEXT NOT NULL,
#     `cc_list` TEXT,
#     `subject` VARCHAR(255) NOT NULL,
#     `body` TEXT,
#     `email_template_id` BIGINT,
#     `template` VARCHAR(100),
#     `template_variables` TEXT,
#     `status` VARCHAR(100) NOT NULL,
#     `error_message` TEXT,
#     `sent_at` DATETIME,
#
#     `is_active` TINYINT(1) DEFAULT 1,
#     `is_deleted` TINYINT(1) DEFAULT 0,
#     `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
#     `updated_at` DATETIME,
#     `created_by` VARCHAR(100),
#     `updated_by` VARCHAR(100)
# );
#
# CREATE TABLE IF NOT EXISTS `cvconnect-notify-service`.job_config (
#     `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
#
#     `job_name` VARCHAR(100) NOT NULL UNIQUE,
#     `schedule_type` VARCHAR(50) NOT NULL,
#     `expression` VARCHAR(100) NOT NULL,
#     `description` VARCHAR(500),
#
#     `is_active` TINYINT(1) DEFAULT 1,
#     `is_deleted` TINYINT(1) DEFAULT 0,
#     `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
#     `updated_at` DATETIME,
#     `created_by` VARCHAR(100),
#     `updated_by` VARCHAR(100)
# );
#
# CREATE TABLE IF NOT EXISTS `cvconnect-notify-service`.shedlock (
#     name VARCHAR(64) NOT NULL,
#     lock_until DATETIME NOT NULL,
#     locked_at DATETIME NOT NULL,
#     locked_by VARCHAR(255) NOT NULL,
#     PRIMARY KEY (name)
# );
#
# insert into `cvconnect-notify-service`.email_config
# (host, port, email, password, is_ssl, protocol, org_id, created_by) values
# ('smtp-relay.brevo.com', 587, '784652002@smtp-brevo.com', '', 0, 'smtp', null, 'admin');
#
# insert into `cvconnect-notify-service`.job_config
# (job_name, schedule_type, expression, description, created_by) values
# ('email_resend', 'FIXED_RATE', '900', 'Gửi lại email thất bại', 'admin');

INSERT INTO `cvconnect-notify-service`.placeholders (code, label, description, member_type_used, created_by)
VALUES
('${jobPosition}', 'Vị trí tuyển dụng', NULL, NULL, 'admin'),
('${postTitle}', 'Tiêu đề tin đăng', NULL, NULL, 'admin'),
('${currentRound}', 'Tên vòng hiện tại', NULL, NULL, 'admin'),
('${interviewLink}', 'Đường dẫn phỏng vấn trực tuyến', NULL, NULL, 'admin'),
('${officeName}', 'Tên văn phòng', NULL, NULL, 'admin'),
('${officeAddress}', 'Địa chỉ văn phòng', NULL, NULL, 'admin'),
('${candidateName}', 'Tên ứng viên', NULL, NULL, 'admin'),
('${salutation}', 'Anh/Chị', NULL, NULL, 'admin'),
('${companyName}', 'Tên công ty', NULL, NULL, 'admin'),
('${hrName}', 'Tên HR', NULL, NULL, 'admin'),
('${hrPhone}', 'SĐT HR', NULL, NULL, 'admin'),
('${hrEmail}', 'Email HR', NULL, NULL, 'admin'),
('${examDate}', 'Ngày làm bài thi', NULL, NULL, 'admin'),
('${startTime}', 'Từ giờ', NULL, NULL, 'admin'),
('${endTime}', 'Đến giờ', NULL, NULL, 'admin'),
('${examDuration}', 'Thời lượng của đề thi', NULL, NULL, 'admin');

alter table `cvconnect-notify-service`.email_templates
drop index code;

alter table `cvconnect-notify-service`.email_templates
add index (code, org_id);

alter table `cvconnect-notify-service`.email_logs
modify column template TEXT;

update `cvconnect-notify-service`.placeholders
set code = '${orgName}', label = 'Tên công ty'
where code = '${companyName}';

update `cvconnect-notify-service`.placeholders
set code = '${orgAddress}', label = 'Địa chỉ công ty'
where code = '${officeAddress}';

delete from `cvconnect-notify-service`.placeholders where code = '${officeName}';

INSERT INTO `cvconnect-notify-service`.placeholders (code, label, description, member_type_used, created_by)
VALUES ('${interview-examLocation}', 'Địa điểm phỏng vấn/thi tuyển', NULL, NULL, 'admin');

update `cvconnect-notify-service`.placeholders
set label = 'Ngày làm bài thi/phỏng vấn'
where code = '${examDate}';

INSERT INTO `cvconnect-notify-service`.email_config (id, host, port, email, password, is_ssl, protocol, org_id, is_active, is_deleted, created_at, updated_at, created_by, updated_by)
VALUES (2, 'smtp-relay.brevo.com', 587, '784652002@smtp-brevo.com', '', 0, 'smtp', 4, 1, 0, '2025-08-31 15:04:40', null, 'admin', null);\

alter table `cvconnect-notify-service`.email_templates
modify column body TEXT NOT NULL;
