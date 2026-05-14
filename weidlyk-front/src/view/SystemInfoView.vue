<template>
  <div class="system-info-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span><i class="el-icon-setting"></i> 系统配置信息 (系统管理)</span>
        </div>
      </template>

      <el-descriptions :column="2" border>
        <el-descriptions-item label="系统名称">{{ systemInfo.name || '未配置' }}</el-descriptions-item>
        <el-descriptions-item label="系统版本">{{ systemInfo.version || '未配置' }}</el-descriptions-item>
        <el-descriptions-item label="官方网址">{{ systemInfo.site || '未配置' }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ systemInfo.tel || '未配置' }}</el-descriptions-item>
        <el-descriptions-item label="联系邮箱">{{ systemInfo.email || '未配置' }}</el-descriptions-item>
        <el-descriptions-item label="公司地址">{{ systemInfo.address || '未配置' }}</el-descriptions-item>
        <el-descriptions-item label="系统描述" :span="2">{{ systemInfo.description || '暂无描述' }}</el-descriptions-item>
      </el-descriptions>

      <div style="margin-top: 20px;">
        <el-alert title="数据审计说明" type="info" description="系统已开启底层自动化行为审计，所有针对线索与交易的操作均由安全上下文自动提取操作者ID并实时记录。" show-icon :closable="false" />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { doGet } from '../http/httpRequest.js';

const systemInfo = ref({
  name: '加载中...',
  version: '',
  site: '',
  tel: '',
  email: '',
  address: '',
  description: ''
});

const loadSystemInfo = async () => {
  try {
    // 注意这里的路径：和我们刚写的 Controller 对应
    const res = await doGet('/api/systemInfo/info');
    if (res.data.code === 200 && res.data.data) {
      systemInfo.value = res.data.data;
    }
  } catch (error) {
    console.error("获取系统信息失败:", error);
  }
};

onMounted(() => {
  loadSystemInfo();
});
</script>

<style scoped>
.system-info-container { padding: 20px; }
.card-header { font-weight: bold; color: #005088; }
</style>