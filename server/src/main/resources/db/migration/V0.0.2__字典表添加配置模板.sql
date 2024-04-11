ALTER TABLE `base_dict`
    ADD COLUMN `template` json NOT NULL COMMENT '字典模板注释' AFTER `name`;