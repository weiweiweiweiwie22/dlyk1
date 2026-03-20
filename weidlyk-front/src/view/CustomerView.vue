<script setup>
import { nextTick, onMounted, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessageBox } from 'element-plus';
import { doGet, doPost, doGetFile, doPostFile } from "../http/httpRequest.js";
import { messageTip } from "../util/util.js";

const router = useRouter();

// --- 1. 响应式数据状态 ---
const createInitialSearchState = () => ({
  ownerId: '',
  productId: '',
  fullName: '',
  phone: '',
});
const searchForm = reactive(createInitialSearchState());

const data = reactive({
  customerList: [],
  ownerList: [],
  productList: [], // 用于搜索下拉框和修改弹窗
  total: 0,
});

const pagination = reactive({
  current: 1,
  size: 10
});

const tableRef = ref(null);
const selectionMap = ref(new Map());

// --- 2. 修改功能状态 ---
const editDialogVisible = ref(false);
const editForm = reactive({
  id: null,              // 【关键】：保存时必须带着客户ID
  product: null,
  description: '',
  nextContactTime: ''
});

// --- 3. 生命周期与初始化 ---
onMounted(() => {
  getCustomerList();
  loadOwnerList();
  loadProductList();
});

// --- 4. 数据交互方法 ---

// 获取客户分页列表
const getCustomerList = () => {
  const params = {
    current: pagination.current,
    size: pagination.size,
    ...searchForm
  };
  doGet('/api/customers', params).then(res => {
    if (res.data.code === 200) {
      data.customerList = res.data.data.records;
      data.total = res.data.data.total;
      // 保持复选框勾选状态
      nextTick(() => {
        if (tableRef.value) {
          data.customerList.forEach(c => {
            if (selectionMap.value.has(c.id)) tableRef.value.toggleRowSelection(c, true);
          });
        }
      });
    }
  });
};

const loadOwnerList = () => doGet('/api/user/all').then(res => { if (res.data.code === 200) data.ownerList = res.data.data; });
const loadProductList = () => doGet('/api/product/all').then(res => { if (res.data.code === 200) data.productList = res.data.data; });

// --- 5. 业务操作逻辑 ---

// 跳转详情页
const view = (id) => {
  router.push(`/dashboard/customer/${id}`);
};

// 打开修改弹窗并回显数据
const edit = (id) => {
  doGet(`/api/customer/${id}`).then(res => {
    if (res.data.code === 200) {
      // 此时 res.data.data 里包含了 id, product, description 等所有字段
      Object.assign(editForm, res.data.data);
      editDialogVisible.value = true;
    }
  });
};

// 提交修改
const submitEdit = async () => {
  // 确保后端已改为 @PostMapping("/customer/update") 以适配此处的 doPost
  const res = await doPost('/api/customer/update', editForm);
  if (res.data.code === 200) {
    messageTip("修改成功", "success");
    editDialogVisible.value = false;
    getCustomerList(); // 刷新当前列表
  } else {
    messageTip(res.data.message || "修改失败", "error");
  }
};

// 删除客户
const del = (id) => {
  ElMessageBox.confirm('确定要删除该客户吗？', '警告', { type: 'warning' }).then(async () => {
    const res = await doPost(`/api/customer/delete/${id}`);
    if (res.data.code === 200) {
      messageTip("删除成功", "success");
      getCustomerList();
    }
  }).catch(() => {});
};

// --- 6. 搜索、分页与导出 ---
const onSearch = () => { pagination.current = 1; getCustomerList(); };
const onReset = () => { Object.assign(searchForm, createInitialSearchState()); onSearch(); };
const handlePageChange = (val) => { pagination.current = val; getCustomerList(); };

const handleSelect = (selection, row) => {
  if (selection.some(item => item.id === row.id)) selectionMap.value.set(row.id, row);
  else selectionMap.value.delete(row.id);
};

const processDownload = (res, name) => {
  const blob = new Blob([res.data]);
  const link = document.createElement('a');
  link.href = URL.createObjectURL(blob);
  link.download = name;
  link.click();
  URL.revokeObjectURL(link.href);
};

const exportCustomer = () => {
  ElMessageBox.confirm('确定导出筛选后的全部客户吗？', '提示').then(() => {
    doGetFile('/api/customer/export', { ...searchForm }).then(res => processDownload(res, '客户列表.xlsx'));
  });
};

const exportSelected = () => {
  const ids = Array.from(selectionMap.value.keys());
  if (ids.length === 0) return messageTip("请先勾选数据", "warning");
  doPostFile('/api/customer/export/selected', ids).then(res => processDownload(res, '选中客户.xlsx'));
};
</script>

<template>
  <div class="page-container">
    <el-card shadow="never" class="box-card">
      <el-form :model="searchForm" label-width="80px">
        <el-row :gutter="20">
          <el-col :xs="24" :sm="12" :md="6">
            <el-form-item label="负责人">
              <el-select v-model="searchForm.ownerId" clearable style="width: 100%">
                <el-option v-for="u in data.ownerList" :key="u.id" :label="u.name" :value="u.id"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="6">
            <el-form-item label="意向产品">
              <el-select v-model="searchForm.productId" clearable style="width: 100%">
                <el-option v-for="p in data.productList" :key="p.id" :label="p.name" :value="p.id"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="6">
            <el-form-item label="客户姓名"><el-input v-model="searchForm.fullName" clearable/></el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="6">
            <el-form-item label="手机号"><el-input v-model="searchForm.phone" clearable/></el-form-item>
          </el-col>
        </el-row>
        <el-row justify="end">
          <el-button type="primary" @click="onSearch">筛选</el-button>
          <el-button @click="onReset">重置</el-button>
        </el-row>
      </el-form>
    </el-card>

    <el-card shadow="never" class="box-card" style="margin-top: 15px;">
      <div class="toolbar">
        <el-button type="success" @click="exportCustomer" plain>导出全部(Excel)</el-button>
        <el-button type="warning" @click="exportSelected" :disabled="selectionMap.size === 0" plain>导出选中</el-button>
      </div>

      <el-table
          ref="tableRef"
          :data="data.customerList"
          @select="handleSelect"
          border
          stripe
          style="width: 100%; margin-top: 15px;"
      >
        <el-table-column type="selection" width="50" fixed="left" />
        <el-table-column property="fullName" label="姓名" width="110" />
        <el-table-column property="phone" label="手机" width="130" />
        <el-table-column property="ownerName" label="负责人" width="110" />

        <el-table-column property="productName" label="意向产品" min-width="180" show-overflow-tooltip />

        <el-table-column property="nextContactTime" label="下次联系" width="170" show-overflow-tooltip />
        <el-table-column property="createTime" label="录入时间" width="170" />

        <el-table-column label="操作" width="240" align="center" fixed="right">
          <template #default="scope">
            <div class="op-group">
              <el-button text type="primary" @click="view(scope.row.id)">详情</el-button>
              <el-button text type="success" @click="edit(scope.row.id)">修改</el-button>
              <el-button text type="danger" @click="del(scope.row.id)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
          style="margin-top: 20px; justify-content: center;"
          background
          layout="total, prev, pager, next, jumper"
          :total="data.total"
          @current-change="handlePageChange"
      />
    </el-card>

    <el-dialog v-model="editDialogVisible" title="编辑客户信息" width="500px">
      <el-form :model="editForm" label-width="100px">
        <el-form-item label="意向产品">
          <el-select v-model="editForm.product" style="width: 100%">
            <el-option v-for="p in data.productList" :key="p.id" :label="p.name" :value="p.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="跟进时间">
          <el-date-picker v-model="editForm.nextContactTime" type="datetime" style="width: 100%"/>
        </el-form-item>
        <el-form-item label="描述备注">
          <el-input v-model="editForm.description" type="textarea" :rows="4" placeholder="输入客户需求描述..."/>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEdit">提交修改</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.page-container {
  padding: 15px;
  background-color: #f0f2f5;
  min-height: calc(100vh - 84px);
}
.box-card {
  border: none;
}
.toolbar {
  display: flex;
  gap: 12px;
}
/* 强制按钮在同一行排列且居中 */
.op-group {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 0;
  white-space: nowrap;
}
</style>