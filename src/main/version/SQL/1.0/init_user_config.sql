
DROP TABLE IF EXISTS `users_config`;
CREATE TABLE `users_config`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id,主键',
  `create_time` datetime COMMENT '创建时间',
  `update_time` datetime COMMENT '更新时间',
  `pay_password` varchar(128) COMMENT '支付密码',
  `customer_window_show` char(1) COMMENT '客服悬浮窗口(0:不显示；1:显示)',
  `no_password_pay` char(1) COMMENT '支付密码(0:需要密码；1:不需要密码)',
  `receive_new_msg` char(1) COMMENT '接受新消息通知(0:不接收；1:接收)',
  `show_msg_detail` char(1) COMMENT '显示消息详情(0:不显示；1:显示)',
  `have_voice` char(1) COMMENT '声音开关(0:没有声音；1:有声音)',
  `have_shock` char(1) COMMENT '震动开关(0:没有震动；1:震动)',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户设置';


