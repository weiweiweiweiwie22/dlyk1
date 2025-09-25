<template>
  <el-container>
<!--    左侧菜单-->
    <el-aside :width="data.isCollapse ? '64px' : '200px'">

      <div class="menuTitle">
        @微宏客管理系统
      </div>
      <el-menu
          active-text-color="#ffd04b"
          background-color="#334157"
          class="el-menu-vertical-demo"
          default-active="2"
          text-color="#fff"
          style="border-right: solid 0;"
          :collapse="data.isCollapse"
          :collapse-transition="false"
          :router="true"
          :unique-opened="true">

        <el-sub-menu index="1">
          <template #title>
            <el-icon><OfficeBuilding /></el-icon>
            <span>市场活动</span>
          </template>
            <el-menu-item index="1-1">
              <el-icon><FullScreen /></el-icon>
              市场活动
            </el-menu-item>
            <el-menu-item index="1-2">
              <el-icon><PieChart /></el-icon>
              市场统计
            </el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="2">
          <template #title>
            <el-icon><Operation /></el-icon>
            <span>线索管理</span>
          </template>

          <el-menu-item index="1-1">线索管理</el-menu-item>

        </el-sub-menu>

        <el-sub-menu index="3">
          <template #title>
            <el-icon><User /></el-icon>
            <span>客户管理</span>
          </template>

          <el-menu-item index="1-1">客户管理</el-menu-item>

        </el-sub-menu>

        <el-sub-menu index="4">
          <template #title>
            <el-icon><location /></el-icon>
            <span>交易管理</span>
          </template>

          <el-menu-item index="1-1">客户管理</el-menu-item>

        </el-sub-menu>

        <el-sub-menu index="5">
          <template #title>
            <el-icon><location /></el-icon>
            <span>产品管理</span>
          </template>

          <el-menu-item index="1-1">客户管理</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="6">
          <template #title>
            <el-icon><location /></el-icon>
            <span>字典管理</span>
          </template>

          <el-menu-item index="1-1">客户管理</el-menu-item>

        </el-sub-menu>

        <el-sub-menu index="7">
          <template #title>
            <el-icon><location /></el-icon>
            <span>用户管理</span>
          </template>

          <el-menu-item index="/dashboard/user">用户管理</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="8">
          <template #title>
            <el-icon><location /></el-icon>
            <span>系统管理</span>
          </template>

          <el-menu-item index="1-1">客户管理</el-menu-item>
          <el-menu-item index="1-2">客户管理</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>

<!--    右侧显示区域-->
    <el-container class="right">
      <el-header>
        <el-icon class="show" @click="showMenu"><Operation /></el-icon>

        <el-dropdown>
    <span class="el-dropdown-link">
      {{ data.user.name }}
      <el-icon class="el-icon--right">
        <arrow-down />
      </el-icon>
    </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>我的资料</el-dropdown-item>
              <el-dropdown-item>修改密码</el-dropdown-item>
              <el-dropdown-item divided @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>

      </el-header>
<!--      中间-->
      <el-main>
        <router-view/>
      </el-main>

      <el-footer>@版权所有 2025-2099 微宏科技 成都工业学院</el-footer>
    </el-container>
  </el-container>
</template>

<script setup>

import {FullScreen, OfficeBuilding, Operation, PieChart} from "@element-plus/icons-vue";
import {onMounted, reactive} from "vue";
import {doGet, doPost} from "../http/httpRequest.js";
import {messageConfirm, messageTip, removeHistoryToken} from "../util/util.js";

const data = reactive({
  isCollapse: false,
  user: {},

});
const showMenu = () => {
  data.isCollapse = !data.isCollapse;
};

onMounted(() => {
  loadLoginUser() ;
});


const loadLoginUser = () => {
  doGet('/api/login/info').then(res => {
    console.log(res);
    data.user = res.data.data;
  });
};

const logout = () => {
  doPost('/api/logout',{}).then(res => {
    if (res.data.code === 200){
      removeHistoryToken();
      messageTip("退出成功","success")
      window.location.href = "/";
    }else {
      messageConfirm(res.data.message + '出现错误,是否强行退出？', 'warning')
        .then(() => {
          removeHistoryToken();
          window.location.href = "/";
        })
        .catch(() => {
          messageTip('取消退出', 'warning');
        })
    }
  })
}
</script>


<style scoped>

.el-aside{
  background-color: #1a1a1a;
}
.el-header{
  background-color:azure;
  line-height: 35px;
  height: 35px;
}
.el-footer{
  background-color:aliceblue;
  height: 35px;
  text-align: center;
  line-height: 35px;
}
.right{
  height: calc(100vh);
}
.menuTitle{
  text-align: center;
  color: white;
  height: 35px;
  line-height: 35px;
}
.show svg {
  cursor: pointer;
}
.el-dropdown{
  height: 35px;
  line-height: 35px;
  float: right;
}
</style>