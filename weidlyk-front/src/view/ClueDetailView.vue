<script setup>
import { onMounted, reactive, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { messageTip } from "../util/util.js";
import { doGet, doPost } from "../http/httpRequest.js";

// --- 状态定义 ---
const route = useRoute();
const router = useRouter();

// 用于存储线索详情
const clueData = reactive({
  detail: {
    remarkList: []
  }
});

// 用于跟踪记录功能的状态
const noteWayList = ref([]);
const newRemarkContent = ref('');
const newRemarkNoteWay = ref(null);

// V V V 新增：用于“转换客户”功能的状态 V V V
const convertDialogVisible = ref(false); // 控制转换弹窗的显示
const convertFormRef = ref(null); // 转换表单的引用
const productList = ref([]); // 存储产品列表
const convertForm = reactive({ // 转换表单的数据模型
  productId: null,
  description: '',
  nextContactTime: ''
});
const convertRules = reactive({ // 转换表单的校验规则
  productId: [
    { required: true, message: '请选择意向产品', trigger: 'change' }
  ],
  description: [
    { max: 255, message: '客户描述不能超过255个字符', trigger: 'blur' }
  ],
  nextContactTime: [
    { required: true, message: '请选择下次联系时间', trigger: 'change' }
  ]
});
// ^ ^ ^ 新增 ^ ^ ^


// --- 生命周期钩子 ---
onMounted(() => {
  loadClueDetail();
  loadNoteWayList();
  loadProductList(); // V V V 新增：页面加载时，获取产品列表 V V V
});


// --- 方法定义 ---

/**
 * 加载线索详情的核心方法
 */
const loadClueDetail = () => {
  const clueId = route.params.id;
  if (!clueId) {
    messageTip('无效的线索ID', 'error');
    router.back();
    return;
  }
  doGet(`/api/clue/detail/${clueId}`).then(res => {
    if (res.data.code === 200) {
      res.data.data.remarkList = res.data.data.remarkList || [];
      clueData.detail = res.data.data;
    } else {
      messageTip(res.data.message || '获取线索详情失败', 'error');
    }
  });
};

/**
 * 加载“跟踪方式”列表
 */
const loadNoteWayList = () => {
  doGet('/api/noteWay/all').then(res => {
    if (res.data.code === 200) {
      noteWayList.value = res.data.data;
    }
  });
};

/**
 * V V V 新增：加载产品列表，用于下拉框 V V V
 */
const loadProductList = () => {
  // 【API 依赖】: 确保你有一个后端接口可以返回所有产品的简化列表
  doGet('/api/intentionProduct/all').then(res => {
    if (res.data.code === 200) {
      productList.value = res.data.data;
    }
  });
};

/**
 * 处理添加跟踪记录的逻辑
 */
const handleAddRemark = async () => {
  if (!newRemarkNoteWay.value) {
    messageTip('请选择跟踪方式', 'warning');
    return;
  }
  if (!newRemarkContent.value.trim()) {
    messageTip('跟踪内容不能为空', 'warning');
    return;
  }
  const payload = {
    clueId: route.params.id,
    noteWay: newRemarkNoteWay.value,
    noteContent: newRemarkContent.value
  };
  try {
    const res = await doPost('/api/clue/remark/add', payload);
    if (res.data.code === 200) {
      messageTip('添加成功', 'success');
      newRemarkContent.value = '';
      newRemarkNoteWay.value = null;
      loadClueDetail();
    } else {
      messageTip(res.data.message || '添加失败', 'error');
    }
  } catch (error) {
    messageTip('请求异常，请稍后重试', 'error');
  }
};

/**
 * V V V 新增：打开“转换客户”弹窗 V V V
 */
const handleOpenConvertDialog = () => {
  // 重置表单内容
  convertForm.productId = null;
  convertForm.description = '';
  convertForm.nextContactTime = '';
  // 显示弹窗
  convertDialogVisible.value = true;
};

/**
 * V V V 新增：提交“转换客户”表单 V V V
 */
const handleSubmitConversion = async () => {
  if (!convertFormRef.value) return;
  try {
    await convertFormRef.value.validate();

    const payload = {
      clueId: route.params.id,
      product: convertForm.productId,
      description: convertForm.description,
      nextContactTime: convertForm.nextContactTime
    };

    // 【API 依赖】: 确保你有一个后端接口处理线索转换的逻辑
    const res = await doPost('/api/clue/convert', payload);
    if (res.data.code === 200) {
      messageTip('转换成功', 'success');
      convertDialogVisible.value = false;
      // 转换成功后，通常会跳转到客户列表或线索列表
      router.push('/dashboard/clue');
    } else {
      messageTip(res.data.message || '转换失败', 'error');
    }
  } catch (error) {
    messageTip('请检查必填项', 'warning');
  }
};


/**
 * 返回到上一个页面
 */
const goBack = () => {
  router.back();
};
</script>

<template>
  <div class="page-container">
    <el-card shadow="never">
      <el-page-header @back="goBack" content="线索详情">
        <template #extra>
          <div class="page-header-actions">
            <el-button type="success" @click="handleOpenConvertDialog" v-if="clueData.detail.state !== '已转客户'">转换为客户</el-button>
          </div>
        </template>
      </el-page-header>
      <el-divider />

      <el-descriptions :column="2" border title="线索基础信息">
        <el-descriptions-item label-align="right" align="left" label="线索ID">{{ clueData.detail.id || 'N/A' }}</el-descriptions-item>
        <el-descriptions-item label-align="right" align="left" label="姓名">{{ clueData.detail.fullName || 'N/A' }}</el-descriptions-item>
        <el-descriptions-item label-align="right" align="left" label="负责人"><el-tag size="small">{{ clueData.detail.ownerName || 'N/A' }}</el-tag></el-descriptions-item>
        <el-descriptions-item label-align="right" align="left" label="所属活动">{{ clueData.detail.activityName || 'N/A' }}</el-descriptions-item>
        <el-descriptions-item label-align="right" align="left" label="手机">{{ clueData.detail.phone || 'N/A' }}</el-descriptions-item>
        <el-descriptions-item label-align="right" align="left" label="微信">{{ clueData.detail.weixin || 'N/A' }}</el-descriptions-item>
        <el-descriptions-item label-align="right" align="left" label="称呼">{{ clueData.detail.appellation || 'N/A' }}</el-descriptions-item>
        <el-descriptions-item label-align="right" align="left" label="是否贷款">{{ clueData.detail.needLoan || 'N/A' }}</el-descriptions-item>
        <el-descriptions-item label-align="right" align="left" label="意向状态">{{ clueData.detail.intentionState || 'N/A' }}</el-descriptions-item>
        <el-descriptions-item label-align="right" align="left" label="意向产品">{{ clueData.detail.intentionProduct || 'N/A' }}</el-descriptions-item>
        <el-descriptions-item label-align="right" align="left" label="线索状态">
          <el-tag type="warning" size="small">{{ clueData.detail.state || 'N/A' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label-align="right" align="left" label="线索来源">{{ clueData.detail.source || 'N/A' }}</el-descriptions-item>
        <el-descriptions-item label-align="right" align="left" label="下次联系时间">{{ clueData.detail.nextContactTime || 'N/A' }}</el-descriptions-item>
        <el-descriptions-item label-align="right" align="left" label="线索描述" :span="2">{{ clueData.detail.description || '无' }}</el-descriptions-item>
      </el-descriptions>

      <el-descriptions :column="2" border title="系统信息" style="margin-top: 20px;">
        <el-descriptions-item label-align="right" align="left" label="创建时间">{{ clueData.detail.createTime || 'N/A' }}</el-descriptions-item>
        <el-descriptions-item label-align="right" align="left" label="创建人">{{ clueData.detail.createBy || 'N/A' }}</el-descriptions-item>
        <el-descriptions-item label-align="right" align="left" label="最后修改时间">{{ clueData.detail.editTime || 'N/A' }}</el-descriptions-item>
        <el-descriptions-item label-align="right" align="left" label="最后修改人">{{ clueData.detail.editBy || 'N/A' }}</el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">跟踪记录</el-divider>

      <div class="remark-add-box">
        <el-select v-model="newRemarkNoteWay" placeholder="请选择跟踪方式" style="width: 100%;">
          <el-option
              v-for="item in noteWayList"
              :key="item.id"
              :label="item.typeValue"
              :value="item.id"
          />
        </el-select>
        <el-input
            v-model="newRemarkContent"
            :rows="3"
            type="textarea"
            placeholder="请输入跟踪内容..."
        />
        <el-button
            type="primary"
            @click="handleAddRemark"
            :disabled="!newRemarkContent.trim() || !newRemarkNoteWay"
            class="remark-add-button"
        >
          添加记录
        </el-button>
      </div>

      <el-empty v-if="!clueData.detail.remarkList || clueData.detail.remarkList.length === 0" description="暂无跟踪记录" />
      <el-timeline v-else style="margin-top: 20px;">
        <el-timeline-item
            v-for="remark in clueData.detail.remarkList"
            :key="remark.id"
            :timestamp="remark.createTime"
            placement="top"
        >
          <el-card>
            <template #header>
              <div class="card-header">
                <span>跟踪方式: <strong>{{ remark.noteWay }}</strong></span>
              </div>
            </template>
            <p>{{ remark.noteContent }}</p>
            <small class="remark-meta">
              记录人: {{ remark.createBy || '未知' }}
            </small>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </el-card>

    <el-dialog v-model="convertDialogVisible" title="转换为客户" width="500px" center>
      <el-form ref="convertFormRef" :model="convertForm" :rules="convertRules" label-width="100px">
        <el-form-item label="意向产品" prop="productId">
          <el-select v-model="convertForm.productId" placeholder="请选择客户意向产品" style="width: 100%;">
            <el-option
                v-for="product in productList"
                :key="product.id"
                :label="product.typeValue"
                :value="product.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="客户描述" prop="description">
          <el-input v-model="convertForm.description" type="textarea" :rows="3" placeholder="请输入客户描述" />
        </el-form-item>
        <el-form-item label="下次联系时间" prop="nextContactTime">
          <el-date-picker v-model="convertForm.nextContactTime" type="datetime" placeholder="选择下次联系时间" style="width: 100%;" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="convertDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmitConversion">
            提 交
          </el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<style scoped>
/* 样式无重大改动，仅为 page-header-actions 添加 flex 布局 */
.page-container {
  padding: 20px;
}
.el-descriptions {
  margin-top: 20px;
}
.remark-add-box {
  display: flex;
  flex-direction: column;
  gap: 10px;
  max-width: 600px;
  margin-bottom: 20px;
}
.remark-add-button {
  align-self: flex-end;
}
.remark-meta {
  color: #909399;
  font-size: 12px;
  margin-top: 8px;
  display: block;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.page-header-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}
</style>