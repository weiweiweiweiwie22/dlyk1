package com.weiwei.weidlykserver.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.weiwei.weidlykserver.common.DataScope;
import com.weiwei.weidlykserver.entity.Activity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weiwei.weidlykserver.query.ActivityQuery;
import org.apache.ibatis.annotations.Param;
/**
 * <p>
 * 市场活动表 Mapper 接口
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
public interface ActivityMapper extends BaseMapper<Activity> {

    @DataScope(tableAlias = "ta", tableField = "owner_id")
    IPage<Activity> pageItem(IPage<Activity> page, @Param("query") ActivityQuery query);

    /**
     * 根据活动名称查询活动信息
     * @param name 活动名称
     * @return 活动信息
     */
    Activity selectByName(String name);
}
