package com.weiwei.weidlykserver.service;

import com.weiwei.weidlykserver.dto.activity.ActivityRemarkAddDto;
import com.weiwei.weidlykserver.entity.ActivityRemark;
import com.baomidou.mybatisplus.extension.service.IService;
import com.weiwei.weidlykserver.vo.ActivityRemarkVo;

import java.util.List;

/**
 * <p>
 * 市场活动备注表 服务类
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
public interface ActivityRemarkService extends IService<ActivityRemark> {
    void addActivityRemark(ActivityRemarkAddDto activityRemarkAddDto, Integer createById);

    List<ActivityRemarkVo> getRemarkVoListByActivityId(Integer activityId);

}
