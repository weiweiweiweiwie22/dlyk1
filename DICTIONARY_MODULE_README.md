# 字典管理模块使用指南

## 模块概述

字典管理模块是一个完整的数据字典管理系统，用于管理系统中的各种字典类型和字典值。通过该模块，管理员可以灵活地维护系统中使用的各种枚举数据。

## 功能特性

### 1. 字典类型管理
- **查询**：支持按类型代码和类型名称模糊查询
- **新增**：添加新的字典类型
- **编辑**：修改现有字典类型信息
- **删除**：删除单个或批量删除字典类型
- **分页**：支持分页显示

### 2. 字典值管理
- **查询**：支持按字典类型和字典值模糊查询
- **新增**：为指定字典类型添加字典值
- **编辑**：修改字典值信息
- **删除**：删除单个或批量删除字典值
- **排序**：支持自定义排序顺序
- **分页**：支持分页显示

## 后端API接口

### 字典类型管理接口

#### 1. 获取所有字典类型
```
GET /api/dicType/all
```
**响应示例：**
```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "typeCode": "appellation",
      "typeName": "称呼",
      "remark": "客户称呼类型"
    }
  ]
}
```

#### 2. 分页查询字典类型
```
GET /api/dicType/list?current=1&size=10&typeCode=&typeName=
```
**参数说明：**
- `current`: 当前页码（默认1）
- `size`: 每页记录数（默认10）
- `typeCode`: 字典类型代码（可选，模糊查询）
- `typeName`: 字典类型名称（可选，模糊查询）

#### 3. 新增字典类型
```
POST /api/dicType/add
Content-Type: application/json

{
  "typeCode": "newType",
  "typeName": "新类型",
  "remark": "备注信息"
}
```

#### 4. 修改字典类型
```
PUT /api/dicType/edit
Content-Type: application/json

{
  "id": 1,
  "typeCode": "appellation",
  "typeName": "称呼",
  "remark": "客户称呼类型"
}
```

#### 5. 删除字典类型
```
DELETE /api/dicType/delete/{id}
```

#### 6. 批量删除字典类型
```
DELETE /api/dicType/batchDelete
Content-Type: application/json

[1, 2, 3]
```

### 字典值管理接口

#### 1. 获取所有字典值
```
GET /api/dicValue/all
```

#### 2. 分页查询字典值
```
GET /api/dicValue/list?current=1&size=10&typeCode=&typeValue=
```
**参数说明：**
- `current`: 当前页码（默认1）
- `size`: 每页记录数（默认10）
- `typeCode`: 字典类型代码（可选，精确查询）
- `typeValue`: 字典值（可选，模糊查询）

#### 3. 根据类型代码获取字典值列表
```
GET /api/dicValue/listByTypeCode/{typeCode}
```
**示例：**
```
GET /api/dicValue/listByTypeCode/appellation
```

#### 4. 新增字典值
```
POST /api/dicValue/add
Content-Type: application/json

{
  "typeCode": "appellation",
  "typeValue": "先生",
  "order": 1,
  "remark": ""
}
```

#### 5. 修改字典值
```
PUT /api/dicValue/edit
Content-Type: application/json

{
  "id": 1,
  "typeCode": "appellation",
  "typeValue": "先生",
  "order": 1,
  "remark": ""
}
```

#### 6. 删除字典值
```
DELETE /api/dicValue/delete/{id}
```

#### 7. 批量删除字典值
```
DELETE /api/dicValue/batchDelete
Content-Type: application/json

[1, 2, 3]
```

### 业务接口（保留兼容）

这些接口用于其他模块获取特定的字典值列表：

```
GET /api/dicValue/appellation/all      # 获取称呼列表
GET /api/dicValue/status/all           # 获取线索状态列表
GET /api/dicValue/intentionProduct/all # 获取意向产品列表
GET /api/dicValue/needsLoan/all        # 获取需要贷款列表
GET /api/dicValue/intentionStatus/all  # 获取意向状态列表
GET /api/dicValue/source/all           # 获取线索来源列表
GET /api/dicValue/noteWay/all          # 获取跟踪方式列表
```

## 前端页面

### 1. 字典类型管理页面
**路由：** `/dashboard/dictionary/type`
**文件：** `src/view/DictionaryTypeView.vue`

**功能：**
- 搜索和过滤字典类型
- 新增、编辑、删除字典类型
- 批量删除功能
- 分页显示

### 2. 字典值管理页面
**路由：** `/dashboard/dictionary/value`
**文件：** `src/view/DictionaryValueView.vue`

**功能：**
- 按字典类型和字典值搜索
- 新增、编辑、删除字典值
- 批量删除功能
- 分页显示
- 自定义排序

## 数据库表结构

### t_dic_type 表（字典类型表）
```sql
CREATE TABLE t_dic_type (
  id INT PRIMARY KEY AUTO_INCREMENT,
  type_code VARCHAR(50) NOT NULL UNIQUE,
  type_name VARCHAR(100) NOT NULL,
  remark VARCHAR(255)
);
```

### t_dic_value 表（字典值表）
```sql
CREATE TABLE t_dic_value (
  id INT PRIMARY KEY AUTO_INCREMENT,
  type_code VARCHAR(50) NOT NULL,
  type_value VARCHAR(100) NOT NULL,
  `order` INT DEFAULT 0,
  remark VARCHAR(255),
  FOREIGN KEY (type_code) REFERENCES t_dic_type(type_code)
);
```

## 初始化步骤

### 1. 执行数据库脚本
在数据库中执行以下SQL脚本：
- `src/main/resources/sql/dictionary_init_data.sql` - 初始化字典数据

### 2. 配置权限（可选）
如果需要为特定角色分配字典管理权限，执行：
- `src/main/resources/sql/dictionary_permissions.sql` - 配置权限

### 3. 重启应用
重启Spring Boot应用使配置生效

## 使用示例

### 前端调用示例

#### 获取字典值列表
```javascript
import { doGet } from "../http/httpRequest.js";

// 获取称呼列表
doGet('/api/dicValue/appellation/all').then(res => {
  if (res.data.code === 200) {
    const appellationList = res.data.data;
    console.log(appellationList);
  }
});

// 或者通过类型代码获取
doGet('/api/dicValue/listByTypeCode/appellation').then(res => {
  if (res.data.code === 200) {
    const appellationList = res.data.data;
    console.log(appellationList);
  }
});
```

#### 在表单中使用字典值
```vue
<template>
  <el-select v-model="form.appellation" placeholder="请选择称呼">
    <el-option
        v-for="item in appellationList"
        :key="item.id"
        :label="item.typeValue"
        :value="item.typeValue"
    />
  </el-select>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { doGet } from "../http/httpRequest.js";

const appellationList = ref([]);

onMounted(() => {
  doGet('/api/dicValue/appellation/all').then(res => {
    if (res.data.code === 200) {
      appellationList.value = res.data.data;
    }
  });
});
</script>
```

## 常见问题

### Q1: 如何添加新的字典类型？
A: 在字典类型管理页面点击"新增类型"按钮，填写类型代码和类型名称，然后提交即可。

### Q2: 字典值的排序有什么作用？
A: 排序字段用于控制字典值在下拉列表中的显示顺序，数值越小越靠前。

### Q3: 删除字典类型会影响字典值吗？
A: 删除字典类型时，建议先删除该类型下的所有字典值，否则可能导致数据不一致。

### Q4: 如何在其他模块中使用字典值？
A: 通过调用 `/api/dicValue/listByTypeCode/{typeCode}` 接口获取特定类型的字典值列表。

## 扩展建议

1. **缓存优化**：可以将字典数据缓存到Redis中，提高查询性能
2. **导入导出**：支持字典数据的Excel导入导出功能
3. **版本控制**：记录字典数据的修改历史
4. **国际化**：支持多语言字典值
5. **权限控制**：细粒度的字典管理权限控制

## 技术栈

- **后端**：Spring Boot + MyBatis Plus
- **前端**：Vue 3 + Element Plus
- **数据库**：MySQL
- **API文档**：Swagger/Knife4j

## 相关文件

```
后端文件：
- src/main/java/com/weiwei/weidlykserver/controller/dic/DicTypeController.java
- src/main/java/com/weiwei/weidlykserver/controller/dic/DicValueController.java
- src/main/java/com/weiwei/weidlykserver/service/DicTypeService.java
- src/main/java/com/weiwei/weidlykserver/service/DicValueService.java
- src/main/java/com/weiwei/weidlykserver/entity/DicType.java
- src/main/java/com/weiwei/weidlykserver/entity/DicValue.java

前端文件：
- src/view/DictionaryTypeView.vue
- src/view/DictionaryValueView.vue
- src/router/router.js
```

## 许可证

MIT License
