<script setup>
import { onMounted, reactive, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { messageTip } from "../util/util.js";
import { doGet, doPost } from "../http/httpRequest.js"; // 确保 doPost 已导入

// --- 状态定义 ---
const route = useRoute();
const router = useRouter();

// 用于存储活动详情和备注列表
const activityData = reactive({
  detail: {
    remarkList: [] // 确保 remarkList 初始为一个空数组，防止模板渲染出错
  }
});

// V V V 新增：用于双向绑定新备注的输入框 V V V
const newRemarkContent = ref('');
// ^ ^ ^ 新增 ^ ^ ^


// --- 生命周期钩子 ---
onMounted(() => {
  loadActivityDetail();
});


// --- 方法定义 ---

/**
 * 加载活动详情的核心方法 (无改动)
 */
const loadActivityDetail = () => {
  const activityId = route.params.id;
  if (!activityId) {
    messageTip('无效的活动ID', 'error');
    return;
  }
  doGet(`/api/activity/detail/${activityId}`).then(res => {
    if (res.data.code === 200) {
      activityData.detail = res.data.data;
    } else {
      messageTip(res.data.message || '获取活动详情失败，请稍后重试', 'error');
    }
  });
};

/**
 * V V V 新增：处理添加备注的逻辑 V V V
 */
const handleAddRemark = async () => {
  // 简单的前端校验，防止提交空备注
  if (!newRemarkContent.value.trim()) {
    messageTip('备注内容不能为空', 'warning');
    return;
  }

  // 准备提交到后端的数据
  const payload = {
    activityId: route.params.id,
    noteContent: newRemarkContent.value
  };

  try {
    // 调用后端接口添加备注
    const res = await doPost('/api/activity/remark/add', payload);
    if (res.data.code === 200) {
      messageTip('备注添加成功', 'success');
      // 清空输入框
      newRemarkContent.value = '';
      // 重新加载活动详情，以刷新备注列表
      loadActivityDetail();
    } else {
      messageTip(res.data.message || '添加失败', 'error');
    }
  } catch (error) {
    messageTip('请求异常，请稍后重试', 'error');
  }
};

/**
 * 返回到上一个页面 (无改动)
 */
const goBack = () => {
  router.back();
};
</script>

<template>
  <div class="page-container">
    <el-card shadow="never">
      <el-page-header @back="goBack" content="市场活动详情"></el-page-header>
      <el-divider />

      <el-descriptions :column="2" border title="活动基础信息">
        <el-descriptions-item label-align="right" align="left" label="活动ID">{{ activityData.detail.id || 'N/A' }}</el-descriptions-item>
        <el-descriptions-item label-align="right" align="left" label="活动名称">{{ activityData.detail.name || 'N/A' }}</el-descriptions-item>
        <el-descriptions-item label-align="right" align="left" label="负责人"><el-tag size="small">{{ activityData.detail.ownerName || 'N/A' }}</el-tag></el-descriptions-item>
        <el-descriptions-item label-align="right" align="left" label="活动预算"><span class="cost-text">{{ activityData.detail.cost ? `${activityData.detail.cost} 元` : 'N/A' }}</span></el-descriptions-item>
        <el-descriptions-item label-align="right" align="left" label="开始时间">{{ activityData.detail.startTime || 'N/A' }}</el-descriptions-item>
        <el-descriptions-item label-align="right" align="left" label="结束时间">{{ activityData.detail.endTime || 'N/A' }}</el-descriptions-item>
        <el-descriptions-item label-align="right" align="left" label="活动描述" :span="2">{{ activityData.detail.description || '无' }}</el-descriptions-item>
      </el-descriptions>

      <el-descriptions :column="2" border title="系统信息" style="margin-top: 20px;">
        <el-descriptions-item label-align="right" align="left" label="创建时间">{{ activityData.detail.createTime || 'N/A' }}</el-descriptions-item>
        <el-descriptions-item label-align="right" align="left" label="创建人">{{ activityData.detail.createByName || 'N/A' }}</el-descriptions-item>
        <el-descriptions-item label-align="right" align="left" label="最后修改时间">{{ activityData.detail.editTime || 'N/A' }}</el-descriptions-item>
        <el-descriptions-item label-align="right" align="left" label="最后修改人">{{ activityData.detail.editByName || 'N/A' }}</el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">活动备注</el-divider>

      <div class="remark-add-box">
        <el-input
            v-model="newRemarkContent"
            :rows="3"
            type="textarea"
            placeholder="请输入备注内容..."
        />
        <el-button
            type="primary"
            @click="handleAddRemark"
            :disabled="!newRemarkContent.trim()"
            class="remark-add-button"
        >
          添加备注
        </el-button>
      </div>

      <el-empty v-if="!activityData.detail.remarkList || activityData.detail.remarkList.length === 0" description="暂无备注信息" />
      <el-timeline v-else style="margin-top: 20px;">
        <el-timeline-item
            v-for="remark in activityData.detail.remarkList"
            :key="remark.id"
            :timestamp="remark.createTime"
            placement="top"
        >
          <el-card>
            <p>{{ remark.noteContent }}</p>
            <small class="remark-meta">
              备注人: {{ remark.createByName || '未知' }}
            </small>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </el-card>
  </div>
</template>

<style scoped>
.page-container {
  padding: 20px;
}
.el-descriptions {
  margin-top: 20px;
}
.cost-text {
  color: #F56C6C;
  font-weight: bold;
}
.remark-add-box {
  display: flex;
  flex-direction: column;
  gap: 10px; /* 输入框和按钮之间的间距 */
  max-width: 600px;
}
.remark-add-button {
  align-self: flex-end; /* 按钮靠右对齐 */
}
.remark-meta {
  color: #909399;
  font-size: 12px;
  margin-top: 8px;
  display: block;
}
</style>