<script setup>
import { nextTick, onMounted, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessageBox } from 'element-plus';
import { doGet, doPost, doDelete } from "../http/httpRequest.js";
import { messageTip } from "../util/util.js";

const router = useRouter();

// --- 响应式状态定义 ---

const createInitialSearchState = () => ({
  ownerId: '',
  name: '',
  dateRange: [],
  cost: undefined,
  createTime: '',
});
const searchForm = reactive(createInitialSearchState());

// v-v-v 最终修正 #1：统一新增/编辑表单的字段名 v-v-v
const createInitialAddFormState = () => ({
  name: '',
  ownerId: '',
  startTime: '', // 将 startDate 改为 startTime
  endTime: '',   // 将 endDate 改为 endTime
  cost: undefined,
  description: ''
});
// ^-^-^ 最终修正 #1 ^-^-^

const data = reactive({
  activityList: [],
  ownerList: [],
  total: 0,
  dialogVisible: false,
  addActivityForm: createInitialAddFormState(), // 使用函数初始化
  // v-v-v 最终修正 #2：同步修改校验规则的字段名 v-v-v
  addActivityRules: {
    name: [{ required: true, message: '请输入活动名称', trigger: 'blur' }],
    ownerId: [{ required: true, message: '请选择负责人', trigger: 'change' }],
    startTime: [{ required: true, message: '请选择开始时间', trigger: 'blur' }], // 将 startDate 改为 startTime
    endTime: [{ required: true, message: '请选择结束时间', trigger: 'blur' }],   // 将 endDate 改为 endTime
    cost: [
      { required: true, message: '请输入活动预算', trigger: 'blur' },
      { type: 'number', message: '预算必须为数字' }
    ],
  }
  // ^-^-^ 最终修正 #2 ^-^-^
});

const pagination = reactive({
  current: 1,
  size: 10
});

const tableRef = ref(null);
const addActivityFormRef = ref(null);
const selectionMap = ref(new Map());


onMounted(() => {
  getActivityList();
  loadOwner();
});


const getActivityList = () => {
  const params = {
    current: pagination.current,
    size: pagination.size,
    ...searchForm
  };
  if (params.dateRange && params.dateRange.length === 2) {
    params.startTime = params.dateRange[0];
    params.endTime = params.dateRange[1];
  }
  delete params.dateRange;

  doGet('/api/activity/list', params).then(res => {
    if (res.data.code === 200) {
      data.activityList = res.data.data.records;
      data.total = res.data.data.total;
      nextTick(() => {
        if (tableRef.value) {
          data.activityList.forEach(activity => {
            if (selectionMap.value.has(activity.id)) {
              tableRef.value.toggleRowSelection(activity, true);
            }
          });
        }
      });
    }
  });
}

const onSearch = () => {
  pagination.current = 1;
  getActivityList();
}

const onReset = () => {
  Object.assign(searchForm, createInitialSearchState());
  onSearch();
}

const handlePageChange = (newPage) => {
  pagination.current = newPage;
  getActivityList();
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
    data.activityList.forEach(activity => selectionMap.value.set(activity.id, activity));
  } else {
    data.activityList.forEach(activity => selectionMap.value.delete(activity.id));
  }
};

const add = () => {
  if(addActivityFormRef.value) {
    addActivityFormRef.value.resetFields();
  }
  Object.assign(data.addActivityForm, createInitialAddFormState());
  data.dialogVisible = true;
};
const submit = async () => {
  try {
    await addActivityFormRef.value.validate();
    doPost('/api/activity/add', data.addActivityForm).then(res => {
      if (res.data.code === 200) {
        messageTip("新增成功", "success");
        data.dialogVisible = false;
        getActivityList();
      } else {
        messageTip(res.data.message || "新增失败", "error");
      }
    });
  } catch (error) {
    messageTip("请检查输入项！", "warning");
  }
};
const delSelected = () => {
  const selectedCount = selectionMap.value.size;
  if (selectedCount === 0) {
    messageTip("请选择要删除的活动", "warning");
    return;
  }
  ElMessageBox.confirm(`确认删除选中的 ${selectedCount} 个活动吗?`, '提示', { type: 'warning' })
      .then(() => {
        const ids = Array.from(selectionMap.value.keys());
        doPost('/api/activity/delete/batch', ids).then(res => {
          if (res.data.code === 200) {
            messageTip("删除成功", "success");
            selectionMap.value.clear();
            if (data.activityList.length === ids.length && pagination.current > 1) {
              pagination.current--;
            }
            getActivityList();
          } else {
            messageTip(res.data.message || "删除失败", "error");
          }
        });
      }).catch(() => { /* 取消操作 */ });
};
const del = (id) => {
  ElMessageBox.confirm('确认删除该活动吗?', '提示', { type: 'warning' })
      .then(() => {
        doPost('/api/activity/delete/' + id).then(res => {
          if (res.data.code === 200) {
            messageTip("删除成功", "success");
            getActivityList();
          } else {
            messageTip(res.data.message || "删除失败", "error");
          }
        });
      }).catch(() => { /* 取消操作 */ });
};
const view = (id) => {
  router.push("/dashboard/activity/" + id);
};
const edit = (id) => {
  router.push("/dashboard/activity/edit/" + id);
};

const loadOwner = () => {
  doGet('/api/user/all').then(res => {
    if (res.data.code === 200) {
      data.ownerList = res.data.data;
    }
  });
};
</script>

<template>
  <div class="page-container">
    <el-card shadow="never">
      <el-form :model="searchForm" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="6" :xs="24" :sm="12" :md="6">
            <el-form-item label="负责人">
              <el-select v-model="searchForm.ownerId" placeholder="请选择" clearable style="width: 100%;">
                <el-option
                    v-for="owner in data.ownerList"
                    :key="owner.id"
                    :label="owner.name"
                    :value="owner.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6" :xs="24" :sm="12" :md="6">
            <el-form-item label="活动名称">
              <el-input v-model="searchForm.name" placeholder="请输入" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="6" :xs="24" :sm="12" :md="6">
            <el-form-item label="活动预算">
              <el-input v-model.number="searchForm.cost" placeholder="请输入预算金额" clearable>
                <template #append>元</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6" :xs="24" :sm="12" :md="6">
            <el-form-item label="创建时间">
              <el-date-picker v-model="searchForm.createTime" type="datetime" placeholder="选择创建时间" style="width: 100%;" format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12" :xs="24">
            <el-form-item label="活动时间">
              <el-date-picker v-model="searchForm.dateRange" type="datetimerange" start-placeholder="开始" end-placeholder="结束" style="width: 100%;"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row justify="end">
          <el-form-item>
            <el-button type="primary" @click="onSearch">搜索</el-button>
            <el-button @click="onReset">重置</el-button>
          </el-form-item>
        </el-row>
      </el-form>
    </el-card>

    <el-card shadow="never" style="margin-top: 20px;">
      <div class="toolbar">
        <el-button type="primary" @click="add">录入市场活动</el-button>
        <el-button type="danger" @click="delSelected" :disabled="selectionMap.size === 0">批量删除</el-button>
        <div class="selection-info" v-if="selectionMap.size > 0">
          <el-tag type="info" size="large">已选择 {{ selectionMap.size }} 项</el-tag>
        </div>
      </div>
      <el-table
          ref="tableRef"
          :data="data.activityList"
          :row-key="activity => activity.id"
          @select="handleSelect"
          @select-all="handleSelectAll"
          border stripe style="margin-top: 20px;"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column type="index" label="序号" width="80" align="center" />
        <el-table-column property="ownerName" label="负责人" show-overflow-tooltip />
        <el-table-column property="name" label="活动名称" show-overflow-tooltip />
        <el-table-column property="startTime" label="开始时间" width="180" show-overflow-tooltip />
        <el-table-column property="endTime" label="结束时间" width="180" show-overflow-tooltip />
        <el-table-column property="cost" label="活动预算(元)" show-overflow-tooltip />
        <el-table-column property="createTime" label="创建时间" width="180" show-overflow-tooltip />
        <el-table-column label="操作" width="250" align="center" fixed="right">
          <template #default="scope">
            <el-button text type="primary" @click="view(scope.row.id)">详情</el-button>
            <el-button text type="success" @click="edit(scope.row.id)">修改</el-button>
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

    <el-dialog v-model="data.dialogVisible" title="新增市场活动" width="600px" center draggable>
      <el-form ref="addActivityFormRef" :model="data.addActivityForm" :rules="data.addActivityRules" label-width="100px">
        <el-form-item label="活动名称" prop="name">
          <el-input v-model="data.addActivityForm.name" />
        </el-form-item>
        <el-form-item label="负责人" prop="ownerId">
          <el-select v-model="data.addActivityForm.ownerId" placeholder="请选择" style="width: 100%;">
            <el-option
                v-for="owner in data.ownerList"
                :key="owner.id"
                :label="owner.name"
                :value="owner.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker v-model="data.addActivityForm.startTime" type="datetime" placeholder="选择开始时间" style="width: 100%;" format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss"/>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker v-model="data.addActivityForm.endTime" type="datetime" placeholder="选择结束时间" style="width: 100%;" format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss"/>
        </el-form-item>
        <el-form-item label="活动预算" prop="cost">
          <el-input v-model.number="data.addActivityForm.cost">
            <template #append>元</template>
          </el-input>
        </el-form-item>
        <el-form-item label="活动描述" prop="description">
          <el-input v-model="data.addActivityForm.description" type="textarea" />
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