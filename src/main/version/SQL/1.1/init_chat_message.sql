
DROP TABLE IF EXISTS `chat_message`;
CREATE TABLE `chat_message` (
  `msg_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `header` varchar(1000) NOT NULL COMMENT '消息头Json格式',
  `chat_type` varchar(15) NOT NULL COMMENT '聊天类型',
  `msg_type` varchar(15) NOT NULL COMMENT '消息类型',
  `status` varchar(15) NOT NULL DEFAULT '1' COMMENT '消息状态',
  `send_user_id` bigint(20) NOT NULL COMMENT '发送消息人',
  `target_user_id` bigint(20) DEFAULT NULL COMMENT '接收消息人',
  `target_group_id` bigint(20) DEFAULT NULL COMMENT '接收消息群',
  `content` varchar(500) DEFAULT NULL COMMENT '消息内容',
  `date` varchar(15) NOT NULL COMMENT '发送日期',
  `time` varchar(15) NOT NULL COMMENT '发送时间',
  PRIMARY KEY (`msg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息记录';

DROP TABLE IF EXISTS `msg_group_menber_relationship`;
CREATE TABLE `msg_group_menber_relationship` (
  `r_msg_id` bigint(20) NOT NULL COMMENT '消息编号',
  `r_user_id` bigint(20) NOT NULL COMMENT '用户编号',
  PRIMARY KEY (`r_msg_id`,`r_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='群消息接收关联表';

DROP TABLE IF EXISTS `packet`;
CREATE TABLE `packet` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `s_user_id` varchar(45) NOT NULL COMMENT '发包人',
  `receive_user_id` bigint(20) DEFAULT NULL COMMENT '接收人',
  `group_id` bigint(20) DEFAULT NULL COMMENT '接收群',
  `amount` decimal(20,2) NOT NULL COMMENT '红包金额',
  `packet_num` int(11) DEFAULT NULL COMMENT '红包数量',
  `hit_num` int(11) DEFAULT NULL COMMENT '红包尾数',
  `send_date` varchar(10) NOT NULL COMMENT '发包日期(yyyy-mm-dd)',
  `send_time` varchar(8) NOT NULL COMMENT '发包时间(24HH:mi:ss)',
  `remarke` varchar(64) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='红包表';



DROP TABLE IF EXISTS `packet_detail`;
CREATE TABLE `packet_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `d_packet_id` bigint(20) NOT NULL COMMENT '红包编号',
  `got_user_id` bigint(20) NOT NULL COMMENT '抢到红包的人',
  `got_amount` decimal(20,2) NOT NULL COMMENT '抢到的金额',
  `is_got_bomb` varchar(1) NOT NULL DEFAULT '0' COMMENT '是否中雷',
  `is_free` varchar(1) NOT NULL DEFAULT '0' COMMENT '是否免单包',
  `is_maximum` varchar(1) NOT NULL DEFAULT '0' COMMENT '红包金额是否最多',
  `create_date` varchar(10) NOT NULL COMMENT '创建日期(yyyy-mm-dd)',
  `create_time` varchar(8) NOT NULL COMMENT '创建时间(24HH:mi:ss)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='红包明细';

  

ALTER TABLE `user_wallet` 
ADD UNIQUE INDEX `u_id_UNIQUE` (`u_id` ASC);
