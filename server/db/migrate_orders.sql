-- 订单数据迁移脚本
-- 从 sys_order 迁移到 tn_order 和 tn_order_goods

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

-- 1. 迁移订单主表数据到 tn_order
INSERT INTO `tn_order` (
    `id`,
    `create_by`,
    `create_time`,
    `update_by`,
    `update_time`,
    `sys_org_code`,
    `order_no`,
    `payable_amount`,
    `paid_amount`,
    `collect_amount`,
    `recipient_id`,
    `order_status`,
    `is_new_customer`
) SELECT
    UUID() AS `id`,
    CONCAT('user_', so.`user_id`) AS `create_by`,
    so.`created_at` AS `create_time`,
    CONCAT('user_', so.`user_id`) AS `update_by`,
    so.`updated_at` AS `update_time`,
    '000000' AS `sys_org_code`,
    CONCAT('ORD', LPAD(so.`id`, 8, '0')) AS `order_no`,
    -- 从 ext 字段中提取应付金额
    JSON_UNQUOTE(JSON_EXTRACT(so.`ext`, '$.order_orderShouldPaid')) AS `payable_amount`,
    -- 从 ext 字段中提取实付金额
    JSON_UNQUOTE(JSON_EXTRACT(so.`ext`, '$.order_receiveMoneyNumber')) AS `paid_amount`,
    -- 从 ext 字段中提取代收金额
    JSON_UNQUOTE(JSON_EXTRACT(so.`ext`, '$.order_agentMoneyNumber')) AS `collect_amount`,
    -- 从 ext 字段中提取收件人ID
    JSON_UNQUOTE(JSON_EXTRACT(so.`ext`, '$.receiver_id')) AS `recipient_id`,
    so.`order_status`,
    -- 从 ext 字段中提取是否新客户
    JSON_UNQUOTE(JSON_EXTRACT(so.`ext`, '$.order_isNewClient')) AS `is_new_customer`
FROM
    `sys_order` so;

-- 2. 迁移订单商品数据到 tn_order_goods
-- 注意：由于 goodsList 是复杂的JSON结构，需要使用存储过程或更复杂的JSON解析
-- 这里提供一个基本的迁移方法，具体可能需要根据实际数据结构调整

-- 创建临时表来存储解析后的商品数据
CREATE TEMPORARY TABLE IF NOT EXISTS temp_order_goods (
    `order_id` varchar(36) NOT NULL,
    `goods_spec` varchar(64) NULL,
    `goods_name` varchar(100) NULL,
    `unit_price` decimal(10,2) NULL,
    `quantity` int NULL,
    `create_by` varchar(50) NULL,
    `create_time` datetime NULL
);

-- 使用存储过程解析 goodsList 并插入临时表
DELIMITER //
CREATE PROCEDURE migrate_order_goods()
BEGIN
    DECLARE done INT DEFAULT 0;
    DECLARE old_order_id INT;
    DECLARE new_order_id VARCHAR(36);
    DECLARE goods_list JSON;
    DECLARE create_by VARCHAR(50);
    DECLARE create_time DATETIME;
    DECLARE goods_class VARCHAR(10);
    DECLARE goods_array JSON;
    DECLARE goods_item JSON;
    DECLARE i INT;
    DECLARE j INT;
    DECLARE goods_count INT;
    DECLARE class_count INT;
    
    -- 游标声明
    DECLARE cur CURSOR FOR 
        SELECT so.`id`, tn.`id`, so.`goodsList`, CONCAT('user_', so.`user_id`), so.`created_at`
        FROM `sys_order` so
        JOIN `tn_order` tn ON CONCAT('ORD', LPAD(so.`id`, 8, '0')) = tn.`order_no`;
    
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    
    OPEN cur;
    
    read_loop: LOOP
        FETCH cur INTO old_order_id, new_order_id, goods_list, create_by, create_time;
        
        IF done THEN
            LEAVE read_loop;
        END IF;
        
        -- 获取 goodsList 中的所有键（商品分类）
        SET class_count = JSON_LENGTH(goods_list);
        SET i = 0;
        
        WHILE i < class_count DO
            -- 获取商品分类键名
            SET goods_class = JSON_UNQUOTE(JSON_EXTRACT(JSON_KEYS(goods_list), CONCAT('$[', i, ']')));
            -- 获取该分类下的商品数组
            SET goods_array = JSON_EXTRACT(goods_list, CONCAT('$."', goods_class, '"'));
            SET goods_count = JSON_LENGTH(goods_array);
            SET j = 0;
            
            WHILE j < goods_count DO
                -- 获取单个商品信息
                SET goods_item = JSON_EXTRACT(goods_array, CONCAT('$[', j, ']'));
                
                -- 插入临时表
                INSERT INTO temp_order_goods (
                    `order_id`,
                    `goods_spec`,
                    `goods_name`,
                    `unit_price`,
                    `quantity`,
                    `create_by`,
                    `create_time`
                ) VALUES (
                    new_order_id,
                    -- 从 ext.goods_id 提取商品规格ID
                    JSON_UNQUOTE(JSON_EXTRACT(goods_item, '$.ext.goods_id')),
                    -- 从 ext.goods_type 和 ext.goods_title 提取商品名称
                    CONCAT(
                        COALESCE(JSON_UNQUOTE(JSON_EXTRACT(goods_item, '$.ext.goods_title')), ''),
                        ' - ',
                        COALESCE(JSON_UNQUOTE(JSON_EXTRACT(goods_item, '$.ext.goods_type')), '')
                    ),
                    -- 从 ext.goods_price 提取单价
                    JSON_UNQUOTE(JSON_EXTRACT(goods_item, '$.ext.goods_price')),
                    -- 从 ext.goods_count 提取数量
                    JSON_UNQUOTE(JSON_EXTRACT(goods_item, '$.ext.goods_count')),
                    create_by,
                    create_time
                );
                
                SET j = j + 1;
            END WHILE;
            
            SET i = i + 1;
        END WHILE;
    END LOOP;
    
    CLOSE cur;
END //
DELIMITER ;

-- 执行存储过程
CALL migrate_order_goods();

-- 3. 更新 tn_order 表中的 goods_spec_id 和 good_id 字段，直接关联 temp_order_goods
-- 在将临时表数据插入 tn_order_goods 之前先更新 tn_order
UPDATE `tn_order` tn
JOIN (
    SELECT 
        tog.`order_id`,
        MAX(tog.`goods_spec`) AS first_goods_spec,
        MAX(tog.`goods_name`) AS first_goods_name
    FROM 
        temp_order_goods tog
    GROUP BY 
        tog.`order_id`
) tog ON tn.`id` = tog.`order_id`
SET 
    tn.`goods_spec_id` = tog.`first_goods_spec`,
    tn.`good_id` = tog.`first_goods_name`;

-- 将临时表数据插入到 tn_order_goods
INSERT INTO `tn_order_goods` (
    `id`,
    `create_by`,
    `create_time`,
    `update_by`,
    `update_time`,
    `sys_org_code`,
    `order_id`,
    `goods_spec`,
    `goods_name`,
    `unit_price`,
    `quantity`
) SELECT
    UUID() AS `id`,
    `create_by`,
    `create_time`,
    `create_by` AS `update_by`,
    `create_time` AS `update_time`,
    '000000' AS `sys_org_code`,
    `order_id`,
    `goods_spec`,
    `goods_name`,
    `unit_price`,
    `quantity`
FROM
    temp_order_goods;

-- 清理临时表和存储过程
DROP TABLE IF EXISTS temp_order_goods;
DROP PROCEDURE IF EXISTS migrate_order_goods;

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

-- 迁移完成提示
SELECT '订单数据迁移完成！' AS message;
SELECT COUNT(*) AS total_orders FROM `tn_order`;
SELECT COUNT(*) AS total_order_goods FROM `tn_order_goods`;

-- 大数据量订单列表接口优化建议：
-- 1. 添加必要的索引
--    CREATE INDEX idx_tn_order_order_no ON tn_order(order_no);
--    CREATE INDEX idx_tn_order_create_time ON tn_order(create_time);
--    CREATE INDEX idx_tn_order_order_status ON tn_order(order_status);
--    CREATE INDEX idx_tn_order_goods_order_id ON tn_order_goods(order_id);
--
-- 2. 接口设计建议：
--    - 采用分页查询，默认每页20-50条记录
--    - 支持按订单号、创建时间、订单状态等字段筛选
--    - 支持按创建时间等字段排序
--    - 主表查询时只查询必要字段，避免SELECT *
--    - 商品列表采用懒加载或单独接口查询
--    - 考虑使用缓存机制缓存热点数据
--
-- 3. SQL查询优化建议：
--    - 使用JOIN优化而非子查询
--    - 避免在WHERE子句中使用函数操作
--    - 合理使用索引覆盖查询
--    - 考虑使用读写分离架构
--
-- 4. 代码层面优化：
--    - 使用MyBatis-Plus等ORM框架的分页功能
--    - 实现异步加载关联数据
--    - 使用DTO模式传输数据，避免传输过多字段
--    - 考虑使用响应式编程处理大量数据