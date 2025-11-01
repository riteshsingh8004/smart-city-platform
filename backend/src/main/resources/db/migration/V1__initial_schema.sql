-- ======================================================
-- V1__initial_schema.sql
-- Smart City Platform - Initial Schema + Default Admin
-- ======================================================

-- ✅ Database auto-created by Spring URL, so no CREATE DATABASE needed

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('CITIZEN','OFFICER','ADMIN') NOT NULL DEFAULT 'CITIZEN',
    phone VARCHAR(15),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    status ENUM('ACTIVE','INACTIVE') DEFAULT 'ACTIVE'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS issue_category (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS issue_reports (
    report_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    category_id INT,
    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),
    image_url VARCHAR(255),
    status ENUM('OPEN','ASSIGNED','RESOLVED','REJECTED') DEFAULT 'OPEN',
    assigned_to BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_issue_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_issue_category FOREIGN KEY (category_id) REFERENCES issue_category(category_id),
    CONSTRAINT fk_issue_assigned FOREIGN KEY (assigned_to) REFERENCES users(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS comments (
    comment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    report_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    comment_text TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_comment_report FOREIGN KEY (report_id) REFERENCES issue_reports(report_id),
    CONSTRAINT fk_comment_user FOREIGN KEY (user_id) REFERENCES users(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS notifications (
    notification_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notification_user FOREIGN KEY (user_id) REFERENCES users(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS audit_logs (
    log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    entity_type VARCHAR(100),
    entity_id BIGINT,
    action VARCHAR(50),
    performed_by BIGINT,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_audit_user FOREIGN KEY (performed_by) REFERENCES users(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Default Admin User (password: admin123)
INSERT INTO users (name, email, password_hash, role, phone, status)
SELECT 'System Admin', 'admin@smartcity.gov.in',
       '$2a$10$7sK/jeQUa8iEPMFzFYZxheJpMjYxv.NqVgOPMxMuUZyEw5T8zK6fO',
       'ADMIN', '9999999999', 'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@smartcity.gov.in');

-- Default Issue Categories
INSERT INTO issue_category (category_name)
SELECT 'Garbage' WHERE NOT EXISTS (SELECT 1 FROM issue_category WHERE category_name = 'Garbage');
INSERT INTO issue_category (category_name)
SELECT 'Water Supply' WHERE NOT EXISTS (SELECT 1 FROM issue_category WHERE category_name = 'Water Supply');
INSERT INTO issue_category (category_name)
SELECT 'Road Maintenance' WHERE NOT EXISTS (SELECT 1 FROM issue_category WHERE category_name = 'Road Maintenance');
INSERT INTO issue_category (category_name)
SELECT 'Street Light' WHERE NOT EXISTS (SELECT 1 FROM issue_category WHERE category_name = 'Street Light');
