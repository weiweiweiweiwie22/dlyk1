import { createRouter, createWebHistory } from 'vue-router'


// 定义变量
const router = createRouter({
    //历史路由
    history: createWebHistory(),
    //路由
    routes: [
        {
            path: '/',
            name: 'login',
            component: () => import('../view/LoginView.vue')
        },
        {
            path: '/dashboard',
            name: 'dashboard',
            component: () => import('../view/DashboardView.vue')
        },

    ]
     })

export default router
