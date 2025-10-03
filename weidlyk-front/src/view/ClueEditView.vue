<script setup>
import { onMounted, reactive, ref } from "vue";
import { useRoute, useRouter } from 'vue-router';
import { doGet, doPost } from "../http/httpRequest.js";
import { messageTip } from "../util/util.js";

// --- 1. 表单、路由和状态定义 ---
const route = useRoute();
const router = useRouter();
const editFormRef = ref(null); // el-form 组件的引用

// 用于绑定表单的响应式数据
const formData = reactive({
  id: null,
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
  description: '',
  // 不可编辑的审计信息
  createTime: '',
  createBy: '',
  editTime: '',
  editBy: ''
});

// 用于存储从服务器获取的原始数据，方便“重置”
let originalData = {};

// 用于存储所有下拉列表的数据
const dropdownData = reactive({
  ownerList: [],
  activityList: [],
  appellationList: [],
  needsLoanList: [],
  intentionStatusList: [],
  intentionProductList: [],
  statusList: [],
  sourceList: [],
});

// --- 2. 校验规则 (与新增线索保持一致) ---
const checkPhoneUnique = async (rule, value, callback) => {
  const phoneRegex = /^1[3-9]\d{9}$/;
  if (!value || !phoneRegex.test(value)) {
    return callback();
  }
  // 在编辑模式下，如果手机号未改变，则无需校验唯一性
  if (value === originalData.phone) {
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

const editRules = reactive({
  ownerId: [{ required: true, message: '请选择负责人', trigger: 'change' }],
  fullName: [
    { required: true, message: '请输入客户姓名', trigger: 'blur' },
    { pattern: /^[\u4e00-\u9fa5]{2,4}$/, message: '姓名必须是2至4个汉字', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号码', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号码格式不正确', trigger: 'blur' },
    { validator: checkPhoneUnique, trigger: 'blur' }
  ],
  state: [{ required: true, message: '请选择线索状态', trigger: 'change' }],
  source: [{ required: true, message: '请选择线索来源', trigger: 'change' }]
});


// --- 3. 数据加载 ---
onMounted(() => {
  loadClueData(route.params.id);
  loadAllDropdownData();
});

const loadClueData = async (id) => {
  if (!id) {
    messageTip("无效的线索ID", "error");
    router.back();
    return;
  }
  try {
    // 【关键修改】: 将调用的接口从 /detail/ 改为 /edit/
    const res = await doGet(`/api/clue/edit/${id}`);
    if (res.data.code === 200) {
      Object.assign(formData, res.data.data);
      originalData = JSON.parse(JSON.stringify(res.data.data));
    } else {
      messageTip(res.data.message || "数据加载失败", "error");
    }
  } catch (error) {
    messageTip("数据加载异常", "error");
  }
};

// 加载所有下拉列表数据
const loadAllDropdownData = () => {
  doGet('/api/user/all').then(res => { dropdownData.ownerList = res.data.data; });
  doGet('/api/activity/all').then(res => { dropdownData.activityList = res.data.data; });
  doGet('/api/appellation/all').then(res => { dropdownData.appellationList = res.data.data; });
  doGet('/api/needsLoan/all').then(res => { dropdownData.needsLoanList = res.data.data; });
  doGet('/api/intentionStatus/all').then(res => { dropdownData.intentionStatusList = res.data.data; });
  doGet('/api/intentionProduct/all').then(res => { dropdownData.intentionProductList = res.data.data; });
  doGet('/api/status/all').then(res => { dropdownData.statusList = res.data.data; });
  doGet('/api/source/all').then(res => { dropdownData.sourceList = res.data.data; });
};


// --- 4. 核心操作 ---

// 提交表单
const submitForm = async () => {
  if (!editFormRef.value) return;
  try {
    await editFormRef.value.validate();
    // 【API 依赖】: 假设更新接口是 /api/clue/update
    const res = await doPost('/api/clue/update', formData);
    if (res.data.code === 200) {
      messageTip("更新成功", "success");
      router.back(); // 成功后返回上一页
    } else {
      messageTip(res.data.message || "更新失败", "error");
    }
  } catch (validationError) {
    messageTip("请检查表单输入项！", "warning");
  }
};

// 重置表单
const resetForm = () => {
  Object.assign(formData, originalData);
  if (editFormRef.value) {
    editFormRef.value.clearValidate();
  }
  messageTip("已重置", "info");
};

// 返回上一页
const goBack = () => {
  router.back();
}
</script>

<template>
  <div class="page-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>编辑线索信息</span>
          <el-button class="button" text @click="goBack">返回</el-button>
        </div>
      </template>

      <el-skeleton v-if="!formData.id" :rows="10" animated />

      <el-form
          v-if="formData.id"
          ref="editFormRef"
          :model="formData"
          :rules="editRules"
          label-width="100px"
          class="edit-form"
      >
        <el-divider content-position="left">基础信息</el-divider>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="线索ID">
              <span class="readonly-text">{{ formData.id }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人" prop="ownerId">
              <el-select v-model="formData.ownerId" placeholder="请选择负责人" style="width: 100%;">
                <el-option
                    v-for="owner in dropdownData.ownerList"
                    :key="owner.id"
                    :label="owner.name"
                    :value="owner.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名" prop="fullName">
              <el-input v-model="formData.fullName" placeholder="请输入客户姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="称呼" prop="appellation">
              <el-select v-model="formData.appellation" placeholder="请选择称呼" style="width: 100%;">
                <el-option v-for="app in dropdownData.appellationList" :key="app.id" :label="app.typeValue" :value="app.id" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="手机" prop="phone">
              <el-input v-model="formData.phone" placeholder="请输入手机号码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="微信" prop="weixin">
              <el-input v-model="formData.weixin" placeholder="请输入微信号" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属活动" prop="activityId">
              <el-select v-model="formData.activityId" placeholder="请选择所属活动" style="width: 100%;">
                <el-option v-for="act in dropdownData.activityList" :key="act.id" :label="act.name" :value="act.id"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="线索状态" prop="state">
              <el-select v-model="formData.state" placeholder="请选择线索状态" style="width: 100%;">
                <el-option v-for="s in dropdownData.statusList" :key="s.id" :label="s.typeValue" :value="s.id"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="线索来源" prop="source">
              <el-select v-model="formData.source" placeholder="请选择线索来源" style="width: 100%;">
                <el-option v-for="s in dropdownData.sourceList" :key="s.id" :label="s.typeValue" :value="s.id"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否贷款" prop="needLoan">
              <el-select v-model="formData.needLoan" placeholder="请选择是否贷款" style="width: 100%;">
                <el-option v-for="ndl in dropdownData.needsLoanList" :key="ndl.id" :label="ndl.typeValue" :value="ndl.id"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="意向状态" prop="intentionState">
              <el-select v-model="formData.intentionState" placeholder="请选择意向状态" style="width: 100%;">
                <el-option v-for="is in dropdownData.intentionStatusList" :key="is.id" :label="is.typeValue" :value="is.id"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="意向产品" prop="intentionProduct">
              <el-select v-model="formData.intentionProduct" placeholder="请选择意向产品" style="width: 100%;">
                <el-option v-for="ip in dropdownData.intentionProductList" :key="ip.id" :label="ip.typeValue" :value="ip.id"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="下次联系" prop="nextContactTime">
          <el-date-picker v-model="formData.nextContactTime" type="datetime" placeholder="选择下次联系时间" style="width: 100%;" format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss"/>
        </el-form-item>

        <el-form-item label="线索描述" prop="description">
          <el-input v-model="formData.description" type="textarea" :rows="3" placeholder="请输入线索描述" />
        </el-form-item>

        <el-divider content-position="left">审计信息</el-divider>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="创建时间">
              <span class="readonly-text">{{ formData.createTime || '---' }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建人">
              <span class="readonly-text">{{ formData.createBy || '---' }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="修改时间">
              <span class="readonly-text">{{ formData.editTime || '---' }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="修改人">
              <span class="readonly-text">{{ formData.editBy || '---' }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider />
        <el-form-item class="action-buttons">
          <el-button type="primary" @click="submitForm">提 交 修 改</el-button>
          <el-button @click="resetForm">重 置</el-button>
        </el-form-item>

      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
/* 样式与您的参考页一致，确保整体风格统一 */
.page-container {
  padding: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.box-card {
  width: 90%;
  max-width: 900px;
  margin: 20px auto;
}
.readonly-text {
  color: #606266;
}
.edit-form {
  margin-top: 20px;
}
.action-buttons {
  margin-top: 20px;
  margin-bottom: 0;
}
:deep(.action-buttons .el-form-item__content) {
  justify-content: center;
}
</style>