# 字典管理模块使用示例

## 目录
1. [后端使用示例](#后端使用示例)
2. [前端使用示例](#前端使用示例)
3. [常见场景](#常见场景)

## 后端使用示例

### 1. 在Service中使用DictionaryUtils

```java
import com.weiwei.weidlykserver.util.DictionaryUtils;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;

@Service
public class ClueService {
    
    @Resource
    private DictionaryUtils dictionaryUtils;
    
    /**
     * 获取线索状态列表
     */
    public List<DicValue> getClueStatusOptions() {
        return dictionaryUtils.getClueStateList();
    }
    
    /**
     * 验证线索状态是否有效
     */
    public boolean isValidClueStatus(String status) {
        List<DicValue> statusList = dictionaryUtils.getClueStateList();
        return statusList.stream()
            .anyMatch(dv -> dv.getTypeValue().equals(status));
    }
}
```

### 2. 在Controller中使用

```java
import com.weiwei.weidlykserver.util.DictionaryUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.annotation.Resource;

@RestController
@RequestMapping("/api/clue")
public class ClueController {
    
    @Resource
    private DictionaryUtils dictionaryUtils;
    
    @GetMapping("/options")
    public Result<Map<String, List<DicValue>>> getClueOptions() {
        Map<String, List<DicValue>> options = new HashMap<>();
        options.put("status", dictionaryUtils.getClueStateList());
        options.put("source", dictionaryUtils.getSourceList());
        options.put("intentionProduct", dictionaryUtils.getIntentionProductList());
        return Result.ok(options);
    }
}
```

### 3. 缓存管理

```java
import com.weiwei.weidlykserver.util.DictionaryUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.annotation.Resource;

@RestController
@RequestMapping("/api/dictionary")
public class DictionaryController {
    
    @Resource
    private DictionaryUtils dictionaryUtils;
    
    /**
     * 刷新字典缓存
     */
    @PostMapping("/cache/refresh")
    public Result<Void> refreshCache(String typeCode) {
        if (typeCode != null && !typeCode.isEmpty()) {
            dictionaryUtils.refreshCache(typeCode);
        } else {
            dictionaryUtils.clearAllCache();
        }
        return Result.ok();
    }
    
    /**
     * 获取缓存统计信息
     */
    @GetMapping("/cache/stats")
    public Result<Map<String, Object>> getCacheStats() {
        return Result.ok(dictionaryUtils.getCacheStats());
    }
}
```

## 前端使用示例

### 1. 在Vue组件中使用字典

```vue
<script setup>
import { onMounted, ref } from 'vue';
import { getDicValuesByTypeCode, getAppellationList } from '../util/dictionaryUtils.js';

const appellationList = ref([]);
const statusList = ref([]);

onMounted(async () => {
  // 方式1：使用便捷方法
  appellationList.value = await getAppellationList();
  
  // 方式2：使用通用方法
  statusList.value = await getDicValuesByTypeCode('clueState');
});
</script>

<template>
  <div>
    <el-select v-model="form.appellation" placeholder="请选择称呼">
      <el-option
          v-for="item in appellationList"
          :key="item.id"
          :label="item.typeValue"
          :value="item.typeValue"
      />
    </el-select>
  </div>
</template>
```

### 2. 预加载字典数据

在 `main.js` 中预加载常用字典：

```javascript
import { createApp } from 'vue'
import App from './App.vue'
import { preloadCommonDictionaries } from './util/dictionaryUtils.js'

const app = createApp(App)

// 预加载常用字典
preloadCommonDictionaries().then(() => {
  app.mount('#app')
})
```

### 3. 创建字典选项

```vue
<script setup>
import { onMounted, ref } from 'vue';
import { createDicOptions } from '../util/dictionaryUtils.js';

const statusOptions = ref([]);

onMounted(async () => {
  statusOptions.value = await createDicOptions('clueState');
});
</script>

<template>
  <el-select v-model="form.status" placeholder="请选择状态">
    <el-option
        v-for="option in statusOptions"
        :key="option.id"
        :label="option.label"
        :value="option.value"
    />
  </el-select>
</template>
```

### 4. 批量获取多个字典

```vue
<script setup>
import { onMounted, ref } from 'vue';
import { getBatchDicValues } from '../util/dictionaryUtils.js';

const dictionaries = ref({});

onMounted(async () => {
  dictionaries.value = await getBatchDicValues([
    'appellation',
    'clueState',
    'source'
  ]);
});
</script>

<template>
  <div>
    <el-select v-model="form.appellation" placeholder="请选择称呼">
      <el-option
          v-for="item in dictionaries.appellation"
          :key="item.id"
          :label="item.typeValue"
          :value="item.typeValue"
      />
    </el-select>
    
    <el-select v-model="form.status" placeholder="请选择状态">
      <el-option
          v-for="item in dictionaries.clueState"
          :key="item.id"
          :label="item.typeValue"
          :value="item.typeValue"
      />
    </el-select>
  </div>
</template>
```

## 常见场景

### 场景1：线索管理页面

**需求**：在线索编辑页面中显示称呼、状态、来源等下拉列表

```vue
<script setup>
import { onMounted, reactive, ref } from 'vue';
import { 
  getAppellationList, 
  getClueStateList, 
  getSourceList,
  getIntentionProductList 
} from '../util/dictionaryUtils.js';

const form = reactive({
  appellation: '',
  status: '',
  source: '',
  intentionProduct: ''
});

const options = reactive({
  appellation: [],
  status: [],
  source: [],
  intentionProduct: []
});

onMounted(async () => {
  options.appellation = await getAppellationList();
  options.status = await getClueStateList();
  options.source = await getSourceList();
  options.intentionProduct = await getIntentionProductList();
});
</script>

<template>
  <el-form :model="form">
    <el-form-item label="称呼">
      <el-select v-model="form.appellation" placeholder="请选择称呼">
        <el-option
            v-for="item in options.appellation"
            :key="item.id"
            :label="item.typeValue"
            :value="item.typeValue"
        />
      </el-select>
    </el-form-item>
    
    <el-form-item label="状态">
      <el-select v-model="form.status" placeholder="请选择状态">
        <el-option
            v-for="item in options.status"
            :key="item.id"
            :label="item.typeValue"
            :value="item.typeValue"
        />
      </el-select>
    </el-form-item>
    
    <el-form-item label="来源">
      <el-select v-model="form.source" placeholder="请选择来源">
        <el-option
            v-for="item in options.source"
            :key="item.id"
            :label="item.typeValue"
            :value="item.typeValue"
        />
      </el-select>
    </el-form-item>
    
    <el-form-item label="意向产品">
      <el-select v-model="form.intentionProduct" placeholder="请选择意向产品">
        <el-option
            v-for="item in options.intentionProduct"
            :key="item.id"
            :label="item.typeValue"
            :value="item.typeValue"
        />
      </el-select>
    </el-form-item>
  </el-form>
</template>
```

### 场景2：表格中显示字典值的中文含义

```vue
<script setup>
import { onMounted, ref } from 'vue';
import { getDicValuesByTypeCode } from '../util/dictionaryUtils.js';

const clueList = ref([]);
const statusMap = ref({});

onMounted(async () => {
  // 获取线索列表
  clueList.value = [
    { id: 1, name: '张三', status: 'new' },
    { id: 2, name: '李四', status: 'following' }
  ];
  
  // 获取状态字典
  const statusList = await getDicValuesByTypeCode('clueState');
  statusList.forEach(item => {
    statusMap.value[item.typeValue] = item.typeValue;
  });
});

const getStatusText = (status) => {
  return statusMap.value[status] || status;
};
</script>

<template>
  <el-table :data="clueList">
    <el-table-column property="name" label="姓名" />
    <el-table-column property="status" label="状态">
      <template #default="scope">
        {{ getStatusText(scope.row.status) }}
      </template>
    </el-table-column>
  </el-table>
</template>
```

### 场景3：动态表单生成

```vue
<script setup>
import { onMounted, ref } from 'vue';
import { getDicValuesByTypeCode } from '../util/dictionaryUtils.js';

const formFields = ref([
  { name: 'appellation', label: '称呼', type: 'select', dictType: 'appellation' },
  { name: 'status', label: '状态', type: 'select', dictType: 'clueState' },
  { name: 'source', label: '来源', type: 'select', dictType: 'source' }
]);

const fieldOptions = ref({});

onMounted(async () => {
  for (const field of formFields.value) {
    if (field.type === 'select') {
      fieldOptions.value[field.name] = await getDicValuesByTypeCode(field.dictType);
    }
  }
});
</script>

<template>
  <el-form>
    <template v-for="field in formFields" :key="field.name">
      <el-form-item :label="field.label" v-if="field.type === 'select'">
        <el-select :model-value="form[field.name]" placeholder="请选择">
          <el-option
              v-for="option in fieldOptions[field.name]"
              :key="option.id"
              :label="option.typeValue"
              :value="option.typeValue"
          />
        </el-select>
      </el-form-item>
    </template>
  </el-form>
</template>
```

### 场景4：缓存管理

```vue
<script setup>
import { getCacheStats, clearDicCache } from '../util/dictionaryUtils.js';

const cacheStats = ref({});

const showCacheStats = () => {
  cacheStats.value = getCacheStats();
  console.log('缓存统计:', cacheStats.value);
};

const clearCache = (typeCode) => {
  clearDicCache(typeCode);
  console.log(`已清空 ${typeCode} 的缓存`);
};

const clearAllCache = () => {
  clearDicCache();
  console.log('已清空所有缓存');
};
</script>

<template>
  <div>
    <el-button @click="showCacheStats">查看缓存统计</el-button>
    <el-button @click="clearAllCache">清空所有缓存</el-button>
    
    <div v-if="cacheStats.cacheSize">
      <p>缓存大小: {{ cacheStats.cacheSize }}</p>
      <p>总项数: {{ cacheStats.totalItems }}</p>
      <p>已缓存类型: {{ cacheStats.cachedTypes.join(', ') }}</p>
    </div>
  </div>
</template>
```

## 性能优化建议

### 1. 预加载常用字典
在应用启动时预加载常用字典，避免首次使用时的延迟。

### 2. 缓存策略
- 字典数据通常不会频繁变化，可以长期缓存
- 当字典数据更新时，及时清空相应的缓存

### 3. 批量加载
使用 `getBatchDicValues` 方法批量加载多个字典，减少API请求次数。

### 4. 懒加载
对于不常用的字典，可以在需要时再加载，避免浪费资源。

## 常见问题

**Q: 如何在组件间共享字典数据？**
A: 使用 `getCachedDicValues` 方法获取已缓存的数据，或者使用Pinia/Vuex进行状态管理。

**Q: 字典数据更新后如何同步？**
A: 调用 `clearDicCache` 清空缓存，下次使用时会重新从服务器加载。

**Q: 如何处理字典加载失败？**
A: 在 `dictionaryUtils.js` 中已添加错误处理，返回空数组。可根据需要添加重试逻辑。
