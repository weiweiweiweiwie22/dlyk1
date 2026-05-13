<script setup>
import { nextTick, onMounted, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessageBox } from 'element-plus';
import { doGet, doPost, doPut, doDelete } from "../http/httpRequest.js";
import { messageTip } from "../util/util.js";

const router = useRouter();

// --- 响应式状态定义 ---

const createInitialSearchState = () => ({
  name: '',
  state: undefined
});
const searchForm = reactive(createInitialSearchState());

const createInitialAddFormState = () => ({
  id: null,
  name: '',
  guidePriceS: undefined,
  guidePriceE: undefined,
  quotation: undefined,
  state: 0
});

const data = reactive({
  productList: [],
  total: 0,
  dialogVisible: false,
  dialogTitle: '新增产品',
  addProductForm: createInitialAddFormState(),
  addProductRules: {
    name: [{ required: true, message: '请输入产品名称', trigger: 'blur' }],
    guidePriceS: [{ required: true, message: '请输入官方指导起始价', trigger: 'change' }],
    guidePriceE: [{ required: true, message: '请输入官方指导最高价', trigger: 'change' }],
    quotation: [{ required: true, message: '请输入经销商报价', trigger: 'change' }],
    state: [{ required: true, message: '请选择状态', trigger: 'change' }]
  }
});

const pagination = reactive({
  current: 1,
  size: 10
});

const tableRef = ref(null);
const addProductFormRef = ref(null);
const selectionMap = ref(new Map());

onMounted(() => {
  getProductList();
});

const getProductList = () => {
  const params = {
    current: pagination.current,
    size: pagination.size,
    ...searchForm
  };

  doGet('/api/product/list', params).then(res => {
    if (res.data.code === 200) {
      data.productList = res.data.data.records;
      data.total = res.data.data.total;
      nextTick(() => {
        if (tableRef.value) {
          data.productList.forEach(product => {
            if (selectionMap.value.has(product.id)) {
              tableRef.value.toggleRowSelection(product, true);
            }
          });
        }
      });
    }
  });
}

const onSearch = () => {
  pagination.current = 1;
  getProductList();
}

const onReset = () => {
  Object.assign(searchForm, createInitialSearchState());
  onSearch();
}

const handlePageChange = (newPage) => {
  pagination.current = newPage;
  getProductList();
};

const handleSelect = (selection, row) => {
  const isSelected = selection.some(item => item.id === row.id);
  if (isSelected) {
    selectionMap.value.set(row.id, row);
  } else {
    selectionMap.value.delete(row.id);
  }
};
const handleSelectAll = (selection) => {
  if (selection.length > 0) {
    data.productList.forEach(product => selectionMap.value.set(product.id, product));
  } else {
    data.productList.forEach(product => selectionMap.value.delete(product.id));
  }
};

const add = () => {
  if(addProductFormRef.value) {
    addProductFormRef.value.resetFields();
  }
  Object.assign(data.addProductForm, createInitialAddFormState());
  data.dialogTitle = '新增产品';
  data.dialogVisible = true;
};

const edit = (row) => {
  if(addProductFormRef.value) {
    addProductFormRef.value.resetFields();
  }
  // 将 row 的数据赋值给表单，特别是 id 字段必须有，这样才能触发后端的编辑操作
  Object.assign(data.addProductForm, { 
    id: row.id,
    name: row.name,
    guidePriceS: row.guidePriceS,
    guidePriceE: row.guidePriceE,
    quotation: row.quotation,
    state: row.state
  });
  data.dialogTitle = '修改产品';
  data.dialogVisible = true;
};

const submit = async () => {
  try {
    await addProductFormRef.value.validate();
  } catch (error) {
    messageTip("请检查输入项！", "warning");
    return;
  }
  
  if (data.addProductForm.id) {
    doPut('/api/product/edit', data.addProductForm).then(res => {
      if (res.data.code === 200) {
        messageTip("修改成功", "success");
        data.dialogVisible = false;
        getProductList();
      } else {
        messageTip(res.data.message || "修改失败", "error");
      }
    }).catch(error => {
      messageTip("系统异常，请稍后重试", "error");
    });
  } else {
    doPost('/api/product/add', data.addProductForm).then(res => {
      if (res.data.code === 200) {
        messageTip("新增成功", "success");
        data.dialogVisible = false;
        getProductList();
      } else {
        messageTip(res.data.message || "新增失败", "error");
      }
    }).catch(error => {
      messageTip("系统异常，请稍后重试", "error");
    });
  }
};

const delSelected = () => {
  const selectedCount = selectionMap.value.size;
  if (selectedCount === 0) {
    messageTip("请选择要删除的产品", "warning");
    return;
  }
  ElMessageBox.confirm(`确认删除选中的 ${selectedCount} 个产品吗?`, '提示', { type: 'warning' })
      .then(() => {
        const ids = Array.from(selectionMap.value.keys());
        doDelete('/api/product/batchDelete', { data: ids }).then(res => {
          if (res.data.code === 200) {
            messageTip("删除成功", "success");
            selectionMap.value.clear();
            if (data.productList.length === ids.length && pagination.current > 1) {
              pagination.current--;
            }
            getProductList();
          } else {
            messageTip(res.data.message || "删除失败", "error");
          }
        });
      }).catch(() => { /* 取消操作 */ });
};

const del = (id) => {
  ElMessageBox.confirm('确认删除该产品吗?', '提示', { type: 'warning' })
      .then(() => {
        doDelete('/api/product/delete/' + id).then(res => {
          if (res.data.code === 200) {
            messageTip("删除成功", "success");
            getProductList();
          } else {
            messageTip(res.data.message || "删除失败", "error");
          }
        });
      }).catch(() => { /* 取消操作 */ });
};

</script>

<template>
  <div class="page-container">
    <el-card shadow="never">
      <el-form :model="searchForm" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="6" :xs="24" :sm="12" :md="6">
            <el-form-item label="产品名称">
              <el-input v-model="searchForm.name" placeholder="请输入产品名称" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="6" :xs="24" :sm="12" :md="6">
            <el-form-item label="状态">
              <el-select v-model="searchForm.state" placeholder="请选择状态" clearable style="width: 100%;">
                <el-option label="在售" :value="0" />
                <el-option label="售罄" :value="1" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6" :xs="24" :sm="12" :md="6">
            <el-form-item>
              <el-button type="primary" @click="onSearch">搜索</el-button>
              <el-button @click="onReset">重置</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <el-card shadow="never" style="margin-top: 20px;">
      <div class="toolbar">
        <el-button type="primary" @click="add">录入产品</el-button>
        <el-button type="danger" @click="delSelected" :disabled="selectionMap.size === 0">批量删除</el-button>
        <div class="selection-info" v-if="selectionMap.size > 0">
          <el-tag type="info" size="large">已选择 {{ selectionMap.size }} 项</el-tag>
        </div>
      </div>
      <el-table
          ref="tableRef"
          :data="data.productList"
          :row-key="product => product.id"
          @select="handleSelect"
          @select-all="handleSelectAll"
          border stripe style="margin-top: 20px;"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column type="index" label="序号" width="80" align="center" />
        <el-table-column property="name" label="产品名称" show-overflow-tooltip />
        <el-table-column property="guidePriceS" label="指导起始价(元)" show-overflow-tooltip />
        <el-table-column property="guidePriceE" label="指导最高价(元)" show-overflow-tooltip />
        <el-table-column property="quotation" label="经销商报价(元)" show-overflow-tooltip />
        <el-table-column property="state" label="状态" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.state === 0 ? 'success' : 'danger'">
              {{ scope.row.state === 0 ? '在售' : '售罄' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column property="createTime" label="创建时间" width="180" show-overflow-tooltip />
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template #default="scope">
            <el-button text type="primary" @click="edit(scope.row)">编辑</el-button>
            <el-button text type="danger" @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
          style="margin-top: 20px; justify-content: center;"
          background
          layout="total, prev, pager, next, jumper"
          :total="data.total"
          :page-size="pagination.size"
          :current-page="pagination.current"
          @current-change="handlePageChange"
      />
    </el-card>

    <el-dialog v-model="data.dialogVisible" :title="data.dialogTitle" width="600px" center draggable>
      <el-form ref="addProductFormRef" :model="data.addProductForm" :rules="data.addProductRules" label-width="120px">
        <el-form-item label="产品名称" prop="name">
          <el-input v-model="data.addProductForm.name" placeholder="请输入产品名称" />
        </el-form-item>
        <el-form-item label="指导起始价" prop="guidePriceS">
          <el-input-number v-model="data.addProductForm.guidePriceS" :min="0" :precision="2" :step="1000" style="width: 100%" />
        </el-form-item>
        <el-form-item label="指导最高价" prop="guidePriceE">
          <el-input-number v-model="data.addProductForm.guidePriceE" :min="0" :precision="2" :step="1000" style="width: 100%" />
        </el-form-item>
        <el-form-item label="经销商报价" prop="quotation">
          <el-input-number v-model="data.addProductForm.quotation" :min="0" :precision="2" :step="1000" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态" prop="state">
          <el-radio-group v-model="data.addProductForm.state">
            <el-radio :label="0">在售</el-radio>
            <el-radio :label="1">售罄</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="data.dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.page-container {
  padding: 20px;
}
.toolbar {
  display: flex;
  align-items: center;
  gap: 16px;
}
</style>