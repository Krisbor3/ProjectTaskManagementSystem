CREATE SCHEMA `ptms` ;

CREATE TABLE `ptms`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `firstname` VARCHAR(45) NULL,
  `lastname` VARCHAR(45) NULL,
  `email` VARCHAR(80) NULL,
  `username` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  `role` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE);

  ALTER TABLE `ptms`.`user`
  RENAME TO  `ptms`.`users` ;

CREATE TABLE `ptms`.`tasks` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `difficulty` VARCHAR(45) NULL,
  `userId` INT NULL,
  `isCompleted` BIT NULL DEFAULT 0,
  PRIMARY KEY (`id`));

ALTER TABLE `ptms`.`users`
ADD COLUMN `mentor/senior` INT NULL AFTER `role`;

CREATE TABLE `ptms`.`senior_tasks` (
  `senior_id` INT NOT NULL,
  `task_id` INT NOT NULL);

ALTER TABLE `ptms`.`users`
DROP COLUMN `tasktocheck`;

ALTER TABLE `ptms`.`senior_tasks`
CHANGE COLUMN `senior_id` `senior_id` INT NOT NULL ,
CHANGE COLUMN `task_id` `task_id` INT NOT NULL ,
ADD PRIMARY KEY (`senior_id`, `task_id`);

ALTER TABLE `ptms`.`tasks`
ADD COLUMN `resultId` INT NULL AFTER `isCompleted`;

CREATE TABLE `ptms`.`result` (
  `id` INT NOT NULL,
  `feedback` VARCHAR(300) NULL,
  `grade` INT(100) NULL,
  PRIMARY KEY (`id`));

ALTER TABLE `ptms`.`result`
CHANGE COLUMN `id` `id` INT NOT NULL AUTO_INCREMENT ;

ALTER TABLE `ptms`.`tasks`
CHANGE COLUMN `isCompleted` `isCompleted` INT(1) NULL DEFAULT 0 ;

ALTER TABLE `ptms`.`tasks`
ADD COLUMN `contentId` INT NULL AFTER `resultId`;

CREATE TABLE `ptms`.`tasks_contents` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `content` VARCHAR(1024) NOT NULL,
  `comment` VARCHAR(60) NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `ptms`.`projects` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(100) NOT NULL,
  `isCompleted` INT NULL DEFAULT 0,
  PRIMARY KEY (`id`));
