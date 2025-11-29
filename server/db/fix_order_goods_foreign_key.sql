-- 修复 tn_order_goods 表的 order_id 字段长度，从32改为36以匹配 tn_order 表的 id 字段
ALTER TABLE `tongnian`.`tn_order_goods` 
MODIFY COLUMN `order_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '订单id';