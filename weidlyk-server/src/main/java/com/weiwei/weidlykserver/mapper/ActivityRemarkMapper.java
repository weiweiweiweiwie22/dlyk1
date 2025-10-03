package com.weiwei.weidlykserver.mapper;

import com.weiwei.weidlykserver.entity.ActivityRemark;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weiwei.weidlykserver.vo.ActivityRemarkVo;

import java.util.List;

/**
 * <p>
 * 市场活动备注表 Mapper 接口
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
public interface ActivityRemarkMapper extends BaseMapper<ActivityRemark> {
    List<ActivityRemarkVo> selectRemarkVoListByActivityId(Integer activityId);
}
