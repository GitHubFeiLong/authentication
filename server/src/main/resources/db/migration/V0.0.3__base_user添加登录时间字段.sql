ALTER TABLE `base_user`
    ADD COLUMN `last_login_time` datetime NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '最近登录时间' AFTER `remark`;

ALTER TABLE `base_app`
    MODIFY COLUMN `home_page` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '登录回调页面，处理登录逻辑' AFTER `name`;