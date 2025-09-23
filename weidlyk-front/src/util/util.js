import {ElMessage} from "element-plus";

 export function messageTip(msg,type){
    ElMessage({
        showClose: true,
        center: true,
        message: msg,
        type: type,
    })
}

