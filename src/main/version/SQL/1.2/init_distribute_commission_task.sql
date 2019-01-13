DROP TABLE IF EXISTS `distribute_commission_task`;
CREATE TABLE `distribute_commission_task` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) NOT NULL COMMENT '分佣用户',
  `game_id` BIGINT(20) NOT NULL COMMENT '游戏编号',
  `base_commission_amount` DECIMAL(20,2) NOT NULL COMMENT '分佣基础金额',
  `is_handle` INT NOT NULL DEFAULT 0 COMMENT '是否处理:1-已处理,0-未处理',
  `create_date` VARCHAR(10) NOT NULL,
  `create_time` VARCHAR(8) NOT NULL,
  `update_date` VARCHAR(10) NOT NULL,
  `update_time` VARCHAR(8) NOT NULL,
  PRIMARY KEY (`id`))
COMMENT = '分佣任务';


CREATE TABLE `dictionary` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) NOT NULL COMMENT '字典名称',
  `code` varchar(45) NOT NULL COMMENT '字典代码',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT '字典状态:0-停用,1-启用',
  `parent_id` int(11) DEFAULT NULL COMMENT '父字典ID',
  `biz_code` varchar(45) NOT NULL COMMENT '业务代码',
  `remarke` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='字典表'