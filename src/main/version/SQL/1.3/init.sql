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
