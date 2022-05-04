DROP DATABASE IF EXISTS final_project;
CREATE DATABASE final_project;

USE final_project;

DROP TABLE IF EXISTS user_info;
DROP TABLE IF EXISTS login_info;
DROP TABLE IF EXISTS response_table;
DROP TABLE IF EXISTS matches_table;

CREATE TABLE `user_info` (
	`user_id` int NOT NULL AUTO_INCREMENT,
  `age` int NOT NULL,
  `full_name` varchar(45) NOT NULL,
  `gender` varchar(45),
  `budget` int NOT NULL,
  `min_roommate_age` int NOT NULL,
  `max_roommate_age` int NOT NULL,
  `housing_style` varchar(45) NOT NULL,
  `biography` varchar(500) NOT NULL,
  `email` varchar(45) NOT NULL,
  PRIMARY KEY (`user_id`)
  
);

CREATE TABLE `login_info` (
	`user_id` int NOT NULL AUTO_INCREMENT,
    `email` varchar(45) NOT NULL,
    `password` varchar(45) NOT NULL,
    PRIMARY KEY (`user_id`)
);

CREATE TABLE `response_table` (
	`user_id` int NOT NULL,
    `other_id` int NOT NULL,
    `choice` boolean NOT NULL    
);

CREATE TABLE `matches_table` (
	`user_id` int NOT NULL,
    `other_id` int NOT NULL
);

ALTER TABLE user_info AUTO_INCREMENT = 1;
ALTER TABLE login_info AUTO_INCREMENT = 1;
ALTER TABLE response_table AUTO_INCREMENT = 1;
ALTER TABLE matches_table AUTO_INCREMENT = 1;