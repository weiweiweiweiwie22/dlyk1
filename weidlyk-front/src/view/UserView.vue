<script setup>


import {doDelete, doGet, doPost} from "../http/httpRequest.js";
import {nextTick, onMounted, reactive, ref} from "vue";
import {useRouter} from "vue-router";
import {messageTip} from "../util/util.js";
import {ElMessageBox} from "element-plus";


// data 和 pagination 保持不变
const data = reactive({
  userList: [],
  selectedUserList: [],
  total: 0,
  dialogVisible : false,
  userQuery:{
    loginAct: "",
    loginPwd: "",
    name: "",
    phone: "",
    email: "",
    accountNoExpired: "",
    credentialsNoExpired: "",
    accountNoLocked: "",
    accountEnabled: "",
  },
  addUserRules: {
    loginAct: [
      { required: true, message: '请输入账号', trigger: 'blur' },
    ],
    loginPwd: [
      { required: true, message: '请输入密码', trigger: 'blur' },
      { min: 6, max:16, message: '密码长度请在6-16位之间', trigger: 'blur' },
    ],
    name: [
      { required: true, message: '请输入姓名', trigger: 'blur' },
      { min: 2, max:6, message: '姓名长度请在2-6位之间', trigger: 'blur' },
      { pattern: /^[\u4e00-\u9fa5]+$/, message: '姓名只能包含中文', trigger: 'blur' },
    ],
    phone: [
      { required: true, message: '请输入手机号', trigger: 'blur' },
      { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur'},
    ],
    email:[
      { required: true, message: '请输入邮箱', trigger: 'blur' },
      { pattern: /^([A-Za-z0-9_\-\.])+@([A-Za-z0-9_\-\.])+\.[A-Za-z]{2,4}$/, message: '请输入正确的邮箱', trigger: 'blur'},
    ],
    accountNoExpired: [
      { required: true, message: '请选择账号是否过期', trigger: 'blur' },
    ],
    credentialsNoExpired: [
      { required: true, message: '请选择密码是否过期', trigger: 'blur' },
    ],
    accountNoLocked: [
      { required: true, message: '请选择账号是否锁定', trigger: 'blur' },
    ],
    accountEnabled: [
      { required: true, message: '请选择账号是否启用', trigger: 'blur' },
    ]
  }
});

const pagination = reactive({
  current: 1, // 当前页码
  size: 10    // 每页条数
});

onMounted(() => {
  // 页面加载时获取第一页数据
  getUserList();
});

const getUserList = () => {
  doGet('/api/users', {
    current: pagination.current,
    size: pagination.size
  }).then(res => {
    if (res.data.code === 200) {
      data.userList = res.data.data.records;
      data.total = res.data.data.total;

      // ✅ 关键：数据加载后，等待DOM更新，然后恢复本页的勾选状态
      nextTick(() => {
        if (tableRef.value) {
          data.userList.forEach(user => {
            if (selectionMap.value.has(user.id)) {
              tableRef.value.toggleRowSelection(user, true);
            }
          });
        }
      });
    }
  });
}

// ✅ 新增 handlePageChange 方法，用于处理页码改变事件
const handlePageChange = (newPage) => {
  // 1. 更新当前页码
  pagination.current = newPage;
  // 2. 重新请求数据
  getUserList();
};

// ✅ 新增：处理“单行勾选/取消勾选”的事件
const handleSelect = (selection, row) => {
  const isSelected = selection.some(item => item.id === row.id);
  if (isSelected) {
    // 如果是勾选
    selectionMap.value.set(row.id, row);
  } else {
    // 如果是取消勾选
    selectionMap.value.delete(row.id);
  }
};

// ✅ 新增：处理“全选/全不选”的事件
const handleSelectAll = (selection) => {
  if (selection.length > 0) {
    // 本页全选
    data.userList.forEach(user => selectionMap.value.set(user.id, user));
  } else {
    // 本页全不选
    data.userList.forEach(user => selectionMap.value.delete(user.id));
  }
};

const router = useRouter();

// 4. --- 修改 delSelected 函数 ---
const delSelected = () => {
  // ✅ 从我们自己的 Map 中获取选中数量
  const selectedCount = selectionMap.value.size;
  if (selectedCount === 0) {
    messageTip("请选择要删除的用户", "warning");
    return;
  }

  ElMessageBox.confirm(
      `此操作将永久删除选中的 ${selectedCount} 个用户, 是否继续?`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
  ).then(() => {
    // ✅ 从 Map 的 key 中提取所有选中用户的 ID
    const ids = Array.from(selectionMap.value.keys());

    doPost('/api/user/delete/batch', ids).then(res => {
      if (res.data.code === 200) {
        messageTip("删除成功", "success");
        // ✅ 关键：删除成功后，清空我们的“选择集”
        selectionMap.value.clear();
        getUserList();
      } else {
        messageTip(res.data.message || "删除失败", "error");
      }
    })
  }).catch(() => {
    messageTip("已取消删除", "info");
  });
};

const del = (id) => {
  console.log(id);
  ElMessageBox.confirm(
    '此操作将永久删除该用户, 是否继续?',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    doDelete('/api/user/delete/' + id).then(res => {
      if (res.data.code === 200) {
        messageTip("删除成功", "success");
        getUserList();
      } else {
        messageTip(res.data.message || "删除失败", "error");
      }
    })
  })
}

const edit = (id) => {
  console.log(id);
  // 跳转到用户详情页面
  router.push("/dashboard/user/edit/" + id);
};

const view = (id) => {
  console.log(id);
  // 跳转到用户详情页面
  router.push("/dashboard/user/" + id);
};

const add = () => {
  data.dialogVisible = true;
};

const submit = async () => {
  try {
    // 1. 使用 await 等待校验完成
    await addUserFormRef.value.validate();

    // 2. 校验通过后，直接发送 data.userQuery 这个 JS 对象
    doPost('/api/user/add', data.userQuery).then(res => {
      if (res.data.code === 200) {
        messageTip("新增成功", "success");
        data.dialogVisible = false;
        getUserList();
      } else {
        messageTip(res.data.message || "新增失败", "error");
      }
    });
  } catch (error) {
    messageTip("请检查输入项！", "warning");
  }
};

const addUserFormRef = ref(null);

const tableRef = ref(null);

const selectionMap = ref(new Map());
</script>

<template>
  <el-button type="primary" @click=add>新增用户</el-button>

<!--  新增用户-->
  <el-dialog v-model="data.dialogVisible" title="新增用户" width="500" center draggable>
<!--    表单内容-->
    <el-form ref="addUserFormRef" :model="data.userQuery" label-width="85px" :rules="data.addUserRules">
      <el-form-item label="账号" prop="loginAct">
        <el-input v-model="data.userQuery.loginAct" />
      </el-form-item>

      <el-form-item label="密码" prop="loginPwd">
        <el-input  v-model="data.userQuery.loginPwd" />
      </el-form-item>

      <el-form-item label="姓名" prop="name">
        <el-input  v-model="data.userQuery.name" />
      </el-form-item>

      <el-form-item label="手机号" prop="phone">
        <el-input  v-model="data.userQuery.phone" />
      </el-form-item>

      <el-form-item label="邮箱" prop="email">
        <el-input  v-model="data.userQuery.email" />
      </el-form-item>

      <el-form-item label="账号过期" prop="accountNoExpired">
        <el-select v-model="data.userQuery.accountNoExpired" placeholder="请选择" style="width: 100%">
          <el-option label="未过期" :value="1"/>
          <el-option label="已过期" :value="0"/>
        </el-select>
      </el-form-item>

      <el-form-item label="密码过期" prop="credentialsNoExpired">
        <el-select v-model="data.userQuery.credentialsNoExpired" placeholder="请选择" style="width: 100%">
          <el-option label="未过期" :value="1"/>
          <el-option label="已过期" :value="0"/>
        </el-select>
      </el-form-item>

      <el-form-item label="账号锁定" prop="accountNoLocked">
        <el-select v-model="data.userQuery.accountNoLocked" placeholder="请选择" style="width: 100%">
          <el-option label="未锁定" :value="1"/>
          <el-option label="已锁定" :value="0"/>
        </el-select>
      </el-form-item>

      <el-form-item label="账号状态" prop="accountEnabled">
        <el-select v-model="data.userQuery.accountEnabled" placeholder="请选择" style="width: 100%">
          <el-option label="未启用" :value="0"/>
          <el-option label="已启用" :value="1"/>
        </el-select>
      </el-form-item>

    </el-form>
<!--    下方的按钮-->
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="data.dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit">提交</el-button>
      </div>
    </template>
  </el-dialog>

  <el-button type="danger" @click="delSelected" :disabled="selectionMap.size === 0">批量删除</el-button>
  <div class="selection-info" v-if="selectionMap.size > 0">
    <el-alert :title="'已选择 ' + selectionMap.size + ' 项'" type="info" show-icon :closable="false" />
  </div>
  <el-table
      ref="tableRef"
      :data="data.userList"
      :row-key="user => user.id"
      @select="handleSelect"
      @select-all="handleSelectAll"
      border
      stripe
  >

    <el-table-column type="selection" width="55" />

    <el-table-column type="index" label="序号" width="80" align="center"></el-table-column>

    <el-table-column property="loginAct" label="账号" show-overflow-tooltip />

    <el-table-column property="name" label="姓名" show-overflow-tooltip/>

    <el-table-column property="phone" label="手机"  show-overflow-tooltip/>

    <el-table-column property="email" label="邮箱" show-overflow-tooltip/>

    <el-table-column property="createTime" label="创建时间"  show-overflow-tooltip/>

    <el-table-column label="操作" width="250" align="center">
      <template #default="scope">
        <el-button type="primary" plain @click="view(scope.row.id)">详情</el-button>
        <el-button type="success" plain @click="edit(scope.row.id)">修改</el-button>
        <el-button type="danger" plain @click="del(scope.row.id)">删除</el-button>
      </template>
    </el-table-column>


  </el-table>

  <el-pagination
      background
      layout="prev, pager, next"
      :total="data.total"
      :page-size="pagination.size"
      :current-page="pagination.current"
      @current-change="handlePageChange"
  />

</template>

<style scoped>
.el-pagination{
  margin-top: 12px;
  text-align: center;
}
.el-table{
  margin-top: 12px;
}
.selection-info {
  margin-top: 12px;
}
</style>