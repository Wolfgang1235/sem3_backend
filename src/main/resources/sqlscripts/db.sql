-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema startcode_v2
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema startcode_v2
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `startcode_v2` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `startcode_v2` ;

-- -----------------------------------------------------
-- Table `startcode_v2`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `startcode_v2`.`roles` (
                                                      `id` INT NOT NULL AUTO_INCREMENT,
                                                      `role` VARCHAR(20) NULL DEFAULT NULL,
    PRIMARY KEY (`id`))
    ENGINE = InnoDB
    AUTO_INCREMENT = 3
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `startcode_v2`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `startcode_v2`.`users` (
                                                      `id` INT NOT NULL AUTO_INCREMENT,
                                                      `age` INT NULL DEFAULT NULL,
                                                      `password` VARCHAR(255) NULL DEFAULT NULL,
    `username` VARCHAR(25) NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `username` (`username` ASC) VISIBLE)
    ENGINE = InnoDB
    AUTO_INCREMENT = 4
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `startcode_v2`.`users_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `startcode_v2`.`users_roles` (
                                                            `user_id` INT NOT NULL,
                                                            `role_id` INT NOT NULL,
                                                            PRIMARY KEY (`user_id`, `role_id`),
    INDEX `FK_users_roles_role_id` (`role_id` ASC) VISIBLE,
    CONSTRAINT `FK_users_roles_role_id`
    FOREIGN KEY (`role_id`)
    REFERENCES `startcode_v2`.`roles` (`id`),
    CONSTRAINT `FK_users_roles_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `startcode_v2`.`users` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
