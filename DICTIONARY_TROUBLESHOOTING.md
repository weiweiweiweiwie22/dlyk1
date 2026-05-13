# 字典管理模块 - 问题排查指南

## 问题描述
点击字典管理菜单后，页面显示空白，功能没有实现。

## 🔍 问题原因分析

### 原因1：菜单权限未配置（最可能）
系统菜单是从后端数据库动态加载的，如果数据库中没有添加字典管理菜单权限，前端就无法显示菜单项。

**症状**：
- 菜单中看不到"字典管理"选项
- 直接访问URL也显示空白页面

**解决方案**：执行菜单权限配置脚本

### 原因2：路由配置不正确
前端路由配置可能有问题。

**症状**：
- 菜单能点击，但页面空白
- 浏览器控制台有错误信息

**解决方案**：检查路由配置

### 原因3：页面组件未正确加载
Vue组件可能没有正确导入或编译。

**症状**：
- 浏览器控制台显示404错误
- 网络请求中看不到组件文件

**解决方案**：检查组件文件是否存在

## ✅ 解决步骤

### 步骤1：添加菜单权限（必须）

在MySQL数据库中执行以下脚本：

```sql
-- 添加字典管理菜单
INSERT INTO t_permission (name, code, url, type, parent_id, order_no, icon) 
VALUES ('字典管理', 'dictionary', '', 'menu', 0, 8, 'Management');

-- 添加字典类型管理子菜单
INSERT INTO t_permission (name, code, url, type, parent_id, order_no, icon) 
VALUES ('字典类型', 'dictionary:type', '/dashboard/dictionary/type', 'menu', 
        (SELECT id FROM t_permission WHERE code = 'dictionary'), 1, 'List');

-- 添加字典值管理子菜单
INSERT INTO t_permission (name, code, url, type, parent_id, order_no, icon) 
VALUES ('字典值', 'dictionary:value', '/dashboard/dictionary/value', 'menu', 
        (SELECT id FROM t_permission WHERE code = 'dictionary'), 2, 'List');

-- 将权限分配给管理员角色（假设管理员角色ID为1）
INSERT INTO t_role_permission (role_id, permission_id)
SELECT 1, id FROM t_permission WHERE code IN (
    'dictionary', 'dictionary:type', 'dictionary:value'
) AND id NOT IN (SELECT permission_id FROM t_role_permission WHERE role_id = 1);
```

**或者使用脚本文件：**
```bash
mysql -u root -p < weidlyk-server/src/main/resources/sql/add_dictionary_menu.sql
```

### 步骤2：重新登录

1. 退出当前登录
2. 重新登录系统
3. 菜单应该会显示"字典管理"选项

### 步骤3：验证菜单是否出现

登录后，在左侧菜单中应该看到：
```
字典管理
  ├── 字典类型
  └── 字典值
```

### 步骤4：点击菜单项

- 点击"字典类型"应该显示字典类型管理页面
- 点击"字典值"应该显示字典值管理页面

## 🐛 常见问题排查

### 问题1：执行SQL脚本后仍然看不到菜单

**检查清单：**
1. ✅ 确认SQL脚本执行成功（查看数据库中是否有新记录）
2. ✅ 确认已重新登录
3. ✅ 确认用户角色ID是否为1（如果不是，修改SQL中的角色ID）
4. ✅ 清空浏览器缓存，刷新页面

**验证SQL执行结果：**
```sql
-- 查看是否添加了菜单
SELECT * FROM t_permission WHERE code LIKE 'dictionary%';

-- 查看是否分配了权限
SELECT * FROM t_role_permission WHERE permission_id IN (
    SELECT id FROM t_permission WHERE code LIKE 'dictionary%'
);
```

### 问题2：菜单能看到，但点击后页面空白

**检查清单：**
1. ✅ 打开浏览器开发者工具（F12）
2. ✅ 查看Console标签是否有错误信息
3. ✅ 查看Network标签是否有404错误
4. ✅ 检查路由配置是否正确

**查看浏览器错误：**
```javascript
// 在浏览器控制台执行
console.log(window.location.href)  // 查看当前URL
```

### 问题3：浏览器控制台显示组件加载错误

**可能的错误信息：**
```
Failed to load module script: ...DictionaryTypeView.vue
404 Not Found
```

**解决方案：**
1. 确认文件是否存在：
   - `weidlyk-front/src/view/DictionaryTypeView.vue`
   - `weidlyk-front/src/view/DictionaryValueView.vue`

2. 重新编译前端：
   ```bash
   cd weidlyk-front
   npm run dev
   ```

### 问题4：后端API返回错误

**检查清单：**
1. ✅ 后端应用是否正常运行
2. ✅ 数据库连接是否正常
3. ✅ 查看后端日志是否有错误

**查看后端日志：**
```bash
# 查看Spring Boot应用日志
tail -f logs/application.log
```

## 📋 完整解决方案

### 方案A：使用SQL脚本（推荐）

```bash
# 1. 执行菜单权限脚本
mysql -u root -p < weidlyk-server/src/main/resources/sql/add_dictionary_menu.sql

# 2. 重启后端应用
cd weidlyk-server
mvn spring-boot:run

# 3. 重新登录前端
# 打开浏览器，访问 http://localhost:8080
# 退出登录，重新登录
```

### 方案B：手动执行SQL

```bash
# 1. 连接数据库
mysql -u root -p

# 2. 选择数据库
use your_database_name;

# 3. 执行SQL脚本（复制上面的SQL语句）
INSERT INTO t_permission ...

# 4. 验证
SELECT * FROM t_permission WHERE code LIKE 'dictionary%';
```

### 方案C：使用数据库管理工具

1. 打开Navicat或其他数据库管理工具
2. 连接到数据库
3. 打开SQL编辑器
4. 复制并执行上面的SQL脚本
5. 验证数据是否正确插入

## 🔧 调试技巧

### 1. 查看菜单加载情况

在浏览器控制台执行：
```javascript
// 查看当前路由
console.log(this.$route)

// 查看所有路由
console.log(this.$router.getRoutes())
```

### 2. 查看API响应

在浏览器Network标签中：
1. 刷新页面
2. 查找 `/api/permission/menus` 请求
3. 查看Response中是否包含字典管理菜单

### 3. 查看数据库数据

```sql
-- 查看所有菜单
SELECT id, name, code, url, type, parent_id FROM t_permission 
WHERE type = 'menu' ORDER BY parent_id, order_no;

-- 查看用户的权限
SELECT p.* FROM t_permission p
JOIN t_role_permission rp ON p.id = rp.permission_id
JOIN t_user_role ur ON rp.role_id = ur.role_id
WHERE ur.user_id = 1;  -- 替换为实际用户ID
```

## 📞 获取帮助

如果按照上述步骤仍然无法解决问题，请：

1. **收集信息**
   - 浏览器控制台的错误信息
   - 后端日志中的错误信息
   - 数据库中的菜单权限数据

2. **检查清单**
   - [ ] SQL脚本是否执行成功
   - [ ] 是否重新登录
   - [ ] 浏览器缓存是否清空
   - [ ] 后端应用是否重启
   - [ ] 前端应用是否重新编译

3. **联系支持**
   - 提供上述信息
   - 描述问题现象
   - 提供复现步骤

## ✅ 验证清单

完成以下步骤后，字典管理功能应该正常工作：

- [ ] 执行了菜单权限SQL脚本
- [ ] 重新登录了系统
- [ ] 菜单中看到了"字典管理"选项
- [ ] 点击"字典类型"显示了管理页面
- [ ] 点击"字典值"显示了管理页面
- [ ] 可以新增、编辑、删除字典数据
- [ ] 分页功能正常
- [ ] 搜索功能正常

## 🎯 快速参考

| 问题 | 解决方案 |
|------|---------|
| 看不到菜单 | 执行菜单权限SQL脚本 |
| 菜单空白 | 检查浏览器控制台错误 |
| 页面加载失败 | 检查组件文件是否存在 |
| API错误 | 检查后端日志 |
| 数据为空 | 执行初始化数据SQL脚本 |

---

**提示**：大多数情况下，问题是由于菜单权限未配置导致的。执行菜单权限SQL脚本通常可以解决问题。
