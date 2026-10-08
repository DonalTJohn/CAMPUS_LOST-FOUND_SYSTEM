-- =========================================================
-- Campus Lost & Found Portal Database Script
-- Database: lost_found_db
-- Compatible with MySQL 8.x / 9.x
-- =========================================================

CREATE DATABASE IF NOT EXISTS `lost_found_db` 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE `lost_found_db`;

-- Drop tables in reverse order of foreign keys if re-creating
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `notifications`;
DROP TABLE IF EXISTS `claim_verifications`;
DROP TABLE IF EXISTS `claims`;
DROP TABLE IF EXISTS `items`;
DROP TABLE IF EXISTS `categories`;
DROP TABLE IF EXISTS `users`;
SET FOREIGN_KEY_CHECKS = 1;

-- 1. Users Table
CREATE TABLE `users` (
    `user_id` INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `email` VARCHAR(100) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `phone` VARCHAR(15) NOT NULL,
    `role` ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
    `status` ENUM('ACTIVE', 'BLOCKED') NOT NULL DEFAULT 'ACTIVE',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 2. Categories Table
CREATE TABLE `categories` (
    `category_id` INT PRIMARY KEY AUTO_INCREMENT,
    `category_name` VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- 3. Items Table
CREATE TABLE `items` (
    `item_id` INT PRIMARY KEY AUTO_INCREMENT,
    `user_id` INT NOT NULL,
    `category_id` INT NOT NULL,
    `item_name` VARCHAR(100) NOT NULL,
    `item_type` ENUM('LOST', 'FOUND') NOT NULL,
    `description` VARCHAR(500) NOT NULL,
    `location` VARCHAR(150) NOT NULL,
    `incident_date` DATE NOT NULL,
    `image_path` VARCHAR(255) DEFAULT NULL,
    `status` ENUM('PENDING', 'ACTIVE', 'CLAIMED', 'RECOVERED', 'REJECTED') NOT NULL DEFAULT 'ACTIVE',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_items_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_items_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 4. Claims Table
CREATE TABLE `claims` (
    `claim_id` INT PRIMARY KEY AUTO_INCREMENT,
    `item_id` INT NOT NULL,
    `claimant_id` INT NOT NULL,
    `proof_description` VARCHAR(500) NOT NULL,
    `claim_status` ENUM('PENDING', 'APPROVED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    `claim_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_claims_item` FOREIGN KEY (`item_id`) REFERENCES `items` (`item_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_claims_claimant` FOREIGN KEY (`claimant_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 5. Claim Verifications Table
CREATE TABLE `claim_verifications` (
    `verification_id` INT PRIMARY KEY AUTO_INCREMENT,
    `claim_id` INT NOT NULL,
    `admin_id` INT NOT NULL,
    `remarks` VARCHAR(500) NOT NULL,
    `decision` ENUM('APPROVED', 'REJECTED') NOT NULL,
    `verified_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_verifications_claim` FOREIGN KEY (`claim_id`) REFERENCES `claims` (`claim_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_verifications_admin` FOREIGN KEY (`admin_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 6. Notifications Table
CREATE TABLE `notifications` (
    `notification_id` INT PRIMARY KEY AUTO_INCREMENT,
    `user_id` INT NOT NULL,
    `message` VARCHAR(500) NOT NULL,
    `is_read` BOOLEAN NOT NULL DEFAULT FALSE,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_notifications_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- =========================================================
-- Initial Seed Data
-- =========================================================

-- Seed Categories
INSERT INTO `categories` (`category_id`, `category_name`) VALUES
(1, 'Electronics & Gadgets'),
(2, 'Wallets & Purses'),
(3, 'Keys & Keychains'),
(4, 'ID Cards & Documents'),
(5, 'Books & Stationery'),
(6, 'Clothing & Accessories'),
(7, 'Bottles & Containers'),
(8, 'Other Items');

-- Seed Users (Admin and standard users)
-- Password for admin: admin123
-- Password for student: student123
INSERT INTO `users` (`user_id`, `name`, `username`, `email`, `password`, `phone`, `role`, `status`) VALUES
(1, 'Campus Administrator', 'admin', 'admin@campus.edu', 'admin123', '9876543210', 'ADMIN', 'ACTIVE'),
(2, 'John Doe', 'johndoe', 'john.doe@campus.edu', 'student123', '9876543211', 'USER', 'ACTIVE'),
(3, 'Sarah Connor', 'sarahc', 'sarah.c@campus.edu', 'student123', '9876543212', 'USER', 'ACTIVE');

-- Seed Sample Items
INSERT INTO `items` (`item_id`, `user_id`, `category_id`, `item_name`, `item_type`, `description`, `location`, `incident_date`, `image_path`, `status`) VALUES
(1, 2, 1, 'Dell XPS 13 Laptop Charger', 'LOST', 'Black 65W USB-C charger left on desk 4B in Central Library second floor.', 'Central Library 2nd Floor', CURDATE() - INTERVAL 2 DAY, NULL, 'ACTIVE'),
(2, 3, 2, 'Brown Leather Fossil Wallet', 'FOUND', 'Contains a student ID card and bus pass. Found near Cafeteria billing counter.', 'Campus Cafeteria Counter', CURDATE() - INTERVAL 1 DAY, NULL, 'ACTIVE'),
(3, 2, 3, 'Set of 3 Keys with Batman Keychain', 'LOST', 'Lost keys near the Engineering Block entrance or parking lot A.', 'Engineering Block Parking', CURDATE() - INTERVAL 3 DAY, NULL, 'ACTIVE'),
(4, 3, 4, 'College ID Card - Computer Science', 'FOUND', 'CS Department ID card found on bench outside Lab 304.', 'Science Block Lab 304', CURDATE(), NULL, 'ACTIVE');

-- Seed Sample Notification
INSERT INTO `notifications` (`user_id`, `message`, `is_read`) VALUES
(2, 'Welcome to the Campus Lost & Found Portal! Report any lost or found items here.', FALSE),
(3, 'Welcome to the Campus Lost & Found Portal! Report any lost or found items here.', FALSE);
