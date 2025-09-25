import axios from "axios";
import {messageConfirm, messageTip, removeHistoryToken, tokenName} from "../util/util.js";
import {ElMessage, ElMessageBox} from "element-plus";


axios.defaults.baseURL = "http://localhost:8089";
axios.defaults.withCredentials = true; // 开启跨域携带 cookie

export function doGet(url,params) {
    return axios({
        method: 'get',
        url: url,
        params: params,
        dataType: 'json'
    })

}

export function doDelete(url,params) {
    axios({
        method: 'delete',
        url: url,
        params: params,
        dataType: 'json'
    }).then(function (response) {
        console.log(response);
    }).catch(function (error) {
        console.log(error);
    });

}

export function doPost(url,data) {
    return  axios({
        method: 'post',
        url: url,
        data: data,
        dataType: 'json'
    })

}

export function doPut(url,data) {
    axios({
        method: 'put',
        url: url,
        data: data,
        dataType: 'json'
    }).then(function (response) {
        console.log(response);
    }).catch(function (error) {
        console.log(error);
    });

}


// 添加请求拦截器
axios.interceptors.request.use(function (config) {
    // 在发送请求之前做些什么
    //请求头存放token
    let token =  window.localStorage.getItem(tokenName())
    if(!token){
        token = window.sessionStorage.getItem(tokenName())
    }
    if(token){
        config.headers.Authorization = token
    }
    return config;
}, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
});

// 添加响应拦截器
axios.interceptors.response.use(function (response) {
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么

    //token验证不通过
    if(response.data.code > 900) {
        {
            messageConfirm(response.data.message + ',是否重新登录？', 'warning')
                .then(() => {
                    //token验证失败，删除错误的token，跳转到登录页
                    removeHistoryToken();
                    window.location.href = '/'
                })
                .catch(() => {
                    messageTip('取消登录', 'warning');
                })
        }
        return Promise.reject(response.data);
    }
    return response;
}, function (error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    return Promise.reject(error);
});
