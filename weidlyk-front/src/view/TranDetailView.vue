<template>
  <div style="padding: 20px;">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span style="font-weight: bold; color: #005088;"><i class="el-icon-tickets"></i> 交易流水详情</span>
          <el-button style="float: right; padding: 3px 0" type="primary" link @click="$router.go(-1)">返回列表</el-button>
        </div>
      </template>

      <el-descriptions :column="2" border>
        <el-descriptions-item label="交易流水号">{{ tranDetail.tranNo }}</el-descriptions-item>
        <el-descriptions-item label="交易阶段">
          <el-tag :type="tranDetail.stageName === '已成交' ? 'success' : ''">{{ tranDetail.stageName }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="交易金额">{{ tranDetail.money ? '￥' + tranDetail.money : '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="预计成交日期">{{ tranDetail.expectedDate || '未定' }}</el-descriptions-item>
        <el-descriptions-item label="下次联系时间">{{ tranDetail.nextContactTime || '未定' }}</el-descriptions-item>
        <el-descriptions-item label="创建人">{{ tranDetail.createByName || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ tranDetail.createTime }}</el-descriptions-item>
        <el-descriptions-item label="最后修改人">{{ tranDetail.editByName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="交易描述" :span="2">{{ tranDetail.description || '暂无描述' }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { doGet } from '../http/httpRequest.js';

const route = useRoute();
const tranDetail = ref({});

const loadTranDetail = async () => {
  try {
    const res = await doGet(`/api/tran/detail/${route.params.id}`);
    if (res.data.code === 200) {
      tranDetail.value = res.data.data;
    }
  } catch (error) {
    console.error("获取交易详情失败:", error);
  }
};

onMounted(() => {
  if (route.params.id) {
    loadTranDetail();
  }
});
</script>