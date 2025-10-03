<template>
  <el-container>
    <el-aside :width="data.isCollapse ? '64px' : '200px'">

      <div class="menuTitle">
        @微宏客管理系统
      </div>
      <el-menu
          active-text-color="#ffd04b"
          background-color="#334157"
          class="el-menu-vertical-demo"
          :default-active="data.currentRouterPath"
          text-color="#fff"
          style="border-right: solid 0;"
          :collapse="data.isCollapse"
          :collapse-transition="false"
          :router="true"
          :unique-opened="true">

        <template v-for="menu in data.menuTree" :key="menu.id">

          <el-sub-menu v-if="menu.children && menu.children.length > 0" :index="menu.id.toString()">
            <template #title>
              <el-icon><component :is="menu.icon || 'Menu'" /></el-icon>
              <span>{{ menu.name }}</span>
            </template>
            <el-menu-item v-for="child in menu.children" :key="child.id" :index="child.url">
              <el-icon><component :is="child.icon || 'Menu'" /></el-icon>
              {{ child.name }}
            </el-menu-item>
          </el-sub-menu>

          <el-menu-item v-else :index="menu.url">
            <el-icon><component :is="menu.icon || 'Menu'" /></el-icon>
            <span>{{ menu.name }}</span>
          </el-menu-item>
        </template>
      </el-menu>
    </el-aside>

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

      <el-main>
        <router-view/>
      </el-main>

      <el-footer>@版权所有 2025-2099 微宏科技 成都工业学院</el-footer>
    </el-container>
  </el-container>
</template>

<script setup>
// 【核心修改 1】: 引入更多所需模块
import { onMounted, reactive, watch } from "vue";
import { useRoute } from "vue-router";
import { doGet, doPost } from "../http/httpRequest.js";
import { messageConfirm, messageTip, removeHistoryToken } from "../util/util.js";
// 引入所有需要的图标
import {
  FullScreen,
  OfficeBuilding,
  Operation,
  User,
  Location,
  ArrowDown,
  Menu // 引入一个默认图标
} from "@element-plus/icons-vue";
import {setUserPermissions} from "../util/permission.js";

const route = useRoute();

const data = reactive({
  isCollapse: false,
  user: {},
  currentRouterPath: '',
  menuTree: [], // 新增：用于存储树形结构的菜单
});

/**
 * 辅助函数：将后端返回的扁平列表转换为树形结构
 * @param menuList 扁平的菜单列表
 * @returns {[]} 树形结构的菜单
 */
const buildMenuTree = (menuList) => {
  const map = {};
  const tree = [];
  // 首先，将所有菜单项放入map中，方便通过id快速查找
  menuList.forEach(item => {
    // parentId 是数据库字段名，在JS中通常是驼峰 parentId
    // 如果你的Permission实体类中是 parent_id，请在这里改为 item.parent_id
    map[item.id] = { ...item, children: [] };
  });

  // 然后，遍历map，将每个菜单项放入其父菜单的children数组中
  for (const item of Object.values(map)) {
    // 同样，这里也可能是 item.parent_id
    if (item.parentId && item.parentId !== 0) {
      if (map[item.parentId]) {
        map[item.parentId].children.push(item);
      }
    } else {
      // parentId为0的是顶级菜单
      tree.push(item);
    }
  }
  return tree;
};


const showMenu = () => {
  data.isCollapse = !data.isCollapse;
};

const updateActiveMenu = (path) => {
  const pathParts = path.split('/');
  if (pathParts.length > 3) {
    data.currentRouterPath = pathParts.slice(0, 3).join('/');
  } else {
    data.currentRouterPath = path;
  }
};

onMounted(() => {
  loadLoginUser();
  updateActiveMenu(route.path);
  // 新增：页面加载时，获取并构建菜单
  loadAndBuildMenus();
});

watch(() => route.path, (newPath) => {
  updateActiveMenu(newPath);
});

/**
 * 从后端加载菜单数据，并构建成树
 */
const loadAndBuildMenus = async () => {
  try {
    const res = await doGet('/api/permission/menus');
    if (res.data.code === 200) {
      // 将后端返回的扁平列表转换为树形结构
      data.menuTree = buildMenuTree(res.data.data);
    }
  } catch (error) {
    messageTip("菜单加载失败", "error");
  }
};

const loadLoginUser = () => {
  doGet('/api/login/info').then(res => {
    if (res.data.code === 200) {
      data.user = res.data.data;

      // 2. 关键：当获取到用户信息时，立刻用 permissionList 初始化全局权限
      // 这里的 permissionList 字段名需要和你 User 实体类中返回给前端的字段名一致
      setUserPermissions(res.data.data.permissionList || []);
    }
  });
};

const logout = () => {
  doPost('/api/logout', {}).then(res => {
    if (res.data.code === 200) {
      removeHistoryToken();
      messageTip("退出成功", "success")
      window.location.href = "/";
    } else {
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
/* 样式无需改动 */
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