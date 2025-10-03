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
            ,
            children:[
                {
                    path:'',
                    name:'dashboardTotal',
                    component:()=>import('../view/StatisticView.vue')
                },
                {
                    path:'user',
                    name:'user',
                    component:()=>import('../view/UserView.vue')
                },
                {
                    path:"user/:id",
                    name:'userDetail',
                    component:()=>import('../view/UserDetailView.vue')
                },
                {
                    path:"user/edit/:id",
                    name:'userEdit',
                    component:()=>import('../view/UserInfoEditView.vue')
                },
                {
                    path:"activity",
                    name:'activity',
                    component:()=>import('../view/ActivityView.vue')
                },
                {
                    path:"activity/:id",
                    name:'activityDetail',
                    component:()=>import('../view/ActivityDetailView.vue')
                },
                {
                    path:"activity/edit/:id",
                    name:'activityEdit',
                    component:()=>import('../view/ActivityEditView.vue')
                },
                {
                    path:"clue",
                    name:'clue',
                    component:()=>import('../view/ClueView.vue')
                },
                {
                    path:"clue/:id",
                    name:'clueDetail',
                    component:()=>import('../view/ClueDetailView.vue')
                },
                {
                    path:"clue/edit/:id",
                    name:'clueEdit',
                    component:()=>import('../view/ClueEditView.vue')
                },
                {
                    path:"customer",
                    name:'customer',
                    component:()=>import('../view/CustomerView.vue')
                },
            ]
        },

    ]
     })

export default router
