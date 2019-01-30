DROP TABLE IF EXISTS `sys_version`;
CREATE TABLE `sys_version` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `contact_date` varchar(16) COMMENT '日期',
  `contact_time` varchar(16) COMMENT '时间',
  `version` varchar(20)  NOT NULL COMMENT '系统版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统版本表';

INSERT INTO `sys_version` VALUES (100001, '2019-01-29 17:26:42', '2019-01-29 17:26:42', NULL, NULL, '1');

-- 每次版本更新需要注意几点

-- 1、更新文件到 application.properties 里面配置好的路径下面
-- #下载文件路径
-- download.file.path=E:/debug/neighbor
-- #下载文件名称
-- download.file.name=neighbor.apk

-- 2、更新表sys_version，新增一条最新的记录
