DROP TABLE IF EXISTS `os_nginx_host`;
CREATE TABLE `os_nginx_host` (
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


DROP TABLE IF EXISTS `os_nginx_conf`;
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

insert into paasos.os_nginx_host select * FROM openbridge.sys_host where host_type='nginx';
