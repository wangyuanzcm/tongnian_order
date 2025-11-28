-- 检查表是否存在，如果不存在则创建
CREATE TABLE IF NOT EXISTS `tn_image` (
  `id` varchar(32) NOT NULL COMMENT '主键ID',
  `url` varchar(500) DEFAULT NULL COMMENT '图片URL',
  `name` varchar(200) DEFAULT NULL COMMENT '图片名称',
  `sort` int DEFAULT NULL COMMENT '排序',
  `status` int DEFAULT NULL COMMENT '状态',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='图片表';

-- 创建商品图片关联表
CREATE TABLE IF NOT EXISTS `tn_goods_images` (
  `id` varchar(32) NOT NULL COMMENT '主键ID',
  `goods_spec_id` varchar(32) DEFAULT NULL COMMENT '商品规格ID',
  `image_id` varchar(32) DEFAULT NULL COMMENT '图片ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门',
  PRIMARY KEY (`id`),
  KEY `idx_goods_spec_id` (`goods_spec_id`),
  KEY `idx_image_id` (`image_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品图片关联表';

-- 注意：如果需要删除外键约束，请根据实际情况单独执行以下语句
-- ALTER TABLE `tn_image` DROP FOREIGN KEY `fk_image_goods`;
-- ALTER TABLE `tn_image` DROP FOREIGN KEY `fk_image_goods_spec`;

-- 迁移数据到图片表和商品图片关联表
-- 注意：image_url 字段是二进制格式，需要先转换为 UTF-8 字符串，然后再处理 JSON
INSERT INTO `tn_image` (`id`, `url`, `name`, `sort`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `sys_org_code`)
SELECT 
  -- 使用UUID_SHORT()生成更短的唯一ID
  CONCAT('IMG', UUID_SHORT()) as id,
  -- 先将二进制数据转换为 UTF-8 字符串，然后提取 JSON 数据
  JSON_UNQUOTE(JSON_EXTRACT(CONVERT(image_url USING utf8mb4), '$[0].url')) as url,
  JSON_UNQUOTE(JSON_EXTRACT(CONVERT(image_url USING utf8mb4), '$[0].name')) as name,
  0 as sort,
  1 as status,
  create_by,
  create_time,
  update_by,
  update_time,
  sys_org_code
FROM `tn_goods_spec`
WHERE image_url IS NOT NULL AND image_url != '' AND image_url != '[]';

-- 关联商品规格和图片
INSERT INTO `tn_goods_images` (`id`, `goods_spec_id`, `image_id`, `create_by`, `create_time`, `update_by`, `update_time`, `sys_org_code`)
SELECT 
  -- 使用UUID_SHORT()生成更短的唯一ID
  CONCAT('GI', UUID_SHORT()) as id,
  tgs.id as goods_spec_id,
  ti.id as image_id,
  tgs.create_by,
  tgs.create_time,
  tgs.update_by,
  tgs.update_time,
  tgs.sys_org_code
FROM `tn_goods_spec` tgs
INNER JOIN `tn_image` ti ON JSON_UNQUOTE(JSON_EXTRACT(CONVERT(tgs.image_url USING utf8mb4), '$[0].url')) = ti.url
WHERE tgs.image_url IS NOT NULL AND tgs.image_url != '' AND tgs.image_url != '[]';

-- 清空原image_url字段（可选，根据实际需求决定是否执行）
-- UPDATE `tn_goods_spec` SET image_url = '[]' WHERE image_url IS NOT NULL AND image_url != '[]';