DROP TABLE IF EXISTS `friend`;
CREATE TABLE `friend` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `contact_date` varchar(16) COMMENT '日期',
  `contact_time` varchar(16) COMMENT '时间',
  `user_id` bigint(20)  NOT NULL COMMENT '用户Id',
  `friend_user_id` bigint(20)  NOT NULL COMMENT '好友ID',
  `friend_desc` varchar(50) COMMENT '好友备注',
  `states` varchar(50) NOT NULL COMMENT '状态',
  `add_direction` varchar(1) NOT NULL COMMENT '添加方向，1:主动添加；2：被动添加',
  `code` varchar(50) NOT NULL COMMENT '通讯录排序code',
  `add_type` varchar(50) NOT NULL COMMENT '添加类型，1：链接添加；2：APP添加',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unit_key` (`user_id`,`friend_user_id`) COMMENT '唯一组合键',
  KEY `index_friend_u_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='好友表';


DROP TABLE IF EXISTS `friend_apply`;
CREATE TABLE `friend_apply` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `contact_date` varchar(16) COMMENT '日期',
  `contact_time` varchar(16) COMMENT '时间',
  `user_id` bigint(20)  NOT NULL COMMENT '用户Id',
  `friend_user_id` bigint(20)  NOT NULL COMMENT '好友ID',
  `friend_desc` varchar(50) COMMENT '好友备注',
  `states` varchar(50) NOT NULL COMMENT '状态，1：申请；2：审核通过；3：审核拒绝；',
  `add_direction` varchar(1) NOT NULL COMMENT '添加方向，1:主动添加；2：被动添加',
  `add_type` varchar(50) NOT NULL COMMENT '添加类型，1：链接添加；2：APP添加',
  PRIMARY KEY (`id`),
  KEY `index_friend_apply_u_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='好友申请表';

DROP TABLE IF EXISTS `users_config`;
CREATE TABLE `users_config` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id,主键',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `customer_window_show` char(1) DEFAULT '0' COMMENT '客服悬浮窗口(0:不显示；1:显示)',
  `no_password_pay` char(1) DEFAULT '0' COMMENT '支付密码(0:需要密码；1:不需要密码)',
  `receive_new_msg` char(1) DEFAULT '0' COMMENT '接受新消息通知(0:不接收；1:接收)',
  `show_msg_detail` char(1) DEFAULT '0' COMMENT '显示消息详情(0:不显示；1:显示)',
  `have_voice` char(1) DEFAULT '0' COMMENT '声音开关(0:没有声音；1:有声音)',
  `have_shock` char(1) DEFAULT '0' COMMENT '震动开关(0:没有震动；1:震动)',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户设置';


