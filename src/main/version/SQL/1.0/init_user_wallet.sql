
DROP TABLE IF EXISTS `user_wallet`;
CREATE TABLE `user_wallet` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `u_id` bigint(20)  NOT NULL COMMENT '用户Id',
  `score` decimal(20,4)  NOT NULL default 0 COMMENT '积分',
  `available_amount` decimal(20,4) NOT NULL default 0 COMMENT '可用余额',
  `freeze_amount` decimal(20,4) DEFAULT 0 COMMENT '冻结金额',
  PRIMARY KEY (`id`),
  KEY `index_user_wallet_u_id` (`u_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户钱包';

DROP TABLE IF EXISTS `recharge`;
CREATE TABLE `recharge` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` varchar(50) DEFAULT NULL COMMENT '流水号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `u_id` bigint(20)  NOT NULL COMMENT '用户Id',
  `channel_type` varchar(50) NOT NULL COMMENT '充值渠道（1:微信支付，2：支付宝支付）',
  `amount` decimal(20,4) NOT NULL default 0 COMMENT '充值金额',
  `available_amount` decimal(20,4) NOT NULL default 0 COMMENT '可用余额',
  `states` varchar(50) NOT NULL COMMENT '状态（1:初始，2：操作中,3：成功，4：失败）',
  `remarks` varchar(50) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `index_recharge_u_id` (`u_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='充值表';

DROP TABLE IF EXISTS `transfer`;
CREATE TABLE `transfer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` varchar(50) DEFAULT NULL COMMENT '流水号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `u_id` bigint(20)  NOT NULL COMMENT '用户Id',
  `transfer_way` varchar(50) NOT NULL COMMENT '转账方式（1:转入，2：转出）',
  `transfer_user_id` bigint(20)  NOT NULL COMMENT '转账账户',
  `amount` decimal(20,4) NOT NULL default 0 COMMENT '金额',
  `available_amount` decimal(20,4) NOT NULL default 0 COMMENT '可用余额',
  `states` varchar(50) NOT NULL COMMENT '状态（1:初始，2：操作中,3：成功，4：失败）',
  `remarks` varchar(50) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `index_transfer_u_id` (`u_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='转账记录表';


DROP TABLE IF EXISTS `withdraw`;
CREATE TABLE `withdraw` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` varchar(50) DEFAULT NULL COMMENT '流水号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `u_id` bigint(20)  NOT NULL COMMENT '用户Id',
  `amount` decimal(20,4) NOT NULL default 0 COMMENT '金额',
  `available_amount` decimal(20,4) NOT NULL default 0 COMMENT '可用余额',
  `bank_card_no` varchar(50) DEFAULT NULL COMMENT '银行卡号',
  `branch_info` varchar(50) DEFAULT NULL COMMENT '支行信息',
  `real_name` varchar(20) DEFAULT NULL COMMENT '真实姓名',
  `states` varchar(50) NOT NULL COMMENT '状态（1:初始，2：操作中,3：成功，4：失败）',
  `remarks` varchar(50) DEFAULT NULL COMMENT '备注',
  actual_amount decimal(20,4) COMMENT '实际到账金额',
  cost decimal(20,4) COMMENT '提现手续费',
  PRIMARY KEY (`id`),
  KEY `index_withdraw_u_id` (`u_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='取现记录表';


DROP TABLE IF EXISTS `balance_detail`;
CREATE TABLE `balance_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `u_id` bigint(20) NOT NULL COMMENT '用户Id',
  `amount` decimal(20,4) NOT NULL DEFAULT '0.0000' COMMENT '交易金额',
  `transaction_type` varchar(50) NOT NULL COMMENT '交易类型（1:充值，2：提现，3：转账转入，4：转账转出，5：返佣，6：抢红包，7：中雷收入，8：中雷支出）',
  `available_amount` decimal(20,4) NOT NULL DEFAULT '0.0000' COMMENT '余额',
  `transaction_id` bigint(20) DEFAULT NULL COMMENT '交易Id',
  `remarks` varchar(50) DEFAULT NULL COMMENT '备注',
  `transaction_sub_type` varchar(50) NOT NULL COMMENT '交易子类型',
  PRIMARY KEY (`id`),
  KEY `index_balance_detail_u_id` (`u_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='余额明细表';



DROP TABLE IF EXISTS `bank_card`;
CREATE TABLE `bank_card` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `bind_date` varchar(16) COMMENT '绑定日期',
  `bind_time` varchar(16) COMMENT '绑定时间',
  `u_id` bigint(20)  NOT NULL COMMENT '用户Id',
  `bank_code` varchar(50) COMMENT '银行卡代号',
  `bank_name` varchar(50) COMMENT '银行卡名称',
  `card_type` varchar(50) COMMENT '银行卡类型',
  `card_type_name` varchar(50) COMMENT '银行卡类型名称',
  `bank_card_no` varchar(50) DEFAULT NULL COMMENT '银行卡号',
  `bank_card_end_no` varchar(10) DEFAULT NULL COMMENT '银行卡号尾号4位',
  `branch_info` varchar(50) DEFAULT NULL COMMENT '支行信息',
  `real_name` varchar(20) DEFAULT NULL COMMENT '真实姓名',
  `remarks` varchar(50) COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `index_k_bank_card_u_id` (`u_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='银行卡';


alter table user_wallet add pay_password varchar(50) DEFAULT NULL COMMENT '支付密码';


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
  `add_direction` varchar(50) NOT NULL COMMENT '添加方向，1:主动添加；2：被动添加',
  `code` varchar(50) NOT NULL COMMENT '通讯录排序code',
  PRIMARY KEY (`id`),
  KEY `index_friend_u_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='好友表';
