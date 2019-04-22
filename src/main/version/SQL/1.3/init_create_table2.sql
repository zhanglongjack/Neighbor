
DROP TABLE IF EXISTS `commission_handle_task`;
CREATE TABLE `commission_handle_task` (
  `commission_id` BIGINT(20) NOT NULL AUTO_INCREMENT  COMMENT '主键',
  `group_master_u_id` BIGINT(20) NOT NULL COMMENT '群主用户编号',
  `user_id` BIGINT(20) NOT NULL COMMENT '发包用户编号',
  `game_id` BIGINT(20) NOT NULL COMMENT '发包用户编号',
  `split_amount` DECIMAL(10,2) NOT NULL COMMENT '分佣金额',
   status int not null default '0' comment '状态:0-未处理,1-处理中,2-处理完成',
   created_time varchar(20) not null comment '创建时间(yyyy-MM-dd 24HH:mi:ss)',
  `updated_time` varchar(20) null comment '更新时间(yyyy-MM-dd 24HH:mi:ss)',
  PRIMARY KEY (`commission_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 1
COMMENT = '分佣处理任务表';

