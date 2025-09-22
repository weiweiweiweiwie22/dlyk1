

<template>
  <el-container>
    <!--左侧栏目-->
    <el-aside width="200px">
      <img src="../assets/loginBox.svg" alt="logo">
      <p class="imgTitle">
        欢迎使用微宏客管理系统
      </p>
    </el-aside>
    <!--右侧栏目-->
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
          <el-checkbox label="记住我" name="rememberMe"/>
        </el-form-item>
      </el-form>
    </el-main>
  </el-container>
</template>

<script>
import {doPost} from "../http/httpRequest.js";

export default ({
  name: "LoginView",

  // 定义数据变量
  data(){
    return{
      user: {
        loginAct: "",
        loginPwd:"",
      },
      loginRules:{
        loginAct:[
            { required: true, message: '请输入账号', trigger: 'blur' },
        ],
        loginPwd:[
            { required: true, message: '请输入密码', trigger: 'blur' },
            { min: 6, max:16, message: '密码长度请在6-16位之间', trigger: 'blur' },
        ],
      }

    }
  },

  //定义函数
  methods:{
    login(){
      //提交前校验数据
      this.$refs.loginRefForm.validate((isValid)=>{
        if(isValid){

          let formData = new FormData();
          formData.append("loginAct",this.user.loginAct);
          formData.append("loginPwd",this.user.loginPwd);

          doPost('/api/login',formData).then((res)=>{
            console.log(res);
          });
        }
      })
    }
  }
})


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