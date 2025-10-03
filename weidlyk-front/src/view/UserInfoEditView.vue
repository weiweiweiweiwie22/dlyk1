<script setup>
import { onMounted, reactive, ref } from "vue";
import { useRoute, useRouter } from 'vue-router';
import { doGet, doPost } from "../http/httpRequest.js"; // 假设你有一个 doPost 用于提交
import { messageTip } from "../util/util.js";

// 1. --- 表单和路由 ---
const route = useRoute();
const router = useRouter();
const editFormRef = ref(null); // 用于引用 el-form 组件

// 2. --- 状态管理 ---
// 用于绑定表单的响应式数据
const formData = reactive({
  id: null,
  loginAct: '',
  name: '',
  phone: '',
  email: '',
  accountNoExpired: '',
  credentialsNoExpired: '',
  accountNoLocked: '',
  accountEnabled: '',
  // 不可编辑的字段也一并放入，方便展示
  createTime: '',
  createByName: '',
  editByName: '',
  lastLoginTime: ''
});

// 用于备份从服务器获取的原始数据，方便“重置”
let originalData = {};

// 3. --- 校验规则 (可以从“新增用户”的规则复制过来，并按需修改) ---
const editRules = reactive({
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 6, message: '姓名长度请在2-6位之间', trigger: 'blur' },
    { pattern: /^[\u4e00-\u9fa5]+$/, message: '姓名只能包含中文', trigger: 'blur' },
  ],
  loginAct: [
    { required: true, message: '请输入登录账号', trigger: 'blur' },
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur'},
  ],
  email:[
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { pattern: /^([A-Za-z0-9_\-\.])+@([A-Za-z0-9_\-\.])+\.[A-Za-z]{2,4}$/, message: '请输入正确的邮箱', trigger: 'blur'},
  ],
  // ... 其他字段的规则 ...
});


// 4. --- 数据加载 ---
const loadUserData = async (id) => {
  if (!id) return;
  try {
    const res = await doGet(`/api/getUserDetail/${id}`);
    if (res.data.code === 200) {
      // 同时填充表单数据和备份原始数据
      Object.assign(formData, res.data.data);
      originalData = JSON.parse(JSON.stringify(res.data.data)); // 深拷贝一份作为备份
    }
  } catch (error) {
    messageTip("数据加载失败", "error");
  }
};

onMounted(() => {
  loadUserData(route.params.id);
});


// 5. --- 核心操作 ---
// 提交表单
const submitForm = async () => {
  if (!editFormRef.value) return;
  try {
    await editFormRef.value.validate();
    // 校验成功
    // 假设你的更新接口是 /api/user/update
    const res = await doPost('/api/user/update', formData);
    if (res.data.code === 200) {
      messageTip("更新成功", "success");
      router.back(); // 成功后返回上一页
    } else {
      messageTip(res.data.message || "更新失败", "error");
    }
  } catch (validationError) {
    messageTip("请检查输入项！", "warning");
  }
};

// 重置表单
const resetForm = () => {
  // 将备份的原始数据恢复到表单数据中
  Object.assign(formData, originalData);
  // 清除表单的校验状态
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
          <span>编辑用户资料</span>
          <el-button class="button"  text @click="goBack">返回</el-button>
        </div>
      </template>

      <el-form
          v-if="formData.id"
          ref="editFormRef"
          :model="formData"
          :rules="editRules"
          label-width="100px"
          class="edit-form"
      >
        <el-divider content-position="left">基本信息</el-divider>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户ID">
              <span class="readonly-text">{{ formData.id }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="登录账号" prop="loginAct">
              <el-input v-model="formData.loginAct" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="formData.name" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="formData.phone" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="formData.email" />
        </el-form-item>

        <el-divider content-position="left">状态信息</el-divider>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="账号状态" prop="accountEnabled">
              <el-select v-model="formData.accountEnabled" placeholder="请选择" style="width: 100%">
                <el-option label="启用" :value="1"/>
                <el-option label="禁用" :value="0"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="账号锁定" prop="accountNoLocked">
              <el-select v-model="formData.accountNoLocked" placeholder="请选择" style="width: 100%">
                <el-option label="正常" :value="1"/>
                <el-option label="已锁定" :value="0"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="账号过期" prop="accountNoExpired">
              <el-select v-model="formData.accountNoExpired" placeholder="请选择" style="width: 100%">
                <el-option label="正常" :value="1"/>
                <el-option label="已过期" :value="0"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="密码过期" prop="credentialsNoExpired">
              <el-select v-model="formData.credentialsNoExpired" placeholder="请选择" style="width: 100%">
                <el-option label="正常" :value="1"/>
                <el-option label="已过期" :value="0"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">审计信息</el-divider>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="创建时间">
              <span class="readonly-text">{{ new Date(formData.createTime).toLocaleString() }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建人">
              <span class="readonly-text">{{ formData.createByName || '---' }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="修改人">
              <span class="readonly-text">{{ formData.editByName || '---' }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最近登录">
              <span class="readonly-text">{{ new Date(formData.lastLoginTime).toLocaleString() }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider />
        <el-form-item class="action-buttons">
          <el-button type="primary" @click="submitForm">提 交</el-button>
          <el-button @click="resetForm">重 置</el-button>
        </el-form-item>

      </el-form>

      <el-empty v-if="!formData.id" description="数据加载中..." />
    </el-card>
  </div>
</template>

<style scoped>
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
  max-width: 900px; /* 增加最大宽度限制，防止在大屏幕上过于拉伸 */
  margin: 20px auto;
}
.readonly-text {
  color: #606266;
}
.edit-form {
  margin-top: 20px;
}
/* 用于覆盖 el-form-item 默认的 margin-bottom，使按钮组不与下方有太大间距 */
.action-buttons {
  margin-top: 20px;
  margin-bottom: 0;
}
/* 让按钮组内的按钮水平居中 */
:deep(.action-buttons .el-form-item__content) {
  justify-content: center;
}
</style>