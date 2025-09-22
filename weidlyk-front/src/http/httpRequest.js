import axios from "axios";


axios.defaults.baseURL = "http://localhost:8089";

export function doGet(url,params) {
    axios({
        method: 'get',
        url: url,
        params: params,
        dataType: 'json'
    }).then(function (response) {
        console.log(response);
    }).catch(function (error) {
        console.log(error);
    });

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