-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- 主机： mysql:3306
-- 生成日期： 2025-11-29 03:38:09
-- 服务器版本： 9.5.0
-- PHP 版本： 8.3.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 数据库： `tongnian`
--

-- --------------------------------------------------------

--
-- 表的结构 `tn_cart`
--

CREATE TABLE `tn_cart` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所属部门',
  `user_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户ID',
  `goods_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品ID',
  `goods_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
  `spec_type` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品规格',
  `price` decimal(10,2) NOT NULL COMMENT '商品单价',
  `quantity` int NOT NULL DEFAULT 1 COMMENT '商品数量',
  `image_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品图片',
  `is_selected` tinyint DEFAULT 0 COMMENT '是否选中（0：未选中，1：选中）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- 转储表的索引
--

--
-- 表的索引 `tn_cart`
--
ALTER TABLE `tn_cart`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `idx_user_goods_spec` (`user_id`,`goods_id`,`spec_type`),
  ADD KEY `idx_user_id` (`user_id`),
  ADD KEY `idx_goods_id` (`goods_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
