
-- ----------------------------
-- Table structure for `sys_tenant_quota`
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant_quota`;
CREATE TABLE `sys_tenant_quota` (
  `id` varchar(32) NOT NULL,
  `tenant_id` varchar(32) NOT NULL COMMENT '租户ID',
  `category_type` varchar(32) NOT NULL,
  `sub_category_type` varchar(32) NOT NULL,
  `item_lookup_type` varchar(32) NOT NULL,
  `quota` varchar(8) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `sys_tenant_quota_item`
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant_quota_item`;
CREATE TABLE `sys_tenant_quota_item` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `code` varchar(32) NOT NULL COMMENT '编码和code_index 联合主键',
  `parent_code` varchar(32) DEFAULT NULL COMMENT '父级编码',
  `code_display` varchar(32) DEFAULT NULL COMMENT '索引key',
  `code_desc` varchar(32) DEFAULT NULL COMMENT '索引value',
  `units` varchar(32) DEFAULT NULL COMMENT '单位',
  `default_value` VARCHAR(32) DEFAULT NULL COMMENT '配额默认值',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQ_ID` (`id`) USING BTREE,
  UNIQUE KEY `UNIQ_CODE_INDEX` (`code`,`code_display`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*
Navicat MySQL Data Transfer

Source Server         : OpenBridge
Source Server Version : 50547
Source Host           : 192.168.31.210:3306
Source Database       : paasos

Target Server Type    : MYSQL
Target Server Version : 50547
File Encoding         : 65001

Date: 2016-07-12 19:37:18
*/

-- ----------------------------
-- Table structure for `sys_tenant_preset`
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant_preset`;
CREATE TABLE `sys_tenant_preset` (
  `id` varchar(32) NOT NULL,
  `tenant_id` varchar(32) DEFAULT NULL COMMENT '租户ID',
  `preset_id` varchar(32) DEFAULT NULL COMMENT '预置应用ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

 
INSERT INTO `sys_tenant_quota_item` VALUES ('1', 'deploy', null, '部署', '部署', null, '1');
INSERT INTO `sys_tenant_quota_item` VALUES ('2', 'database', null, '数据库', '数据库', '个', '1');
INSERT INTO `sys_tenant_quota_item` VALUES ('3', 'mq', null, '消息中间件', '消息中间件', '个', '1');
INSERT INTO `sys_tenant_quota_item` VALUES ('4', 'cache', null, '缓存', '缓存', '个', '1');
INSERT INTO `sys_tenant_quota_item` VALUES ('5', 'storage', null, '存储', '存储', 'GB', '1');

INSERT INTO `sys_tenant_quota_item` VALUES ('6', 'docker', 'deploy', 'Docker', 'Docker', '个', '1');
INSERT INTO `sys_tenant_quota_item` VALUES ('8', 'docker_cpu', 'docker', 'CPU', 'CPU', '核', '1');
INSERT INTO `sys_tenant_quota_item` VALUES ('9', 'docker_memory', 'docker', '内存', '内存', 'GB', '1');

INSERT INTO `sys_tenant_quota_item` VALUES ('12', 'mysql', 'database', 'MySql', 'MySql', 'GB', '1');
INSERT INTO `sys_tenant_quota_item` VALUES ('15', 'mysql_count', 'mysql', '个数', '个数', '个', '1');
INSERT INTO `sys_tenant_quota_item` VALUES ('16', 'mysql_memory', 'mysql', '内存', '内存', 'GB', '1');
INSERT INTO `sys_tenant_quota_item` VALUES ('17', 'mysql_volume', 'mysql', '容量', '容量', 'GB', '1');

INSERT INTO `sys_tenant_quota_item` VALUES ('24', 'rabbitmq', 'mq', 'RabbitMQ', 'RabbitMQ', '个数', '1');
INSERT INTO `sys_tenant_quota_item` VALUES ('25', 'rabbitmq_count', 'rabbitmq', '个数', '个数', '个', '1');
INSERT INTO `sys_tenant_quota_item` VALUES ('34', 'rabbitmq_memory', 'rabbitmq', '内存', '内存', 'GB', '1');

INSERT INTO `sys_tenant_quota_item` VALUES ('26', 'redis', 'cache', 'Redis', 'Redis', '个', '1');
INSERT INTO `sys_tenant_quota_item` VALUES ('27', 'redis_count', 'redis', '个数', '个数', '个', '1');
INSERT INTO `sys_tenant_quota_item` VALUES ('28', 'redis_memory', 'redis', '内存', '内存', 'GB', '1');

INSERT INTO `sys_tenant_quota_item` VALUES ('29', 'nas', 'storage', 'Nas', 'Nas', '个', '1');
INSERT INTO `sys_tenant_quota_item` VALUES ('30', 'nas_count', 'nas', '个数', '个数', '个', '1');
INSERT INTO `sys_tenant_quota_item` VALUES ('31', 'nas_volume', 'nas', '容量', '容量', 'GB', '1');