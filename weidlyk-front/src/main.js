//导入函数
import { createApp } from 'vue'


//导入样式,不需要写组件
// import './style.css'

import ElementPlus from 'element-plus'

import 'element-plus/dist/index.css'

import router from "./router/router.js";

//导入组件
import App from './App.vue'

import LoginView from './view/LoginView.vue'

import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App);

for (const [key, component] of Object.entries(ElementPlusIconsVue)){
    app.component(key, component)
}

//创建app，运行一下'./App.vue'页面

 app.use(ElementPlus)
    .use( router)
    //挂载app，将运行结构放到id为app的标签下
    .mount('#app')
