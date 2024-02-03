/*
 Navicat Premium Data Transfer

 Source Server         : localhost5.7
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : localhost:3306
 Source Schema         : authentication-server

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 20/01/2024 09:20:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for base_app
-- ----------------------------
DROP TABLE IF EXISTS `base_app`;
CREATE TABLE `base_app`
(
    `id`                 bigint(20)                                              NOT NULL AUTO_INCREMENT,
    `secret`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '应用密钥',
    `name`               varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '应用名称',
    `home_page`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '首页',
    `enabled`            bit(1)                                                  NOT NULL DEFAULT b'0' COMMENT '是否激活',
    `remark`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '备注',
    `created_date`       datetime(0)                                             NULL     DEFAULT NULL COMMENT '创建时间',
    `last_modified_date` datetime(0)                                             NULL     DEFAULT NULL COMMENT '最后修改时间',
    `created_by`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '创建人',
    `last_modified_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '最后修改人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_app_name` (`name`) USING BTREE COMMENT '应用名称唯一'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1748326743003348993
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '应用表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_app
-- ----------------------------
INSERT INTO `base_app`
VALUES (1, '0b32758851f04e92bc0f874a4f82c4c2', 'admin', 'http://127.0.0.1:9999/#/login-success', b'1',
        '认证服务应用', '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');

-- ----------------------------
-- Table structure for base_app_cert
-- ----------------------------
DROP TABLE IF EXISTS `base_app_cert`;
CREATE TABLE `base_app_cert`
(
    `id`                 bigint(20)                                               NOT NULL,
    `app_id`             bigint(20)                                               NOT NULL COMMENT '应用id',
    `serial_number`      varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NOT NULL COMMENT '证书序号',
    `cert`               varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '证书',
    `private_key`        varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '私钥',
    `public_key`         varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '公钥',
    `valid_time`         datetime(0)                                              NOT NULL COMMENT '有效期截止时间',
    `remark`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '备注',
    `created_date`       datetime(0)                                              NULL DEFAULT NULL COMMENT '创建时间',
    `last_modified_date` datetime(0)                                              NULL DEFAULT NULL COMMENT '修改时间',
    `created_by`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '创建人',
    `last_modified_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_app_id_serial_number` (`app_id`, `serial_number`) USING BTREE COMMENT '应用证书唯一',
    CONSTRAINT `fx_app_id` FOREIGN KEY (`app_id`) REFERENCES `base_app` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '应用证书密钥信息'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_app_cert
-- ----------------------------
INSERT INTO `base_app_cert`
VALUES (1, 1, '90bd8a5d2dd906e8',
        'MIICpTCCAY2gAwIBAgIJAJC9il0t2QboMA0GCSqGSIb3DQEBCwUAMBIxEDAOBgNVBAMTB2dvdWRvbmcwIBcNMjQwMTIwMDExOTI5WhgPOTk5OTEyMzExNTU5NTlaMBAxDjAMBgNVBAMTBWFkbWluMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA37VMn30LwiZuzX/R/sYgiVHmUuGXpi2DZk3IV5eY/kP/qGgH4Z3Jdlb4Mt5GD1eeZbCvJVL0+RT0p1R9xDmMn3kjJWJQVtaUQmpa33YiHpXEuKtFk1S6HVpuc4b37z7GQIwXBFtRFftlleafgFw7UKOGcpXXmVYVSD/Oo2zF2f6up17t//t3Qr3CQal9jbmPklZr5UJN2qV3FSxf84xRmvlYgZ34kAsPRg1E01E6zkJF/FF4vddddGZ69L69+ZVa1nDpKcZg9y9qvGau0zsrsDoz4TcZeBsDV1+JVQLCKGmva08HZzuEfpGgg3+jewgSMX4rVnf+YsNa5e0f3yaJhQIDAQABMA0GCSqGSIb3DQEBCwUAA4IBAQCXJKr/icO7qgEWCIx9twAnqSl1DJbVLeNFkjex3f1RBjvks0C3ma7J3SQqNOm0wwftFCUKYsuChrnUuwpQ1nOEA9aVZHKuVuMti7oHja64CgfoP2F/HNmqwTYIytoRHQb4bkrCoDQrVeN4YkU035YavrcSvTaWg2z4bo8RPpUBjvpYI1fa0fdOo5mcBsRpN3mUFKUEAgd66Q19TjR3U73hrfgS8njuJF4K50UUnO2qDWBDLbY40BIf1tWaXVSFFAmYtW5zz8rxLOvuoGWuGHvBEFWSDqO9GnsiRKIbqx3ZkTpqVqRGqxQOAmsMQ2iLu4M7hMDjlK395GyC5OG/INfy',
        'MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDftUyffQvCJm7Nf9H+xiCJUeZS4ZemLYNmTchXl5j+Q/+oaAfhncl2Vvgy3kYPV55lsK8lUvT5FPSnVH3EOYyfeSMlYlBW1pRCalrfdiIelcS4q0WTVLodWm5zhvfvPsZAjBcEW1EV+2WV5p+AXDtQo4ZyldeZVhVIP86jbMXZ/q6nXu3/+3dCvcJBqX2NuY+SVmvlQk3apXcVLF/zjFGa+ViBnfiQCw9GDUTTUTrOQkX8UXi91110Znr0vr35lVrWcOkpxmD3L2q8Zq7TOyuwOjPhNxl4GwNXX4lVAsIoaa9rTwdnO4R+kaCDf6N7CBIxfitWd/5iw1rl7R/fJomFAgMBAAECggEAH+i4WTd46eSZgW5uAa05Dt1DwSa0ftuXFoyZuwTAqeQBH4BXP3NGPT8CgB78V0vkKBUfpm9CyhSXIRcIKWkcCdM5lCeF1h0dYKvS8nTjQQGT4dNsRSucsHsprgjebJ3DoYlZ7yEnW79UKiGPNrvxKftpPgbpjdGdj4SqcN0fC4zoBzmVSf7h2MbdJuG7JjQ+B19m12GuSzLkfobGU81Wbcq5rwQwUUORH/PZlGo2EKqbpLQ+Gd5Kvugm5gUAbJFEoHJGY/W97u7tDLebiGw1ethLrUEBx/zIIzo2xVnV/YodQaLUeGDmRpAG9twxIc2Bdyg4OljxDn+SBFxATwWZAQKBgQD20oV4JDvbLWPrX2dFy5V/Og9iPWu/QmbEaUJqybr0Yd3iuSNZiNV0BTBlbMvbxzWytbSx58/3Ya8oYEUJGo4cwgnD/wm7r/YUpl9vsx3o1q6WzShEXKLKcXj9F6H65avgsNe/WMV0lqRih6IoVq5E4uxsJDh2DYv6iO0nNWiI0QKBgQDoBsGs+DmI0DXbU8ZhKsUxp/wo6JAvYOTFsWk45j1PxfJlaeSRE5dArNg63GTg+aqi2YL1HVR/5GjzVBE+9fxdeKn/bFlZPMs/M5YxNnfM5hgWTJAeXR/tiGWP3dc2ImxzYIeLqmlkYLBLfO3NkpZmGZ1EACfHObQ9h+DX0gBidQKBgQDfN1KXbYVoWWtk4PnfqBcCYXqqKMv5w053UI5NTzUxcBSfxzlN7q7D5zF2eGcA0u8gOK46ul9c5U3oIPAuJILjRSRZyZt+zsqosDHdLcrd2NE59j31XSWoGekGUsodwnmnkab8GIcyQk5WAT3IZ2PhTqvrvPIG6YdvQ0feTrHEEQKBgERECKOtfLAGInJfWZSpEs+jTwtyMUnfRi/sHLtC8nMQGFtCJp+QlgXPVOcshPm0O0IioOchNAVU3JQK/e/6S7/sioe+ByO8QUhdEylzjxfX8upMqHJmj8hBQzBq4rDou+POWNL81nlUAUqUfRrosBtmGeEx8oIpNiCMY9jbzoUpAoGACBCN3VnqB33vN0LldGGUqqApJGpH89rd5wycp4dPLuCz2XuJbmlrGZ57NyfeMWslfa6w7pJW9VsvrBZtLLX4nMcUTrjb6zcV4h/59dPEvL2N2NT2ho6VE5P0XfZ61gvlrASbdW1wa9xRWNKxCljj53l1qwtJ3VpWd05A4VzENTY=',
        'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA37VMn30LwiZuzX/R/sYgiVHmUuGXpi2DZk3IV5eY/kP/qGgH4Z3Jdlb4Mt5GD1eeZbCvJVL0+RT0p1R9xDmMn3kjJWJQVtaUQmpa33YiHpXEuKtFk1S6HVpuc4b37z7GQIwXBFtRFftlleafgFw7UKOGcpXXmVYVSD/Oo2zF2f6up17t//t3Qr3CQal9jbmPklZr5UJN2qV3FSxf84xRmvlYgZ34kAsPRg1E01E6zkJF/FF4vddddGZ69L69+ZVa1nDpKcZg9y9qvGau0zsrsDoz4TcZeBsDV1+JVQLCKGmva08HZzuEfpGgg3+jewgSMX4rVnf+YsNa5e0f3yaJhQIDAQAB',
        '9999-12-31 23:59:59', NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');

-- ----------------------------
-- Table structure for base_menu
-- ----------------------------
DROP TABLE IF EXISTS `base_menu`;
CREATE TABLE `base_menu`
(
    `id`                 bigint(20)                                             NOT NULL AUTO_INCREMENT,
    `app_id`             bigint(20)                                             NOT NULL COMMENT '应用id',
    `parent_id`          bigint(20)                                             NULL     DEFAULT NULL COMMENT '父级主键id',
    `permission_id`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '权限标识',
    `name`               varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '菜单名称',
    `type`               int(11)                                                NOT NULL COMMENT '菜单类型（1：菜单；2：按钮；3：接口）',
    `path`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '路由或接口地址',
    `method`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '请求方式',
    `sort_num`           int(11)                                                NOT NULL DEFAULT 0 COMMENT '排序字段（值越小越靠前，仅仅针对前端路由）',
    `hide`               bit(1)                                                 NOT NULL COMMENT '是否是隐藏菜单',
    `meta`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '前端菜单元数据',
    `remark`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '备注',
    `created_date`       datetime(0)                                            NULL     DEFAULT NULL COMMENT '创建时间',
    `last_modified_date` datetime(0)                                            NULL     DEFAULT NULL COMMENT '最后修改时间',
    `created_by`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '创建人',
    `last_modified_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL     DEFAULT NULL COMMENT '最后修改人',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1676562074974236673
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '菜单表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_menu
-- ----------------------------
INSERT INTO `base_menu` VALUES (1000, 1, NULL, 'sys', '系统管理', 1, '/user', NULL, 1, b'0', '{\"icon\": \"el-icon-s-tools\", \"title\": \"系统管理\"}', NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1100, 1, 1000, 'sys:user', '用户管理', 1, '/user/index', NULL, 7, b'0', '{\"icon\": \"peoples\", \"title\": \"用户管理\"}', NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1101, 1, 1100, 'sys:user:query', '查询用户', 2, '/user/page/base-users', '[\"POST\"]', 0, b'0', '{}', NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1102, 1, 1100, 'sys:user:add', '新增用户', 2, '/user/base-user/simple-create', '[\"POST\"]', 4, b'0', NULL, NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1103, 1, 1100, 'sys:user:edit', '编辑用户', 2, '/user/base-user/simple-update', '[\"PUT\"]', 0, b'0', '{}', NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1104, 1, 1100, 'sys:user:delete', '删除用户', 2, '/user/base-users', '[\"DELETE\"]', 0, b'0', '{}', NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1105, 1, 1100, 'sys:user:reset-password', '重置密码', 2, '/user/base-user/reset-password/*', '[\"PUT\"]', 7, b'0', NULL, NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1106, 1, 1100, 'sys:user:enable', '激活用户', 2, '/user/base-user/change-enabled/*', '[\"PUT\"]', 7, b'0', NULL, NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1107, 1, 1100, 'sys:user:lock', '锁定用户', 2, '/user/base-user/change-locked/*', '[\"PUT\"]', 8, b'0', NULL, NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1108, 1, 1100, 'sys:user:import', '导入用户', 2, '/import-export/import-user', '[\"POST\"]', 8, b'0', NULL, NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1109, 1, 1100, 'sys:user:export', '导出用户', 2, '/import-export/export-user', '[\"POST\"]', 8, b'0', NULL, NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1200, 1, 1000, 'sys:role', '角色管理', 1, '/role/index', NULL, 8, b'0', '{\"icon\": \"iconfont-jueseguanli\", \"title\": \"角色管理\"}', NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1201, 1, 1200, 'sys:role:query', '查询角色', 2, '/role/page/base-roles', '[\"POST\"]', 9, b'0', NULL, NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1202, 1, 1200, 'sys:role:add', '新增角色', 2, '/role/base-role', '[\"POST\"]', 10, b'0', NULL, NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1203, 1, 1200, 'sys:role:edit', '编辑角色', 2, '/role/base-role', '[\"PUT\"]', 11, b'0', NULL, NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1204, 1, 1200, 'sys:role:delete', '删除角色', 2, '/role/base-roles', '[\"DELETE\"]', 12, b'0', NULL, NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1205, 1, 1200, 'sys:role:permission:query', '查询角色权限', 2, '/role/base-role/permission-list/*', '[\"GET\"]', 13, b'0', NULL, NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1206, 1, 1200, 'sys:role:permission:edit', '分配角色权限', 2, '/role/base-role/permission-list', '[\"POST\"]', 13, b'0', NULL, NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1207, 1, 1200, 'sys:role:import', '导入角色', 2, '/import-export/import-role', '[\"POST\"]', 23, b'0', NULL, NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1208, 1, 1200, 'sys:role:export', '导出角色', 2, '/import-export/export-role', '[\"POST\"]', 21, b'0', NULL, NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1300, 1, 1000, 'sys:menu', '菜单管理', 1, '/menu/index', NULL, 14, b'0', '{\"icon\": \"iconfont-caidanguanli\", \"title\": \"菜单管理\"}', NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1301, 1, 1300, 'sys:menu:query', '查询菜单', 2, '/menu/base-menus', '[\"POST\"]', 15, b'0', NULL, NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1304, 1, 1300, 'sys:menu:add', '新增菜单', 2, '/menu/base-menu', '[\"POST\"]', 19, b'0', NULL, NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1305, 1, 1300, 'sys:menu:edit', '编辑菜单', 2, '/menu/base-menu', '[\"PUT\"]', 20, b'0', NULL, NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1306, 1, 1300, 'sys:menu:delete', '删除菜单', 2, '/menu/base-menu/*', '[\"DELETE\"]', 21, b'0', NULL, NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1307, 1, 1300, 'sys:menu:sort', '排序菜单', 2, '/menu/base-menu/sort-num', '[\"PUT\"]', 21, b'0', NULL, NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1308, 1, 1300, 'sys:menu:import', '导入菜单', 2, '/import-export/import-menu', '[\"POST\"]', 22, b'0', NULL, NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1309, 1, 1300, 'sys:menu:export', '导出菜单', 2, '/import-export/export-menu', '[\"POST\"]', 24, b'0', NULL, NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1400, 1, 1000, 'sys:app', '应用管理', 1, '/app/index', NULL, 16, b'0', '{\"icon\": \"iconfont-yingyongguanli\", \"title\": \"应用管理\"}', NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1401, 1, 1400, 'sys:app:add', '新增应用', 2, '/app/base-app', '[\"POST\"]', 19, b'0', NULL, NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1402, 1, 1400, 'sys:app:query', '查询应用', 2, '/app/page/base-apps', '[\"POST\"]', 18, b'0', NULL, NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1403, 1, 1400, 'sys:app:edit', '修改应用', 2, '/app/base-app', '[\"PUT\"]', 20, b'0', NULL, NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1404, 1, 1400, 'sys:app:delete', '删除应用', 2, '/app/base-app/*', '[\"DELETE\"]', 21, b'0', NULL, NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1405, 1, 1400, 'sys:app:cert:add', '新增证书', 2, '/app/base-app-cert', '[\"POST\"]', 21, b'0', NULL, NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1406, 1, 1400, 'sys:app:cert:query', '应用证书列表', 2, '/app/base-app-certs/*', '[\"GET\"]', 21, b'0', NULL, NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1407, 1, 1400, 'sys:app:import', '导入应用', 2, '/import-export/import-app', '[\"POST\"]', 22, b'0', NULL, NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');
INSERT INTO `base_menu` VALUES (1408, 1, 1400, 'sys:app:export', '导出应用', 2, '/import-export/export-app', '[\"POST\"]', 22, b'0', NULL, NULL, '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');

-- ----------------------------
-- Table structure for base_role
-- ----------------------------
DROP TABLE IF EXISTS `base_role`;
CREATE TABLE `base_role`
(
    `id`                 bigint(20)                                              NOT NULL AUTO_INCREMENT,
    `app_id`             bigint(20)                                              NOT NULL COMMENT '应用id',
    `name`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '角色名称',
    `remark`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
    `created_date`       datetime(0)                                             NULL DEFAULT NULL COMMENT '创建时间',
    `last_modified_date` datetime(0)                                             NULL DEFAULT NULL COMMENT '最后修改时间',
    `created_by`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
    `last_modified_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '最后修改人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_base_role_app_id_name` (`app_id`, `name`) USING BTREE COMMENT '角色应用下唯一'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1748326745427656705
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '角色表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_role
-- ----------------------------
INSERT INTO `base_role`
VALUES (1, 1, 'ROLE_APP_SUPER_ADMIN', '所有应用的超级管理员', '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin',
        'admin');
INSERT INTO `base_role`
VALUES (2, 1, 'ROLE_APP_ADMIN', '应用管理员', '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');

-- ----------------------------
-- Table structure for base_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `base_role_menu`;
CREATE TABLE `base_role_menu`
(
    `id`                 bigint(20)                                              NOT NULL AUTO_INCREMENT,
    `role_id`            bigint(20)                                              NULL DEFAULT NULL,
    `menu_id`            bigint(20)                                              NULL DEFAULT NULL,
    `created_date`       datetime(6)                                             NULL DEFAULT NULL,
    `last_modified_date` datetime(6)                                             NULL DEFAULT NULL,
    `created_by`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
    `last_modified_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `fk_base_role_menu_role_id` (`role_id`) USING BTREE,
    INDEX `fk_base_role_menu_menu_id` (`menu_id`) USING BTREE,
    CONSTRAINT `fk_base_role_menu_menu_id` FOREIGN KEY (`menu_id`) REFERENCES `base_menu` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
    CONSTRAINT `fk_base_role_menu_role_id` FOREIGN KEY (`role_id`) REFERENCES `base_role` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 72
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '菜单角色中间表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for base_user
-- ----------------------------
DROP TABLE IF EXISTS `base_user`;
CREATE TABLE `base_user`
(
    `id`                 bigint(20)                                              NOT NULL AUTO_INCREMENT,
    `app_id`             bigint(20)                                              NOT NULL COMMENT '应用id',
    `real_app_id`        bigint(20)                                              NOT NULL COMMENT '真实应用id（例如xx应用管理员，app_id是认证服务应用的app_id，但是real_app_id是自己所管理xx应用的app_id）',
    `username`           varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '用户名',
    `password`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '密码',
    `valid_time`         datetime(6)                                             NOT NULL COMMENT '过期时间，不填永久有效',
    `enabled`            bit(1)                                                  NOT NULL COMMENT '激活状态：true 激活；false 未激活',
    `locked`             bit(1)                                                  NOT NULL COMMENT '锁定状态：true 锁定；false 未锁定',
    `remark`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
    `created_date`       datetime(0)                                             NULL DEFAULT NULL COMMENT '创建时间',
    `last_modified_date` datetime(0)                                             NULL DEFAULT NULL COMMENT '最后修改时间',
    `created_by`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
    `last_modified_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '最后修改人',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_app_id_username` (`app_id`, `username`) USING BTREE COMMENT '用户名应用下唯一'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1747545560254488577
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '基础用户'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_user
-- ----------------------------
INSERT INTO `base_user`
VALUES (1, 1, 1, 'admin', '$2a$10$G6ZOnXyHRuHM2eukWyrW6.sMMrtNZDl4URljrWR23EhvMOXY6JTWq', '9999-12-31 23:59:59.000000',
        b'1', b'0', '认证服务应用管理员', '1970-01-01 00:00:00', '1970-01-01 00:00:00', 'admin', 'admin');

-- ----------------------------
-- Table structure for base_user_role
-- ----------------------------
DROP TABLE IF EXISTS `base_user_role`;
CREATE TABLE `base_user_role`
(
    `id`                 bigint(20)                                              NOT NULL AUTO_INCREMENT,
    `user_id`            bigint(20)                                              NULL DEFAULT NULL,
    `role_id`            bigint(20)                                              NULL DEFAULT NULL,
    `created_date`       datetime(6)                                             NULL DEFAULT NULL,
    `last_modified_date` datetime(6)                                             NULL DEFAULT NULL,
    `created_by`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
    `last_modified_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `fk_base_user_role_user_id` (`user_id`) USING BTREE,
    INDEX `fk_base_user_role_role_id` (`role_id`) USING BTREE,
    CONSTRAINT `fk_base_user_role_role_id` FOREIGN KEY (`role_id`) REFERENCES `base_role` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
    CONSTRAINT `fk_base_user_role_user_id` FOREIGN KEY (`user_id`) REFERENCES `base_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 24
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '用户角色中间表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_user_role
-- ----------------------------
INSERT INTO `base_user_role`
VALUES (1, 1, 1, '1970-01-01 00:00:00.000000', '1970-01-01 00:00:00.000000', 'admin', 'admin');

SET FOREIGN_KEY_CHECKS = 1;
