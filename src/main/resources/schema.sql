SET MODE MYSQL;
CREATE SCHEMA IF NOT EXISTS spring;
SET SCHEMA spring;

CREATE TABLE IF NOT EXISTS `spring`.`user` (
  `username` VARCHAR(45) NOT NULL,
  `password` TEXT NULL,
  PRIMARY KEY (`username`));

CREATE TABLE IF NOT EXISTS `spring`.`otp` (
  `username` VARCHAR(45) NOT NULL,
  `code` VARCHAR(45) NULL,
  PRIMARY KEY (`username`));

