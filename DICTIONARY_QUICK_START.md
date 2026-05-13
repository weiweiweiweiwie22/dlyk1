# 字典管理模块 - 快速开始指南

## 5分钟快速上手

### 第一步：执行数据库脚本

在MySQL数据库中执行以下脚本：

```sql
-- 1. 初始化字典数据
source src/main/resources/sql/dictionary_init_data.sql;

-- 2. 可选：配置权限（如果需要权限控制）
source src/main/resources/sql/dictionary_permissions.sql;
```

### 第二步：重启应用

```bash
# 后端
mvn clean install
mvn spring-boot:run

# 或者直接运行JAR
java -jar weidlyk-server.jar
```

### 第三步：访问管理页面

打开浏览器访问：
- 字典类型管理：`http://localhost:8080/dashboard/dictionary/type`
- 字典值管理：`http://localhost:8080/dashboard/dictionary/value`

## 核心功能速览

### 字典类型管理
- 📝 新增字典类型
- ✏️ 编辑字典类型
- 🗑️ 删除字典类型
- 🔍 搜索字典类型
- 📄 分页显示

### 字典值管理
- 📝 新增字典值
- ✏️ 编辑字典值
- 🗑️ 删除字典值
- 🔍 按类型和值搜索
- 📊 自定义排序
- 📄 分页显示

## 常用API调用

### 获取字典值列表

```bash
# 获取称呼列表
curl http://localhost:8080/api/dicValue/appellation/all

# 获取线索状态列表
curl http://localhost:8080/api/dicValue/status/all

# 按类型代码获取字典值
curl http://localhost:8080/api/dicValue/listByTypeCode/appellation
```

### 在前端中使用

```javascript
import { getAppellationList } from './util/dictionaryUtils.js'

// 获取称呼列表
const appellationList = await getAppellationList()
console.log(appellationList)
```

## 预置字典类型

系统已预置以下字典类型：

| 类型代码 | 类型名称 | 说明 |
|---------|---------|------|
| appellation | 称呼 | 客户称呼类型 |
| clueState | 线索状态 | 线索的各种状态 |
| intentionProduct | 意向产品 | 客户意向的产品类型 |
| needLoan | 是否需要贷款 | 客户是否需要贷款 |
| intentionState | 意向状态 | 客户意向的状态 |
| source | 线索来源 | 线索的来源渠道 |
| noteWay | 跟踪方式 | 跟踪客户的方式 |

## 在其他模块中使用字典

### 后端使用示例

```java
import com.weiwei.weidlykserver.util.DictionaryUtils;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;

@Service
public class MyService {
    @Resource
    private DictionaryUtils dictionaryUtils;
    
    public void myMethod() {
        // 获取称呼列表
        var appellationList = dictionaryUtils.getAppellationList();
        
        // 获取线索状态列表
        var statusList = dictionaryUtils.getClueStateList();
    }
}
```

### 前端使用示例

```vue
<script setup>
import { onMounted, ref } from 'vue'
import { getAppellationList } from '../util/dictionaryUtils.js'

const appellationList = ref([])

onMounted(async () => {
  appellationList.value = await getAppellationList()
})
</script>

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
```

## 常见操作

### 添加新的字典类型

1. 打开字典类型管理页面
2. 点击"新增类型"按钮
3. 填写类型代码和类型名称
4. 点击"提交"

### 添加字典值

1. 打开字典值管理页面
2. 点击"新增字典值"按钮
3. 选择字典类型
4. 填写字典值和排序号
5. 点击"提交"

### 修改字典值

1. 在字典值管理页面找到要修改的项
2. 点击"编辑"按钮
3. 修改相关信息
4. 点击"提交"

### 删除字典值

1. 在字典值管理页面找到要删除的项
2. 点击"删除"按钮
3. 确认删除

### 批量删除

1. 在表格中勾选要删除的项
2. 点击"批量删除"按钮
3. 确认删除

## 性能优化

### 前端缓存

字典数据会自动缓存在前端，避免重复请求：

```javascript
import { preloadCommonDictionaries } from './util/dictionaryUtils.js'

// 在应用启动时预加载常用字典
preloadCommonDictionaries()
```

### 后端缓存

后端使用ConcurrentHashMap缓存字典数据，提高查询性能。

## 故障排除

### 问题1：页面无法访问

**解决方案**：
1. 确保应用已启动
2. 检查路由配置是否正确
3. 查看浏览器控制台是否有错误

### 问题2：字典数据为空

**解决方案**：
1. 确保已执行初始化脚本
2. 检查数据库连接是否正常
3. 查看后端日志是否有错误

### 问题3：权限不足

**解决方案**：
1. 确保用户角色已分配字典管理权限
2. 执行权限配置脚本
3. 重新登录

## 下一步

- 📖 阅读 [完整使用指南](./DICTIONARY_MODULE_README.md)
- 💡 查看 [使用示例](./DICTIONARY_USAGE_EXAMPLES.md)
- 🔧 了解 [模块总结](./DICTIONARY_MODULE_SUMMARY.md)

## 获取帮助

如有问题，请：
1. 查看文档中的常见问题部分
2. 检查后端日志
3. 查看浏览器控制台错误
4. 联系开发团队

---

**提示**：首次使用建议先在开发环境测试，确保功能正常后再部署到生产环境。
