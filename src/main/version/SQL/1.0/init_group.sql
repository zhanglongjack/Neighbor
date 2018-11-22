
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
  `states` varchar(50) NOT NULL COMMENT '状态（是否禁言）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='群基础表';


DROP TABLE IF EXISTS `group_member`;
CREATE TABLE `group_member`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime COMMENT '创建时间',
  `update_time` datetime COMMENT '更新时间',
  `group_id` bigint(20) NOT NULL  COMMENT '群ID',
  `user_id` bigint(20) NOT NULL COMMENT '成员Id',
  `member_type` varchar(16) NOT NULL COMMENT '群主或成员',
  `show_msg_switch` varchar(16) COMMENT '消息免打扰',
  `show_nick_name_switch`  varchar(16) COMMENT '显示群昵称',
  `unread_message_num` bigint(20) COMMENT '未读消息数量',
  `states` varchar(50) NOT NULL COMMENT '状态',
  `automatic_grab_switch` varchar(16) COMMENT '自动抢红包开关',
  `topping_flag` varchar(50)  COMMENT '是否聊天置顶',
  `last_chat_date_time` datetime COMMENT '最后消息时间',
  `last_chat_date` varchar(50) COMMENT '最后消息日期',
  `last_chat_time` varchar(50) COMMENT '最后消息时间',
  `last_chat_message_id` bigint(20) COMMENT '最后消息ID',
  `last_chat_message_type` varchar(50) COMMENT '最后消息类型',
  `last_chat_message_content` varchar(500) COMMENT '最后消息内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='群成员表';


DROP TABLE IF EXISTS `chat_list`;
CREATE TABLE `chat_list`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime COMMENT '创建时间',
  `update_time` datetime COMMENT '更新时间',
  `cre_date` varchar(16) COMMENT '建立日期',
  `cre_time` varchar(16) COMMENT '建立时间',
  `user_id` bigint(20) NOT NULL COMMENT '成员Id',
  `friend_id` bigint(20) NOT NULL COMMENT '好友Id',
  `last_chat_date_time` datetime COMMENT '最后消息时间',
  `last_chat_date` varchar(50) COMMENT '最后消息日期',
  `last_chat_time` varchar(50) COMMENT '最后消息时间',
  `last_chat_message_id` bigint(20) COMMENT '最后消息ID',
  `last_chat_message_type` varchar(50) COMMENT '最后消息类型',
  `last_chat_message_content` varchar(500) COMMENT '最后消息内容',
  `unread_message_num` bigint(20) COMMENT '未读消息数量',
  `topping_flag` varchar(50)  COMMENT '是否聊天置顶',
  `show_msg_switch` varchar(50) COMMENT '消息免打扰',
  `chat_history_set` varchar(50)  COMMENT '聊天历史设置',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='聊天好友列表';



