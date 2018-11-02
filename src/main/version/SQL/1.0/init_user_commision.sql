
DROP TABLE IF EXISTS `users_info`;
CREATE TABLE `users_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_photo` varchar(100) COMMENT '头像',
  `nick_name` varchar(50) NOT NULL COMMENT '昵称',
  `user_account` varchar(50) NOT NULL COMMENT '账号',
  `user_password` varchar(128) COMMENT '密码',
  `qr_code` varchar(100) COMMENT '二维码',
  `sex` varchar(1) COMMENT '性别(0:男,1:女,2:未知)',
  `mobile_phone` varchar(15) NOT NULL COMMENT '手机号',
  `real_name` varchar(20) NOT NULL COMMENT '真实姓名',
  `wechat` varchar(20) COMMENT '微信号',
  `qq` varchar(20) COMMENT 'QQ号',
  `robot_sno` varchar(20) COMMENT '机器人编号',
  `upuser_id` bigint(20)  NOT NULL COMMENT '上线用户id',
  `create_time` datetime COMMENT '创建时间',
  `update_time` datetime COMMENT '更新时间',
  `remark` varchar(20) COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息';


DROP TABLE IF EXISTS `user_commision`;
CREATE TABLE `user_commision`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `u_id` bigint(20) NOT NULL COMMENT '用户Id',
  `commision_amt` decimal(20, 4) COMMENT '佣金金额',
  `down_level` varchar(5) COMMENT '下线级别',
  `gain_proportion` varchar(5) COMMENT '获取比例',
  `gain_date` varchar(16) COMMENT '获取日期',
  `gain_time` varchar(16) COMMENT '获取时间',
  `remarks` varchar(50) COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `index_commision_u_id`(`u_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='佣金表';

