
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

DROP TABLE IF EXISTS `robot_group_relation`; 

INSERT INTO `neighbor_dev`.`dictionary` (`name`, `code`, `status`, `biz_code`, `remarke`) VALUES ('未知', '0', '1', 'sex', '性别');
INSERT INTO `neighbor_dev`.`dictionary` (`name`, `code`, `status`, `biz_code`, `remarke`) VALUES ('男', '1', '1', 'sex', '性别');
INSERT INTO `neighbor_dev`.`dictionary` (`name`, `code`, `status`, `biz_code`, `remarke`) VALUES ('女', '2', '1', 'sex', '性别');
commit;

alter table users_info add re_code varchar(50) DEFAULT null COMMENT '我的推荐码';



ALTER TABLE `packet_detail` 
ADD COLUMN `remain_size` BIGINT(20) NULL AFTER `head_url`,
ADD COLUMN `remain_money` DECIMAL(20,2) NULL AFTER `remain_size`;



INSERT INTO `neighbordb`.`user_wallet` (`update_time`, `u_id`, `score`, `available_amount`, `freeze_amount`) 
VALUES ( '2019-01-04 16:56:47', '1', '0.00', '0.00', '0.0000');

INSERT INTO `neighbordb`.`users_info` (id, `user_photo`, `nick_name`, `user_account`, `user_password`, `sex`, `mobile_phone`, `create_time`, `update_time`, `user_role`, `re_code`) 
VALUES (1, 'img/head-user2.png', 'trewww', '1', 'e10adc3949ba59abbe56e057f20f883e', '2', '15999585910', '2019-01-13 19:46:19', '2019-01-13 19:46:19', '2', '7d4af105');

alter table game add url varchar(500) DEFAULT null COMMENT '规则图片地址';



INSERT INTO `dictionary` (`id`,`name`,`code`,`status`,`parent_id`,`biz_code`,`remarke`) VALUES (6,'红包游戏','1',1,NULL,'gameType','游戏类型');
INSERT INTO `dictionary` (`id`,`name`,`code`,`status`,`parent_id`,`biz_code`,`remarke`) VALUES (7,'猜猜乐','2',1,NULL,'gameType','游戏类型');
INSERT INTO `dictionary` (`id`,`name`,`code`,`status`,`parent_id`,`biz_code`,`remarke`) VALUES (8,'休闲游戏','3',1,NULL,'gameType','游戏类型');
INSERT INTO `dictionary` (`id`,`name`,`code`,`status`,`parent_id`,`biz_code`,`remarke`) VALUES (9,'福利群','4',1,NULL,'gameType','游戏类型');
INSERT INTO `dictionary` (`id`,`name`,`code`,`status`,`parent_id`,`biz_code`,`remarke`) VALUES (10,'返佣规则','1',1,NULL,'ruleType','游戏规则');
INSERT INTO `dictionary` (`id`,`name`,`code`,`status`,`parent_id`,`biz_code`,`remarke`) VALUES (11,'中奖规则','2',1,NULL,'ruleType','游戏规则');
INSERT INTO `dictionary` (`id`,`name`,`code`,`status`,`parent_id`,`biz_code`,`remarke`) VALUES (12,'中雷规则','3',1,NULL,'ruleType','游戏规则');
INSERT INTO `dictionary` (`id`,`name`,`code`,`status`,`parent_id`,`biz_code`,`remarke`) VALUES (13,'单个值','1',1,NULL,'ruleSubType','游戏子规则');
INSERT INTO `dictionary` (`id`,`name`,`code`,`status`,`parent_id`,`biz_code`,`remarke`) VALUES (14,'顺子','2',1,NULL,'ruleSubType','游戏子规则');
INSERT INTO `dictionary` (`id`,`name`,`code`,`status`,`parent_id`,`biz_code`,`remarke`) VALUES (15,'同数','3',1,NULL,'ruleSubType','游戏子规则');

alter table group_member add is_customer varchar(2) DEFAULT '0' COMMENT '是否是群客服（1是，0不是）';
update group_member set is_customer = '1' where member_type = '1';


alter table game add head_url varchar(500) DEFAULT null COMMENT '游戏头像图片地址';

ALTER TABLE `packet` ADD COLUMN `hit_chance` DECIMAL(4,2) NULL DEFAULT 0 COMMENT '中雷概率';
ALTER TABLE `packet` ADD COLUMN `paid_rate` DECIMAL(4,2) NULL DEFAULT 0 COMMENT '赔付率';

alter table recharge add method varchar(50) DEFAULT null COMMENT '接口列表';

alter table recharge add body varchar(500) DEFAULT null COMMENT '商品描述';

alter table recharge add pay_state varchar(10) DEFAULT null COMMENT '支付状态0未支付，1已支付，2已退款';

alter table recharge add out_trade_no varchar(500) DEFAULT null COMMENT '支付平台订单号';

alter table recharge add transaction_id varchar(500) DEFAULT null COMMENT '支付渠道流水号';

alter table recharge add code_url varchar(5000) DEFAULT null COMMENT '二维码地址';

ALTER TABLE `chat_message` 
CHANGE COLUMN `msg_type` `msg_type` VARCHAR(30) NOT NULL COMMENT '消息类型' ;

ALTER TABLE `chat_message_backup` 
CHANGE COLUMN `msg_type` `msg_type` VARCHAR(30) NOT NULL COMMENT '消息类型' ;
