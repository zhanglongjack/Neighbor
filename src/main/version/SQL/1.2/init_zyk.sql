
DROP TABLE IF EXISTS `game`;
CREATE TABLE `game`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime COMMENT '创建时间',
  `update_time` datetime COMMENT '更新时间',
  `game_name` varchar(50) NOT NULL COMMENT '游戏名称',
  `game_type` bigint(20) NOT NULL COMMENT '游戏类型（1:红包游戏,2:猜猜乐,3:休闲游戏,4:福利群）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='群基础表';

DROP TABLE IF EXISTS `game_rule`;
CREATE TABLE `game_rule`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime COMMENT '创建时间',
  `update_time` datetime COMMENT '更新时间',
  `game_id` bigint(20) NOT NULL COMMENT '游戏ID',
  `rule_type` bigint(20) NOT NULL COMMENT '游戏规则类型（1:返佣比例,2:中奖数字奖励,3:多雷奖励）',
  `rule_code` varchar(500) NOT NULL COMMENT '游戏规则代码',
  `rule_value` varchar(500) NOT NULL COMMENT '游戏规则值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='游戏规则表';



/*   返佣规则  */
INSERT INTO `game_rule`(`create_time`,`update_time`,`game_id`,`rule_type`,`rule_code`,`rule_value`) VALUES ( '2018-12-29 18:39:19', NULL, 1, 1, '1', '0.12');
INSERT INTO `game_rule`(`create_time`,`update_time`,`game_id`,`rule_type`,`rule_code`,`rule_value`) VALUES ( '2018-12-29 18:39:19', NULL, 1, 1, '2', '0.10');
INSERT INTO `game_rule`(`create_time`,`update_time`,`game_id`,`rule_type`,`rule_code`,`rule_value`) VALUES ( '2018-12-29 18:39:19', NULL, 1, 1, '3', '0.08');
INSERT INTO `game_rule`(`create_time`,`update_time`,`game_id`,`rule_type`,`rule_code`,`rule_value`) VALUES ( '2018-12-29 18:39:19', NULL, 1, 1, '4', '0.05');
INSERT INTO `game_rule`(`create_time`,`update_time`,`game_id`,`rule_type`,`rule_code`,`rule_value`) VALUES ( '2018-12-29 18:39:19', NULL, 1, 1, '5', '0.03');
/*   中奖规则  */
INSERT INTO `game_rule`(`create_time`,`update_time`,`game_id`,`rule_type`,`rule_code`,`rule_value`) VALUES ( '2018-12-29 18:39:19', NULL, 1, 2, '0.01', '6.66');
INSERT INTO `game_rule`(`create_time`,`update_time`,`game_id`,`rule_type`,`rule_code`,`rule_value`) VALUES ( '2018-12-29 18:39:19', NULL, 1, 2, '5.20', '13.14');
INSERT INTO `game_rule`(`create_time`,`update_time`,`game_id`,`rule_type`,`rule_code`,`rule_value`) VALUES ( '2018-12-29 18:39:19', NULL, 1, 2, '13.14', '16.88');
INSERT INTO `game_rule`(`create_time`,`update_time`,`game_id`,`rule_type`,`rule_code`,`rule_value`) VALUES ( '2018-12-29 18:39:19', NULL, 1, 2, '1.23-7.89', '8.88');
INSERT INTO `game_rule`(`create_time`,`update_time`,`game_id`,`rule_type`,`rule_code`,`rule_value`) VALUES ( '2018-12-29 18:39:19', NULL, 1, 2, '12.34-45.67', '16.88');
/*   多雷奖励规则  */
INSERT INTO `game_rule`(`create_time`,`update_time`,`game_id`,`rule_type`,`rule_code`,`rule_value`) VALUES ( '2018-12-29 18:39:19', NULL, 1, 3, '3', '18');
INSERT INTO `game_rule`(`create_time`,`update_time`,`game_id`,`rule_type`,`rule_code`,`rule_value`) VALUES ( '2018-12-29 18:39:19', NULL, 1, 3, '4', '38');
INSERT INTO `game_rule`(`create_time`,`update_time`,`game_id`,`rule_type`,`rule_code`,`rule_value`) VALUES ( '2018-12-29 18:39:19', NULL, 1, 3, '5', '188');
INSERT INTO `game_rule`(`create_time`,`update_time`,`game_id`,`rule_type`,`rule_code`,`rule_value`) VALUES ( '2018-12-29 18:39:19', NULL, 1, 3, '6', '388');
INSERT INTO `game_rule`(`create_time`,`update_time`,`game_id`,`rule_type`,`rule_code`,`rule_value`) VALUES ( '2018-12-29 18:39:19', NULL, 1, 3, '7', '588');

DROP TABLE IF EXISTS `group`;
CREATE TABLE `group`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '群聊号码,主键',
  `create_time` datetime COMMENT '创建时间',
  `update_time` datetime COMMENT '更新时间',
  `cre_date` varchar(16) COMMENT '建立日期',
  `cre_time` varchar(16) COMMENT '建立时间',
  `group_name` varchar(50)  COMMENT '群名称',
  `user_num` bigint(20) COMMENT '群人数',
  `online_num` bigint(20) COMMENT '在线人数',
  `group_notice` varchar(500)  COMMENT '群公告',
  `group_head_img_url` varchar(500)  COMMENT '群头像地址',
  `states` varchar(50) default '1' COMMENT '状态（是1否0禁言）',
  `game_id` bigint(20)  COMMENT '游戏ID',
   red_pack_amount_limit varchar(500) DEFAULT '30-50'  COMMENT '红包金额限制大小',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100000 DEFAULT CHARSET=utf8 COMMENT='群基础表';

DROP TABLE IF EXISTS `group_member`;
CREATE TABLE `group_member`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime COMMENT '创建时间',
  `update_time` datetime COMMENT '更新时间',
  `group_id` bigint(20) NOT NULL  COMMENT '群ID',
  `user_id` bigint(20) NOT NULL COMMENT '成员Id',
  `member_type` varchar(16) NOT NULL default '0' COMMENT '群主1或成员0',
  `show_msg_switch` varchar(16) default '0' COMMENT '消息免打扰是1否0',
  `show_nick_name_switch`  varchar(16) default '0' COMMENT '显示群昵称是1否0',
  `unread_message_num` bigint(20)  COMMENT '未读消息数量',
  `states` varchar(50) NOT NULL default '0' COMMENT '状态（0正常，1其他）',
  `automatic_grab_switch` varchar(10) default '0' COMMENT '自动抢红包开关是1否0',
  `topping_flag` varchar(10) default '0' COMMENT '是否聊天置顶是1否0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='群成员表';