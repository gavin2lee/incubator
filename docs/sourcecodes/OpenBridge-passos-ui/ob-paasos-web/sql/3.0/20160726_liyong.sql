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
