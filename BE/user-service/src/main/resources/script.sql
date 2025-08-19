CREATE TABLE IF NOT EXISTS `cvconnect-user-service`.users (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,

    `username` VARCHAR(100) NOT NULL UNIQUE ,
    `password` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL UNIQUE,
    `full_name` VARCHAR(255) NOT NULL,
    `phone_number` VARCHAR(20),
    `address` VARCHAR(255),
    `date_of_birth` DATE,
    `avatar_url` VARCHAR(255),
    `is_email_verified` TINYINT(1) DEFAULT 0,
    `access_method` VARCHAR(255) NOT NULL,

    `is_active` TINYINT(1) DEFAULT 1,
    `is_deleted` TINYINT(1) DEFAULT 0,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME,
    `created_by` VARCHAR(100),
    `updated_by` VARCHAR(100),

    INDEX idx_username (username),
    INDEX idx_email (email)
    );

CREATE TABLE IF NOT EXISTS `cvconnect-user-service`.roles (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,

    `code` VARCHAR(100) NOT NULL UNIQUE,
    `name` VARCHAR(255) NOT NULL UNIQUE,

    `is_active` TINYINT(1) DEFAULT 1,
    `is_deleted` TINYINT(1) DEFAULT 0,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME,
    `created_by` VARCHAR(100),
    `updated_by` VARCHAR(100)
    );


CREATE TABLE IF NOT EXISTS `cvconnect-user-service`.menus (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,

    `code` VARCHAR(100) NOT NULL UNIQUE,
    `label` VARCHAR(255) NOT NULL UNIQUE,
    `icon` VARCHAR(255),
    `url` VARCHAR(255),
    `parent_id` BIGINT DEFAULT NULL,

    `is_active` TINYINT(1) DEFAULT 1,
    `is_deleted` TINYINT(1) DEFAULT 0,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME,
    `created_by` VARCHAR(100),
    `updated_by` VARCHAR(100)
    );

CREATE TABLE IF NOT EXISTS `cvconnect-user-service`.role_user (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,

    `user_id` BIGINT NOT NULL,
    `role_id` BIGINT NOT NULL,

    `is_active` TINYINT(1) DEFAULT 1,
    `is_deleted` TINYINT(1) DEFAULT 0,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME,
    `created_by` VARCHAR(100),
    `updated_by` VARCHAR(100),
    FOREIGN KEY (role_id) REFERENCES `cvconnect-user-service`.roles(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES `cvconnect-user-service`.users(id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE (role_id, user_id)
    );

CREATE TABLE IF NOT EXISTS `cvconnect-user-service`.role_menu (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,

    `role_id` BIGINT NOT NULL,
    `menu_id` BIGINT NOT NULL,
    `permission` VARCHAR(255) NOT NULL,

    `is_active` TINYINT(1) DEFAULT 1,
    `is_deleted` TINYINT(1) DEFAULT 0,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME,
    `created_by` VARCHAR(100),
    `updated_by` VARCHAR(100),
    FOREIGN KEY (role_id) REFERENCES `cvconnect-user-service`.roles(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (menu_id) REFERENCES `cvconnect-user-service`.menus(id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE (role_id, menu_id)
    );

CREATE TABLE IF NOT EXISTS `cvconnect-user-service`.candidates (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,

    `user_id` BIGINT NOT NULL UNIQUE,

    `is_active` TINYINT(1) DEFAULT 1,
    `is_deleted` TINYINT(1) DEFAULT 0,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME,
    `created_by` VARCHAR(100),
    `updated_by` VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES `cvconnect-user-service`.users(id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS `cvconnect-user-service`.org_members (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,

    `user_id` BIGINT NOT NULL UNIQUE,
    `org_id` BIGINT NOT NULL,

    `is_active` TINYINT(1) DEFAULT 1,
    `is_deleted` TINYINT(1) DEFAULT 0,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME,
    `created_by` VARCHAR(100),
    `updated_by` VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES `cvconnect-user-service`.users(id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS `cvconnect-user-service`.management_members (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,

    `user_id` BIGINT NOT NULL UNIQUE,

    `is_active` TINYINT(1) DEFAULT 1,
    `is_deleted` TINYINT(1) DEFAULT 0,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME,
    `created_by` VARCHAR(100),
    `updated_by` VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES `cvconnect-user-service`.users(id) ON DELETE CASCADE ON UPDATE CASCADE
    );