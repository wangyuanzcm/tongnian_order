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
-- 表的结构 `tn_order`
--

CREATE TABLE `tn_order` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所属部门',
  `order_no` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单编号',
  `payable_amount` decimal(10,2) DEFAULT NULL COMMENT '应付金额',
  `paid_amount` decimal(10,2) DEFAULT NULL COMMENT '实付金额',
  `collect_amount` decimal(10,2) DEFAULT NULL COMMENT '代收金额',
  `recipient_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收件人',
  `order_status` int DEFAULT NULL COMMENT '订单状态',
  `is_new_customer` int DEFAULT NULL COMMENT '是否新客户',
  `goods_spec_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单和商品映射id',
  `good_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- 表的结构 `tn_order_goods`
--

CREATE TABLE `tn_order_goods` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所属部门',
  `order_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单id',
  `goods_spec` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品id',
  `goods_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品名称',
  `unit_price` decimal(10,2) DEFAULT NULL COMMENT '商品单价',
  `quantity` int DEFAULT NULL COMMENT '商品数量'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- 转储表的索引
--

--
-- 表的索引 `tn_order`
--
ALTER TABLE `tn_order`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_order_status` (`order_status`),
  ADD KEY `idx_order_buyer` (`create_by`);

--
-- 表的索引 `tn_order_goods`
--
ALTER TABLE `tn_order_goods`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_order_goods_order` (`order_id`),
  ADD KEY `idx_order_goods_goods` (`goods_spec`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
