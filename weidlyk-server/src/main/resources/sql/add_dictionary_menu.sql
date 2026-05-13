-- 添加字典管理菜单到系统
-- 执行此脚本后，字典管理菜单会出现在系统中

-- 1. 添加字典管理父菜单
INSERT INTO t_permission (name, code, url, type, parent_id, order_no, icon) 
VALUES ('字典管理', 'dictionary', '', 'menu', 0, 8, 'Management');

-- 2. 添加字典类型管理子菜单
INSERT INTO t_permission (name, code, url, type, parent_id, order_no, icon) 
VALUES ('字典类型', 'dictionary:type', '/dashboard/dictionary/type', 'menu', 
        (SELECT id FROM t_permission WHERE code = 'dictionary'), 1, 'List');

-- 3. 添加字典值管理子菜单
INSERT INTO t_permission (name, code, url, type, parent_id, order_no, icon) 
VALUES ('字典值', 'dictionary:value', '/dashboard/dictionary/value', 'menu', 
        (SELECT id FROM t_permission WHERE code = 'dictionary'), 2, 'List');

-- 4. 将字典管理权限分配给管理员角色（假设管理员角色ID为1）
INSERT INTO t_role_permission (role_id, permission_id)
SELECT 1, id FROM t_permission WHERE code IN (
    'dictionary', 'dictionary:type', 'dictionary:value'
) AND id NOT IN (SELECT permission_id FROM t_role_permission WHERE role_id = 1);

-- 验证插入结果
SELECT * FROM t_permission WHERE code LIKE 'dictionary%' ORDER BY id DESC;
