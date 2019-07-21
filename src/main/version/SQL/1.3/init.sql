delete from dictionary
where biz_code = 'packet_conf';

INSERT INTO `dictionary` (`name`, `code`, `status`, `biz_code`, `remarke`) 
VALUES ('1.1', 'packet.hit.rate', '1', 'packet_conf', '赔付倍率');

INSERT INTO `dictionary` (`name`, `code`, `status`, `biz_code`, `remarke`) 
VALUES ('9', 'pacekt_base_num', '1', 'packet_conf', '红包数量');

INSERT INTO `dictionary` (`name`, `code`, `status`, `biz_code`, `remarke`) 
VALUES ('0.1', 'reward_score', '1', 'packet_conf', '积分奖励');

INSERT INTO `dictionary` (`name`, `code`, `status`, `biz_code`, `remarke`) 
VALUES ('0.25', 'sys.commission.percent', '1', 'packet_conf', '系统佣金提成比例');

INSERT INTO `dictionary` (`name`, `code`, `status`, `biz_code`, `remarke`) 
VALUES ('1000', 'robot.grap.limit.amount', '1', 'packet_conf', '机器人抢包亏损下限限额');

INSERT INTO `dictionary` (`name`, `code`, `status`, `biz_code`, `remarke`) 
VALUES ('1000', 'robot.send.limit.amount', '1', 'packet_conf', '机器人发包亏损下限限额');

INSERT INTO `dictionary` (`name`, `code`, `status`, `biz_code`, `remarke`) 
VALUES ('5', 'robot_grap_sleep_seconds', '1', 'packet_conf', '机器人抢包最长休眠时间(秒)');

INSERT INTO `dictionary` (`name`, `code`, `status`, `biz_code`, `remarke`) 
VALUES ('30', 'robot_grap_queue_handle_size', '1', 'packet_conf', '机器人抢包队列数量超限设置(超过此数量队列的,一秒钟休眠就开始抢包)');

INSERT INTO `dictionary` (`name`, `code`, `status`, `biz_code`, `remarke`) 
VALUES ('豹子', '4', 1, 'ruleSubType', '游戏子规则');


INSERT INTO `dictionary` (`name`, `code`, `status`, `biz_code`, `remarke`) 
VALUES ('0.03', 'payment_rate', 1, 'payment', '支付接口抽成比例');



INSERT INTO `dictionary` (`name`, `code`, `status`, `biz_code`, `remarke`) VALUES ('1', 'alipay', '1', 'recharge_channel_off', '支付宝充值开关（1打开，其他0关闭）');
INSERT INTO `dictionary` (`name`, `code`, `status`, `biz_code`, `remarke`) VALUES ('1', 'wxpay', '1', 'recharge_channel_off', '微信支付开关（1打开，其他0关闭）');

INSERT INTO `dictionary` (`name`, `code`, `status`, `biz_code`, `remarke`) VALUES ('904', 'alipay', '1', 'recharge_channel_no', '支付宝充值渠道编号）');
INSERT INTO `dictionary` (`name`, `code`, `status`, `biz_code`, `remarke`) VALUES ('902', 'wxpay', '1', 'recharge_channel_no', '微信支付充值渠道编号');

INSERT INTO `dictionary` (`name`, `code`, `status`, `biz_code`, `remarke`) VALUES ('200,300,500,600,800,1000,2000,3000,5000', 'alipay', '1', 'recharge_channel_limit', '支付宝限额');
INSERT INTO `dictionary` (`name`, `code`, `status`, `biz_code`, `remarke`) VALUES ('100,200,300,500,600,800,1000,2000,3000', 'wxpay', '1', 'recharge_channel_limit', '微信限额');

alter table withdraw add bank_name varchar(50) DEFAULT null COMMENT '银行名称';

alter table withdraw add card_type_name varchar(50) DEFAULT null COMMENT '银行卡类型名称';

update withdraw w,(select w.id,b.bank_name,b.card_type_name from withdraw w INNER JOIN bank_card b on w.bank_card_no = b.bank_card_no) a set w.bank_name = a.bank_name,w.card_type_name=a.card_type_name where w.id=a.id


INSERT INTO `dictionary` (`name`, `code`, `status`, `biz_code`, `remarke`) VALUES ('pay_gyf', '1', '1', 'recharge_pay_channel', '支付渠道（国易付:pay_gyf）');
