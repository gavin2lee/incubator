drop table if exists t_article_catalog;
CREATE TABLE `t_article_catalog` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(45) COLLATE utf8_unicode_ci NOT NULL COMMENT '分类名称',
  `pid` int(11) DEFAULT '0' COMMENT '父级ID',
  `order` int(11) DEFAULT NULL COMMENT '顺序',
  `type` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '类型',
  `code` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '分类编码',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`)
) comment='文章分类';