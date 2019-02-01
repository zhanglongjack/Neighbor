DROP TABLE IF EXISTS `distribute_commission_task`;
CREATE TABLE `distribute_commission_task` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) NOT NULL COMMENT '分佣用户',
  `game_id` BIGINT(20) NOT NULL COMMENT '游戏编号',
  `base_commission_amount` DECIMAL(20,2) NOT NULL COMMENT '分佣基础金额',
  `is_handle` INT NOT NULL DEFAULT 0 COMMENT '是否处理:1-已处理,0-未处理',
  `create_date` VARCHAR(10) NOT NULL,
  `create_time` VARCHAR(8) NOT NULL,
  `update_date` VARCHAR(10) NOT NULL,
  `update_time` VARCHAR(8) NOT NULL,
  PRIMARY KEY (`id`))
COMMENT = '分佣任务';


CREATE TABLE `dictionary` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) NOT NULL COMMENT '字典名称',
  `code` varchar(45) NOT NULL COMMENT '字典代码',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT '字典状态:0-停用,1-启用',
  `parent_id` int(11) DEFAULT NULL COMMENT '父字典ID',
  `biz_code` varchar(45) NOT NULL COMMENT '业务代码',
  `remarke` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='字典表'


INSERT INTO  `users_info` (`id`, `user_photo`, `nick_name`, `user_account`, `sex`, `mobile_phone`, `upuser_id`)
 VALUES ('1', 'img/head-user2.png', '超管', '1', '2', '18999586019', null);
 
ALTER TABLE `packet` 
ADD COLUMN `send_head_url` VARCHAR(256) NULL COMMENT '发红包的头像' AFTER `random_amount`;

 ALTER TABLE  `packet` 
ADD COLUMN `random_amount` VARCHAR(256) NULL COMMENT '各个红包的金额,逗号分隔' AFTER `remarke`;

 ALTER TABLE  `packet` 
ADD COLUMN `status` VARCHAR(15) NULL COMMENT '红包领取状态' ;

 ALTER TABLE  `packet` 
ADD COLUMN `collected_num` INT NULL COMMENT '红包已领取数量'  ;

ALTER TABLE `packet` 
CHANGE COLUMN `collected_num` `collected_num` INT(11) NOT NULL DEFAULT 0 COMMENT '红包已领取数量' ;

ALTER TABLE `chat_message` 
ADD COLUMN `s_nick_name` VARCHAR(50) NULL COMMENT '发送人昵称' AFTER `target_user_delete_Flag`;


ALTER TABLE  `msg_group_menber_relationship` 
ADD COLUMN `status` VARCHAR(15) NOT NULL DEFAULT 'received' COMMENT '消息状态' AFTER `r_user_id`;

CREATE TABLE `chat_message_backup` (
  `msg_id` bigint(20) NOT NULL COMMENT '主键',
  `request_id` varchar(126) NOT NULL COMMENT '请求编号',
  `master_msg_type` varchar(126) NOT NULL DEFAULT '1' COMMENT '消息大类型',
  `header` varchar(1000) NOT NULL COMMENT '消息头Json格式',
  `chat_type` varchar(15) NOT NULL COMMENT '聊天类型',
  `msg_type` varchar(15) NOT NULL COMMENT '消息类型',
  `status` varchar(15) NOT NULL DEFAULT '1' COMMENT '消息状态',
  `send_user_id` bigint(20) NOT NULL COMMENT '发送消息人',
  `target_user_id` bigint(20) DEFAULT NULL COMMENT '接收消息人',
  `target_group_id` bigint(20) DEFAULT NULL COMMENT '接收消息群',
  `biz_id` bigint(20) DEFAULT NULL COMMENT '业务编号',
  `content` varchar(500) DEFAULT NULL COMMENT '消息内容',
  `date` varchar(15) NOT NULL COMMENT '发送日期',
  `time` varchar(15) NOT NULL COMMENT '发送时间',
  `send_user_delete_flag` varchar(50) DEFAULT '0' COMMENT '发送者删除状态',
  `target_user_delete_Flag` varchar(50) DEFAULT '0' COMMENT '接受者删除状态',
  `s_nick_name` VARCHAR(50) NULL COMMENT '发送人昵称',
  PRIMARY KEY (`msg_id`)
) ENGINE=InnoDB AUTO_INCREMENT=328 DEFAULT CHARSET=utf8 COMMENT='消息记录备份';

ALTER TABLE `packet_detail` 
ADD COLUMN `head_url` VARCHAR(256) NULL COMMENT '抢红包人的头像' AFTER `create_time`;

