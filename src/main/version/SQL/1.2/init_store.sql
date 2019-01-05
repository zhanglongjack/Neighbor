DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `contact_date` varchar(16) COMMENT '日期',
  `contact_time` varchar(16) COMMENT '时间',
  `name` varchar(50)  NOT NULL COMMENT '商品名称',
  `score` decimal(20,2)  NOT NULL COMMENT '商品价格，使用的是积分',
  `product_desc` varchar(500) COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品表';

DROP TABLE IF EXISTS `product_img`;
CREATE TABLE `product_img` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `contact_date` varchar(16) COMMENT '日期',
  `contact_time` varchar(16) COMMENT '时间',
  `product_id` bigint(20)  NOT NULL COMMENT '商品id',
  `img_type` char(1) COMMENT '图像类型，1:列表小兔；2:详情顶部图；3:详情明细长图；',
  `url` varchar(200)  COMMENT '图片的地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品图像表';


DROP TABLE IF EXISTS `product_order`;
CREATE TABLE `product_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `contact_date` varchar(16) COMMENT '日期',
  `contact_time` varchar(16) COMMENT '时间',
  `score` decimal(20,2)  NOT NULL COMMENT '订单价格，使用的是积分',
  `user_id` bigint(20)  NOT NULL COMMENT '用户id',
  `address` varchar(200)  COMMENT '送货地址',
  `phone` varchar(20)  COMMENT '送货电话',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品订单表';

DROP TABLE IF EXISTS `product_order_item`;
CREATE TABLE `product_order_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `contact_date` varchar(16) COMMENT '日期',
  `contact_time` varchar(16) COMMENT '时间',
  `order_id` bigint(20)  NOT NULL COMMENT '订单id',
  `product_id` bigint(20)  NOT NULL COMMENT '商品id',
  `num` bigint(10)  NOT NULL COMMENT '商品数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单商品关联表';