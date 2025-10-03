// src/util/permission.js

import { ref } from 'vue';

// 使用一个响应式的 Set 来存储权限码，查询效率更高 (O(1))
const userPermissions = ref(new Set());

/**
 * 在用户登录或刷新后，设置当前用户的权限码列表
 * @param {string[]} perms 从后端获取的权限码数组
 */
export function setUserPermissions(perms) {
    if (perms && perms.length > 0) {
        userPermissions.value = new Set(perms);
    } else {
        userPermissions.value = new Set();
    }
}

/**
 * 检查用户是否拥有某个权限
 * @param {string} code 需要检查的权限码，例如 'clue:add'
 * @returns {boolean}
 */
export function hasPermission(code) {
    return userPermissions.value.has(code);
}