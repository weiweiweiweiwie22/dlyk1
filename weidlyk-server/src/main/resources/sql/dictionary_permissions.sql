-- 添加字典管理菜单权限的SQL脚本
-- 注意：请根据实际的数据库ID自增情况调整ID值

-- 1. 添加字典管理父菜单
INSERT INTO t_permission (name, code, url, type, parent_id, order_no, icon) 
VALUES ('字典管理', 'dictionary', '', 'menu', 0, 8, 'Management');

-- 获取刚插入的字典管理菜单ID（假设为 @dictionaryMenuId）
-- 在实际执行时，需要查询最新插入的ID

-- 2. 添加字典类型管理子菜单
INSERT INTO t_permission (name, code, url, type, parent_id, order_no, icon) 
VALUES ('字典类型', 'dictionary:type', '/dashboard/dictionary/type', 'menu', 
        (SELECT id FROM t_permission WHERE code = 'dictionary'), 1, 'List');

-- 3. 添加字典值管理子菜单
INSERT INTO t_permission (name, code, url, type, parent_id, order_no, icon) 
VALUES ('字典值', 'dictionary:value', '/dashboard/dictionary/value', 'menu', 
        (SELECT id FROM t_permission WHERE code = 'dictionary'), 2, 'List');

-- 4. 添加字典类型管理的操作权限
INSERT INTO t_permission (name, code, url, type, parent_id, order_no, icon) 
VALUES ('字典类型查询', 'dictionary:type:query', '', 'button', 
        (SELECT id FROM t_permission WHERE code = 'dictionary:type'), 1, NULL);

INSERT INTO t_permission (name, code, url, type, parent_id, order_no, icon) 
VALUES ('字典类型新增', 'dictionary:type:add', '', 'button', 
        (SELECT id FROM t_permission WHERE code = 'dictionary:type'), 2, NULL);

INSERT INTO t_permission (name, code, url, type, parent_id, order_no, icon) 
VALUES ('字典类型编辑', 'dictionary:type:edit', '', 'button', 
        (SELECT id FROM t_permission WHERE code = 'dictionary:type'), 3, NULL);

INSERT INTO t_permission (name, code, url, type, parent_id, order_no, icon) 
VALUES ('字典类型删除', 'dictionary:type:delete', '', 'button', 
        (SELECT id FROM t_permission WHERE code = 'dictionary:type'), 4, NULL);

-- 5. 添加字典值管理的操作权限
INSERT INTO t_permission (name, code, url, type, parent_id, order_no, icon) 
VALUES ('字典值查询', 'dictionary:value:query', '', 'button', 
        (SELECT id FROM t_permission WHERE code = 'dictionary:value'), 1, NULL);

INSERT INTO t_permission (name, code, url, type, parent_id, order_no, icon) 
VALUES ('字典值新增', 'dictionary:value:add', '', 'button', 
        (SELECT id FROM t_permission WHERE code = 'dictionary:value'), 2, NULL);

INSERT INTO t_permission (name, code, url, type, parent_id, order_no, icon) 
VALUES ('字典值编辑', 'dictionary:value:edit', '', 'button', 
        (SELECT id FROM t_permission WHERE code = 'dictionary:value'), 3, NULL);

INSERT INTO t_permission (name, code, url, type, parent_id, order_no, icon) 
VALUES ('字典值删除', 'dictionary:value:delete', '', 'button', 
        (SELECT id FROM t_permission WHERE code = 'dictionary:value'), 4, NULL);

-- 6. 将字典管理权限分配给管理员角色（假设管理员角色ID为1）
-- 先获取字典管理相关的所有权限ID，然后插入到t_role_permission表
INSERT INTO t_role_permission (role_id, permission_id)
SELECT 1, id FROM t_permission WHERE code IN (
    'dictionary', 'dictionary:type', 'dictionary:value',
    'dictionary:type:query', 'dictionary:type:add', 'dictionary:type:edit', 'dictionary:type:delete',
    'dictionary:value:query', 'dictionary:value:add', 'dictionary:value:edit', 'dictionary:value:delete'
) AND id NOT IN (SELECT permission_id FROM t_role_permission WHERE role_id = 1);
