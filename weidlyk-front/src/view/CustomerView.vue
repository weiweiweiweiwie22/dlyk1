<script setup>
import { nextTick, onMounted, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessageBox } from 'element-plus';
// 确保引入了 doGetFile 和 doPostFile
import { doGet, doPost, doDelete, doGetFile, doPostFile } from "../http/httpRequest.js";
import { messageTip } from "../util/util.js";

const router = useRouter();

// --- 响应式状态定义 ---
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
  productList: [],
  total: 0,
});

const pagination = reactive({
  current: 1,
  size: 10
});

const tableRef = ref(null);
const selectionMap = ref(new Map());

// --- 生命周期 ---
onMounted(() => {
  getCustomerList();
  loadOwnerList();
  loadProductList();
});

// --- 数据获取 ---
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
      nextTick(() => {
        if (tableRef.value) {
          data.customerList.forEach(customer => {
            if (selectionMap.value.has(customer.id)) {
              tableRef.value.toggleRowSelection(customer, true);
            }
          });
        }
      });
    }
  });
};

const loadOwnerList = () => {
  doGet('/api/user/all').then(res => {
    if (res.data.code === 200) {
      data.ownerList = res.data.data;
    }
  });
};

const loadProductList = () => {
  doGet('/api/product/all').then(res => {
    if (res.data.code === 200) {
      data.productList = res.data.data;
    }
  });
};


// --- 搜索与重置 ---
const onSearch = () => {
  pagination.current = 1;
  getCustomerList();
};

const onReset = () => {
  Object.assign(searchForm, createInitialSearchState());
  onSearch();
};

// --- 分页与操作 ---
const handlePageChange = (newPage) => {
  pagination.current = newPage;
  getCustomerList();
};

const handleSelect = (selection, row) => {
  if (selection.some(item => item.id === row.id)) {
    selectionMap.value.set(row.id, row);
  } else {
    selectionMap.value.delete(row.id);
  }
};
const handleSelectAll = (selection) => {
  if (selection.length > 0) {
    data.customerList.forEach(customer => selectionMap.value.set(customer.id, customer));
  } else {
    data.customerList.forEach(customer => selectionMap.value.delete(customer.id));
  }
};

const view = (id) => {
  messageTip("详情功能待开发", "info");
};
const del = (id) => {
  messageTip("删除功能待开发", "info");
};

// 【核心修改】: 重写“导出全部”函数，使其不再跳转页面
const exportCustomer = () => {
  ElMessageBox.confirm('确定要导出当前搜索结果的客户列表吗？', '提示', {
    confirmButtonText: '确定导出',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 1. 准备请求参数（即当前的搜索条件）
    const params = { ...searchForm };

    // 2. 使用 doGetFile 方法请求文件流
    doGetFile('/api/customer/export', params).then(res => {
      const blob = new Blob([res.data]);
      const contentDisposition = res.headers['content-disposition'];
      let fileName = '客户列表.xlsx';
      if (contentDisposition) {
        const fileNameMatch = contentDisposition.match(/filename\*=utf-8''(.+)/);
        if (fileNameMatch && fileNameMatch.length > 1) {
          fileName = decodeURIComponent(fileNameMatch[1]);
        }
      }

      // 3. 创建隐藏链接并模拟点击，触发下载
      const link = document.createElement('a');
      link.href = URL.createObjectURL(blob);
      link.download = fileName;
      document.body.appendChild(link);
      link.click();

      // 4. 清理
      document.body.removeChild(link);
      URL.revokeObjectURL(link.href);

      messageTip("导出成功", "success");

    }).catch(err => {
      messageTip("导出失败，请稍后重试", "error");
    });

  }).catch(() => {
    messageTip("已取消导出", "info");
  });
};

// “导出选中”函数保持不变，它已经是正确的
const exportSelected = () => {
  if (selectionMap.value.size === 0) {
    messageTip("请至少选择一条数据进行导出", "warning");
    return;
  }
  ElMessageBox.confirm(`确定要导出选中的 ${selectionMap.value.size} 条客户数据吗？`, '提示', {
    confirmButtonText: '确定导出',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const ids = Array.from(selectionMap.value.keys());
    doPostFile('/api/customer/export/selected', ids).then(res => {
      const blob = new Blob([res.data]);
      const contentDisposition = res.headers['content-disposition'];
      let fileName = '选中的客户.xlsx';
      if (contentDisposition) {
        const fileNameMatch = contentDisposition.match(/filename\*=utf-8''(.+)/);
        if (fileNameMatch && fileNameMatch.length > 1) {
          fileName = decodeURIComponent(fileNameMatch[1]);
        }
      }
      const link = document.createElement('a');
      link.href = URL.createObjectURL(blob);
      link.download = fileName;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      URL.revokeObjectURL(link.href);
      messageTip("导出成功", "success");
      tableRef.value.clearSelection();
      selectionMap.value.clear();
    }).catch(err => {
      messageTip("导出失败，请稍后重试", "error");
    });
  }).catch(() => {
    messageTip("已取消导出", "info");
  });
};
</script>

<template>
  <div class="page-container">
    <el-card shadow="never">
      <el-form :model="searchForm" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="负责人">
              <el-select v-model="searchForm.ownerId" clearable placeholder="请选择负责人">
                <el-option v-for="owner in data.ownerList" :key="owner.id" :label="owner.name" :value="owner.id"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="意向产品">
              <el-select v-model="searchForm.productId" clearable placeholder="请选择产品">
                <el-option v-for="product in data.productList" :key="product.id" :label="product.name" :value="product.id"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="客户姓名">
              <el-input v-model="searchForm.fullName" clearable placeholder="请输入客户姓名"/>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="手机号">
              <el-input v-model="searchForm.phone" clearable placeholder="请输入手机号"/>
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
        <el-button type="success" @click="exportCustomer">导出全部(Excel)</el-button>
        <el-button type="warning" @click="exportSelected" :disabled="selectionMap.size === 0">导出选中(Excel)</el-button>
      </div>

      <el-table ref="tableRef" :data="data.customerList" @select="handleSelect" @select-all="handleSelectAll" border stripe style="margin-top: 20px;">
        <el-table-column type="selection" width="55" fixed="left" />
        <el-table-column type="index" label="序号" width="70" align="center" fixed="left" />
        <el-table-column property="fullName" label="客户姓名" width="120" show-overflow-tooltip />
        <el-table-column property="phone" label="手机" width="120" show-overflow-tooltip />
        <el-table-column property="ownerName" label="负责人" width="120" show-overflow-tooltip />
        <el-table-column property="productName" label="意向产品" width="180" show-overflow-tooltip />
        <el-table-column property="nextContactTime" label="下次联系时间" width="180" show-overflow-tooltip />
        <el-table-column property="createTime" label="成为客户时间" width="180" show-overflow-tooltip />
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template #default="scope">
            <el-button text type="primary" @click="view(scope.row.id)">详情</el-button>
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
.el-select {
  width: 100%;
}
</style>