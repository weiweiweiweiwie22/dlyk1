//导入函数
import { createApp } from 'vue'


//导入样式,不需要写组件
// import './style.css'

import ElementPlus from 'element-plus'

import 'element-plus/dist/index.css'

//导入组件
import App from './App.vue'

import LoginView from './vue/LoginView.vue'

//创建app，运行一下'./App.vue'页面
createApp(LoginView)
    .use(ElementPlus)
    //挂载app，将运行结构放到id为app的标签下
    .mount('#app')
