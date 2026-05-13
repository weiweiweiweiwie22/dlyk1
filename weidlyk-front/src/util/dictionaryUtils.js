/**
 * 字典管理工具函数
 * 提供字典值的获取、缓存等功能
 */

import { ref, reactive } from 'vue';
import { doGet } from '../http/httpRequest.js';

// 字典缓存
const dictionaryCache = reactive({});

/**
 * 根据类型代码获取字典值列表
 * @param {string} typeCode - 字典类型代码
 * @returns {Promise<Array>} 字典值列表
 */
export async function getDicValuesByTypeCode(typeCode) {
  // 先从缓存获取
  if (dictionaryCache[typeCode]) {
    return Promise.resolve(dictionaryCache[typeCode]);
  }

  try {
    const res = await doGet(`/api/dicValue/listByTypeCode/${typeCode}`);
    if (res.data.code === 200) {
      dictionaryCache[typeCode] = res.data.data;
      return res.data.data;
    }
    return [];
  } catch (error) {
    console.error(`获取字典值失败: ${typeCode}`, error);
    return [];
  }
}

/**
 * 清空指定类型的字典缓存
 * @param {string} typeCode - 字典类型代码
 */
export function clearDicCache(typeCode) {
  if (typeCode) {
    delete dictionaryCache[typeCode];
  } else {
    // 清空所有缓存
    Object.keys(dictionaryCache).forEach(key => {
      delete dictionaryCache[key];
    });
  }
}

/**
 * 获取缓存中的字典值列表（不会触发API请求）
 * @param {string} typeCode - 字典类型代码
 * @returns {Array|null} 缓存中的字典值列表，不存在则返回null
 */
export function getCachedDicValues(typeCode) {
  return dictionaryCache[typeCode] || null;
}

/**
 * 检查缓存中是否存在指定类型的字典值
 * @param {string} typeCode - 字典类型代码
 * @returns {boolean} true 存在，false 不存在
 */
export function isCached(typeCode) {
  return !!dictionaryCache[typeCode];
}

// ==================== 便捷方法 ====================

/**
 * 获取称呼列表
 */
export async function getAppellationList() {
  return getDicValuesByTypeCode('appellation');
}

/**
 * 获取线索状态列表
 */
export async function getClueStateList() {
  return getDicValuesByTypeCode('clueState');
}

/**
 * 获取意向产品列表
 */
export async function getIntentionProductList() {
  return getDicValuesByTypeCode('intentionProduct');
}

/**
 * 获取是否需要贷款列表
 */
export async function getNeedLoanList() {
  return getDicValuesByTypeCode('needLoan');
}

/**
 * 获取意向状态列表
 */
export async function getIntentionStateList() {
  return getDicValuesByTypeCode('intentionState');
}

/**
 * 获取线索来源列表
 */
export async function getSourceList() {
  return getDicValuesByTypeCode('source');
}

/**
 * 获取跟踪方式列表
 */
export async function getNoteWayList() {
  return getDicValuesByTypeCode('noteWay');
}

/**
 * 根据字典值获取对应的显示文本
 * @param {string} typeCode - 字典类型代码
 * @param {string} typeValue - 字典值
 * @returns {Promise<string>} 字典值对应的显示文本
 */
export async function getDicValueText(typeCode, typeValue) {
  const dicValues = await getDicValuesByTypeCode(typeCode);
  const dicValue = dicValues.find(dv => dv.typeValue === typeValue);
  return dicValue ? dicValue.typeValue : typeValue;
}

/**
 * 批量获取多个字典类型的值
 * @param {Array<string>} typeCodes - 字典类型代码数组
 * @returns {Promise<Object>} 字典值对象，key为typeCode，value为字典值列表
 */
export async function getBatchDicValues(typeCodes) {
  const result = {};
  
  for (const typeCode of typeCodes) {
    result[typeCode] = await getDicValuesByTypeCode(typeCode);
  }
  
  return result;
}

/**
 * 创建字典选项列表（用于Element Plus的el-option）
 * @param {string} typeCode - 字典类型代码
 * @returns {Promise<Array>} 选项列表，格式为 [{label: '显示文本', value: '值'}, ...]
 */
export async function createDicOptions(typeCode) {
  const dicValues = await getDicValuesByTypeCode(typeCode);
  return dicValues.map(dv => ({
    label: dv.typeValue,
    value: dv.typeValue,
    id: dv.id
  }));
}

/**
 * 预加载常用字典
 * 在应用启动时调用，提高性能
 */
export async function preloadCommonDictionaries() {
  const commonTypes = [
    'appellation',
    'clueState',
    'intentionProduct',
    'needLoan',
    'intentionState',
    'source',
    'noteWay'
  ];

  try {
    await Promise.all(
      commonTypes.map(typeCode => getDicValuesByTypeCode(typeCode))
    );
    console.log('常用字典预加载完成');
  } catch (error) {
    console.error('字典预加载失败', error);
  }
}

/**
 * 获取缓存统计信息
 * @returns {Object} 缓存统计信息
 */
export function getCacheStats() {
  const stats = {
    cacheSize: Object.keys(dictionaryCache).length,
    totalItems: 0,
    cachedTypes: Object.keys(dictionaryCache)
  };

  Object.values(dictionaryCache).forEach(items => {
    stats.totalItems += items.length;
  });

  return stats;
}
