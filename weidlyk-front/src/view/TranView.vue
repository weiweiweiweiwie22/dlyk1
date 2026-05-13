<script setup>
import { nextTick, onMounted, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessageBox } from 'element-plus';
import { doGet, doPost, doPut } from "../http/httpRequest.js";
import { messageTip } from "../util/util.js";

const router = useRouter();

// --- 1. 响应式状态定义 ---
const createInitialSearchState = () => ({
  tranNo: '',
  stage: null,
  customerName: '',
});
const searchForm = reactive(createInitialSearchState());

const data = reactive({
  tranList: [],
  total: 0,
});

const pagination = reactive({
  current: 1,
  size: 10
});

const tableRef = ref(null);

// --- 2. 推进阶段弹窗状态 ---
const stageDialogVisible = ref(false);
const stageForm = reactive({
  id: null,
  stage: null
});

// --- 3. 生命周期 ---
onMounted(() => {
  getTranList();
});

// --- 4. 数据交互逻辑 ---
const getTranList = () => {
  const params = {
    current: pagination.current,
    size: pagination.size,
    ...searchForm
  };
  // 调用后端接口：/api/tran/list
  doGet('/api/tran/list', params).then(res => {
    if (res.data.code === 200) {
      data.tranList = res.data.data.records;
      data.total = res.data.data.total;
    }
  });
};

// 搜索与重置
const onSearch = () => {
  pagination.current = 1;
  getTranList();
};

const onReset = () => {
  Object.assign(searchForm, createInitialSearchState());
  onSearch();
};

const handlePageChange = (val) => {
  pagination.current = val;
  getTranList();
};

// --- 5. 业务操作逻辑 ---

// 跳转详情
const view = (id) => {
  router.push(`/dashboard/tran/${id}`);
};

// 推进阶段（打开弹窗）
const handleStage = (row) => {
  stageForm.id = row.id;
  stageForm.stage = row.stage;
  stageDialogVisible.value = true;
};

// 提交阶段更新
const submitStage = async () => {
  // 对应你 Controller 里的 @PutMapping("/stage")
  const res = await doPut('/api/tran/stage', stageForm);
  if (res.data.code === 200) {
    messageTip("阶段推进成功", "success");
    stageDialogVisible.value = false;
    getTranList();
  }
};

// 删除交易
const del = (id) => {
  ElMessageBox.confirm('确定要删除这笔交易记录吗？', '警告', { type: 'warning' }).then(async () => {
    const res = await doPost(`/api/tran/delete/${id}`);
    if (res.data.code === 200) {
      messageTip("删除成功", "success");
      getTranList();
    }
  }).catch(() => {});
};

// 阶段格式化（将 ID 转为文字显示）
const formatStage = (stageId) => {
  const map = { 10: '跟进中', 12: '已成交', 13: '已失单' };
  return map[stageId] || '未知阶段';
};
</script>

<template>
  <div class="page-container">
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" label-width="80px">
        <el-row :gutter="20">
          <el-col :xs="24" :sm="12" :md="6">
            <el-form-item label="流水号">
              <el-input v-model="searchForm.tranNo" placeholder="请输入流水号" clearable />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="6">
            <el-form-item label="交易阶段">
              <el-select v-model="searchForm.stage" placeholder="请选择阶段" clearable style="width: 100%">
                <el-option label="跟进中" :value="10" />
                <el-option label="已成交" :value="12" />
                <el-option label="已失单" :value="13" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="6">
            <el-form-item label="客户姓名">
              <el-input v-model="searchForm.customerName" placeholder="模糊搜索客户" clearable />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="6" style="text-align: right;">
            <el-button type="primary" @click="onSearch">筛选交易</el-button>
            <el-button @click="onReset">重置</el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card" style="margin-top: 15px;">
      <el-table
          ref="tableRef"
          :data="data.tranList"
          border
          stripe
          style="width: 100%;"
      >
        <el-table-column type="index" label="序号" width="60" align="center" fixed="left" />
        <el-table-column property="tranNo" label="交易流水号" width="160" show-overflow-tooltip />

        <el-table-column property="customerName" label="客户姓名" min-width="120" />

        <el-table-column property="money" label="交易金额(元)" width="130" align="right">
          <template #default="scope">
            <span style="color: #f56c6c; font-weight: bold;">￥{{ scope.row.money }}</span>
          </template>
        </el-table-column>

        <el-table-column property="stage" label="交易阶段" width="110" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.stage === 12 ? 'success' : (scope.row.stage === 13 ? 'danger' : 'warning')">
              {{ formatStage(scope.row.stage) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column property="expectedLocalDateTime" label="预计成交日期" width="170" />
        <el-table-column property="createTime" label="创建时间" width="170" />

        <el-table-column label="操作" width="220" align="center" fixed="right">
          <template #default="scope">
            <div class="op-group">
              <el-button text type="primary" size="small" @click="view(scope.row.id)">详情</el-button>
              <el-button text type="success" size="small" @click="handleStage(scope.row)">推进</el-button>
              <el-button text type="danger" size="small" @click="del(scope.row.id)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
          style="margin-top: 20px; justify-content: center;"
          background
          layout="total, prev, pager, next"
          :total="data.total"
          @current-change="handlePageChange"
      />
    </el-card>

    <el-dialog v-model="stageDialogVisible" title="推进交易阶段" width="400px">
      <el-form :model="stageForm" label-width="80px">
        <el-form-item label="当前阶段">
          <el-select v-model="stageForm.stage" style="width: 100%">
            <el-option label="跟进中" :value="10" />
            <el-option label="已成交" :value="12" />
            <el-option label="已失单" :value="13" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="stageDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitStage">确认推进</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.page-container {
  padding: 15px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);
}
.search-card, .table-card {
  border: none;
}
/* 核心：确保按钮在同一行 */
.op-group {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 5px;
  white-space: nowrap;
}
</style>