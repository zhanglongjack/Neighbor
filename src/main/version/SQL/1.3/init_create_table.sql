
DROP TABLE IF EXISTS `robot_config`;
CREATE TABLE `robot_config` (
   `robot_id` INT NOT NULL AUTO_INCREMENT  COMMENT '主键',
  `hit_chance` DECIMAL(3,2) NOT NULL DEFAULT 0.00 COMMENT '中雷概率',
  `grap_chance` DECIMAL(3,2) NOT NULL DEFAULT 0.00 COMMENT '抢包频率',
  `send_packet_chance` DECIMAL(3,2) NOT NULL DEFAULT 0.00 COMMENT '发包频率',
  status int not null default '0' comment '状态:0-未启动,1-已启动',
  `update_date_time` BIGINT(20) not NULL  COMMENT '更新时间(时间的长整型)',
  PRIMARY KEY (`robot_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 100000
COMMENT = '机器人配置表';

 

INSERT INTO `neighbor_dev`.`dictionary` (`name`, `code`, `status`, `biz_code`, `remarke`) VALUES ('未知', '0', '1', 'sex', '性别');
INSERT INTO `neighbor_dev`.`dictionary` (`name`, `code`, `status`, `biz_code`, `remarke`) VALUES ('男', '1', '1', 'sex', '性别');
INSERT INTO `neighbor_dev`.`dictionary` (`name`, `code`, `status`, `biz_code`, `remarke`) VALUES ('女', '2', '1', 'sex', '性别');
commit;

alter table users_info add re_code varchar(50) DEFAULT null COMMENT '我的推荐码';