
DROP TABLE IF EXISTS `users_config`;
CREATE TABLE `users_config`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id,主键',
  `create_time` datetime COMMENT '创建时间',
  `update_time` datetime COMMENT '更新时间',
  `pay_password` varchar(128) COMMENT '支付密码',
  `customerWindowShow` char(1) COMMENT '客服悬浮窗口(0:不显示；1:显示)',
  `noPasswordPay` char(1) COMMENT '支付密码(0:需要密码；1:不需要密码)',
  `receiveNewMsg` char(1) COMMENT '接受新消息通知(0:不接收；1:接收)',
  `showMsgDetail` char(1) COMMENT '显示消息详情(0:不显示；1:显示)',
  `haveVoice` char(1) COMMENT '声音开关(0:没有声音；1:有声音)',
  `haveShock` char(1) COMMENT '震动开关(0:没有震动；1:震动)',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户设置';


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

