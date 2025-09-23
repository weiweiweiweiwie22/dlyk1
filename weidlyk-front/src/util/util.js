import {ElMessage} from "element-plus";

//消息提示
 export function messageTip(msg,type){
    ElMessage({
        showClose: true,
        center: true,
        message: msg,
        type: type,
    })
}

//获取token名字
export function tokenName(){
    return "weidlyk_token";
}

//删除历史token
export function removeHistoryToken(){
    window.sessionStorage.removeItem(tokenName())
    window.sessionStorage.removeItem(tokenName())
}
