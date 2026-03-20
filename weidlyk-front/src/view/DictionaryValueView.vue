<script setup>
import { nextTick, onMounted, reactive, ref } from 'vue';
import { ElMessageBox } from 'element-plus';
import { doGet, doPost, doPut, doDelete } from "../http/httpRequest.js";
import { messageTip } from "../util/util.js";

// --- 响应式状态定义 ---

const createInitialSearchState = () => ({
  typeCode: '',
  typeValue: ''
});
const searchForm = reactive(createInitialSearchState());

const createInitialAddFormState = () => ({
  id: null,
  typeCode: '',
  typeValue: '',
  order: 0,
  remark: ''
});

const data = reactive({
  dicValueList: [],
  dicTypeOptions: [],
  total: 0,
  dialogVisible: false,
  dialogTitle: '新增字典值',
  addDicValueForm: createInitialAddFormState(),
  addDicValueRules: {
    typeCode: [{ required: true, message: '请选择字典类型', trigger: 'change' }],
    typeValue: [
      { required: true, message: '请输入字典值', trigger: 'blur' },
      { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }
    ],
    order: [{ required: true, message: '请输入排序号', trigger: 'blur' }]
  }
});

const pagination = reactive({
  current: 1,
  size: 10
});

const tableRef = ref(null);
const addDicValueFormRef = ref(null);
const selectionMap = ref(new Map());

onMounted(() => {
  getDicTypeOptions();
  getDicValueList();
});

const getDicTypeOptions = () => {
  doGet('/api/dicType/all').then(res => {
    if (res.data.code === 200) {
      data.dicTypeOptions = res.data.data;
    }
  });
}

const getDicValueList = () => {
  const params = {
    current: pagination.current,
    size: pagination.size,
    ...searchForm
  };

  doGet('/api/dicValue/list', params).then(res => {
    if (res.data.code === 200) {
      data.dicValueList = res.data.data.records;
      data.total = res.data.data.total;
      nextTick(() => {
        if (tableRef.value) {
          data.dicValueList.forEach(dicValue => {
            if (selectionMap.value.has(dicValue.id)) {
              tableRef.value.toggleRowSelection(dicValue, true);
            }
          });
        }
      });
    }
  });
}

const onSearch = () => {
  pagination.current = 1;
  getDicValueList();
}

const onReset = () => {
  Object.assign(searchForm, createInitialSearchState());
  onSearch();
}

const handlePageChange = (newPage) => {
  pagination.current = newPage;
  getDicValueList();
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
    data.dicValueList.forEach(dicValue => selectionMap.value.set(dicValue.id, dicValue));
  } else {
    data.dicValueList.forEach(dicValue => selectionMap.value.delete(dicValue.id));
  }
};

const add = () => {
  if(addDicValueFormRef.value) {
    addDicValueFormRef.value.resetFields();
  }
  Object.assign(data.addDicValueForm, createInitialAddFormState());
  data.dialogTitle = '新增字典值';
  data.dialogVisible = true;
};

const edit = (row) => {
  if(addDicValueFormRef.value) {
    addDicValueFormRef.value.resetFields();
  }
  Object.assign(data.addDicValueForm, {
    id: row.id,
    typeCode: row.typeCode,
    typeValue: row.typeValue,
    order: row.order,
    remark: row.remark
  });
  data.dialogTitle = '修改字典值';
  data.dialogVisible = true;
};

const submit = async () => {
  try {
    await addDicValueFormRef.value.validate();
  } catch (error) {
    messageTip("请检查输入项！", "warning");
    return;
  }

  if (data.addDicValueForm.id) {
    doPut('/api/dicValue/edit', data.addDicValueForm).then(res => {
      if (res.data.code === 200) {
        messageTip("修改成功", "success");
        data.dialogVisible = false;
        getDicValueList();
      } else {
        messageTip(res.data.message || "修改失败", "error");
      }
    }).catch(error => {
      messageTip("系统异常，请稍后重试", "error");
    });
  } else {
    doPost('/api/dicValue/add', data.addDicValueForm).then(res => {
      if (res.data.code === 200) {
        messageTip("新增成功", "success");
        data.dialogVisible = false;
        getDicValueList();
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
    messageTip("请选择要删除的字典值", "warning");
    return;
  }
  ElMessageBox.confirm(`确认删除选中的 ${selectedCount} 个字典值吗?`, '提示', { type: 'warning' })
      .then(() => {
        const ids = Array.from(selectionMap.value.keys());
        doDelete('/api/dicValue/batchDelete', { data: ids }).then(res => {
          if (res.data.code === 200) {
            messageTip("删除成功", "success");
            selectionMap.value.clear();
            if (data.dicValueList.length === ids.length && pagination.current > 1) {
              pagination.current--;
            }
            getDicValueList();
          } else {
            messageTip(res.data.message || "删除失败", "error");
          }
        });
      }).catch(() => { /* 取消操作 */ });
};

const del = (id) => {
  ElMessageBox.confirm('确认删除该字典值吗?', '提示', { type: 'warning' })
      .then(() => {
        doDelete('/api/dicValue/delete/' + id).then(res => {
          if (res.data.code === 200) {
            messageTip("删除成功", "success");
            getDicValueList();
          } else {
            messageTip(res.data.message || "删除失败", "error");
          }
        });
      }).catch(() => { /* 取消操作 */ });
};

const getTypeName = (typeCode) => {
  const type = data.dicTypeOptions.find(t => t.typeCode === typeCode);
  return type ? type.typeName : typeCode;
};

</script>

<template>
  <div class="page-container">
    <el-card shadow="never">
      <el-form :model="searchForm" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="6" :xs="24" :sm="12" :md="6">
            <el-form-item label="字典类型">
              <el-select v-model="searchForm.typeCode" placeholder="请选择字典类型" clearable style="width: 100%;">
                <el-option
                    v-for="item in data.dicTypeOptions"
                    :key="item.typeCode"
                    :label="item.typeName"
                    :value="item.typeCode"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6" :xs="24" :sm="12" :md="6">
            <el-form-item label="字典值">
              <el-input v-model="searchForm.typeValue" placeholder="请输入字典值" clearable />
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
        <el-button type="primary" @click="add">新增字典值</el-button>
        <el-button type="danger" @click="delSelected" :disabled="selectionMap.size === 0">批量删除</el-button>
        <div class="selection-info" v-if="selectionMap.size > 0">
          <el-tag type="info" size="large">已选择 {{ selectionMap.size }} 项</el-tag>
        </div>
      </div>
      <el-table
          ref="tableRef"
          :data="data.dicValueList"
          :row-key="dicValue => dicValue.id"
          @select="handleSelect"
          @select-all="handleSelectAll"
          border stripe style="margin-top: 20px;"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column type="index" label="序号" width="80" align="center" />
        <el-table-column property="typeCode" label="字典类型" show-overflow-tooltip>
          <template #default="scope">
            {{ getTypeName(scope.row.typeCode) }}
          </template>
        </el-table-column>
        <el-table-column property="typeValue" label="字典值" show-overflow-tooltip />
        <el-table-column property="order" label="排序" width="80" align="center" />
        <el-table-column property="remark" label="备注" show-overflow-tooltip />
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
      <el-form ref="addDicValueFormRef" :model="data.addDicValueForm" :rules="data.addDicValueRules" label-width="120px">
        <el-form-item label="字典类型" prop="typeCode">
          <el-select v-model="data.addDicValueForm.typeCode" placeholder="请选择字典类型" style="width: 100%;">
            <el-option
                v-for="item in data.dicTypeOptions"
                :key="item.typeCode"
                :label="item.typeName"
                :value="item.typeCode"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="字典值" prop="typeValue">
          <el-input v-model="data.addDicValueForm.typeValue" placeholder="请输入字典值" />
        </el-form-item>
        <el-form-item label="排序" prop="order">
          <el-input-number v-model="data.addDicValueForm.order" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="data.addDicValueForm.remark" type="textarea" rows="3" placeholder="请输入备注" />
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
