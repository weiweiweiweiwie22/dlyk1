<script setup>
import { onMounted, reactive, computed } from "vue";
import { useRoute, useRouter } from 'vue-router'; // 1. 使用 Vue Router 推荐的 composable 函数
import { doGet } from "../http/httpRequest.js";

// 2. 通过 useRoute 和 useRouter 获取路由实例
const route = useRoute();
const router = useRouter();

const data = reactive({
  userDetail: {}
});

// 3. 封装一个统一的日期格式化函数
const formatDateTime = (dateTime) => {
  if (!dateTime) return '---'; // 处理空值
  return new Date(dateTime).toLocaleString('zh-CN', { hour12: false }).replace(/\//g, '-');
};

// 4. 使用 computed 计算属性来派生出用于展示的数据，让模板更干净
const formattedCreateTime = computed(() => formatDateTime(data.userDetail.createTime));
const formattedEditTime = computed(() => formatDateTime(data.userDetail.editTime));
const formattedLastLoginTime = computed(() => formatDateTime(data.userDetail.lastLoginTime));

// 将状态数字转换为文本和标签类型
const accountStatus = computed(() => {
  const expired = data.userDetail.accountNoExpired === 0;
  return { text: expired ? '已过期' : '正常', type: expired ? 'danger' : 'success' };
});
const credentialsStatus = computed(() => {
  const expired = data.userDetail.credentialsNoExpired === 0;
  return { text: expired ? '已过期' : '正常', type: expired ? 'danger' : 'success' };
});
const lockStatus = computed(() => {
  const locked = data.userDetail.accountNoLocked === 0;
  return { text: locked ? '已锁定' : '正常', type: locked ? 'warning' : 'success' };
});
const enabledStatus = computed(() => {
  const enabled = data.userDetail.accountEnabled === 1;
  return { text: enabled ? '启用' : '禁用', type: enabled ? 'success' : 'danger' };
});

const loadUserDetail = (id) => {
  if (!id) return;
  doGet(`/api/getUserDetail/${id}`).then(res => {
    if (res.data.code === 200) {
      data.userDetail = res.data.data;
    }
  });
}

// 返回上一页
const goBack = () => {
  router.back();
}

onMounted(() => {
  // 从当前路由的参数中获取 id
  loadUserDetail(route.params.id);
});
</script>

<template>
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <span>用户详情</span>
        <el-button class="button" text @click="goBack">返回</el-button>
      </div>
    </template>

    <el-descriptions
        v-if="data.userDetail.id"
        :column="2"
        border
        title="基本信息"
    >
      <el-descriptions-item label="用户ID">{{ data.userDetail.id }}</el-descriptions-item>
      <el-descriptions-item label="账号">{{ data.userDetail.loginAct }}</el-descriptions-item>
      <el-descriptions-item label="姓名">{{ data.userDetail.name }}</el-descriptions-item>
      <el-descriptions-item label="手机号">{{ data.userDetail.phone }}</el-descriptions-item>
      <el-descriptions-item label="邮箱">{{ data.userDetail.email }}</el-descriptions-item>
      <el-descriptions-item label="密码">********</el-descriptions-item>
    </el-descriptions>

    <el-descriptions
        v-if="data.userDetail.id"
        :column="2"
        border
        class="margin-top"
        title="状态信息"
    >
      <el-descriptions-item label="账号状态">
        <el-tag :type="enabledStatus.type">{{ enabledStatus.text }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="账号锁定">
        <el-tag :type="lockStatus.type">{{ lockStatus.text }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="账号过期">
        <el-tag :type="accountStatus.type">{{ accountStatus.text }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="密码过期">
        <el-tag :type="credentialsStatus.type">{{ credentialsStatus.text }}</el-tag>
      </el-descriptions-item>
    </el-descriptions>

    <el-descriptions
        v-if="data.userDetail.id"
        :column="2"
        border
        class="margin-top"
        title="审计信息"
    >
      <el-descriptions-item label="创建时间">{{ formattedCreateTime }}</el-descriptions-item>
      <el-descriptions-item label="创建人">{{ data.userDetail.createByName }}</el-descriptions-item>
      <el-descriptions-item label="修改时间">{{ formattedEditTime }}</el-descriptions-item>
      <el-descriptions-item label="修改人">{{ data.userDetail.editByName }}</el-descriptions-item>
      <el-descriptions-item label="最近登录">{{ formattedLastLoginTime }}</el-descriptions-item>
    </el-descriptions>

    <el-empty v-if="!data.userDetail.id" description="数据加载中..." />
  </el-card>
</template>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.box-card {
  width: 90%;
  margin: 20px auto;
}

.margin-top {
  margin-top: 20px;
}
</style>