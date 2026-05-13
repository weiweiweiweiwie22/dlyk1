# 字典管理模块 - 文档索引

## 📚 文档导航

### 🚀 快速开始（推荐首先阅读）
- **[DICTIONARY_QUICK_START.md](./DICTIONARY_QUICK_START.md)** - 5分钟快速上手
  - 数据库初始化
  - 应用启动
  - 页面访问
  - 常用API调用

### 📖 完整指南
- **[DICTIONARY_MODULE_README.md](./DICTIONARY_MODULE_README.md)** - 完整使用指南
  - 模块概述
  - 功能特性
  - API接口文档（20个接口）
  - 前端页面说明
  - 数据库表结构
  - 初始化步骤
  - 常见问题解答

### 💡 使用示例
- **[DICTIONARY_USAGE_EXAMPLES.md](./DICTIONARY_USAGE_EXAMPLES.md)** - 详细使用示例
  - 后端使用示例
  - 前端使用示例
  - 常见场景实现（4个场景）
  - 性能优化建议

### 🔧 模块总结
- **[DICTIONARY_MODULE_SUMMARY.md](./DICTIONARY_MODULE_SUMMARY.md)** - 完整实现总结
  - 实现清单
  - API接口总览
  - 工具函数总览
  - 文件结构
  - 核心特性
  - 技术栈
  - 扩展建议
  - 性能指标

### ✅ 完成检查
- **[DICTIONARY_CHECKLIST.md](./DICTIONARY_CHECKLIST.md)** - 完成检查清单
  - 后端实现检查
  - 前端实现检查
  - 文档完整性检查
  - 功能完整性检查
  - 代码质量检查
  - 部署前检查清单

### 🎉 项目总结
- **[DICTIONARY_COMPLETION_SUMMARY.md](./DICTIONARY_COMPLETION_SUMMARY.md)** - 项目完成总结
  - 交付物清单
  - 核心功能
  - API接口统计
  - 技术栈
  - 性能指标
  - 快速开始
  - 项目亮点

## 📁 文件结构

### 后端文件

#### 控制器层
```
src/main/java/com/weiwei/weidlykserver/controller/dic/
├── DicTypeController.java      # 字典类型管理控制器
└── DicValueController.java     # 字典值管理控制器
```

#### 服务层
```
src/main/java/com/weiwei/weidlykserver/service/
├── DicTypeService.java         # 字典类型服务接口
├── DicValueService.java        # 字典值服务接口
└── impl/
    ├── DicTypeServiceImpl.java  # 字典类型服务实现
    └── DicValueServiceImpl.java # 字典值服务实现
```

#### 实体层
```
src/main/java/com/weiwei/weidlykserver/entity/
├── DicType.java                # 字典类型实体
└── DicValue.java               # 字典值实体
```

#### 工具层
```
src/main/java/com/weiwei/weidlykserver/util/
└── DictionaryUtils.java        # 字典工具类
```

#### 测试层
```
src/test/java/com/weiwei/weidlykserver/controller/dic/
└── DictionaryManagementTest.java # 集成测试
```

#### 数据库脚本
```
src/main/resources/
├── mapper/
│   ├── DicTypeMapper.xml       # 字典类型Mapper
│   └── DicValueMapper.xml      # 字典值Mapper
└── sql/
    ├── dictionary_init_data.sql        # 初始化数据
    └── dictionary_permissions.sql      # 权限配置
```

### 前端文件

#### 页面组件
```
src/view/
├── DictionaryTypeView.vue      # 字典类型管理页面
└── DictionaryValueView.vue     # 字典值管理页面
```

#### 工具函数
```
src/util/
└── dictionaryUtils.js          # 字典工具函数
```

#### 路由配置
```
src/router/
└── router.js                   # 路由配置（已更新）
```

## 🎯 按用途查找文档

### 我想快速上手
👉 阅读 [DICTIONARY_QUICK_START.md](./DICTIONARY_QUICK_START.md)

### 我想了解完整功能
👉 阅读 [DICTIONARY_MODULE_README.md](./DICTIONARY_MODULE_README.md)

### 我想看代码示例
👉 阅读 [DICTIONARY_USAGE_EXAMPLES.md](./DICTIONARY_USAGE_EXAMPLES.md)

### 我想了解技术实现
👉 阅读 [DICTIONARY_MODULE_SUMMARY.md](./DICTIONARY_MODULE_SUMMARY.md)

### 我想检查完成情况
👉 阅读 [DICTIONARY_CHECKLIST.md](./DICTIONARY_CHECKLIST.md)

### 我想了解项目概况
👉 阅读 [DICTIONARY_COMPLETION_SUMMARY.md](./DICTIONARY_COMPLETION_SUMMARY.md)

## 📊 API接口速查

### 字典类型接口
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/dicType/all | 获取所有字典类型 |
| GET | /api/dicType/list | 分页查询字典类型 |
| POST | /api/dicType/add | 新增字典类型 |
| PUT | /api/dicType/edit | 修改字典类型 |
| DELETE | /api/dicType/delete/{id} | 删除字典类型 |
| DELETE | /api/dicType/batchDelete | 批量删除字典类型 |

### 字典值接口
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/dicValue/all | 获取所有字典值 |
| GET | /api/dicValue/list | 分页查询字典值 |
| GET | /api/dicValue/listByTypeCode/{code} | 按类型代码获取字典值 |
| POST | /api/dicValue/add | 新增字典值 |
| PUT | /api/dicValue/edit | 修改字典值 |
| DELETE | /api/dicValue/delete/{id} | 删除字典值 |
| DELETE | /api/dicValue/batchDelete | 批量删除字典值 |

### 业务接口
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/dicValue/appellation/all | 获取称呼列表 |
| GET | /api/dicValue/status/all | 获取线索状态列表 |
| GET | /api/dicValue/intentionProduct/all | 获取意向产品列表 |
| GET | /api/dicValue/needsLoan/all | 获取贷款列表 |
| GET | /api/dicValue/intentionStatus/all | 获取意向状态列表 |
| GET | /api/dicValue/source/all | 获取来源列表 |
| GET | /api/dicValue/noteWay/all | 获取跟踪方式列表 |

## 🛠️ 前端工具函数速查

### 基础函数
```javascript
getDicValuesByTypeCode(typeCode)    // 获取字典值列表
clearDicCache(typeCode)             // 清空缓存
getCachedDicValues(typeCode)        // 获取缓存数据
isCached(typeCode)                  // 检查是否已缓存
```

### 便捷方法
```javascript
getAppellationList()                // 获取称呼列表
getClueStateList()                  // 获取线索状态列表
getIntentionProductList()           // 获取意向产品列表
getNeedLoanList()                   // 获取贷款列表
getIntentionStateList()             // 获取意向状态列表
getSourceList()                     // 获取来源列表
getNoteWayList()                    // 获取跟踪方式列表
```

### 高级函数
```javascript
getDicValueText(typeCode, typeValue)    // 获取字典值文本
getBatchDicValues(typeCodes)            // 批量获取字典
createDicOptions(typeCode)              // 创建选项列表
preloadCommonDictionaries()             // 预加载常用字典
getCacheStats()                         // 获取缓存统计
```

## 📋 常见问题速查

### 页面无法访问
👉 查看 [DICTIONARY_QUICK_START.md - 故障排除](./DICTIONARY_QUICK_START.md#故障排除)

### 字典数据为空
👉 查看 [DICTIONARY_MODULE_README.md - 常见问题](./DICTIONARY_MODULE_README.md#常见问题)

### 如何在其他模块中使用字典
👉 查看 [DICTIONARY_USAGE_EXAMPLES.md - 后端使用示例](./DICTIONARY_USAGE_EXAMPLES.md#后端使用示例)

### 如何优化性能
👉 查看 [DICTIONARY_USAGE_EXAMPLES.md - 性能优化建议](./DICTIONARY_USAGE_EXAMPLES.md#性能优化建议)

## 🚀 快速命令

### 数据库初始化
```bash
# 执行初始化脚本
mysql -u root -p < src/main/resources/sql/dictionary_init_data.sql

# 执行权限配置脚本（可选）
mysql -u root -p < src/main/resources/sql/dictionary_permissions.sql
```

### 应用启动
```bash
# 后端启动
mvn spring-boot:run

# 前端启动
npm run dev
```

### 访问地址
```
字典类型管理：http://localhost:8080/dashboard/dictionary/type
字典值管理：http://localhost:8080/dashboard/dictionary/value
```

## 📞 获取帮助

### 第一步：查看文档
1. 查看相关文档中的说明
2. 查看常见问题部分
3. 查看代码示例

### 第二步：检查日志
1. 查看后端日志
2. 查看浏览器控制台
3. 查看数据库连接

### 第三步：联系支持
1. 联系开发团队
2. 提供错误日志
3. 描述问题场景

## 📈 文档统计

| 文档 | 行数 | 内容 |
|------|------|------|
| DICTIONARY_QUICK_START.md | 233 | 快速开始指南 |
| DICTIONARY_MODULE_README.md | 338 | 完整使用指南 |
| DICTIONARY_USAGE_EXAMPLES.md | 457 | 详细使用示例 |
| DICTIONARY_MODULE_SUMMARY.md | 369 | 模块总结 |
| DICTIONARY_CHECKLIST.md | 369 | 完成检查清单 |
| DICTIONARY_COMPLETION_SUMMARY.md | 328 | 项目完成总结 |
| DICTIONARY_INDEX.md | 本文件 | 文档索引 |

**总计：2091行文档**

## 🎓 学习路径

### 初级（了解基础）
1. 阅读 [DICTIONARY_QUICK_START.md](./DICTIONARY_QUICK_START.md)
2. 访问管理页面
3. 尝试基本操作

### 中级（掌握使用）
1. 阅读 [DICTIONARY_MODULE_README.md](./DICTIONARY_MODULE_README.md)
2. 查看 [DICTIONARY_USAGE_EXAMPLES.md](./DICTIONARY_USAGE_EXAMPLES.md)
3. 在项目中集成使用

### 高级（深入理解）
1. 阅读 [DICTIONARY_MODULE_SUMMARY.md](./DICTIONARY_MODULE_SUMMARY.md)
2. 查看源代码
3. 进行扩展开发

## 🔗 相关链接

### 项目文件
- [后端代码](./weidlyk-server/src/main/java/com/weiwei/weidlykserver/)
- [前端代码](./weidlyk-front/src/)
- [数据库脚本](./weidlyk-server/src/main/resources/sql/)

### 外部资源
- [Spring Boot官方文档](https://spring.io/projects/spring-boot)
- [MyBatis Plus官方文档](https://baomidou.com/)
- [Vue 3官方文档](https://vuejs.org/)
- [Element Plus官方文档](https://element-plus.org/)

## 📝 版本信息

- **模块名称**：字典管理模块
- **版本号**：1.0.0
- **发布日期**：2025年3月20日
- **状态**：✅ 生产就绪
- **文档版本**：1.0.0
- **最后更新**：2025年3月20日

## 🎯 下一步

1. **立即开始**
   - 阅读 [DICTIONARY_QUICK_START.md](./DICTIONARY_QUICK_START.md)
   - 执行数据库脚本
   - 启动应用

2. **深入学习**
   - 阅读完整文档
   - 查看代码示例
   - 在项目中使用

3. **扩展开发**
   - 添加新功能
   - 优化性能
   - 改进用户体验

---

**提示**：这是文档索引文件，用于快速查找相关文档。建议将此文件作为入口点。

**建议阅读顺序**：
1. 本文件（了解文档结构）
2. DICTIONARY_QUICK_START.md（快速上手）
3. DICTIONARY_MODULE_README.md（完整指南）
4. DICTIONARY_USAGE_EXAMPLES.md（使用示例）
5. 其他文档（按需查阅）
