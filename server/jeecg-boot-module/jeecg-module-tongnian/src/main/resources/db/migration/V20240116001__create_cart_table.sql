-- 创建购物车表
CREATE TABLE IF NOT EXISTS `tn_cart` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `goods_id` varchar(32) NOT NULL COMMENT '商品ID',
  `goods_name` varchar(255) NOT NULL COMMENT '商品名称',
  `spec_type` varchar(255) DEFAULT NULL COMMENT '规格类型',
  `price` decimal(10,2) NOT NULL COMMENT '价格',
  `quantity` int(11) NOT NULL COMMENT '数量',
  `image_url` varchar(500) DEFAULT NULL COMMENT '图片地址',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';
