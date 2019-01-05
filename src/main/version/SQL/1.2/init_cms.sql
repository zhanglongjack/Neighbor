DROP TABLE IF EXISTS `cms`;
CREATE TABLE `cms` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `contact_date` varchar(16) COMMENT '日期',
  `contact_time` varchar(16) COMMENT '时间',
  `cms_type` char(1)  NOT NULL COMMENT '类型,1：图片；2：文字；',
  `position` char(1)  NOT NULL COMMENT '位置,1：index页面；2：商城页面；',
  `url` varchar(200)  COMMENT '如果类型是图片的时候，这个值就是图片的地址',
  `cms_desc` varchar(50) COMMENT '备注',
  `jump_url` varchar(200)   COMMENT '跳转页面',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='内容管理表';

 ALTER TABLE `neighbor_dev`.`packet` 
ADD COLUMN `random_amount` VARCHAR(256) NULL COMMENT '各个红包的金额,逗号分隔' AFTER `remarke`;