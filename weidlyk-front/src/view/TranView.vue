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
  stageList: [] // 存储从后端动态获取的阶段字典
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
  stage: null,
  money: 0,
  expectedLocalDateTime: ''
});

// --- 3. 生命周期 ---
onMounted(() => {
  getTranList();
  loadStageList();
});

// --- 4. 数据交互逻辑 ---

// 获取阶段字典（使用通用接口，路径已对齐）
const loadStageList = () => {
  doGet('/api/dicValue/listByTypeCode/stage').then(res => {
    if (res.data.code === 200) {
      data.stageList = res.data.data;
    }
  });
};

// 分页获取交易列表
const getTranList = () => {
  const params = {
    current: pagination.current,
    size: pagination.size,
    ...searchForm
  };
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

// 推进阶段：点击时回显当前行的所有关键数据
const handleStage = (row) => {
  stageForm.id = row.id;
  stageForm.stage = row.stage;
  stageForm.money = row.money || 0;
  stageForm.expectedLocalDateTime = row.expectedLocalDateTime || '';
  stageDialogVisible.value = true;
};

// 提交推进表单
const submitStage = async () => {
  // 注意：后台 Controller 需要 Authentication 参数，前端只需把 ID 和变动字段传过去
  const res = await doPut('/api/tran/stage', stageForm);
  if (res.data.code === 200) {
    messageTip("交易信息更新及阶段推进成功", "success");
    stageDialogVisible.value = false;
    getTranList();
  } else {
    messageTip(res.data.msg || "操作失败", "error");
  }
};

// 删除交易
const del = (id) => {
  ElMessageBox.confirm('确定要删除这笔交易记录吗？删除后不可恢复！', '警告', { type: 'warning' }).then(async () => {
    const res = await doPost(`/api/tran/delete/${id}`);
    if (res.data.code === 200) {
      messageTip("删除成功", "success");
      getTranList();
    }
  }).catch(() => {});
};

// 动态格式化阶段显示
const formatStage = (stageId) => {
  if (!stageId) return '未知阶段';
  const stageObj = data.stageList.find(item => item.id === stageId);
  return stageObj ? stageObj.typeValue : '未知阶段';
};

// 根据阶段获取Tag样式
const getStageTagType = (stageName) => {
  if (stageName.includes('成交')) return 'success';
  if (stageName.includes('失单') || stageName.includes('失败')) return 'danger';
  return 'warning';
};
</script>

<template>
  <div class="page-container">
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="流水号">
              <el-input v-model="searchForm.tranNo" placeholder="流水号" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="交易阶段">
              <el-select v-model="searchForm.stage" placeholder="选择阶段" clearable style="width: 100%">
                <el-option
                    v-for="item in data.stageList"
                    :key="item.id"
                    :label="item.typeValue"
                    :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="客户姓名">
              <el-input v-model="searchForm.customerName" placeholder="模糊搜索" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="6" style="text-align: right;">
            <el-button type="primary" @click="onSearch">筛选</el-button>
            <el-button @click="onReset">重置</el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card" style="margin-top: 15px;">
      <el-table :data="data.tranList" border stripe style="width: 100%;">
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
            <el-tag :type="getStageTagType(formatStage(scope.row.stage))">
              {{ formatStage(scope.row.stage) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column property="expectedLocalDateTime" label="预计成交日期" width="170" />
        <el-table-column property="createTime" label="创建时间" width="170" />

        <el-table-column label="操作" width="240" align="center" fixed="right">
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

    <el-dialog v-model="stageDialogVisible" title="推进交易阶段及更新报价" width="450px" center draggable>
      <el-form :model="stageForm" label-width="110px">
        <el-form-item label="交易阶段">
          <el-select v-model="stageForm.stage" style="width: 100%">
            <el-option
                v-for="item in data.stageList"
                :key="item.id"
                :label="item.typeValue"
                :value="item.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="交易金额(元)">
          <el-input-number v-model="stageForm.money" :min="0" :precision="2" :step="1000" style="width: 100%" />
        </el-form-item>

        <el-form-item label="预计成交日期">
          <el-date-picker
              v-model="stageForm.expectedLocalDateTime"
              type="datetime"
              style="width: 100%"
              value-format="YYYY-MM-DD HH:mm:ss"
              placeholder="选择预计成交时间"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="stageDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitStage">确认推进并更新</el-button>
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

/* 【核心修复】：操作按钮组样式，强制不换行并居中对齐 */
.op-group {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 5px;
  white-space: nowrap;
}
</style>