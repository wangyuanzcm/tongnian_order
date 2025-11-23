-- 注意：该页面对应的前台目录为views/receiver文件夹下
-- 如果你想更改到其他目录，请修改sql中component字段对应的值


-- 主菜单
INSERT INTO sys_permission(id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, hide_tab, description, status, del_flag, rule_flag, create_by, create_time, update_by, update_time, internal_or_external)
VALUES ('176380685896301', NULL, '收件人信息', '/receiver/tnRecipientList', 'receiver/TnRecipientList', NULL, NULL, 0, NULL, '1', 0.00, 0, NULL, 1, 0, 0, 0, 0, NULL, '1', 0, 0, 'admin', '2025-11-22 18:20:58', NULL, NULL, 0);

-- 新增
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('176380685896302', '176380685896301', '添加收件人信息', NULL, NULL, 0, NULL, NULL, 2, 'receiver:tn_recipient:add', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-11-22 18:20:58', NULL, NULL, 0, 0, '1', 0);

-- 编辑
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('176380685896303', '176380685896301', '编辑收件人信息', NULL, NULL, 0, NULL, NULL, 2, 'receiver:tn_recipient:edit', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-11-22 18:20:58', NULL, NULL, 0, 0, '1', 0);

-- 删除
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('176380685896304', '176380685896301', '删除收件人信息', NULL, NULL, 0, NULL, NULL, 2, 'receiver:tn_recipient:delete', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-11-22 18:20:58', NULL, NULL, 0, 0, '1', 0);

-- 批量删除
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('176380685896305', '176380685896301', '批量删除收件人信息', NULL, NULL, 0, NULL, NULL, 2, 'receiver:tn_recipient:deleteBatch', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-11-22 18:20:58', NULL, NULL, 0, 0, '1', 0);

-- 导出excel
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('176380685896306', '176380685896301', '导出excel_收件人信息', NULL, NULL, 0, NULL, NULL, 2, 'receiver:tn_recipient:exportXls', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-11-22 18:20:58', NULL, NULL, 0, 0, '1', 0);

-- 导入excel
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('176380685896307', '176380685896301', '导入excel_收件人信息', NULL, NULL, 0, NULL, NULL, 2, 'receiver:tn_recipient:importExcel', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-11-22 18:20:58', NULL, NULL, 0, 0, '1', 0);

-- 角色授权（以 admin 角色为例，role_id 可替换）
INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip) VALUES ('176380685896308', 'f6817f48af4fb3af11b9e8bf182f618b', '176380685896301', NULL, '2025-11-22 18:20:58', '127.0.0.1');
INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip) VALUES ('176380685896309', 'f6817f48af4fb3af11b9e8bf182f618b', '176380685896302', NULL, '2025-11-22 18:20:58', '127.0.0.1');
INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip) VALUES ('176380685896310', 'f6817f48af4fb3af11b9e8bf182f618b', '176380685896303', NULL, '2025-11-22 18:20:58', '127.0.0.1');
INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip) VALUES ('176380685896311', 'f6817f48af4fb3af11b9e8bf182f618b', '176380685896304', NULL, '2025-11-22 18:20:58', '127.0.0.1');
INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip) VALUES ('176380685896312', 'f6817f48af4fb3af11b9e8bf182f618b', '176380685896305', NULL, '2025-11-22 18:20:58', '127.0.0.1');
INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip) VALUES ('176380685896313', 'f6817f48af4fb3af11b9e8bf182f618b', '176380685896306', NULL, '2025-11-22 18:20:58', '127.0.0.1');
INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip) VALUES ('176380685896314', 'f6817f48af4fb3af11b9e8bf182f618b', '176380685896307', NULL, '2025-11-22 18:20:58', '127.0.0.1');