package com.weiwei.weidlykserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.weiwei.weidlykserver.dto.activity.ActivityAddDto;
import com.weiwei.weidlykserver.dto.activity.ActivityRemarkAddDto;
import com.weiwei.weidlykserver.dto.activity.ActivityUpdateDto;
import com.weiwei.weidlykserver.entity.Activity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.weiwei.weidlykserver.query.ActivityQuery;
import com.weiwei.weidlykserver.vo.ActivityDetailVo;

/**
 * <p>
 * 市场活动表 服务类
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
public interface ActivityService extends IService<Activity> {

    IPage<Activity> pageItem(IPage<Activity> page, ActivityQuery  query);

    void addActivity(ActivityAddDto activityAddDto, Integer createById);

    ActivityDetailVo getActivityDetailById(Integer id);

    void updateActivityById(ActivityUpdateDto activityUpdateDto, Integer createById);
}
