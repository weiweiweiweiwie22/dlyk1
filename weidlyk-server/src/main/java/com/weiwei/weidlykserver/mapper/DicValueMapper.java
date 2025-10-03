package com.weiwei.weidlykserver.mapper;

import com.weiwei.weidlykserver.entity.DicValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

/**
 * <p>
 * 字典值表 Mapper 接口
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
public interface DicValueMapper extends BaseMapper<DicValue> {
    @Select("SELECT id FROM t_dic_value WHERE type_code = #{typeCode} AND type_value = #{typeValue}")
    Integer selectIdByTypeCodeAndValue(@Param("typeCode") String typeCode, @Param("typeValue") String typeValue);

}
