package com.weiwei.weidlykserver.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.weiwei.weidlykserver.entity.DicValue;
import com.weiwei.weidlykserver.service.DicValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 字典值工具类
 * 提供字典值的获取、缓存等功能
 */
@Slf4j
@Component
public class DictionaryUtils {

    private final DicValueService dicValueService;

    /**
     * 字典缓存 - 使用ConcurrentHashMap保证线程安全
     * key: typeCode, value: 该类型下的所有字典值列表
     */
    private final Map<String, List<DicValue>> dictionaryCache = new ConcurrentHashMap<>();

    public DictionaryUtils(DicValueService dicValueService) {
        this.dicValueService = dicValueService;
    }

    /**
     * 根据类型代码获取字典值列表
     * 优先从缓存获取，缓存不存在则从数据库查询并缓存
     *
     * @param typeCode 字典类型代码
     * @return 字典值列表
     */
    public List<DicValue> getDicValuesByTypeCode(String typeCode) {
        // 先从缓存获取
        if (dictionaryCache.containsKey(typeCode)) {
            return dictionaryCache.get(typeCode);
        }

        // 缓存不存在，从数据库查询
        LambdaQueryWrapper<DicValue> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DicValue::getTypeCode, typeCode);
        wrapper.orderByAsc(DicValue::getOrder);
        List<DicValue> dicValues = dicValueService.list(wrapper);

        // 存入缓存
        dictionaryCache.put(typeCode, dicValues);
        log.debug("字典值已缓存: typeCode={}, size={}", typeCode, dicValues.size());

        return dicValues;
    }

    /**
     * 根据类型代码和字典值获取对应的字典值对象
     *
     * @param typeCode 字典类型代码
     * @param typeValue 字典值
     * @return 字典值对象，不存在则返回null
     */
    public DicValue getDicValue(String typeCode, String typeValue) {
        List<DicValue> dicValues = getDicValuesByTypeCode(typeCode);
        return dicValues.stream()
                .filter(dv -> dv.getTypeValue().equals(typeValue))
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取所有字典值，按类型分组
     *
     * @return Map<typeCode, List<DicValue>>
     */
    public Map<String, List<DicValue>> getAllDictionaries() {
        Map<String, List<DicValue>> result = new HashMap<>();
        List<DicValue> allDicValues = dicValueService.list();

        for (DicValue dicValue : allDicValues) {
            String typeCode = dicValue.getTypeCode();
            result.computeIfAbsent(typeCode, k -> new java.util.ArrayList<>()).add(dicValue);
        }

        return result;
    }

    /**
     * 清空指定类型的字典缓存
     *
     * @param typeCode 字典类型代码
     */
    public void clearCache(String typeCode) {
        dictionaryCache.remove(typeCode);
        log.debug("字典缓存已清空: typeCode={}", typeCode);
    }

    /**
     * 清空所有字典缓存
     */
    public void clearAllCache() {
        dictionaryCache.clear();
        log.debug("所有字典缓存已清空");
    }

    /**
     * 刷新指定类型的字典缓存
     *
     * @param typeCode 字典类型代码
     */
    public void refreshCache(String typeCode) {
        clearCache(typeCode);
        getDicValuesByTypeCode(typeCode);
    }

    /**
     * 获取缓存中的字典值列表（不会触发数据库查询）
     *
     * @param typeCode 字典类型代码
     * @return 缓存中的字典值列表，不存在则返回null
     */
    public List<DicValue> getCachedDicValues(String typeCode) {
        return dictionaryCache.get(typeCode);
    }

    /**
     * 检查缓存中是否存在指定类型的字典值
     *
     * @param typeCode 字典类型代码
     * @return true 存在，false 不存在
     */
    public boolean isCached(String typeCode) {
        return dictionaryCache.containsKey(typeCode);
    }

    /**
     * 获取缓存统计信息
     *
     * @return 缓存统计信息
     */
    public Map<String, Object> getCacheStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("cacheSize", dictionaryCache.size());
        stats.put("totalItems", dictionaryCache.values().stream().mapToInt(List::size).sum());
        stats.put("cachedTypes", dictionaryCache.keySet());
        return stats;
    }

    // ==================== 便捷方法 ====================

    /**
     * 获取称呼列表
     */
    public List<DicValue> getAppellationList() {
        return getDicValuesByTypeCode("appellation");
    }

    /**
     * 获取线索状态列表
     */
    public List<DicValue> getClueStateList() {
        return getDicValuesByTypeCode("clueState");
    }

    /**
     * 获取意向产品列表
     */
    public List<DicValue> getIntentionProductList() {
        return getDicValuesByTypeCode("intentionProduct");
    }

    /**
     * 获取是否需要贷款列表
     */
    public List<DicValue> getNeedLoanList() {
        return getDicValuesByTypeCode("needLoan");
    }

    /**
     * 获取意向状态列表
     */
    public List<DicValue> getIntentionStateList() {
        return getDicValuesByTypeCode("intentionState");
    }

    /**
     * 获取线索来源列表
     */
    public List<DicValue> getSourceList() {
        return getDicValuesByTypeCode("source");
    }

    /**
     * 获取跟踪方式列表
     */
    public List<DicValue> getNoteWayList() {
        return getDicValuesByTypeCode("noteWay");
    }
}
