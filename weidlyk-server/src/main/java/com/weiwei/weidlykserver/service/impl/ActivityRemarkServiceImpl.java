package com.weiwei.weidlykserver.service.impl;

import com.weiwei.weidlykserver.dto.activity.ActivityRemarkAddDto;
import com.weiwei.weidlykserver.entity.ActivityRemark;
import com.weiwei.weidlykserver.mapper.ActivityRemarkMapper;
import com.weiwei.weidlykserver.service.ActivityRemarkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weiwei.weidlykserver.vo.ActivityRemarkVo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 市场活动备注表 服务实现类
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
@Service
public class ActivityRemarkServiceImpl extends ServiceImpl<ActivityRemarkMapper, ActivityRemark> implements ActivityRemarkService {

    @Resource
    private ActivityRemarkMapper activityRemarkMapper;
    @Override
    public void addActivityRemark(ActivityRemarkAddDto activityRemarkAddDto, Integer createById) {
        ActivityRemark activityRemark = new ActivityRemark();
        BeanUtils.copyProperties(activityRemarkAddDto,activityRemark);
        activityRemark.setCreateBy(createById);
        activityRemark.setCreateTime(LocalDateTime.now());
        activityRemarkMapper.insert(activityRemark);
    }

    @Override
    public List<ActivityRemarkVo> getRemarkVoListByActivityId(Integer activityId) {
        return baseMapper.selectRemarkVoListByActivityId(activityId);
    }
}
