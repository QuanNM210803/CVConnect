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

alter table `cvconnect-user-service`.role_menu
modify column permission varchar(255) null;

select * from `cvconnect-user-service`.users;
select * from `cvconnect-user-service`.roles;
select * from `cvconnect-user-service`.role_user;
select * from `cvconnect-user-service`.management_members;
select * from `cvconnect-user-service`.menus;
select * from `cvconnect-user-service`.role_menu;

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

insert into `cvconnect-user-service`.menus(id, code, label, icon, url, parent_id, sort_order, created_by) values
 (1, 'REPORT', 'Báo cáo', 'material-symbols:dashboard-2-outline', null, null, 1, 'admin')
, (2, 'ORG', 'Tổ chức', 'material-symbols:person-play', '/organizations', null, 2, 'admin')
, (3, 'USER', 'Người dùng', 'material-symbols:account-circle-full', '/users', null, 3, 'admin')
,(4, 'SETUP', 'Thiết lập', 'material-symbols:settings-b-roll-outline', null, null, 4, 'admin')
,(5, 'USER_GROUP', 'Nhóm người dùng', null, '/user-group', 4, 1, 'admin')
, (6, 'CV_TEMPLATE', 'Mẫu CV', null, '/cv-templates', 4, 2, 'admin')
;

insert into `cvconnect-user-service`.role_menu(role_id, menu_id, permission, created_by) values
(1, 1, null, 'admin')
, (1, 2, 'VIEW,ADD,UPDATE,DELETE,EXPORT', 'admin')
, (1, 3, 'VIEW,ADD,UPDATE,DELETE,EXPORT', 'admin')
, (1, 4, null, 'admin')
, (1, 5, 'VIEW,ADD,UPDATE,DELETE,EXPORT', 'admin')
, (1, 6, 'VIEW,ADD,UPDATE,DELETE,EXPORT', 'admin')