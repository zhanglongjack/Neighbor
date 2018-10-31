
DROP TABLE IF EXISTS `users_info`;
CREATE TABLE `users_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_photo` varchar(100)  DEFAULT NULL COMMENT '头像',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '昵称',
  `user_account` varchar(50) DEFAULT NULL COMMENT '账号',
  `qr_code` varchar(100) DEFAULT NULL COMMENT '二维码',
  `sex` varchar(1) DEFAULT NULL COMMENT '性别(0:男,1:女,2:未知)',
  `mobile_phone` varchar(15) DEFAULT NULL COMMENT '手机号',
  `real_name` varchar(20) NULL COMMENT '真实姓名',
  `wechat` varchar(20) DEFAULT NULL COMMENT '微信号',
  `qq` varchar(20) DEFAULT NULL COMMENT 'QQ号',
  `robot_sno` varchar(20) DEFAULT NULL COMMENT '机器人编号',
  `upuser_id` bigint(20)  NOT NULL COMMENT '上线用户id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(20) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息';


DROP TABLE IF EXISTS `user_commision`;
CREATE TABLE `user_commision`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `u_id` bigint(20) NOT NULL COMMENT '用户Id',
  `commision_amt` decimal(20, 4) NOT NULL default 0 COMMENT '佣金金额',
  `down_level` varchar(5) DEFAULT NULL COMMENT '下线级别',
  `gain_proportion` varchar(5) DEFAULT NULL COMMENT '获取比例',
  `gain_date` datetime DEFAULT NULL COMMENT '获取日期',
  `gain_time` datetime DEFAULT NULL COMMENT '获取时间',
  `remarks` varchar(50) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_commision_u_id`(`u_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='佣金表';

