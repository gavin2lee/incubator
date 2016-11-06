
ALTER TABLE `paasos`.`os_project_deploy_env` 
CHANGE COLUMN `resource_id` `resource_id` VARCHAR(120) NULL DEFAULT NULL ;

alter table os_project modify project_code varchar(55);