CREATE SCHEMA `couriers` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE `couriers`.`addresses` (
  `addresses_id` INT NOT NULL AUTO_INCREMENT,
  `kv` VARCHAR(45) NULL,
  `street` VARCHAR(300) NULL,
  `house_number` INT NULL,
  `apartment_count` INT NULL,
  `postbox_type` VARCHAR(45) NULL,
  `postbox_quality` VARCHAR(45) NULL,
  `house_quality` VARCHAR(45) NULL,
  `level_count` VARCHAR(45) NULL,
  `porch_count` INT NULL,
  `city_rayon` VARCHAR(400) NULL,
  `rayon` VARCHAR(400) NULL,
  `house_year` INT NULL,
  `key` VARCHAR(45) NULL,
  `comment` VARCHAR(500) NULL,
  `last_update` VARCHAR(200) NULL,
  `novostroyka` VARCHAR(45) NULL,
  PRIMARY KEY (`addresses_id`),
  UNIQUE INDEX `addresses_id_UNIQUE` (`addresses_id` ASC));

CREATE TABLE `couriers`.`couriers` (
  `courier_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `app_push_id` VARCHAR(200) NULL,
  PRIMARY KEY (`courier_id`));

CREATE TABLE `couriers`.`task` (
  `task_id` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(400) NULL,
  `period_begin` DATETIME NULL,
  `period_end` DATETIME NULL,
  PRIMARY KEY (`task_id`));

CREATE TABLE `couriers`.`task_addresses` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `task_id` INT NOT NULL,
  `addresses_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `task_id_idx` (`task_id` ASC),
  INDEX `address_id_idx` (`addresses_id` ASC),
  CONSTRAINT `task_id`
    FOREIGN KEY (`task_id`)
    REFERENCES `couriers`.`task` (`task_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `address_id`
    FOREIGN KEY (`addresses_id`)
    REFERENCES `couriers`.`addresses` (`addresses_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

ALTER TABLE `couriers`.`task`
ADD COLUMN `courier_id` INT NULL AFTER `period_end`,
ADD INDEX `task_courier_id_idx` (`courier_id` ASC);
ALTER TABLE `couriers`.`task`
ADD CONSTRAINT `task_courier_id`
  FOREIGN KEY (`courier_id`)
  REFERENCES `couriers`.`couriers` (`courier_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

CREATE TABLE `couriers`.`task_address_result` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `task_adresses_id` INT NULL,
  `result` INT NULL,
  `comment` VARCHAR(400) NULL,
  `porch` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));

ALTER TABLE `couriers`.`task_address_result`
ADD INDEX `task_address_result_task_address_idx` (`task_adresses_id` ASC);
ALTER TABLE `couriers`.`task_address_result`
ADD CONSTRAINT `task_address_result_task_address`
  FOREIGN KEY (`task_adresses_id`)
  REFERENCES `couriers`.`task_addresses` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;


CREATE TABLE `couriers`.`files` (
  `files_id` INT NOT NULL AUTO_INCREMENT,
  `files_data` LONGBLOB NULL,
  PRIMARY KEY (`files_id`));

CREATE TABLE `couriers`.`task_address_result_photo` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `result_id` INT NOT NULL,
  `photo_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `task_address_result_photo_result_idx` (`result_id` ASC),
  INDEX `task_address_result_photo_file_idx` (`photo_id` ASC),
  CONSTRAINT `task_address_result_photo_result`
    FOREIGN KEY (`result_id`)
    REFERENCES `couriers`.`task_address_result` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `task_address_result_photo_file`
    FOREIGN KEY (`photo_id`)
    REFERENCES `couriers`.`files` (`files_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

ALTER TABLE `couriers`.`addresses`
CHANGE COLUMN `house_number` `house_number` VARCHAR(45) NULL DEFAULT NULL ;
ALTER TABLE `couriers`.`addresses`
CHANGE COLUMN `house_year` `house_year` VARCHAR(45) NULL DEFAULT NULL ,
CHANGE COLUMN `key` `keya` VARCHAR(45) NULL DEFAULT NULL ;

ALTER TABLE `couriers`.`task_address_result`
ADD COLUMN `location` VARCHAR(400) NULL AFTER `porch`;

ALTER TABLE `couriers`.`couriers`
ADD COLUMN `description` VARCHAR(500) NULL AFTER `app_push_id`;


ALTER TABLE `couriers`.`task_address_result`
ADD COLUMN `correct_place` TINYINT(1) NULL AFTER `location`;

update couriers.task_address_result set correct_place=0 where id!=0;







