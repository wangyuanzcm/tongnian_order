-- 注意：该页面对应的前台目录为views/goods文件夹下
-- 如果你想更改到其他目录，请修改sql中component字段对应的值


-- 主菜单
INSERT INTO sys_permission(id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, hide_tab, description, status, del_flag, rule_flag, create_by, create_time, update_by, update_time, internal_or_external)
VALUES ('176386970799201', NULL, '商品列表', '/goods/tnGoodsList', 'goods/TnGoodsList', NULL, NULL, 0, NULL, '1', 0.00, 0, NULL, 1, 0, 0, 0, 0, NULL, '1', 0, 0, 'admin', '2025-11-23 11:48:27', NULL, NULL, 0);

-- 新增
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('176386970799202', '176386970799201', '添加商品列表', NULL, NULL, 0, NULL, NULL, 2, 'goods:tn_goods:add', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-11-23 11:48:27', NULL, NULL, 0, 0, '1', 0);

-- 编辑
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('176386970799203', '176386970799201', '编辑商品列表', NULL, NULL, 0, NULL, NULL, 2, 'goods:tn_goods:edit', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-11-23 11:48:27', NULL, NULL, 0, 0, '1', 0);

-- 删除
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('176386970799204', '176386970799201', '删除商品列表', NULL, NULL, 0, NULL, NULL, 2, 'goods:tn_goods:delete', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-11-23 11:48:27', NULL, NULL, 0, 0, '1', 0);

-- 批量删除
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('176386970799205', '176386970799201', '批量删除商品列表', NULL, NULL, 0, NULL, NULL, 2, 'goods:tn_goods:deleteBatch', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-11-23 11:48:27', NULL, NULL, 0, 0, '1', 0);

-- 导出excel
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('176386970799206', '176386970799201', '导出excel_商品列表', NULL, NULL, 0, NULL, NULL, 2, 'goods:tn_goods:exportXls', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-11-23 11:48:27', NULL, NULL, 0, 0, '1', 0);

-- 导入excel
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('176386970799207', '176386970799201', '导入excel_商品列表', NULL, NULL, 0, NULL, NULL, 2, 'goods:tn_goods:importExcel', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-11-23 11:48:27', NULL, NULL, 0, 0, '1', 0);

-- 角色授权（以 admin 角色为例，role_id 可替换）
INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip) VALUES ('176386970799208', 'f6817f48af4fb3af11b9e8bf182f618b', '176386970799201', NULL, '2025-11-23 11:48:27', '127.0.0.1');
INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip) VALUES ('176386970799209', 'f6817f48af4fb3af11b9e8bf182f618b', '176386970799202', NULL, '2025-11-23 11:48:27', '127.0.0.1');
INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip) VALUES ('176386970799210', 'f6817f48af4fb3af11b9e8bf182f618b', '176386970799203', NULL, '2025-11-23 11:48:27', '127.0.0.1');
INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip) VALUES ('176386970799211', 'f6817f48af4fb3af11b9e8bf182f618b', '176386970799204', NULL, '2025-11-23 11:48:27', '127.0.0.1');
INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip) VALUES ('176386970799212', 'f6817f48af4fb3af11b9e8bf182f618b', '176386970799205', NULL, '2025-11-23 11:48:27', '127.0.0.1');
INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip) VALUES ('176386970799213', 'f6817f48af4fb3af11b9e8bf182f618b', '176386970799206', NULL, '2025-11-23 11:48:27', '127.0.0.1');
INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip) VALUES ('176386970799214', 'f6817f48af4fb3af11b9e8bf182f618b', '176386970799207', NULL, '2025-11-23 11:48:27', '127.0.0.1');