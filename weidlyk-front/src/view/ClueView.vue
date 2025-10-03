<script setup>
import { nextTick, onMounted, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessageBox } from 'element-plus';
import { doGet, doPost, doDelete } from "../http/httpRequest.js";
import { messageTip } from "../util/util.js";
import {hasPermission} from "../util/permission.js";

const router = useRouter();

// --- 响应式状态定义 ---

const createInitialSearchState = () => ({
  ownerId: '',
  activityId: '',
  fullName: '',
  phone: '',
  status: '',
  source: '',
});
const searchForm = reactive(createInitialSearchState());

const createInitialAddFormState = () => ({
  ownerId: null,
  activityId: null,
  fullName: '',
  appellation: null,
  phone: '',
  weixin: '',
  needLoan: null,
  intentionState: null,
  intentionProduct: null,
  state: null,
  source: null,
  nextContactTime: '',
  description: ''
});

const checkPhoneUnique = async (rule, value, callback) => {
  const phoneRegex = /^1[3-9]\d{9}$/;
  if (!value || !phoneRegex.test(value)) {
    return callback();
  }
  try {
    const res = await doGet('/api/clue/phone/check', { phone: value });
    if (res.data.code === 200 && !res.data.data.isUnique) {
      return callback(new Error('该手机号已存在，请勿重复录入'));
    }
    return callback();
  } catch (error) {
    console.error("手机号唯一性校验失败:", error);
    return callback();
  }
};

const data = reactive({
  clueList: [],
  ownerList: [],
  activityList: [],
  appellationList: [],
  needsLoanList: [],
  intentionStatusList: [],
  intentionProductList: [],
  statusList: [],
  sourceList: [],
  total: 0,
  dialogVisible: false,
  importDialogVisible: false,
  addClueForm: createInitialAddFormState(),
  addClueRules: {
    ownerId: [
      { required: true, message: '请选择负责人', trigger: 'change' }
    ],
    fullName: [
      { required: true, message: '请输入客户姓名', trigger: 'blur' },
      { pattern: /^[\u4e00-\u9fa5]{2,4}$/, message: '姓名必须是2至4个汉字', trigger: 'blur' }
    ],
    phone: [
      { required: true, message: '请输入手机号码', trigger: 'blur' },
      { pattern: /^1[3-9]\d{9}$/, message: '手机号码格式不正确', trigger: 'blur' },
      { validator: checkPhoneUnique, trigger: 'blur' }
    ],
    state: [
      { required: true, message: '请选择线索状态', trigger: 'change' }
    ],
    source: [
      { required: true, message: '请选择线索来源', trigger: 'change' }
    ]
  }
});
const pagination = reactive({
  current: 1,
  size: 10
});
const tableRef = ref(null);
const addClueFormRef = ref(null);
const selectionMap = ref(new Map());
const uploadRef = ref(null);
const uploadLoading = ref(false);
const fileToUpload = ref(null);

// 【核心修改 1】: 新增一个辅助函数，根据状态返回Tag类型
const getTagType = (status) => {
  if (status === '已转客户') {
    return 'success'; // 成功状态（绿色）
  }
  if (['虚假线索', '丢失线索'].includes(status)) {
    return 'danger'; // 危险/错误状态（红色）
  }
  if (status === '需要条件') {
    return 'warning'; // 警告状态（橙色）
  }
  return 'primary'; // 默认状态（蓝色）
};


const handleImportSuccess = (responseData) => {
  uploadLoading.value = false;
  if (responseData.code === 200) {
    messageTip('导入成功', 'success');
    data.importDialogVisible = false;
    getClueList();
  } else {
    messageTip(responseData.msg || '导入失败', 'error');
  }
};

const handleImportError = (error) => {
  uploadLoading.value = false;
  const errorMsg = error.response?.data?.msg || error.message || '未知错误';
  messageTip('导入失败：' + errorMsg, 'error');
};

const handleExceed = (files) => {
  messageTip(`最多只能上传1个文件，当前已选择了 ${files.length} 个文件`, 'warning');
};

const beforeUpload = (file) => {
  const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' ||
      file.type === 'application/vnd.ms-excel';
  const isLt50M = file.size / 1024 / 1024 < 50;

  if (!isExcel) {
    messageTip('只能上传 Excel 文件!', 'error');
    return false;
  }
  if (!isLt50M) {
    messageTip('文件大小不能超过 50MB!', 'error');
    return false;
  }
  return true;
};

const handleFileChange = (uploadFile, uploadFiles) => {
  if (uploadFiles.length > 0) {
    fileToUpload.value = uploadFile.raw;
  } else {
    fileToUpload.value = null;
  }
};

const handleFileRemove = () => {
  fileToUpload.value = null;
};

const submitImport = async () => {
  if (!fileToUpload.value) {
    messageTip('请选择要导入的Excel文件', 'warning');
    return;
  }

  try {
    uploadLoading.value = true;
    const formData = new FormData();
    formData.append('file', fileToUpload.value);

    const response = await doPost('/api/clue/import', formData);

    handleImportSuccess(response.data);
  } catch (error) {
    handleImportError(error);
  }
};

onMounted(() => {
  getClueList();
  loadOwnerList();
  loadActivityList();
  loadAppellationList();
  loadNeedsLoanList();
  loadIntentionStatusList();
  loadIntentionProductList();
  loadStatusList();
  loadSourceList();
});

const getClueList = () => {
  const params = {
    current: pagination.current,
    size: pagination.size,
    ...searchForm
  };
  doGet('/api/clues', params).then(res => {
    if (res.data.code === 200) {
      data.clueList = res.data.data.records;
      data.total = res.data.data.total;
      nextTick(() => {
        if (tableRef.value) {
          data.clueList.forEach(clue => {
            if (selectionMap.value.has(clue.id)) {
              tableRef.value.toggleRowSelection(clue, true);
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

const loadActivityList = () => {
  doGet('/api/activity/all').then(res => {
    if (res.data.code === 200) {
      data.activityList = res.data.data;
    }
  });
};

const loadAppellationList = () => {
  doGet('/api/appellation/all').then(res => {
    if (res.data.code === 200) {
      data.appellationList = res.data.data;
    }
  });
};

const loadNeedsLoanList = () => {
  doGet('/api/needsLoan/all').then(res => {
    if (res.data.code === 200) {
      data.needsLoanList = res.data.data;
    }
  });
};

const loadIntentionStatusList = () => {
  doGet('/api/intentionStatus/all').then(res => {
    if (res.data.code === 200) {
      data.intentionStatusList = res.data.data;
    }
  });
};

const loadIntentionProductList = () => {
  doGet('/api/intentionProduct/all').then(res => {
    if (res.data.code === 200) {
      data.intentionProductList = res.data.data;
    }
  });
};

const loadStatusList = () => {
  doGet('/api/status/all').then(res => {
    if (res.data.code === 200) {
      data.statusList = res.data.data;
    }
  });
};

const loadSourceList = () => {
  doGet('/api/source/all').then(res => {
    if (res.data.code === 200) {
      data.sourceList = res.data.data;
    }
  });
};

const onSearch = () => {
  pagination.current = 1;
  getClueList();
};

const onReset = () => {
  Object.assign(searchForm, createInitialSearchState());
  onSearch();
};

const handlePageChange = (newPage) => {
  pagination.current = newPage;
  getClueList();
};
const handleSelect = (selection, row) => {
  const isSelected = selection.some(item => item.id === row.id);
  if (isSelected) { selectionMap.value.set(row.id, row); }
  else { selectionMap.value.delete(row.id); }
};
const handleSelectAll = (selection) => {
  if (selection.length > 0) { data.clueList.forEach(clue => selectionMap.value.set(clue.id, clue)); }
  else { data.clueList.forEach(clue => selectionMap.value.delete(clue.id)); }
};

const importClue = () => {
  data.importDialogVisible = true;
};
const add = () => {
  if (addClueFormRef.value) { addClueFormRef.value.resetFields(); }
  Object.assign(data.addClueForm, createInitialAddFormState());
  data.dialogVisible = true;
};
const submit = async () => {
  if (!addClueFormRef.value) return;
  try {
    await addClueFormRef.value.validate();
    const res = await doPost('/api/clue/add', data.addClueForm);
    if (res.data.code === 200) {
      messageTip("新增线索成功", "success");
      data.dialogVisible = false;
      getClueList();
    } else {
      messageTip(res.data.message || "新增失败", "error");
    }
  } catch (error) {
    messageTip("请检查必填项！", "warning");
  }
};
const delSelected = () => {
  const selectedCount = selectionMap.value.size;
  if (selectedCount === 0) {
    messageTip("请选择要删除的线索", "warning");
    return;
  }
  ElMessageBox.confirm(`确认删除选中的 ${selectedCount} 条线索吗?`, '提示', { type: 'warning' })
      .then(() => {
        const ids = Array.from(selectionMap.value.keys());
        doPost('/api/clue/delete/batch', ids).then(res => {
          if (res.data.code === 200) {
            messageTip("删除成功", "success");
            selectionMap.value.clear();
            if (data.clueList.length === ids.length && pagination.current > 1) {
              pagination.current--;
            }
            getClueList();
          } else {
            messageTip(res.data.message || "删除失败", "error");
          }
        });
      }).catch(() => {});
};
const del = (id) => {
  ElMessageBox.confirm('确认删除该线索吗?', '提示', { type: 'warning' })
      .then(() => {
        doPost('/api/clue/delete/' + id).then(res => {
          if (res.data.code === 200) {
            messageTip("删除成功", "success");
            getClueList();
          } else {
            messageTip(res.data.message || "删除失败", "error");
          }
        });
      }).catch(() => {});
};
const view = (id) => { router.push("/dashboard/clue/" + id); };
const edit = (id) => { router.push("/dashboard/clue/edit/" + id); };
</script>

<template>
  <div class="page-container">
    <el-card shadow="never">
      <el-form :model="searchForm" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="6"><el-form-item label="负责人"><el-select v-model="searchForm.ownerId" clearable><el-option v-for="owner in data.ownerList" :key="owner.id" :label="owner.name" :value="owner.id"/></el-select></el-form-item></el-col>
          <el-col :span="6"><el-form-item label="所属活动"><el-select v-model="searchForm.activityId" clearable><el-option v-for="act in data.activityList" :key="act.id" :label="act.name" :value="act.id"/></el-select></el-form-item></el-col>
          <el-col :span="6"><el-form-item label="姓名"><el-input v-model="searchForm.fullName" clearable /></el-form-item></el-col>
          <el-col :span="6"><el-form-item label="手机"><el-input v-model="searchForm.phone" clearable /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="6"><el-form-item label="线索状态"><el-input v-model="searchForm.status" clearable /></el-form-item></el-col>
          <el-col :span="6"><el-form-item label="线索来源"><el-input v-model="searchForm.source" clearable /></el-form-item></el-col>
        </el-row>
        <el-row justify="end">
          <el-form-item><el-button type="primary" @click="onSearch">搜索</el-button><el-button @click="onReset">重置</el-button></el-form-item>
        </el-row>
      </el-form>
    </el-card>

    <el-card shadow="never" style="margin-top: 20px;">
      <div class="toolbar">
        <el-button type="primary" @click="add" v-if="hasPermission('clue:add')">录入线索</el-button>
        <el-button type="success" @click="importClue" v-if="hasPermission('clue:import')">导入线索(Excel)</el-button>
        <el-button type="danger" @click="delSelected" :disabled="selectionMap.size === 0" v-if="hasPermission('clue:delete')">批量删除</el-button>
        <div class="selection-info" v-if="selectionMap.size > 0">
          <el-alert :title="'已选择 ' + selectionMap.size + ' 项'" type="info" show-icon :closable="false" />
        </div>
      </div>

      <el-table ref="tableRef" :data="data.clueList" :row-key="clue => clue.id" @select="handleSelect" @select-all="handleSelectAll" border stripe style="margin-top: 20px;">
        <el-table-column type="selection" width="55" fixed="left" />
        <el-table-column type="index" label="序号" width="70" align="center" fixed="left" />
        <el-table-column property="ownerName" label="负责人" width="120" show-overflow-tooltip />
        <el-table-column property="activityName" label="所属活动" width="180" show-overflow-tooltip />
        <el-table-column property="fullName" label="姓名" width="120" show-overflow-tooltip />
        <el-table-column property="appellation" label="称呼" width="100" show-overflow-tooltip />
        <el-table-column property="phone" label="手机" width="120" show-overflow-tooltip />
        <el-table-column property="wechat" label="微信" width="120" show-overflow-tooltip />
        <el-table-column property="needsLoan" label="是否贷款" width="100" align="center"/>
        <el-table-column property="intentionStatus" label="意向状态" width="120" show-overflow-tooltip />
        <el-table-column property="intentionProduct" label="意向产品" width="180" show-overflow-tooltip />

        <el-table-column property="status" label="线索状态" width="120" show-overflow-tooltip>
          <template #default="scope">
            <el-tag :type="getTagType(scope.row.status)" disable-transitions>
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column property="source" label="线索来源" width="120" show-overflow-tooltip />
        <el-table-column property="nextContactTime" label="下次联系时间" width="180" show-overflow-tooltip />
        <el-table-column label="操作" width="250" align="center" fixed="right">
          <template #default="scope">
            <el-button text type="primary" @click="view(scope.row.id) " v-if="hasPermission('clue:view')">详情</el-button>
            <el-button text type="success" @click="edit(scope.row.id)" v-if="hasPermission('clue:edit')">修改</el-button>
            <el-button text type="danger" @click="del(scope.row.id)" >删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination style="margin-top: 20px; justify-content: center;" background layout="total, prev, pager, next, jumper" :total="data.total" :page-size="pagination.size" :current-page="pagination.current" @current-change="handlePageChange" />
    </el-card>

    <el-dialog v-model="data.dialogVisible" title="录入新线索" width="600px" center draggable>
      <el-form ref="addClueFormRef" :model="data.addClueForm" :rules="data.addClueRules" label-width="100px">
        <el-form-item label="负责人" prop="ownerId">
          <el-select v-model="data.addClueForm.ownerId" placeholder="请选择负责人">
            <el-option
                v-for="owner in data.ownerList"
                :key="owner.id"
                :label="owner.name"
                :value="owner.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="所属活动" prop="activityId">
          <el-select v-model="data.addClueForm.activityId" placeholder="请选择所属活动">
            <el-option v-for="act in data.activityList"
                       :key="act.id"
                       :label="act.name"
                       :value="act.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="姓名" prop="fullName">
          <el-input v-model="data.addClueForm.fullName" />
        </el-form-item>
        <el-form-item label="称呼" prop="appellation">
          <el-select v-model="data.addClueForm.appellation" placeholder="请选择称呼">
            <el-option v-for="app in data.appellationList"
                       :key="app.id"
                       :label="app.typeValue"
                       :value="app.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="手机" prop="phone">
          <el-input v-model="data.addClueForm.phone" />
        </el-form-item>
        <el-form-item label="微信" prop="weixin">
          <el-input v-model="data.addClueForm.weixin" />
        </el-form-item>
        <el-form-item label="是否贷款" prop="needLoan">
          <el-select v-model="data.addClueForm.needLoan" placeholder="请选择是否贷款">
            <el-option v-for="ndl in data.needsLoanList"
                       :key="ndl.id"
                       :label="ndl.typeValue"
                       :value="ndl.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="意向状态" prop="intentionState">
          <el-select v-model="data.addClueForm.intentionState" placeholder="请选择意向状态">
            <el-option v-for="ins in data.intentionStatusList"
                       :key="ins.id"
                       :label="ins.typeValue"
                       :value="ins.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="意向产品" prop="intentionProduct">
          <el-select v-model="data.addClueForm.intentionProduct" placeholder="请选择意向产品">
            <el-option v-for="inp in data.intentionProductList"
                       :key="inp.id"
                       :label="inp.typeValue"
                       :value="inp.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="线索状态" prop="state">
          <el-select v-model="data.addClueForm.state" placeholder="请选择线索状态">
            <el-option v-for="ssl in data.statusList"
                       :key="ssl.id"
                       :label="ssl.typeValue"
                       :value="ssl.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="线索来源" prop="source">
          <el-select v-model="data.addClueForm.source" placeholder="请选择线索来源">
            <el-option v-for="sel in data.sourceList"
                       :key="sel.id"
                       :label="sel.typeValue"
                       :value="sel.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="下次联系" prop="nextContactTime">
          <el-date-picker v-model="data.addClueForm.nextContactTime" type="datetime" style="width: 100%;"/>
        </el-form-item>
        <el-form-item label="线索描述" prop="description">
          <el-input v-model="data.addClueForm.description" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="data.dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit">提交</el-button></template>
    </el-dialog>

    <el-dialog v-model="data.importDialogVisible" title="导入线索(Excel)注意事项" width="600px" center draggable :close-on-click-modal="false">
      <el-upload
          ref="uploadRef"
          class="upload-demo"
          :limit="1"
          :auto-upload="false"
          :on-exceed="handleExceed"
          :before-upload="beforeUpload"
          :on-change="handleFileChange"
          :on-remove="handleFileRemove"
          accept=".xls,.xlsx"
      >
        <template #trigger>
          <el-button type="primary" :loading="uploadLoading">选择Excel文件</el-button>
        </template>
        <div class="el-upload__tip">
          仅支持后缀名为.xls或.xlsx的文件
        </div>
        <div class="el-upload__tip text-red">
          重要提示：
          <ul>
            <li>上传仅支持后缀名为.xls或.xlsx的文件</li>
            <li>给定Excel文件的第一行将视为字段名</li>
            <li>请确认您的文件大小不超过50MB</li>
            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式</li>
            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式</li>
            <li>必填字段：姓名、手机号、线索状态、线索来源</li>
          </ul>
        </div>
      </el-upload>

      <template #footer>
        <el-button @click="data.importDialogVisible = false" :disabled="uploadLoading">取消</el-button>
        <el-button type="primary" @click="submitImport" :loading="uploadLoading">导入</el-button>
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
.selection-info {
  margin-top: 15px;
  margin-bottom: -5px;
}
/* 确保下拉选择框宽度100% */
.el-select {
  width: 100%;
}

/* 上传组件样式 */
.upload-demo {
  text-align: center;
  padding: 20px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  margin-bottom: 20px;
}
.upload-demo:hover {
  border-color: #409EFF;
}
.el-upload__tip {
  line-height: 1.5;
  margin-top: 10px;
}
.text-red {
  color: #F56C6C;
}
.text-red ul {
  padding-left: 20px;
  margin: 10px 0;
}
.text-red li {
  line-height: 1.8;
}
</style>