<template>
  <el-container>
    <el-aside width="200px">
      <img src="../assets/loginBox.svg" alt="logo">
      <p class="imgTitle">
        欢迎使用微宏客管理系统
      </p>
    </el-aside>
    <el-main>
      <div class="loginTitle">
        欢迎登陆
      </div>

      <el-form ref="loginRefForm" :model="user" label-width="120px" :rules="loginRules">
        <el-form-item label="账号" prop="loginAct">
          <el-input v-model="user.loginAct" />
        </el-form-item>

        <el-form-item label="密码" prop="loginPwd">
          <el-input type="password" v-model="user.loginPwd" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="login">登 录</el-button>
        </el-form-item>

        <el-form-item>
          <el-checkbox label="记住我" name="rememberMe" v-model="user.rememberMe"/>
        </el-form-item>
      </el-form>
    </el-main>
  </el-container>
</template>

<script setup>
import {onMounted, reactive, ref} from 'vue'; // 1. 导入 reactive 和 ref
import {doGet, doPost} from "../http/httpRequest.js";
import {ElMessage} from "element-plus";
import {messageTip, removeHistoryToken, tokenName} from "../util/util.js";



// 2. 定义表单的 ref 引用
const loginRefForm = ref(null);

// 3. 使用 reactive 来定义响应式数据，替代原来的 data()
const user = reactive({
  loginAct: "",
  loginPwd: "",
  rememberMe: false,
});

const loginRules = reactive({
  loginAct:[
    { required: true, message: '请输入账号', trigger: 'blur' },
  ],
  loginPwd:[
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max:16, message: '密码长度请在6-16位之间', trigger: 'blur' },
  ],
});

// 4. 直接定义函数，替代原来的 methods
const login = async () => {
  try {
    // 使用 await 等待校验完成
    await loginRefForm.value.validate();

    // 校验成功，继续执行
    let formData = new FormData();
    formData.append("loginAct", user.loginAct);
    formData.append("loginPwd", user.loginPwd);
    formData.append("rememberMe", user.rememberMe);

    // 使用 await 等待请求完成
    const res = await doPost('/api/login', formData);

    if (res.data.code === 200) {
      messageTip("登陆成功", "success");
      removeHistoryToken();

      if (user.rememberMe) {
        window.localStorage.setItem(tokenName(), res.data.data);
      } else {
        window.sessionStorage.setItem(tokenName(), res.data.data);
      }
      window.location.href = "/dashboard";
    } else {
      messageTip(res.data.message || "登陆失败", "error");
    }
  } catch (error) {
    // 统一处理校验失败或请求失败
    console.log('登录流程出错', error);
    messageTip("请检查账号和密码", "warning");
  }
}

//免登录
onMounted(() => {
  freeLogin();
});

// 免登录
const freeLogin = () => {
  let token = window.localStorage.getItem(tokenName());
  if (token){
    doGet('/api/login/free',{}).then(res => {
      if (res.data.code === 200){
        window.location.href = "/dashboard";
      }
    })
  }
}
</script>

<style scoped>
.el-aside {
  background-color: black;
  width: 40%;
  text-align: center;
}
.el-main {
  background-color: white;
  height: calc(100vh);
}
img{
  height: 400px;
}
.imgTitle{
  color: white;
  font-size: 28px;
}
.el-form{
  width: 60%;
  margin: auto;
}
.loginTitle{
  text-align: center;
  margin-top: 100px;
  margin-bottom: 15px;
  font-size: 20px;
  font-weight: bold;
}
.el-button{
  width: 100%;
}
</style>