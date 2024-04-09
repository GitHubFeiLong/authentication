SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `base_dict_type`;
CREATE TABLE `base_dict_type`
(
    `id`                 bigint(20)                                            NOT NULL AUTO_INCREMENT,
    `app_id`             bigint(20)                                            NOT NULL COMMENT '应用id',
    `code`               varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '字典类型编码',
    `name`               varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '字典类型名称',
    `template`           json                                                  NOT NULL COMMENT '字典配置模板注释',
    `enabled`            bit(1)                                                NOT NULL DEFAULT b'0' COMMENT '是否激活（true：已激活；false：未激活）',
    `remark`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin         DEFAULT NULL COMMENT '备注',
    `created_date`       datetime                                                       DEFAULT NULL COMMENT '创建时间',
    `last_modified_date` datetime                                                       DEFAULT NULL COMMENT '最后修改时间',
    `created_by`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin         DEFAULT NULL COMMENT '创建人',
    `last_modified_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin         DEFAULT NULL COMMENT '最后修改人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_app_id_code` (`app_id`, `code`) USING BTREE COMMENT '类型编码唯一'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT ='字典类型，对字典进行分类管理';

DROP TABLE IF EXISTS `base_dict`;
CREATE TABLE `base_dict`
(
    `id`                 bigint(20)                                            NOT NULL AUTO_INCREMENT,
    `app_id`             bigint(20)                                            NOT NULL COMMENT '应用id',
    `dict_type_id`       bigint(20)                                            NOT NULL COMMENT '字典类型主键',
    `code`               varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '字典编码,同一应用下编码唯一',
    `name`               varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '字典名',
    `enabled`            bit(1)                                                NOT NULL DEFAULT b'0' COMMENT '是否激活（true：已激活；false：未激活）',
    `remark`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin         DEFAULT NULL COMMENT '备注',
    `created_date`       datetime                                                       DEFAULT NULL COMMENT '创建时间',
    `last_modified_date` datetime                                                       DEFAULT NULL COMMENT '最后修改时间',
    `created_by`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin         DEFAULT NULL COMMENT '创建人',
    `last_modified_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin         DEFAULT NULL COMMENT '最后修改人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_app_id_code` (`app_id`, `code`) USING BTREE COMMENT '同一应用字典唯一索引',
    KEY `dict_type_id` (`dict_type_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT ='字典明细，对字典类型进行细分';

DROP TABLE IF EXISTS `base_dict_setting`;
CREATE TABLE `base_dict_setting`
(
    `id`                 bigint(20)                      NOT NULL AUTO_INCREMENT,
    `app_id`             bigint(20)                      NOT NULL COMMENT '应用id',
    `dict_type_id`       bigint(20)                      NOT NULL COMMENT '字典类型主键',
    `dict_id`            bigint(20)                      NOT NULL COMMENT '字典主键',
    `name`               varchar(16) COLLATE utf8mb4_bin NOT NULL COMMENT '配置名称',
    `template`           json                            NOT NULL COMMENT '配置模板注释',
    `setting`            json                            NOT NULL COMMENT '字典JSON配置',
    `enabled`            bit(1)                          NOT NULL               DEFAULT b'0' COMMENT '是否激活（true：已激活；false：未激活）',
    `defaulted`          bit(1)                          NOT NULL               DEFAULT b'0' COMMENT '是否是默认的（true：默认的；false：非默认的）',
    `remark`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '说明备注',
    `created_date`       datetime                                               DEFAULT NULL COMMENT '创建时间',
    `last_modified_date` datetime                                               DEFAULT NULL COMMENT '最后修改时间',
    `created_by`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人',
    `last_modified_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '最后修改人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `dict_id` (`dict_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT ='字典配置，针对字典进行配置详细信息';
SET FOREIGN_KEY_CHECKS = 1;