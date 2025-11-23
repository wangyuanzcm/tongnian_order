-- 注意：该页面对应的前台目录为views/goods文件夹下
-- 如果你想更改到其他目录，请修改sql中component字段对应的值


-- 主菜单
INSERT INTO sys_permission(id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, hide_tab, description, status, del_flag, rule_flag, create_by, create_time, update_by, update_time, internal_or_external)
VALUES ('176386940515801', NULL, '商品列表', '/goods/tnGoodsList', 'goods/TnGoodsList', NULL, NULL, 0, NULL, '1', 0.00, 0, NULL, 1, 0, 0, 0, 0, NULL, '1', 0, 0, 'admin', '2025-11-23 11:43:25', NULL, NULL, 0);

-- 新增
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('176386940515802', '176386940515801', '添加商品列表', NULL, NULL, 0, NULL, NULL, 2, 'goods:tn_goods:add', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-11-23 11:43:25', NULL, NULL, 0, 0, '1', 0);

-- 编辑
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('176386940515803', '176386940515801', '编辑商品列表', NULL, NULL, 0, NULL, NULL, 2, 'goods:tn_goods:edit', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-11-23 11:43:25', NULL, NULL, 0, 0, '1', 0);

-- 删除
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('176386940515804', '176386940515801', '删除商品列表', NULL, NULL, 0, NULL, NULL, 2, 'goods:tn_goods:delete', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-11-23 11:43:25', NULL, NULL, 0, 0, '1', 0);

-- 批量删除
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('176386940515805', '176386940515801', '批量删除商品列表', NULL, NULL, 0, NULL, NULL, 2, 'goods:tn_goods:deleteBatch', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-11-23 11:43:25', NULL, NULL, 0, 0, '1', 0);

-- 导出excel
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('176386940515806', '176386940515801', '导出excel_商品列表', NULL, NULL, 0, NULL, NULL, 2, 'goods:tn_goods:exportXls', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-11-23 11:43:25', NULL, NULL, 0, 0, '1', 0);

-- 导入excel
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('176386940515807', '176386940515801', '导入excel_商品列表', NULL, NULL, 0, NULL, NULL, 2, 'goods:tn_goods:importExcel', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-11-23 11:43:25', NULL, NULL, 0, 0, '1', 0);

-- 角色授权（以 admin 角色为例，role_id 可替换）
INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip) VALUES ('176386940515808', 'f6817f48af4fb3af11b9e8bf182f618b', '176386940515801', NULL, '2025-11-23 11:43:25', '127.0.0.1');
INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip) VALUES ('176386940515809', 'f6817f48af4fb3af11b9e8bf182f618b', '176386940515802', NULL, '2025-11-23 11:43:25', '127.0.0.1');
INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip) VALUES ('176386940515810', 'f6817f48af4fb3af11b9e8bf182f618b', '176386940515803', NULL, '2025-11-23 11:43:25', '127.0.0.1');
INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip) VALUES ('176386940515811', 'f6817f48af4fb3af11b9e8bf182f618b', '176386940515804', NULL, '2025-11-23 11:43:25', '127.0.0.1');
INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip) VALUES ('176386940515812', 'f6817f48af4fb3af11b9e8bf182f618b', '176386940515805', NULL, '2025-11-23 11:43:25', '127.0.0.1');
INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip) VALUES ('176386940515813', 'f6817f48af4fb3af11b9e8bf182f618b', '176386940515806', NULL, '2025-11-23 11:43:25', '127.0.0.1');
INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip) VALUES ('176386940515814', 'f6817f48af4fb3af11b9e8bf182f618b', '176386940515807', NULL, '2025-11-23 11:43:25', '127.0.0.1');