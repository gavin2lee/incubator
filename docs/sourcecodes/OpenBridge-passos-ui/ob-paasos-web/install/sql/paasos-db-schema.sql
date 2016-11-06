-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: 192.168.10.82    Database: paasos
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

CREATE DATABASE paasos
  DEFAULT CHARACTER SET utf8
  DEFAULT COLLATE utf8_general_ci;
USE paasos;
SET NAMES utf8;

--
-- Table structure for table `os_base_build`
--

DROP TABLE IF EXISTS `os_base_build`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `os_base_build` (
  `id` varchar(32) NOT NULL,
  `name` varchar(45) NOT NULL,
  `file_type` varchar(10) NOT NULL COMMENT 'zip,tar.gz',
  `file_path` varchar(200) NOT NULL COMMENT '文件保存路径',
  `version` varchar(20) DEFAULT NULL,
  `command` varchar(128) DEFAULT NULL COMMENT '启动命令',
  `work_dir` varchar(128) DEFAULT NULL COMMENT '工作目录',
  `ports` varchar(1024) DEFAULT NULL COMMENT '镜像端口',
  `tenant_ids` varchar(1000) DEFAULT NULL COMMENT '可以使用此镜像的组织',
  `status` smallint(4) DEFAULT NULL,
  `build_logs` mediumtext,
  `description` text COMMENT '描述',
  `image_id` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `file_data` varchar(1024) DEFAULT NULL,
  `icon_path` varchar(200) DEFAULT NULL,
  `dockerfile` text NOT NULL COMMENT 'dockerfile内容',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_un` (`name`,`version`),
  KEY `FK_BASE_BUILD_IMAGE` (`image_id`),
  CONSTRAINT `FK_BASE_BUILD_IMAGE` FOREIGN KEY (`image_id`) REFERENCES `os_image` (`image_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='基础镜像';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `os_build_log`
--

DROP TABLE IF EXISTS `os_build_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `os_build_log` (
  `id` varchar(32) NOT NULL,
  `bus_id` varchar(32) NOT NULL,
  `build_logs` mediumtext COMMENT '构建日志',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='构建日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `os_image`
--

DROP TABLE IF EXISTS `os_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `os_image` (
  `image_id` varchar(32) NOT NULL,
  `image_type` varchar(45) DEFAULT NULL COMMENT '镜像类型 取值分为 [app|base|store]',
  `image_name` varchar(200) NOT NULL,
  `image_version` varchar(45) NOT NULL,
  `ports` varchar(1024) DEFAULT NULL,
  `command` varchar(1024) DEFAULT NULL,
  `args` varchar(1024) DEFAULT NULL,
  `workdir` varchar(1024) DEFAULT NULL,
  `docker_file` varchar(45) DEFAULT NULL,
  `build_type` varchar(45) DEFAULT NULL COMMENT 'dockerfile|tar.gz  构建类型，线下构建上传镜像包',
  `build_file` varchar(45) DEFAULT NULL,
  `build_status` varchar(45) DEFAULT NULL COMMENT 'upload->building->success|failure',
  `r_image_id` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`image_id`),
  UNIQUE KEY `img_un` (`image_name`,`image_version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `os_nginx_conf`
--

DROP TABLE IF EXISTS `os_nginx_conf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `os_nginx_conf` (
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
-- Table structure for table `os_nginx_host`
--

DROP TABLE IF EXISTS `os_nginx_host`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `os_nginx_host` (
  `host_id` varchar(32) NOT NULL,
  `host_ip` varchar(45) NOT NULL,
  `host_user` varchar(45) NOT NULL,
  `host_key_prv` varchar(2048) DEFAULT NULL,
  `host_key_pub` varchar(1024) DEFAULT NULL,
  `host_port` varchar(45) NOT NULL,
  `env_type` varchar(10) NOT NULL,
  `host_type` varchar(45) NOT NULL,
  `host_platform` varchar(10) DEFAULT NULL COMMENT 'Ӧ�õ����ͣ�api or app',
  `backup_host` varchar(45) NOT NULL COMMENT '���÷�����',
  `virtual_host` varchar(45) NOT NULL COMMENT 'used for dns resolver for HA',
  `directory_name` varchar(45) NOT NULL COMMENT 'used for store .conf file',
  PRIMARY KEY (`host_id`),
  UNIQUE KEY `i_host_ip` (`host_ip`,`host_type`) USING BTREE,
  UNIQUE KEY `unq_platform_dir` (`host_platform`,`directory_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `os_node`
--

DROP TABLE IF EXISTS `os_node`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `os_node` (
  `id` varchar(32) NOT NULL,
  `name` varchar(45) NOT NULL,
  `env_type` varchar(20) DEFAULT NULL COMMENT '环境类型。live：生成环境    test：测试环境',
  `org_id` varchar(32) NOT NULL COMMENT '关联组织',
  PRIMARY KEY (`id`),
  KEY `FK_NODE_TENANT_ID` (`org_id`),
  CONSTRAINT `FK_NODE_TENANT_ID` FOREIGN KEY (`org_id`) REFERENCES `sys_tenant` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='节点表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `os_preset_app`
--

DROP TABLE IF EXISTS `os_preset_app`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `os_preset_app` (
  `id` varchar(32) NOT NULL,
  `name` varchar(45) NOT NULL,
  `app_name` varchar(45) NOT NULL COMMENT '应用名称',
  `file_type` varchar(10) NOT NULL COMMENT 'zip,tar.gz',
  `file_path` varchar(200) NOT NULL COMMENT '文件保存路径',
  `file_data` varchar(1024) DEFAULT NULL,
  `icon_path` varchar(200) DEFAULT NULL,
  `version` varchar(20) DEFAULT NULL,
  `command` varchar(128) DEFAULT NULL COMMENT '启动命令',
  `work_dir` varchar(128) DEFAULT NULL COMMENT '工作目录',
  `ports` varchar(1024) DEFAULT NULL COMMENT '镜像端口',
  `status` smallint(4) DEFAULT NULL COMMENT '状态。1已保存 5创建中 6变更中 10操作成功 0创建失败 11删除失败',
  `build_logs` mediumtext COMMENT '构建日志',
  `documentation` text NOT NULL COMMENT '详细说明文档',
  `description` varchar(450) DEFAULT NULL COMMENT '描述',
  `image_id` varchar(32) DEFAULT NULL COMMENT '关联os_image 记录',
  `update_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `config` mediumtext COMMENT '配置',
  `dockerfile` text NOT NULL COMMENT 'dockerfile内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='预置应用';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `os_project`
--

DROP TABLE IF EXISTS `os_project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `os_project` (
  `project_id` varchar(32) NOT NULL COMMENT '主键',
  `project_type` varchar(45) DEFAULT NULL COMMENT 'Openbridge API | Openbridge APP | Store | GitHub',
  `business_id` varchar(45) DEFAULT NULL,
  `create_user` varchar(32) NOT NULL,
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `project_name` varchar(200) NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  `project_code` varchar(55) DEFAULT NULL,
  `tenant_id` varchar(32) NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`project_id`),
  UNIQUE KEY `u_p_c` (`project_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `os_project_build`
--

DROP TABLE IF EXISTS `os_project_build`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `os_project_build` (
  `build_id` varchar(32) NOT NULL,
  `image_id` varchar(256) DEFAULT NULL,
  `build_logs` mediumtext,
  `build_time` datetime DEFAULT NULL,
  `build_success` datetime DEFAULT NULL COMMENT '构建成功日期',
  `version_id` varchar(32) DEFAULT NULL COMMENT '版本ID',
  `version_code` varchar(32) DEFAULT NULL COMMENT '版本code',
  `file_path` varchar(2500) DEFAULT NULL,
  `file_name` varchar(200) DEFAULT NULL COMMENT '文件名',
  `build_name` varchar(200) DEFAULT NULL COMMENT '构建名称',
  `build_tag` varchar(200) DEFAULT NULL COMMENT '构建标签',
  `port` varchar(2000) DEFAULT NULL COMMENT '镜像端口',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '状态 1保存  5创建中 10创建成功 0创建失败',
  `project_id` varchar(32) DEFAULT NULL COMMENT '项目ID',
  `delete_status` int(11) DEFAULT NULL COMMENT '镜像删除状态',
  `image_name` varchar(200) DEFAULT NULL COMMENT '在哪个镜像的基础上构建新的镜像',
  `build_no` varchar(32) DEFAULT NULL COMMENT 'api app jenkis调用的时候 构建序号',
  PRIMARY KEY (`build_id`),
  UNIQUE KEY `p_tag` (`project_id`,`build_tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `os_project_deploy`
--

DROP TABLE IF EXISTS `os_project_deploy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `os_project_deploy` (
  `deploy_id` varchar(32) NOT NULL,
  `project_id` varchar(32) NOT NULL,
  `deploy_name` varchar(45) NOT NULL COMMENT '部署名称',
  `description` varchar(1024) DEFAULT NULL COMMENT '描述',
  `tenant_id` varchar(32) NOT NULL COMMENT '租户ID',
  `env_type` varchar(45) NOT NULL COMMENT '环境类型取值[dev|test|live]',
  `create_user` varchar(32) NOT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `service_ip` varchar(45) DEFAULT NULL COMMENT '服务IP',
  `public_ip` varchar(45) DEFAULT NULL COMMENT '外部访问IP',
  `replicas` int(11) NOT NULL COMMENT '副本数',
  `restart_policy` varchar(45) DEFAULT NULL COMMENT '容器重启策略 always',
  `modify_user` varchar(45) DEFAULT NULL COMMENT '最后一次修改用户',
  `modify_time` datetime DEFAULT NULL COMMENT '最后一次修改时间',
  `cpu_` double DEFAULT NULL COMMENT 'cpu 默认单位为个',
  `memory_` double DEFAULT NULL COMMENT '内存 默认单位为m',
  `env_variable` varchar(1024) DEFAULT NULL COMMENT '环境变量',
  `compute_config` varchar(2024) DEFAULT NULL COMMENT '服务器配置',
  `build_id` varchar(32) DEFAULT NULL COMMENT '构建id',
  `status` int(11) DEFAULT NULL COMMENT '发布状态',
  `delete_status` int(11) DEFAULT NULL COMMENT '删除状态',
  `owner_cluster` varchar(200) DEFAULT NULL COMMENT '所在集群',
  `labels` varchar(2000) DEFAULT NULL COMMENT 'rc的标签',
  `deploy_code` varchar(100) DEFAULT NULL COMMENT '业务描述限制英文',
  `image_id` varchar(32) DEFAULT NULL,
  `resource_config` varchar(1024) DEFAULT NULL COMMENT '依赖资源 json格式',
  `env_id` varchar(32) DEFAULT NULL,
  `extra_data` varchar(2000) DEFAULT NULL,
  `extra_key` varchar(100) DEFAULT NULL,
  `deploy_time` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`deploy_id`),
  UNIQUE KEY `s_ip_un` (`service_ip`),
  UNIQUE KEY `v_ip_un` (`public_ip`),
  UNIQUE KEY `u_d_c` (`deploy_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `os_project_deploy_env`
--

DROP TABLE IF EXISTS `os_project_deploy_env`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `os_project_deploy_env` (
  `id` varchar(32) NOT NULL,
  `deploy_id` varchar(32) DEFAULT NULL,
  `key_` varchar(200) DEFAULT NULL,
  `value_` varchar(1000) DEFAULT NULL,
  `resource_id` varchar(120) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `os_project_deploy_port`
--

DROP TABLE IF EXISTS `os_project_deploy_port`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `os_project_deploy_port` (
  `port_id` varchar(32) NOT NULL,
  `deploy_id` varchar(45) NOT NULL,
  `node_port` int(11) DEFAULT NULL,
  `target_port` int(11) NOT NULL,
  `port_key` varchar(45) DEFAULT NULL,
  `port_remark` varchar(45) DEFAULT NULL,
  `port_protocol` varchar(45) NOT NULL,
  PRIMARY KEY (`port_id`),
  UNIQUE KEY `node_port_UNIQUE` (`node_port`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `os_project_deploy_volumn`
--

DROP TABLE IF EXISTS `os_project_deploy_volumn`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `os_project_deploy_volumn` (
  `id` varchar(32) NOT NULL,
  `name` varchar(250) DEFAULT NULL COMMENT '名称',
  `type` varchar(32) DEFAULT NULL COMMENT '类型',
  `mount` varchar(250) DEFAULT NULL COMMENT '挂载点',
  `capacity` varchar(10) DEFAULT NULL COMMENT '数据卷',
  `volumn_id` varchar(32) DEFAULT NULL COMMENT '关联实际id',
  `deploy_id` varchar(32) DEFAULT NULL,
  `allocate_content` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部署挂载点';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `os_project_env`
--

DROP TABLE IF EXISTS `os_project_env`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `os_project_env` (
  `env_id` varchar(32) NOT NULL,
  `env_name` varchar(45) DEFAULT NULL,
  `env_type` varchar(10) DEFAULT NULL,
  `project_id` varchar(32) DEFAULT NULL,
  `business_type` varchar(10) DEFAULT NULL,
  `creator` varchar(32) DEFAULT NULL,
  `creation_time` datetime DEFAULT NULL,
  `env_mark` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`env_id`),
  UNIQUE KEY `u_b_t_n` (`env_name`,`env_type`,`project_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `os_project_env_res`
--

DROP TABLE IF EXISTS `os_project_env_res`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `os_project_env_res` (
  `record_id` varchar(32) NOT NULL,
  `env_id` varchar(32) NOT NULL,
  `resource_id` varchar(45) NOT NULL COMMENT '资源类型',
  `apply_config` mediumtext COMMENT '界面上填写的信息',
  `result_config` mediumtext COMMENT '申请之后审批返回的参数',
  `res_addition` varchar(1024) DEFAULT NULL COMMENT '复用: 数据库为数据库名称，nfs为挂载目录',
  `paasos_res_id` varchar(32) DEFAULT NULL COMMENT 'paasos 资源id',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `uniqueEnvResource` (`env_id`,`resource_id`) USING BTREE,
  KEY `i_b_r` (`env_id`,`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `os_project_log`
--

DROP TABLE IF EXISTS `os_project_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `os_project_log` (
  `id` varchar(32) NOT NULL,
  `key_` varchar(32) DEFAULT NULL,
  `type_` varchar(30) DEFAULT NULL,
  `message_` varchar(2000) DEFAULT NULL,
  `create_date_` mediumtext,
  `create_user_` varchar(32) DEFAULT NULL,
  `begin_` varchar(1) DEFAULT NULL,
  `session_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `os_resource`
--

DROP TABLE IF EXISTS `os_resource`;
/*!50001 DROP VIEW IF EXISTS `os_resource`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `os_resource` AS SELECT 
 1 AS `res_id`,
 1 AS `res_name`,
 1 AS `apply_content`,
 1 AS `creater`,
 1 AS `create_date`,
 1 AS `update_date`,
 1 AS `allocate_content`,
 1 AS `tenant_id`,
 1 AS `project_id`,
 1 AS `env_id`,
 1 AS `env_type`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `os_resource_mysql`
--

DROP TABLE IF EXISTS `os_resource_mysql`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `os_resource_mysql` (
  `mysql_id` varchar(32) NOT NULL COMMENT 'Ö÷¼ü',
  `instance_name` varchar(256) NOT NULL COMMENT 'ÊµÀýÃû³Æ',
  `mysql_type` varchar(32) NOT NULL COMMENT 'ÀàÐÍ£ºµ¥»ú£¬¹²Ïí£¬¼¯Èº',
  `apply_content` varchar(1024) NOT NULL COMMENT 'ÉêÇë×ÊÔ´²ÎÊýÐÅÏ¢',
  `creater` varchar(32) NOT NULL COMMENT 'ÉêÇëÕß»ò´´½¨Õß',
  `create_date` datetime NOT NULL COMMENT 'ÉêÇëÊ±¼ä',
  `update_date` datetime NOT NULL COMMENT 'ÐÞ¸ÄÊ±¼ä',
  `allocate_content` varchar(1024) DEFAULT NULL,
  `tenant_id` varchar(32) NOT NULL COMMENT '×â»§id',
  `project_id` varchar(32) DEFAULT NULL COMMENT 'ÏîÄ¿id: appId»òserviceId',
  `env_id` varchar(32) DEFAULT NULL,
  `env_type` varchar(20) NOT NULL COMMENT '环境类型:测试、生产',
  `on_ready` bit(1) NOT NULL DEFAULT b'0',
  `res_desc` tinytext COMMENT '资源描述',
  `apply_type` varchar(10) DEFAULT NULL COMMENT '申请应用的来源类型,api,app or others',
  `cpu_` double DEFAULT NULL,
  `memory_` double DEFAULT NULL,
  `storage_` double DEFAULT NULL,
  PRIMARY KEY (`mysql_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `os_resource_nas`
--

DROP TABLE IF EXISTS `os_resource_nas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `os_resource_nas` (
  `nas_id` varchar(32) NOT NULL COMMENT '网络存储id',
  `instance_name` varchar(256) NOT NULL COMMENT '网络存储名称',
  `nas_source` varchar(32) NOT NULL COMMENT '申请来源：PaaSOS-UI or PaaSOS',
  `apply_content` varchar(1024) NOT NULL COMMENT '申请资源参数',
  `creater` varchar(32) NOT NULL COMMENT '申请者',
  `create_date` datetime NOT NULL COMMENT '申请时间',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `allocate_content` varchar(2000) DEFAULT NULL,
  `tenant_id` varchar(32) NOT NULL COMMENT '租户id',
  `project_id` varchar(32) DEFAULT NULL COMMENT '项目id: appId或serviceId',
  `env_id` varchar(32) DEFAULT NULL,
  `env_type` varchar(20) NOT NULL COMMENT '环境类型:测试、生产',
  `nas_type` varchar(20) NOT NULL,
  `res_desc` tinytext COMMENT '资源描述',
  `on_ready` bit(1) DEFAULT NULL,
  `on_status` int(11) DEFAULT NULL,
  `apply_type` varchar(10) DEFAULT NULL COMMENT '申请应用的来源类型,api,app or others',
  `cpu_` double DEFAULT NULL,
  `memory_` double DEFAULT NULL,
  `storage_` double DEFAULT NULL,
  PRIMARY KEY (`nas_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `os_resource_rabbitmq`
--

DROP TABLE IF EXISTS `os_resource_rabbitmq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `os_resource_rabbitmq` (
  `mq_id` varchar(32) NOT NULL COMMENT 'Ö÷¼ü',
  `mq_name` varchar(256) NOT NULL COMMENT 'ÊµÀýÃû³Æ',
  `apply_content` varchar(1024) NOT NULL COMMENT 'ÉêÇë×ÊÔ´²ÎÊýÐÅÏ¢',
  `creater` varchar(32) NOT NULL COMMENT 'ÉêÇëÕß»ò´´½¨Õß',
  `create_date` datetime NOT NULL COMMENT 'ÉêÇëÊ±¼ä',
  `update_date` datetime NOT NULL COMMENT 'ÐÞ¸ÄÊ±¼ä',
  `allocate_content` varchar(1024) NOT NULL COMMENT '·ÖÅä×ÊÔ´²ÎÊýÐÅÏ¢',
  `tenant_id` varchar(32) NOT NULL COMMENT '×â»§id',
  `project_id` varchar(32) DEFAULT NULL COMMENT 'ÏîÄ¿id: appId»òserviceId',
  `env_id` varchar(32) DEFAULT NULL,
  `env_type` varchar(20) NOT NULL COMMENT '环境类型:测试、生产',
  `on_ready` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否创建完毕',
  `res_desc` tinytext COMMENT '资源描述',
  `apply_type` varchar(10) DEFAULT NULL COMMENT '申请应用的来源类型,api,app or others',
  `cpu_` double DEFAULT NULL,
  `memory_` double DEFAULT NULL,
  `storage_` double DEFAULT NULL,
  PRIMARY KEY (`mq_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `os_resource_redis`
--

DROP TABLE IF EXISTS `os_resource_redis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `os_resource_redis` (
  `redis_id` varchar(32) NOT NULL COMMENT 'Ö÷¼ü',
  `redis_name` varchar(256) NOT NULL COMMENT 'ÊµÀýÃû³Æ',
  `apply_content` varchar(1024) NOT NULL COMMENT 'ÉêÇë×ÊÔ´²ÎÊýÐÅÏ¢',
  `creater` varchar(32) NOT NULL COMMENT 'ÉêÇëÕß»ò´´½¨Õß',
  `create_date` datetime NOT NULL COMMENT 'ÉêÇëÊ±¼ä',
  `update_date` datetime NOT NULL COMMENT 'ÐÞ¸ÄÊ±¼ä',
  `allocate_content` varchar(2000) DEFAULT NULL,
  `tenant_id` varchar(32) NOT NULL COMMENT '×â»§id',
  `project_id` varchar(32) DEFAULT NULL COMMENT 'ÏîÄ¿id: appId»òserviceId',
  `env_id` varchar(32) DEFAULT NULL,
  `env_type` varchar(20) NOT NULL COMMENT '环境类型:测试、生产',
  `on_ready` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否创建完毕',
  `res_desc` tinytext COMMENT '资源描述',
  `apply_type` varchar(10) DEFAULT NULL COMMENT '申请应用的来源类型,api,app or others',
  `cpu_` double DEFAULT NULL,
  `memory_` double DEFAULT NULL,
  `storage_` double DEFAULT NULL,
  PRIMARY KEY (`redis_id`)
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
-- Table structure for table `sys_department`
--

DROP TABLE IF EXISTS `sys_department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_department` (
  `dept_id` varchar(32) NOT NULL,
  `dept_name` varchar(50) NOT NULL COMMENT '部门名称',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '上级部门',
  `hierarchy_id` varchar(2000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `d_order` int(5) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`dept_id`),
  UNIQUE KEY `depart_name_unique` (`dept_name`,`parent_id`) USING BTREE,
  KEY `i_create_user_idx` (`create_user`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门组织';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_func`
--

DROP TABLE IF EXISTS `sys_func`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_func` (
  `func_id` varchar(32) NOT NULL,
  `func_name` varchar(250) NOT NULL,
  `func_desc` varchar(250) NOT NULL,
  `func_system` varchar(45) NOT NULL,
  `func_module` varchar(100) DEFAULT NULL COMMENT '所属模块',
  PRIMARY KEY (`func_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_group`
--

DROP TABLE IF EXISTS `sys_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_group` (
  `group_id` varchar(32) NOT NULL,
  `group_name` varchar(50) NOT NULL,
  `create_time` datetime NOT NULL,
  `create_user` varchar(32) NOT NULL,
  PRIMARY KEY (`group_id`),
  UNIQUE KEY `group_name_unique` (`group_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_login_logs`
--

DROP TABLE IF EXISTS `sys_login_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_login_logs` (
  `log_id` varchar(32) NOT NULL,
  `login_name` varchar(45) NOT NULL COMMENT '登录名',
  `login_param` varchar(256) DEFAULT NULL COMMENT '登陆参数，密码或者token',
  `login_time` datetime NOT NULL COMMENT '登陆时间',
  `login_type` int(11) NOT NULL COMMENT '登陆类型 1 为 SSO 登陆 0  为正常登录',
  `client_ip` varchar(45) DEFAULT NULL COMMENT '客户端IP',
  `user_agent` varchar(1024) DEFAULT NULL COMMENT '浏览器UserAgent',
  `login_sys` varchar(32) NOT NULL COMMENT '登陆系统 api,app,paasos',
  `user_id` varchar(45) NOT NULL,
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role` (
  `role_id` varchar(32) NOT NULL,
  `role_name` varchar(250) NOT NULL,
  `role_desc` varchar(250) NOT NULL,
  `role_system` varchar(45) NOT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_role_func`
--

DROP TABLE IF EXISTS `sys_role_func`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role_func` (
  `id` varchar(32) NOT NULL,
  `role_id` varchar(250) NOT NULL,
  `func_id` varchar(250) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_tenant`
--

DROP TABLE IF EXISTS `sys_tenant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_tenant` (
  `tenant_id` varchar(32) NOT NULL,
  `tenant_name` varchar(45) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `admin_id` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_tenant_preset`
--

DROP TABLE IF EXISTS `sys_tenant_preset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_tenant_preset` (
  `id` varchar(32) NOT NULL,
  `tenant_id` varchar(32) DEFAULT NULL COMMENT '租户ID',
  `preset_id` varchar(32) DEFAULT NULL COMMENT '预置应用ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_tenant_quota`
--

DROP TABLE IF EXISTS `sys_tenant_quota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_tenant_quota` (
  `id` varchar(32) NOT NULL,
  `tenant_id` varchar(32) NOT NULL COMMENT '租户ID',
  `category_type` varchar(32) NOT NULL,
  `sub_category_type` varchar(32) NOT NULL,
  `item_lookup_type` varchar(32) NOT NULL,
  `quota` varchar(8) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_tenant_quota_item`
--

DROP TABLE IF EXISTS `sys_tenant_quota_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_tenant_quota_item` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `code` varchar(32) NOT NULL COMMENT '编码和code_index 联合主键',
  `parent_code` varchar(32) DEFAULT NULL COMMENT '父级编码',
  `code_display` varchar(32) DEFAULT NULL COMMENT '索引key',
  `code_desc` varchar(32) DEFAULT NULL COMMENT '索引value',
  `units` varchar(32) DEFAULT NULL COMMENT '单位',
  `default_value` varchar(32) DEFAULT NULL COMMENT '配额默认值',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQ_ID` (`id`) USING BTREE,
  UNIQUE KEY `UNIQ_CODE_INDEX` (`code`,`code_display`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_tenant_relation`
--

DROP TABLE IF EXISTS `sys_tenant_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_tenant_relation` (
  `rel_id` varchar(32) NOT NULL,
  `tenant_id` varchar(32) NOT NULL COMMENT '组织ID',
  `user_id` varchar(32) NOT NULL COMMENT '人员ID',
  PRIMARY KEY (`rel_id`),
  UNIQUE KEY `u_id_un` (`user_id`),
  KEY `FK_ORG_MEMBER_ORG_ID` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织成员关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user` (
  `user_id` varchar(32) NOT NULL,
  `user_name` varchar(45) NOT NULL,
  `login_name` varchar(45) DEFAULT NULL,
  `login_password` varchar(32) DEFAULT NULL,
  `roles` varchar(1024) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `activate` bit(1) NOT NULL,
  `email` varchar(45) DEFAULT NULL,
  `token` varchar(32) DEFAULT NULL,
  `sys_user` bit(1) NOT NULL DEFAULT b'0',
  `api_auth_key` varchar(32) DEFAULT NULL COMMENT '服务调用密码',
  `mobile` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `login_name_UNIQUE` (`login_name`) USING BTREE,
  UNIQUE KEY `i_user_token` (`token`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_user_dept`
--

DROP TABLE IF EXISTS `sys_user_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user_dept` (
  `relation_id` varchar(32) NOT NULL,
  `user_id` varchar(45) DEFAULT NULL,
  `dep_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`relation_id`),
  KEY `FK_user_dep_relation_user_idx` (`user_id`) USING BTREE,
  KEY `FK_user_dep_relation_dep_idx` (`dep_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户组织关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_user_group`
--

DROP TABLE IF EXISTS `sys_user_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user_group` (
  `relation_id` varchar(32) NOT NULL,
  `user_id` varchar(32) NOT NULL,
  `group_id` varchar(32) NOT NULL,
  PRIMARY KEY (`relation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user_role` (
  `id` varchar(32) NOT NULL,
  `role_id` varchar(250) NOT NULL,
  `user_id` varchar(250) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Final view structure for view `os_resource`
--

/*!50001 DROP VIEW IF EXISTS `os_resource`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `os_resource` AS select `os_resource_mysql`.`mysql_id` AS `res_id`,`os_resource_mysql`.`instance_name` AS `res_name`,`os_resource_mysql`.`apply_content` AS `apply_content`,`os_resource_mysql`.`creater` AS `creater`,`os_resource_mysql`.`create_date` AS `create_date`,`os_resource_mysql`.`update_date` AS `update_date`,`os_resource_mysql`.`allocate_content` AS `allocate_content`,`os_resource_mysql`.`tenant_id` AS `tenant_id`,`os_resource_mysql`.`project_id` AS `project_id`,`os_resource_mysql`.`env_id` AS `env_id`,`os_resource_mysql`.`env_type` AS `env_type` from `os_resource_mysql` union all select `os_resource_rabbitmq`.`mq_id` AS `res_id`,`os_resource_rabbitmq`.`mq_name` AS `res_name`,`os_resource_rabbitmq`.`apply_content` AS `apply_content`,`os_resource_rabbitmq`.`creater` AS `creater`,`os_resource_rabbitmq`.`create_date` AS `create_date`,`os_resource_rabbitmq`.`update_date` AS `update_date`,`os_resource_rabbitmq`.`allocate_content` AS `allocate_content`,`os_resource_rabbitmq`.`tenant_id` AS `tenant_id`,`os_resource_rabbitmq`.`project_id` AS `project_id`,`os_resource_rabbitmq`.`env_id` AS `env_id`,`os_resource_rabbitmq`.`env_type` AS `env_type` from `os_resource_rabbitmq` union all select `os_resource_redis`.`redis_id` AS `res_id`,`os_resource_redis`.`redis_name` AS `res_name`,`os_resource_redis`.`apply_content` AS `apply_content`,`os_resource_redis`.`creater` AS `creater`,`os_resource_redis`.`create_date` AS `create_date`,`os_resource_redis`.`update_date` AS `update_date`,`os_resource_redis`.`allocate_content` AS `allocate_content`,`os_resource_redis`.`tenant_id` AS `tenant_id`,`os_resource_redis`.`project_id` AS `project_id`,`os_resource_redis`.`env_id` AS `env_id`,`os_resource_redis`.`env_type` AS `env_type` from `os_resource_redis` union all select `os_resource_nas`.`nas_id` AS `res_id`,`os_resource_nas`.`instance_name` AS `res_name`,`os_resource_nas`.`apply_content` AS `apply_content`,`os_resource_nas`.`creater` AS `creater`,`os_resource_nas`.`create_date` AS `create_date`,`os_resource_nas`.`update_date` AS `update_date`,`os_resource_nas`.`allocate_content` AS `allocate_content`,`os_resource_nas`.`tenant_id` AS `tenant_id`,`os_resource_nas`.`project_id` AS `project_id`,`os_resource_nas`.`env_id` AS `env_id`,`os_resource_nas`.`env_type` AS `env_type` from `os_resource_nas` */;
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

-- Dump completed on 2016-08-11 11:32:46

INSERT INTO `sys_role` (`role_id`, `role_name`, `role_desc`, `role_system`) VALUES ('app.architect', 'APP架构师', '架构师，负责创建APP', 'app');
INSERT INTO `sys_role` (`role_id`, `role_name`, `role_desc`, `role_system`) VALUES ('app.manager', '管理员', '查看报表', 'app');


INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('app.base.create', '创建APP', '创建APP', 'app', '应用管理');
INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('app.base.edit', '修改APP', '修改APP，分配开发运维人员', 'app', '应用管理');
INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('app.project.manage', 'Jira SVN', '修改APP与Jira的关联，SVN服务器期绑定', 'app', '应用管理');
INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('app.env.manage', 'APP的环境配置', 'APP的环境配置', 'app', '应用管理');
INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('app.dev.manage', 'APP的版本编译', 'APP的版本编译', 'app', '应用管理');
INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('app.monitor.manage', '监控APP的环境数据', '监控APP的环境数据', 'app', '应用管理');
INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('app.envlive.manage', 'APP的生产环境配置', 'APP的生产环境配置', 'app', '应用管理');

INSERT INTO `sys_role` (`role_id`, `role_name`, `role_desc`, `role_system`) VALUES ('api.architect', 'api架构师', '架构师对服务拥有所有权限', 'api');
INSERT INTO `sys_role` (`role_id`, `role_name`, `role_desc`, `role_system`) VALUES ('api.manager', 'api管理员', '超级管理员的权限包括：拥有系统的所有操作权限', 'api');

INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('api.service.create', '创建服务', '创建服务和导入服务', 'api', '服务管理');
INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('api.service.edit', '编辑服务', '修改服务基本信息', 'api', '服务管理');
INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('api.service.project', '服务项目', '修改服务的项目信息', 'api', '服务管理');
INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('api.service.define', '服务定义', '服务版本定义', 'api', '服务管理');
INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('api.service.dev', '服务开发', '服务开发', 'api', '服务管理');
INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('api.service.env', '服务环境', '服务环境', 'api', '服务管理');
INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('api.service.envlive', '服务生产环境', '服务生产环境', 'api', '服务管理');
INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('api.service.monitor', '服务监控', '服务监控', 'api', '服务管理');
INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('api.service.workflow', '服务审批', '服务审批', 'api', '服务管理');
 
INSERT INTO `sys_role` (`role_id`, `role_name`, `role_desc`, `role_system`) VALUES ('paasos.manager', '环境管理员', '可以对基础镜像、节点及组织进行管理', 'paasos');
INSERT INTO `sys_role` (`role_id`, `role_name`, `role_desc`, `role_system`) VALUES ('paasos.store', '预置应用管理员', '对预置应用管理,预置应用的上传更新构建部署', 'paasos');
INSERT INTO `sys_role` (`role_id`, `role_name`, `role_desc`, `role_system`) VALUES ('passos.operations', '运维人员', '对系统的运行及资源节点进行监控', 'paasos');

INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('paasos.tenant.manager', '组织管理', '组织管理', 'paasos', '环境管理');
INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('paasos.node.manager', '节点管理', '节点管理', 'paasos', '环境管理');
INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('paasos.baseimage.manager', '基础镜像', '基础镜像', 'paasos', '环境管理');
INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('paasos.deploy.manager', '部署列表', '部署列表', 'paasos', '环境管理');
INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('paasos.store.manager', '预置应用上传更新', '预置应用上传更新', 'paasos', '预置应用管理');
INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('paasos.store.deploy', '预置应用构建部署', '预置应用构建部署', 'paasos', '预置应用管理');
INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('paasos.project.envtest', '测试环境部署', '测试环境部署', 'paasos', '项目管理'); 
INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('paasos.project.envlive', '生产环境部署', '生产环境部署', 'paasos', '项目管理'); 
INSERT INTO `sys_func` (`func_id`, `func_name`, `func_desc`, `func_system`, `func_module`) VALUES ('paasos.project.build', '项目构建', '项目构建', 'paasos', '项目管理'); 
