-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: 192.168.10.82    Database: openbridge
-- ------------------------------------------------------
-- Server version	5.5.47-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE DATABASE openbridge
  DEFAULT CHARACTER SET utf8
  DEFAULT COLLATE utf8_general_ci;
USE openbridge;
SET NAMES utf8;

--
-- Table structure for table `mod_app`
--

DROP TABLE IF EXISTS `mod_app`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_app` (
  `app_id` varchar(32) NOT NULL,
  `app_name` varchar(128) NOT NULL,
  `app_desc` varchar(1024) DEFAULT NULL,
  `app_type` varchar(10) NOT NULL,
  `app_logo` varchar(45) DEFAULT NULL,
  `app_leader` varchar(32) NOT NULL,
  `creator` varchar(32) NOT NULL,
  `creation_time` datetime NOT NULL,
  `category_id` varchar(32) DEFAULT NULL,
  `app_information` varchar(2000) DEFAULT NULL,
  `secure_key` varchar(32) DEFAULT NULL COMMENT '动态token',
  `project_code` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`app_id`),
  UNIQUE KEY `unique_project_code` (`project_code`),
  KEY `i_category_id` (`category_id`) USING BTREE,
  CONSTRAINT `mod_app_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `mod_app_category` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_app_category`
--

DROP TABLE IF EXISTS `mod_app_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_app_category` (
  `category_id` varchar(32) NOT NULL,
  `category_name` varchar(100) DEFAULT NULL,
  `c_order` int(4) DEFAULT NULL,
  `hierarchy_id` varchar(300) DEFAULT NULL,
  `parent_id` varchar(32) DEFAULT NULL,
  `service_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`category_id`),
  KEY `parent_id` (`parent_id`) USING BTREE,
  CONSTRAINT `mod_app_category_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `mod_app_category` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_app_comment`
--

DROP TABLE IF EXISTS `mod_app_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_app_comment` (
  `id` varchar(32) NOT NULL DEFAULT '',
  `content` varchar(3000) DEFAULT NULL COMMENT '内容',
  `score` int(11) DEFAULT NULL COMMENT '分数',
  `version_id` varchar(32) DEFAULT NULL,
  `app_id` varchar(32) DEFAULT NULL,
  `create_user` varchar(32) DEFAULT NULL,
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `subject` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_app_comment_help`
--

DROP TABLE IF EXISTS `mod_app_comment_help`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_app_comment_help` (
  `id` varchar(32) NOT NULL DEFAULT '',
  `comment_id` varchar(3000) DEFAULT NULL,
  `comment_help` varchar(1) DEFAULT NULL,
  `create_user` varchar(32) DEFAULT NULL,
  `create_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_app_dashboard_data`
--

DROP TABLE IF EXISTS `mod_app_dashboard_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_app_dashboard_data` (
  `app_id` varchar(32) DEFAULT NULL,
  `app_name` varchar(2000) DEFAULT NULL,
  `double_value` float DEFAULT NULL,
  `string_value` varchar(200) DEFAULT NULL,
  `data_type` int(11) DEFAULT NULL COMMENT '1访问次数 2 访问耗时 3成功率 4ip数 5开发人员数 6 实例数量  7需求数 8 bug数量 9代码行数 10开始时间 11结束时间',
  `time_type` int(11) DEFAULT NULL COMMENT '0 最近半天 1最近一天  2最近半个月  3最近1个月  4最近3个月  5最近1年',
  UNIQUE KEY `app_dashbord` (`app_id`,`data_type`,`time_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_app_dashboard_data_t`
--

DROP TABLE IF EXISTS `mod_app_dashboard_data_t`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_app_dashboard_data_t` (
  `app_id` varchar(32) DEFAULT NULL,
  `app_name` varchar(2000) DEFAULT NULL,
  `double_value` float DEFAULT NULL,
  `string_value` varchar(200) DEFAULT NULL,
  `data_type` int(11) DEFAULT NULL COMMENT '1访问次数 2 访问耗时 3成功率 4ip数 5开发人员数 6 实例数量  7需求数 8 bug数量 9代码行数',
  `time_type` int(11) DEFAULT NULL COMMENT '0 最近半天 1最近一天  2最近半个月  3最近1个月  4最近3个月  5最近1年',
  UNIQUE KEY `app_dashbord` (`app_id`,`data_type`,`time_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='先往这个表里面插入数据 然后统一copy到主表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_app_dev`
--

DROP TABLE IF EXISTS `mod_app_dev`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_app_dev` (
  `app_id` varchar(32) DEFAULT NULL,
  `user_id` varchar(32) DEFAULT NULL,
  `rec_id` varchar(32) NOT NULL,
  PRIMARY KEY (`rec_id`),
  UNIQUE KEY `a_u_un` (`user_id`,`app_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_app_logs`
--

DROP TABLE IF EXISTS `mod_app_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_app_logs` (
  `log_id` varchar(32) NOT NULL,
  `app_id` varchar(45) NOT NULL COMMENT 'appID',
  `version_id` varchar(45) NOT NULL COMMENT '版本ID',
  `oper_time` int(11) NOT NULL COMMENT '操作耗时，秒为单位',
  `oper_success` bit(1) NOT NULL,
  `operation` varchar(45) NOT NULL COMMENT '操作类型',
  `log_time` datetime NOT NULL COMMENT '日志产生时间',
  `log_user` varchar(32) NOT NULL COMMENT '执行用户',
  `log_text` mediumtext NOT NULL COMMENT '日志内容',
  `log_param1` varchar(45) NOT NULL,
  `log_param2` varchar(45) NOT NULL,
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_app_source`
--

DROP TABLE IF EXISTS `mod_app_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_app_source` (
  `app_id` varchar(32) NOT NULL,
  `project_name` varchar(128) DEFAULT NULL,
  `project_package` varchar(256) DEFAULT NULL,
  `scm_type` varchar(45) DEFAULT NULL,
  `scm_url` varchar(255) DEFAULT NULL,
  `dev_lang` varchar(45) DEFAULT NULL,
  `dev_framework` varchar(45) DEFAULT NULL,
  `maven_group_id` varchar(45) DEFAULT NULL,
  `maven_artifact_id` varchar(45) DEFAULT NULL,
  `maven_version` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`app_id`),
  UNIQUE KEY `u_group_artifact` (`maven_group_id`,`maven_artifact_id`) USING BTREE,
  UNIQUE KEY `u_project_name` (`project_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_app_version`
--

DROP TABLE IF EXISTS `mod_app_version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_app_version` (
  `version_id` varchar(32) NOT NULL,
  `app_id` varchar(32) NOT NULL,
  `version_status` varchar(20) NOT NULL,
  `version_code` varchar(45) NOT NULL,
  `scm_url` varchar(255) DEFAULT NULL,
  `creator` varchar(32) NOT NULL,
  `creation_time` datetime NOT NULL,
  `v_enabled` bit(1) NOT NULL,
  `version_desc` varchar(1024) DEFAULT NULL,
  `last_build_time` datetime DEFAULT NULL,
  `last_build_log` mediumtext,
  `last_build_success` bit(1) DEFAULT NULL,
  `last_build_success_file` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`version_id`),
  UNIQUE KEY `versionCode_UN` (`app_id`,`version_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_app_version_api`
--

DROP TABLE IF EXISTS `mod_app_version_api`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_app_version_api` (
  `ref_id` varchar(32) NOT NULL,
  `app_id` varchar(32) NOT NULL,
  `version_id` varchar(32) NOT NULL,
  `service_id` varchar(32) NOT NULL,
  `service_name` varchar(255) NOT NULL,
  `service_version_id` varchar(32) NOT NULL,
  `service_version_code` varchar(255) NOT NULL,
  PRIMARY KEY (`ref_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_dashboard_data`
--

DROP TABLE IF EXISTS `mod_dashboard_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_dashboard_data` (
  `id` varchar(32) NOT NULL,
  `service_id` varchar(32) DEFAULT NULL COMMENT '业务主键',
  `data_type` varchar(32) DEFAULT NULL COMMENT '数据类型 app api',
  `date_time` date DEFAULT NULL COMMENT '数据日期 2015-03-21 格式',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期',
  `bug_count` double DEFAULT NULL COMMENT 'bug数量',
  `demand_count` double DEFAULT NULL COMMENT '需求数',
  `taskelapse_count` double DEFAULT NULL COMMENT 'bug和需求总耗时 秒',
  `complete_count` double DEFAULT NULL COMMENT '完成数',
  `visits_count` double DEFAULT NULL COMMENT '访问次数',
  `elapse_count` double DEFAULT NULL COMMENT '访问耗时 秒',
  `success_count` double DEFAULT NULL COMMENT '访问成功次数',
  `ip_count` double DEFAULT NULL COMMENT '访问ip数量',
  `complete_time_count` double DEFAULT NULL,
  `codeline` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='看板数据 每日收集';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_dashboard_hist`
--

DROP TABLE IF EXISTS `mod_dashboard_hist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_dashboard_hist` (
  `id` varchar(32) NOT NULL,
  `date_time` date DEFAULT NULL,
  `result` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_external_app`
--

DROP TABLE IF EXISTS `mod_external_app`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_external_app` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `name` varchar(128) NOT NULL COMMENT '应用名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `creater` varchar(32) NOT NULL COMMENT '创建者',
  `secure_key` varchar(32) DEFAULT NULL COMMENT '动态token',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniqueName` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='外部应用';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_preset_category`
--

DROP TABLE IF EXISTS `mod_preset_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_preset_category` (
  `category_id` varchar(32) NOT NULL,
  `category_name` varchar(100) NOT NULL,
  `c_order` int(11) DEFAULT NULL,
  `parent_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`category_id`),
  KEY `FK_mod_preset_category_parent_idx` (`parent_id`) USING BTREE,
  CONSTRAINT `mod_preset_category_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `mod_preset_category` (`category_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='预置服务分类';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_process_log`
--

DROP TABLE IF EXISTS `mod_process_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_process_log` (
  `log_id` varchar(32) NOT NULL,
  `create_user` varchar(32) NOT NULL,
  `create_time` datetime NOT NULL,
  `content` varchar(100) NOT NULL,
  `process_id` varchar(32) NOT NULL COMMENT '流程id',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='流程日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_process_node`
--

DROP TABLE IF EXISTS `mod_process_node`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_process_node` (
  `node_id` varchar(32) NOT NULL,
  `process_log` varchar(32) NOT NULL COMMENT '流程日志id',
  `node_name` varchar(50) NOT NULL COMMENT '环节名称',
  `p_time` datetime DEFAULT NULL COMMENT '审批时间',
  `p_user` varchar(32) DEFAULT NULL COMMENT '审批人',
  `p_comments` varchar(255) DEFAULT NULL COMMENT '审批意见',
  `p_result` char(1) DEFAULT NULL COMMENT '审批结果，1：通过，2：驳回',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`node_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='流程节点表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_project_jira_relation`
--

DROP TABLE IF EXISTS `mod_project_jira_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_project_jira_relation` (
  `id` varchar(32) NOT NULL,
  `project_id` varchar(32) NOT NULL COMMENT '项目ID',
  `jira_project_key` varchar(32) NOT NULL COMMENT 'jira项目ID',
  `source` tinyint(4) NOT NULL COMMENT '1：APPFactory    2：APIManager',
  `creator` varchar(32) NOT NULL,
  `creation_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX_JIRA_RELATION_PROJECT_ID` (`project_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目与jira中项目关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_project_jira_story_favorite`
--

DROP TABLE IF EXISTS `mod_project_jira_story_favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_project_jira_story_favorite` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) DEFAULT NULL COMMENT '关联用户',
  `story_key` varchar(32) DEFAULT NULL COMMENT '故事key',
  `project_key` varchar(32) DEFAULT NULL COMMENT 'jira项目key',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='收藏的故事';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_project_jira_story_relation`
--

DROP TABLE IF EXISTS `mod_project_jira_story_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_project_jira_story_relation` (
  `id` varchar(32) NOT NULL,
  `story_key` varchar(32) NOT NULL COMMENT '故事key',
  `jira_relation_id` varchar(32) NOT NULL COMMENT 'jira项目关联记录ID',
  `creator` varchar(32) NOT NULL,
  `creation_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_mod_project_jira_relation_story` (`jira_relation_id`) USING BTREE,
  CONSTRAINT `mod_project_jira_story_relation_ibfk_1` FOREIGN KEY (`jira_relation_id`) REFERENCES `mod_project_jira_relation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目与jira中故事关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_service`
--

DROP TABLE IF EXISTS `mod_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_service` (
  `service_id` varchar(32) NOT NULL,
  `project_name` varchar(45) DEFAULT NULL,
  `project_package` varchar(255) DEFAULT NULL,
  `service_name` varchar(100) NOT NULL,
  `service_desc` varchar(500) DEFAULT NULL,
  `service_leader` varchar(32) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `create_user` varchar(32) NOT NULL,
  `s_logo` varchar(50) DEFAULT NULL,
  `s_protocol` varchar(20) NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `scm_type` varchar(45) DEFAULT NULL,
  `scm_url` varchar(255) DEFAULT NULL,
  `store_category` varchar(32) DEFAULT NULL COMMENT '服务分类',
  `use_amount` int(11) DEFAULT NULL COMMENT '使用量',
  `update_flag` int(1) DEFAULT NULL COMMENT '更新标志，用于统计使用量',
  `market_type` int(1) DEFAULT NULL COMMENT '0：默认值,1:预置服务标识，2：企业服务或本地服务标识',
  `preset_category` varchar(32) DEFAULT NULL COMMENT '预置服务分类',
  `service_source` varchar(1) DEFAULT NULL COMMENT '服务来源,1保存，2导入',
  `service_public` varchar(1) DEFAULT NULL COMMENT '服务是否公开,0公开,1或者null不公开',
  `dev_lang` varchar(45) DEFAULT NULL COMMENT '服务开发语言',
  `dev_framework` varchar(45) DEFAULT NULL COMMENT '服务开发框架',
  `secure_key` varchar(32) DEFAULT NULL COMMENT '动态token',
  `project_code` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`service_id`),
  UNIQUE KEY `project_name_UNIQUE` (`project_name`) USING BTREE,
  UNIQUE KEY `unique_project_code` (`project_code`),
  KEY `i_service_leader` (`service_leader`) USING BTREE,
  KEY `i_server_user` (`create_user`) USING BTREE,
  KEY `FK_mod_service_category_idx` (`store_category`) USING BTREE,
  KEY `FK_mod_service_preset_category_idx` (`preset_category`) USING BTREE,
  CONSTRAINT `mod_service_ibfk_1` FOREIGN KEY (`preset_category`) REFERENCES `mod_preset_category` (`category_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_service_comment`
--

DROP TABLE IF EXISTS `mod_service_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_service_comment` (
  `comment_id` varchar(32) NOT NULL,
  `user_id` varchar(32) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `content` varchar(500) DEFAULT NULL,
  `version_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务评论';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_service_dependency`
--

DROP TABLE IF EXISTS `mod_service_dependency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_service_dependency` (
  `ref_id` varchar(32) NOT NULL,
  `service_id` varchar(32) NOT NULL,
  `version_id` varchar(32) NOT NULL,
  `ref_service_id` varchar(32) NOT NULL,
  `ref_version_id` varchar(32) NOT NULL,
  PRIMARY KEY (`ref_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_service_dev`
--

DROP TABLE IF EXISTS `mod_service_dev`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_service_dev` (
  `rec_id` varchar(32) NOT NULL,
  `service_id` varchar(32) NOT NULL,
  `user_id` varchar(32) NOT NULL,
  PRIMARY KEY (`rec_id`),
  KEY `i_service_id` (`service_id`) USING BTREE,
  KEY `i_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_service_logs`
--

DROP TABLE IF EXISTS `mod_service_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_service_logs` (
  `log_id` varchar(32) NOT NULL,
  `service_id` varchar(45) NOT NULL COMMENT '服务ID',
  `version_id` varchar(45) NOT NULL COMMENT '版本ID',
  `oper_time` int(11) NOT NULL COMMENT '操作耗时，秒为单位',
  `oper_success` bit(1) NOT NULL,
  `operation` varchar(45) NOT NULL COMMENT '操作类型',
  `log_time` datetime NOT NULL COMMENT '日志产生时间',
  `log_user` varchar(32) NOT NULL COMMENT '执行用户',
  `log_text` mediumtext NOT NULL COMMENT '日志内容',
  `log_param1` varchar(45) NOT NULL,
  `log_param2` varchar(45) NOT NULL,
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_service_possession`
--

DROP TABLE IF EXISTS `mod_service_possession`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_service_possession` (
  `possession_id` varchar(32) NOT NULL,
  `application_user` varchar(32) NOT NULL COMMENT '申请人',
  `version_id` varchar(32) NOT NULL COMMENT '版本',
  `application_date` datetime NOT NULL COMMENT '申请时间',
  `reason` varchar(200) DEFAULT NULL COMMENT '申请原因',
  `process_id` varchar(32) DEFAULT NULL COMMENT '流程id',
  `business_type` varchar(32) DEFAULT NULL COMMENT '申请服务的业务类型',
  `business_key` varchar(32) DEFAULT NULL COMMENT '申请服务的业务id',
  PRIMARY KEY (`possession_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务版本占有';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_service_tag`
--

DROP TABLE IF EXISTS `mod_service_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_service_tag` (
  `rec_id` varchar(32) NOT NULL,
  `tag_id` varchar(32) NOT NULL,
  `service_id` varchar(32) NOT NULL,
  PRIMARY KEY (`rec_id`),
  KEY `i_tag_id` (`tag_id`) USING BTREE,
  KEY `i_service_id` (`service_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_service_v_process`
--

DROP TABLE IF EXISTS `mod_service_v_process`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_service_v_process` (
  `process_id` varchar(32) NOT NULL,
  `version_id` varchar(32) NOT NULL,
  `create_time` datetime NOT NULL,
  `result` char(1) DEFAULT NULL COMMENT '1：审批中，2：驳回，3：通过',
  `service_id` varchar(32) NOT NULL COMMENT '服务id',
  `create_user` varchar(32) NOT NULL COMMENT '创建人',
  `process_log` varchar(32) DEFAULT NULL COMMENT '流程日志id',
  `process_type` varchar(45) DEFAULT NULL COMMENT '流程类型',
  PRIMARY KEY (`process_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务版本的审批流程';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_service_version`
--

DROP TABLE IF EXISTS `mod_service_version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_service_version` (
  `version_id` varchar(32) NOT NULL,
  `service_id` varchar(32) NOT NULL,
  `version_code` varchar(45) NOT NULL COMMENT '版本号例如1.0.1',
  `version_status` int(11) NOT NULL DEFAULT '1' COMMENT '1:创建，2:定义，3:开发，4:测试，5:生产，6:审批，7:上架',
  `v_enabled` bit(1) NOT NULL COMMENT '是否启用',
  `v_leader` varchar(32) DEFAULT NULL COMMENT '版本负责',
  `v_desc` varchar(500) DEFAULT NULL COMMENT '版本描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user` varchar(32) NOT NULL COMMENT '创建者',
  `process_status` int(11) NOT NULL COMMENT 'null：未启动，1：待审批，2：已审批，3：已终止',
  `v_process` varchar(32) DEFAULT NULL COMMENT '当前流程ID',
  `v_maven` varchar(1024) DEFAULT NULL COMMENT '客户端开发时MAVEN信息',
  `v_url` varchar(300) DEFAULT NULL COMMENT '客户端调用时的url',
  `v_doc` varchar(40) DEFAULT NULL COMMENT '上传的SDK和文档ID',
  `last_build_time` datetime DEFAULT NULL,
  `last_build_success_file` varchar(500) DEFAULT NULL,
  `last_build_log` varchar(32) DEFAULT NULL,
  `last_build_success` bit(1) DEFAULT NULL,
  `on_state` bit(1) DEFAULT NULL COMMENT '版本上架状态，0：下架  1：上架',
  `test_url` varchar(100) DEFAULT NULL,
  `live_url` varchar(100) DEFAULT NULL,
  `version_type` mediumtext COMMENT '服务版本自定义类型',
  `scm_url` varchar(255) DEFAULT NULL COMMENT '版本的源码分支地址',
  `update_api_status` int(11) DEFAULT NULL COMMENT 'API定义编译状态: 0:默认值,-1:编译失败,1:编译中,2:编译成功',
  `update_api_message` mediumtext COMMENT '编译日志',
  PRIMARY KEY (`version_id`),
  UNIQUE KEY `i_service_code` (`service_id`,`version_code`) USING BTREE,
  KEY `i_v_process` (`v_process`) USING BTREE,
  KEY `i_v_leader` (`v_leader`) USING BTREE,
  KEY `i_create_time` (`create_user`) USING BTREE,
  KEY `i_service_id` (`service_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_service_version_ip`
--

DROP TABLE IF EXISTS `mod_service_version_ip`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_service_version_ip` (
  `id` varchar(32) NOT NULL,
  `version_id` varchar(32) DEFAULT NULL,
  `address_` varchar(20) DEFAULT NULL,
  `port_` varchar(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_service_version_protocol`
--

DROP TABLE IF EXISTS `mod_service_version_protocol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_service_version_protocol` (
  `protocol_id` varchar(32) NOT NULL,
  `version_id` varchar(32) NOT NULL,
  `protocol_type` varchar(45) NOT NULL,
  `protocol_api` mediumtext NOT NULL,
  `last_modify_time` datetime NOT NULL,
  `last_modify_user` varchar(32) NOT NULL,
  `protocol_name` varchar(128) DEFAULT NULL,
  `test_case` mediumtext COMMENT '接口的测试用例',
  PRIMARY KEY (`protocol_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_service_version_protocol_comments`
--

DROP TABLE IF EXISTS `mod_service_version_protocol_comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_service_version_protocol_comments` (
  `id` varchar(32) NOT NULL,
  `version_id` varchar(32) DEFAULT NULL COMMENT '版本id',
  `protocol_id` varchar(32) NOT NULL COMMENT '接口id',
  `participant` varchar(255) NOT NULL COMMENT '批审参与者',
  `comments` mediumtext NOT NULL COMMENT '评审意见',
  `creater` varchar(32) NOT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务接口评审意见表\r\n';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_store_category`
--

DROP TABLE IF EXISTS `mod_store_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_store_category` (
  `category_id` varchar(32) NOT NULL,
  `category_name` varchar(100) DEFAULT NULL,
  `c_order` int(4) DEFAULT NULL,
  `hierarchy_id` varchar(300) DEFAULT NULL,
  `parent_id` varchar(32) DEFAULT NULL,
  `service_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`category_id`),
  KEY `FK_store_category_idx` (`parent_id`) USING BTREE,
  CONSTRAINT `mod_store_category_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `mod_store_category` (`category_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mod_user_service`
--

DROP TABLE IF EXISTS `mod_user_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mod_user_service` (
  `follow_id` varchar(32) NOT NULL,
  `user_id` varchar(32) NOT NULL,
  `service_id` varchar(32) NOT NULL,
  `f_order` int(11) DEFAULT NULL,
  PRIMARY KEY (`follow_id`),
  UNIQUE KEY `s_u_s` (`user_id`,`service_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `paas_docker_container`
--

DROP TABLE IF EXISTS `paas_docker_container`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paas_docker_container` (
  `container_id` varchar(32) NOT NULL COMMENT '容器自己的ID，唯一ID，也是docker 中容器的 —name',
  `host_id` varchar(32) NOT NULL COMMENT '容器运行在那个主机上',
  `container_cpu` int(11) NOT NULL,
  `container_mem` int(11) NOT NULL,
  `container_ip` varchar(15) NOT NULL,
  `container_port` varchar(100) NOT NULL COMMENT '容器开启的端口',
  `image_name` varchar(45) NOT NULL COMMENT '容器使用的镜像名称',
  `create_time` datetime NOT NULL,
  `business_id` varchar(32) NOT NULL COMMENT '外部业务ID，例如  服务、版本、测试环境 申请的容器。一个业务ID可以有多个容器。',
  `business_type` varchar(45) NOT NULL COMMENT '业务类型，例如：APP Factory|app  或  API Manager|api',
  `env_type` varchar(45) NOT NULL,
  `version_id` varchar(32) DEFAULT NULL,
  `sandbox_id` varchar(32) DEFAULT NULL,
  `deploy_time` datetime DEFAULT NULL,
  `deploy_user` varchar(32) DEFAULT NULL,
  `assembly` varchar(255) DEFAULT NULL,
  `container_name` varchar(32) DEFAULT NULL,
  `build_file` varchar(255) DEFAULT NULL COMMENT '构建文件路径',
  `env_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`container_id`),
  UNIQUE KEY `i_host_port_u` (`host_id`,`container_port`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `paas_docker_image`
--

DROP TABLE IF EXISTS `paas_docker_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paas_docker_image` (
  `image_id` varchar(21) NOT NULL,
  `image_name` varchar(45) NOT NULL,
  `image_port` varchar(45) NOT NULL,
  `image_version` varchar(45) NOT NULL,
  `opt_bean` varchar(255) NOT NULL,
  PRIMARY KEY (`image_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `paas_env`
--

DROP TABLE IF EXISTS `paas_env`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paas_env` (
  `env_id` varchar(32) NOT NULL,
  `env_name` varchar(45) DEFAULT NULL,
  `env_type` varchar(10) DEFAULT NULL,
  `business_id` varchar(32) DEFAULT NULL,
  `business_type` varchar(10) DEFAULT NULL,
  `creator` varchar(32) DEFAULT NULL,
  `creation_time` datetime DEFAULT NULL,
  PRIMARY KEY (`env_id`),
  KEY `envName_UN` (`business_id`,`env_type`,`env_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `paas_env_res`
--

DROP TABLE IF EXISTS `paas_env_res`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paas_env_res` (
  `record_id` varchar(32) NOT NULL,
  `env_id` varchar(32) NOT NULL,
  `resource_id` varchar(45) NOT NULL COMMENT '资源ID',
  `apply_config` mediumtext COMMENT '界面上填写的信息',
  `result_config` mediumtext COMMENT '申请之后审批返回的参数',
  `res_addition` varchar(1024) DEFAULT NULL COMMENT '复用: 数据库为数据库名称，nfs为挂载目录',
  `paasos_res_id` varchar(32) DEFAULT NULL COMMENT 'paasos 资源id',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `uniqueEnvResource` (`env_id`,`resource_id`) USING BTREE,
  KEY `i_b_r` (`env_id`,`resource_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `paas_framework`
--

DROP TABLE IF EXISTS `paas_framework`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paas_framework` (
  `fw_id` varchar(32) NOT NULL,
  `fw_lang` varchar(45) NOT NULL,
  `fw_key` varchar(45) NOT NULL,
  `fw_name` varchar(128) NOT NULL,
  `image_id` varchar(45) NOT NULL,
  `sys_type` varchar(45) NOT NULL,
  `docker_file` mediumtext,
  PRIMARY KEY (`fw_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `paas_mysql_allocation`
--

DROP TABLE IF EXISTS `paas_mysql_allocation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paas_mysql_allocation` (
  `allo_id` varchar(32) NOT NULL,
  `host_id` varchar(32) NOT NULL,
  `db_user` varchar(45) NOT NULL,
  `db_name` varchar(45) NOT NULL,
  `db_pwd` varchar(45) NOT NULL,
  `config` mediumtext,
  `allo_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `business_id` varchar(45) DEFAULT NULL COMMENT '资源ID',
  PRIMARY KEY (`allo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `paas_mysql_host`
--

DROP TABLE IF EXISTS `paas_mysql_host`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paas_mysql_host` (
  `host_id` varchar(32) NOT NULL,
  `host_ip` varchar(45) NOT NULL,
  `host_user` varchar(45) DEFAULT NULL,
  `host_key_prv` varchar(2048) DEFAULT NULL,
  `host_key_pub` varchar(1024) DEFAULT NULL,
  `host_pwd` varchar(45) DEFAULT NULL,
  `host_port` varchar(10) DEFAULT NULL,
  `mysql_pwd` varchar(45) NOT NULL,
  `mysql_user` varchar(45) NOT NULL,
  `mysql_port` varchar(10) NOT NULL,
  `env_type` varchar(45) NOT NULL,
  `host_desc` mediumtext,
  PRIMARY KEY (`host_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `paas_nginx_conf`
--

DROP TABLE IF EXISTS `paas_nginx_conf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paas_nginx_conf` (
  `conf_id` varchar(32) NOT NULL,
  `host_id` varchar(45) NOT NULL,
  `conf_content` mediumtext,
  `service_id` varchar(32) NOT NULL,
  `version_id` varchar(32) NOT NULL,
  `env_type` varchar(10) NOT NULL,
  `nginx_name` varchar(32) DEFAULT NULL,
  `env_id` varchar(32) DEFAULT NULL,
  `business_type` varchar(10) DEFAULT NULL,
  `skip_auth` bit(1) DEFAULT b'0',
  `is_support_ssl` bit(1) DEFAULT b'0',
  `ssl_crt_id` varchar(255) DEFAULT NULL,
  `ssl_key_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`conf_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `paas_resource_template`
--

DROP TABLE IF EXISTS `paas_resource_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paas_resource_template` (
  `template_id` varchar(32) NOT NULL,
  `template_name` varchar(45) NOT NULL,
  `template_content` mediumtext NOT NULL,
  `desc` mediumtext,
  `creater_id` varchar(32) DEFAULT NULL,
  `create_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`template_id`),
  UNIQUE KEY `template_name_index` (`template_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `quartz_job_log`
--

DROP TABLE IF EXISTS `quartz_job_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `quartz_job_log` (
  `log_id` varchar(32) NOT NULL,
  `job_name` varchar(255) NOT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `success` bit(1) NOT NULL,
  `job_class` varchar(255) NOT NULL,
  `error_info` mediumtext,
  `server_ip` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_attachment`
--

DROP TABLE IF EXISTS `sys_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_attachment` (
  `att_id` varchar(32) NOT NULL,
  `att_name` varchar(128) NOT NULL,
  `file_path` varchar(255) DEFAULT NULL,
  `att_size` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `business_id` varchar(32) DEFAULT NULL,
  `business_key` varchar(80) DEFAULT NULL,
  `business_type` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`att_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_config`
--

DROP TABLE IF EXISTS `sys_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_config` (
  `conf_id` varchar(32) NOT NULL,
  `conf_key` varchar(255) DEFAULT NULL,
  `conf_value` varchar(4096) DEFAULT NULL,
  PRIMARY KEY (`conf_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_core_config`
--

DROP TABLE IF EXISTS `sys_core_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_core_config` (
  `key` varchar(255) NOT NULL COMMENT '系统核心配置--key',
  `value` varchar(4096) NOT NULL COMMENT '系统核心配置--value',
  PRIMARY KEY (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `sys_department`
--

DROP TABLE IF EXISTS `sys_department`;
/*!50001 DROP VIEW IF EXISTS `sys_department`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `sys_department` AS SELECT 
 1 AS `dept_id`,
 1 AS `dept_name`,
 1 AS `parent_id`,
 1 AS `hierarchy_id`,
 1 AS `create_time`,
 1 AS `create_user`,
 1 AS `d_order`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `sys_func`
--

DROP TABLE IF EXISTS `sys_func`;
/*!50001 DROP VIEW IF EXISTS `sys_func`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `sys_func` AS SELECT 
 1 AS `func_id`,
 1 AS `func_name`,
 1 AS `func_desc`,
 1 AS `func_system`,
 1 AS `func_module`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `sys_group`
--

DROP TABLE IF EXISTS `sys_group`;
/*!50001 DROP VIEW IF EXISTS `sys_group`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `sys_group` AS SELECT 
 1 AS `group_id`,
 1 AS `group_name`,
 1 AS `create_time`,
 1 AS `create_user`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `sys_host`
--

DROP TABLE IF EXISTS `sys_host`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_host` (
  `host_id` varchar(32) NOT NULL,
  `host_ip` varchar(45) NOT NULL,
  `host_user` varchar(45) NOT NULL,
  `host_key_prv` varchar(2048) DEFAULT NULL,
  `host_key_pub` varchar(1024) DEFAULT NULL,
  `host_port` varchar(45) NOT NULL,
  `env_type` varchar(10) NOT NULL,
  `host_type` varchar(45) NOT NULL,
  `host_platform` varchar(10) DEFAULT NULL COMMENT '应用的类型，api or app',
  `backup_host` varchar(45) NOT NULL COMMENT '备用服务器',
  `virtual_host` varchar(45) NOT NULL COMMENT 'used for dns resolver for HA',
  `directory_name` varchar(45) NOT NULL COMMENT 'used for store .conf file',
  PRIMARY KEY (`host_id`),
  UNIQUE KEY `i_host_ip` (`host_ip`,`host_type`) USING BTREE,
  UNIQUE KEY `unq_platform_dir` (`host_platform`,`directory_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `sys_login_logs`
--

DROP TABLE IF EXISTS `sys_login_logs`;
/*!50001 DROP VIEW IF EXISTS `sys_login_logs`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `sys_login_logs` AS SELECT 
 1 AS `log_id`,
 1 AS `login_name`,
 1 AS `login_param`,
 1 AS `login_time`,
 1 AS `login_type`,
 1 AS `client_ip`,
 1 AS `user_agent`,
 1 AS `login_sys`,
 1 AS `user_id`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!50001 DROP VIEW IF EXISTS `sys_role`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `sys_role` AS SELECT 
 1 AS `role_id`,
 1 AS `role_name`,
 1 AS `role_desc`,
 1 AS `role_system`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `sys_role_func`
--

DROP TABLE IF EXISTS `sys_role_func`;
/*!50001 DROP VIEW IF EXISTS `sys_role_func`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `sys_role_func` AS SELECT 
 1 AS `id`,
 1 AS `role_id`,
 1 AS `func_id`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `sys_tag`
--

DROP TABLE IF EXISTS `sys_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_tag` (
  `tag_id` varchar(32) NOT NULL,
  `tag_text` varchar(45) NOT NULL COMMENT '标签文字',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `hot` bit(1) DEFAULT b'0' COMMENT '是否属于热门标签',
  PRIMARY KEY (`tag_id`),
  UNIQUE KEY `tag_text_unique` (`tag_text`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `sys_tenant`
--

DROP TABLE IF EXISTS `sys_tenant`;
/*!50001 DROP VIEW IF EXISTS `sys_tenant`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `sys_tenant` AS SELECT 
 1 AS `tenant_id`,
 1 AS `tenant_name`,
 1 AS `description`,
 1 AS `admin_id`,
 1 AS `create_time`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `sys_tenant_relation`
--

DROP TABLE IF EXISTS `sys_tenant_relation`;
/*!50001 DROP VIEW IF EXISTS `sys_tenant_relation`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `sys_tenant_relation` AS SELECT 
 1 AS `rel_id`,
 1 AS `tenant_id`,
 1 AS `user_id`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!50001 DROP VIEW IF EXISTS `sys_user`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `sys_user` AS SELECT 
 1 AS `user_id`,
 1 AS `user_name`,
 1 AS `login_name`,
 1 AS `login_password`,
 1 AS `roles`,
 1 AS `create_time`,
 1 AS `activate`,
 1 AS `email`,
 1 AS `token`,
 1 AS `sys_user`,
 1 AS `api_auth_key`,
 1 AS `mobile`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `sys_user_dept`
--

DROP TABLE IF EXISTS `sys_user_dept`;
/*!50001 DROP VIEW IF EXISTS `sys_user_dept`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `sys_user_dept` AS SELECT 
 1 AS `relation_id`,
 1 AS `user_id`,
 1 AS `dep_id`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `sys_user_group`
--

DROP TABLE IF EXISTS `sys_user_group`;
/*!50001 DROP VIEW IF EXISTS `sys_user_group`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `sys_user_group` AS SELECT 
 1 AS `relation_id`,
 1 AS `user_id`,
 1 AS `group_id`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!50001 DROP VIEW IF EXISTS `sys_user_role`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `sys_user_role` AS SELECT 
 1 AS `id`,
 1 AS `role_id`,
 1 AS `user_id`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `sys_department`
--

/*!50001 DROP VIEW IF EXISTS `sys_department`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `sys_department` AS select `paasos`.`sys_department`.`dept_id` AS `dept_id`,`paasos`.`sys_department`.`dept_name` AS `dept_name`,`paasos`.`sys_department`.`parent_id` AS `parent_id`,`paasos`.`sys_department`.`hierarchy_id` AS `hierarchy_id`,`paasos`.`sys_department`.`create_time` AS `create_time`,`paasos`.`sys_department`.`create_user` AS `create_user`,`paasos`.`sys_department`.`d_order` AS `d_order` from `paasos`.`sys_department` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `sys_func`
--

/*!50001 DROP VIEW IF EXISTS `sys_func`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `sys_func` AS select `paasos`.`sys_func`.`func_id` AS `func_id`,`paasos`.`sys_func`.`func_name` AS `func_name`,`paasos`.`sys_func`.`func_desc` AS `func_desc`,`paasos`.`sys_func`.`func_system` AS `func_system`,`paasos`.`sys_func`.`func_module` AS `func_module` from `paasos`.`sys_func` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `sys_group`
--

/*!50001 DROP VIEW IF EXISTS `sys_group`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `sys_group` AS select `paasos`.`sys_group`.`group_id` AS `group_id`,`paasos`.`sys_group`.`group_name` AS `group_name`,`paasos`.`sys_group`.`create_time` AS `create_time`,`paasos`.`sys_group`.`create_user` AS `create_user` from `paasos`.`sys_group` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `sys_login_logs`
--

/*!50001 DROP VIEW IF EXISTS `sys_login_logs`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `sys_login_logs` AS select `paasos`.`sys_login_logs`.`log_id` AS `log_id`,`paasos`.`sys_login_logs`.`login_name` AS `login_name`,`paasos`.`sys_login_logs`.`login_param` AS `login_param`,`paasos`.`sys_login_logs`.`login_time` AS `login_time`,`paasos`.`sys_login_logs`.`login_type` AS `login_type`,`paasos`.`sys_login_logs`.`client_ip` AS `client_ip`,`paasos`.`sys_login_logs`.`user_agent` AS `user_agent`,`paasos`.`sys_login_logs`.`login_sys` AS `login_sys`,`paasos`.`sys_login_logs`.`user_id` AS `user_id` from `paasos`.`sys_login_logs` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `sys_role`
--

/*!50001 DROP VIEW IF EXISTS `sys_role`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `sys_role` AS select `paasos`.`sys_role`.`role_id` AS `role_id`,`paasos`.`sys_role`.`role_name` AS `role_name`,`paasos`.`sys_role`.`role_desc` AS `role_desc`,`paasos`.`sys_role`.`role_system` AS `role_system` from `paasos`.`sys_role` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `sys_role_func`
--

/*!50001 DROP VIEW IF EXISTS `sys_role_func`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `sys_role_func` AS select `paasos`.`sys_role_func`.`id` AS `id`,`paasos`.`sys_role_func`.`role_id` AS `role_id`,`paasos`.`sys_role_func`.`func_id` AS `func_id` from `paasos`.`sys_role_func` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `sys_tenant`
--

/*!50001 DROP VIEW IF EXISTS `sys_tenant`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `sys_tenant` AS select `paasos`.`sys_tenant`.`tenant_id` AS `tenant_id`,`paasos`.`sys_tenant`.`tenant_name` AS `tenant_name`,`paasos`.`sys_tenant`.`description` AS `description`,`paasos`.`sys_tenant`.`admin_id` AS `admin_id`,`paasos`.`sys_tenant`.`create_time` AS `create_time` from `paasos`.`sys_tenant` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `sys_tenant_relation`
--

/*!50001 DROP VIEW IF EXISTS `sys_tenant_relation`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `sys_tenant_relation` AS select `paasos`.`sys_tenant_relation`.`rel_id` AS `rel_id`,`paasos`.`sys_tenant_relation`.`tenant_id` AS `tenant_id`,`paasos`.`sys_tenant_relation`.`user_id` AS `user_id` from `paasos`.`sys_tenant_relation` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `sys_user`
--

/*!50001 DROP VIEW IF EXISTS `sys_user`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `sys_user` AS (select `paasos`.`sys_user`.`user_id` AS `user_id`,`paasos`.`sys_user`.`user_name` AS `user_name`,`paasos`.`sys_user`.`login_name` AS `login_name`,`paasos`.`sys_user`.`login_password` AS `login_password`,`paasos`.`sys_user`.`roles` AS `roles`,`paasos`.`sys_user`.`create_time` AS `create_time`,`paasos`.`sys_user`.`activate` AS `activate`,`paasos`.`sys_user`.`email` AS `email`,`paasos`.`sys_user`.`token` AS `token`,`paasos`.`sys_user`.`sys_user` AS `sys_user`,`paasos`.`sys_user`.`api_auth_key` AS `api_auth_key`,`paasos`.`sys_user`.`mobile` AS `mobile` from `paasos`.`sys_user`) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `sys_user_dept`
--

/*!50001 DROP VIEW IF EXISTS `sys_user_dept`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `sys_user_dept` AS select `paasos`.`sys_user_dept`.`relation_id` AS `relation_id`,`paasos`.`sys_user_dept`.`user_id` AS `user_id`,`paasos`.`sys_user_dept`.`dep_id` AS `dep_id` from `paasos`.`sys_user_dept` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `sys_user_group`
--

/*!50001 DROP VIEW IF EXISTS `sys_user_group`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `sys_user_group` AS select `paasos`.`sys_user_group`.`relation_id` AS `relation_id`,`paasos`.`sys_user_group`.`user_id` AS `user_id`,`paasos`.`sys_user_group`.`group_id` AS `group_id` from `paasos`.`sys_user_group` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `sys_user_role`
--

/*!50001 DROP VIEW IF EXISTS `sys_user_role`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `sys_user_role` AS select `paasos`.`sys_user_role`.`id` AS `id`,`paasos`.`sys_user_role`.`role_id` AS `role_id`,`paasos`.`sys_user_role`.`user_id` AS `user_id` from `paasos`.`sys_user_role` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-08-11 11:30:52

INSERT INTO `paas_framework` VALUES ('dubbox', 'java', 'dubbox', 'Dubbox', 'base/dubbo:2.0', 'api', 'FROM ${paasos_docker_registry}/${imageId}\n\nMAINTAINER remoting <remoting@qq.com>\n\nCOPY /tools/${fileName}  /data/app.zip\n\nRUN unzip -o /data/app.zip -d /data/\n\n');
INSERT INTO `paas_framework` VALUES ('jersey', 'java', 'jersey', 'Jersey', 'base/tomcat:2.0', 'api', 'FROM ${paasos_docker_registry}/${imageId}\n\nMAINTAINER remoting <remoting@qq.com>\n\nCOPY /tools/${fileName}  /data/webapps/ROOT.war\n\n');
INSERT INTO `paas_framework` VALUES ('springboot+jersey', 'java', 'springboot+jersey', 'Spring Boot + Jersey', 'base/springboot:2.0', 'api', 'FROM ${paasos_docker_registry}/${imageId}\r\n\r\nMAINTAINER weiyujia <315593147@qq.com>\r\n\r\nCOPY /tools/${fileName}  /opt/app.jar');
INSERT INTO `paas_framework` VALUES ('springmvc+mybatis', 'java', 'springmvc+mybatis', 'SpringMVC+Mybatis', 'base/tomcat:2.0', 'app', 'FROM ${paasos_docker_registry}/${imageId}\n\nMAINTAINER remoting <remoting@qq.com>\n\nCOPY /tools/${fileName}  /data/webapps/ROOT.war\n\n');
