CREATE TABLE chat_message (
    msg_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    header VARCHAR(1000) NOT NULL COMMENT '消息头Json格式',
    chat_type VARCHAR(15) NOT NULL COMMENT '聊天类型',
    msg_type VARCHAR(15) NOT NULL COMMENT '消息类型',
    status VARCHAR(15) NOT NULL DEFAULT 1 COMMENT '消息状态',
    send_user_id BIGINT NOT NULL COMMENT '发送消息人',
    target_user_id BIGINT NULL COMMENT '接收消息人',
    target_group_id BIGINT NULL COMMENT '接收消息群',
    amount DECIMAL(20 , 2 ) NULL COMMENT '红包或转账金额',
    number int NULL COMMENT '红包数量',
    hit_amount_number int NULL COMMENT '雷尾数',
    content VARCHAR(15) NULL COMMENT '消息内容',
    `date` VARCHAR(15) NOT NULL COMMENT '发送日期',
    `time` VARCHAR(15) NOT NULL COMMENT '发送时间',
    PRIMARY KEY (msg_id)
)


ALTER TABLE `user_wallet` 
ADD UNIQUE INDEX `u_id_UNIQUE` (`u_id` ASC);
