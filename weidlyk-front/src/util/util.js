import {ElMessage, ElMessageBox} from "element-plus";

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
    window.localStorage.removeItem(tokenName())
}

//消息确认
export function messageConfirm(msg,type){
    return  ElMessageBox.confirm(
        msg,
        'Warning',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: "warning",
        }
    )
}