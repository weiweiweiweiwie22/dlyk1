<script setup>


import {doGet} from "../http/httpRequest.js";
import {onMounted, reactive} from "vue";


const data = reactive({
  userList: [],
});

onMounted(() => {
  getUserList();
});

const getUserList = (current) => {
  doGet('/api/users', {
    current:current,
  }).then(res => {
    if (res.data.code===200) {
      data.userList = res.data.data;
    }
  });
}

const handleSelectionChange = (val) => {
  console.log(val);
};


</script>

<template>
  <el-button type="primary">Primary</el-button>
  <el-button type="danger">Danger</el-button>

  <el-table
      :data="data.userList"
      style="width: 100%"
      @selection-change="handleSelectionChange"
  >

    <el-table-column type="selection" width="55" />

    <el-table-column label="序号" ></el-table-column>

    <el-table-column property="账号" label="账号" show-overflow-tooltip />

    <el-table-column property="姓名" label="姓名" show-overflow-tooltip/>

    <el-table-column property="手机" label="手机"  show-overflow-tooltip/>

    <el-table-column property="邮箱" label="邮箱" show-overflow-tooltip/>

    <el-table-column property="创建时间" label="创建时间"  show-overflow-tooltip/>

    <el-table-column label="操作">

    </el-table-column>

  </el-table>
</template>

<style scoped>

</style>