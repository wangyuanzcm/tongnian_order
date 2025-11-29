-- 修复 tn_order_goods 表中被截断的 order_id 值
-- 基于观察，所有完整UUID似乎都以"5a6b"结尾

-- 1. 先确认截断的模式并测试修复
SELECT 
    tg.order_id AS truncated_order_id,
    t.id AS full_order_id,
    CONCAT(tg.order_id, '5a6b') AS corrected_order_id
FROM tn_order_goods tg
LEFT JOIN tn_order t ON CONCAT(tg.order_id, '5a6b') = t.id
WHERE t.id IS NOT NULL
LIMIT 5;

-- 2. 更新所有截断的 order_id 值
UPDATE tn_order_goods tg
JOIN tn_order t ON CONCAT(tg.order_id, '5a6b') = t.id
SET tg.order_id = t.id;

-- 3. 验证修复结果
SELECT 
    LENGTH(order_id) as order_id_length,
    COUNT(*) as count 
FROM tn_order_goods 
GROUP BY LENGTH(order_id);

-- 4. 验证关联查询是否正常工作
SELECT 
    t.id AS order_id,
    t.order_no,
    COUNT(tg.id) AS goods_count
FROM tn_order t
LEFT JOIN tn_order_goods tg ON t.id = tg.order_id
GROUP BY t.id, t.order_no
LIMIT 5;
