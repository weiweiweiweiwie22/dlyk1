<template>
  <el-card shadow="never">
    <el-page-header @back="$router.back()" content="客户详情" />
    <el-divider />

    <el-descriptions :column="2" border title="客户基础信息">
      <el-descriptions-item label="客户姓名">{{ data.detail.fullName }}</el-descriptions-item>
      <el-descriptions-item label="手机号码">{{ data.detail.phone }}</el-descriptions-item>
      <el-descriptions-item label="负责人">{{ data.detail.ownerName }}</el-descriptions-item>
      <el-descriptions-item label="选购产品">{{ data.detail.productName }}</el-descriptions-item>
      <el-descriptions-item label="下次联系时间">{{ data.detail.nextContactTime }}</el-descriptions-item>
      <el-descriptions-item label="描述" :span="2">{{ data.detail.description }}</el-descriptions-item>
    </el-descriptions>

    <el-descriptions :column="2" border title="系统信息" style="margin-top: 20px;">
      <el-descriptions-item label="创建时间">{{ data.detail.createTime }}</el-descriptions-item>
      <el-descriptions-item label="创建人">{{ data.detail.createBy }}</el-descriptions-item>
    </el-descriptions>
  </el-card>
</template>

<script setup>
import { reactive, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { doGet } from '../http/httpRequest.js';

const route = useRoute();
const data = reactive({ detail: {} });

const loadDetail = async () => {
  const res = await doGet(`/api/customer/detail/${route.params.id}`);
  if (res.data.code === 200) {
    data.detail = res.data.data;
  }
};

onMounted(() => loadDetail());
</script>