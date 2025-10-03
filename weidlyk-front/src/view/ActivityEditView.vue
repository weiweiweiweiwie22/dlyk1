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
  name: '',
  ownerId: '',
  startTime: '',
  endTime: '',
  cost: undefined,
  description: '',
  // 不可编辑的审计信息
  createTime: '',
  createByName: '',
  editTime: '',
  editByName: ''
});

// 用于存储从服务器获取的原始数据，方便“重置”
let originalData = {};

// 用于存储负责人下拉列表
const ownerList = ref([]);


// --- 2. 校验规则 (与新增活动保持一致) ---
const editRules = reactive({
  name: [{ required: true, message: '请输入活动名称', trigger: 'blur' }],
  ownerId: [{ required: true, message: '请选择负责人', trigger: 'change' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
  cost: [
    { required: true, message: '请输入活动预算', trigger: 'blur' },
    { type: 'number', message: '预算必须为数字' }
  ],
});


// --- 3. 数据加载 ---
onMounted(() => {
  // 页面加载后，同时获取活动详情和负责人列表
  loadActivityData(route.params.id);
  loadOwnerList();
});

const loadActivityData = async (id) => {
  if (!id) {
    messageTip("无效的活动ID", "error");
    return;
  }
  try {
    // 假设获取详情的接口是 /api/activity/detail/{id}
    const res = await doGet(`/api/activity/detail/${id}`);
    if (res.data.code === 200) {
      // 将获取的数据填充到表单中
      Object.assign(formData, res.data.data);
      // 深拷贝一份原始数据用于“重置”功能
      originalData = JSON.parse(JSON.stringify(res.data.data));
    } else {
      messageTip(res.data.message || "数据加载失败", "error");
    }
  } catch (error) {
    messageTip("数据加载异常", "error");
  }
};

const loadOwnerList = async () => {
  try {
    // 假设获取所有用户的接口是 /api/user/all
    const res = await doGet('/api/user/all');
    if (res.data.code === 200) {
      ownerList.value = res.data.data;
    }
  } catch (error) {
    messageTip("负责人列表加载失败", "error");
  }
};


// --- 4. 核心操作 ---

// 提交表单
const submitForm = async () => {
  if (!editFormRef.value) return;
  try {
    await editFormRef.value.validate();
    // 校验成功，提交数据
    // 假设更新接口是 /api/activity/update
    const res = await doPost('/api/activity/update', formData);
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
  // 将备份的原始数据恢复到表单
  Object.assign(formData, originalData);
  // 清除表单的校验提示
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
          <span>编辑市场活动</span>
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
            <el-form-item label="活动ID">
              <span class="readonly-text">{{ formData.id }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="活动名称" prop="name">
              <el-input v-model="formData.name" placeholder="请输入活动名称" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="负责人" prop="ownerId">
              <el-select v-model="formData.ownerId" placeholder="请选择负责人" style="width: 100%;">
                <el-option
                    v-for="owner in ownerList"
                    :key="owner.id"
                    :label="owner.name"
                    :value="owner.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="活动预算" prop="cost">
              <el-input v-model.number="formData.cost" placeholder="请输入预算金额">
                <template #append>元</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始时间" prop="startTime">
              <el-date-picker v-model="formData.startTime" type="datetime" placeholder="选择开始时间" style="width: 100%;" format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间" prop="endTime">
              <el-date-picker v-model="formData.endTime" type="datetime" placeholder="选择结束时间" style="width: 100%;" format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="活动描述" prop="description">
          <el-input v-model="formData.description" type="textarea" :rows="3" placeholder="请输入活动描述" />
        </el-form-item>

        <el-divider content-position="left">审计信息</el-divider>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="创建时间">
              <span class="readonly-text">{{ formData.createTime }}</span>
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
            <el-form-item label="修改时间">
              <span class="readonly-text">{{ formData.editTime || '---' }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="修改人">
              <span class="readonly-text">{{ formData.editByName || '---' }}</span>
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
/* 样式与您的用户编辑页完全一致，确保整体风格统一 */
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