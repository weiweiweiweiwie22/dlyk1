package com.weiwei.weidlykserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.weiwei.weidlykserver.dto.activity.ActivityAddDto;
import com.weiwei.weidlykserver.dto.activity.ActivityRemarkAddDto;
import com.weiwei.weidlykserver.dto.activity.ActivityUpdateDto;
import com.weiwei.weidlykserver.entity.Activity;
import com.weiwei.weidlykserver.entity.ActivityRemark;
import com.weiwei.weidlykserver.entity.User;
import com.weiwei.weidlykserver.exception.BusinessException;
import com.weiwei.weidlykserver.mapper.ActivityMapper;
import com.weiwei.weidlykserver.mapper.UserMapper;
import com.weiwei.weidlykserver.query.ActivityQuery;
import com.weiwei.weidlykserver.result.ResultCodeEnum;
import com.weiwei.weidlykserver.service.ActivityRemarkService;
import com.weiwei.weidlykserver.service.ActivityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weiwei.weidlykserver.vo.ActivityDetailVo;
import com.weiwei.weidlykserver.vo.ActivityRemarkVo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 市场活动表 服务实现类
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {

    @Resource
    private ActivityRemarkService activityRemarkService;

    @Resource
    private ActivityMapper activityMapper;

    @Resource
    private UserMapper userMapper;
    @Override
    public IPage<Activity> pageItem(IPage<Activity> page, ActivityQuery  query) {
        return activityMapper.pageItem(page,query);
    }

    @Override
    public void addActivity(ActivityAddDto activityAddDto, Integer createById) {
        Activity activity = new Activity();
        BeanUtils.copyProperties(activityAddDto,activity);
        activity.setCreateBy(createById);
        activity.setCreateTime(LocalDateTime.now());
        activityMapper.insert( activity);
    }

    @Override
    public ActivityDetailVo getActivityDetailById(Integer id) {
        // 1. 查询活动主信息
        Activity activity = activityMapper.selectById(id);
        if (activity == null) {
            return null; // 或者抛出异常
        }

        ActivityDetailVo activityDetailVo = new ActivityDetailVo();
        BeanUtils.copyProperties(activity, activityDetailVo);

        // 2. 安全地获取关联人姓名
        if (activity.getOwnerId() != null) {
            User owner = userMapper.selectById(activity.getOwnerId());
            if (owner != null) activityDetailVo.setOwnerName(owner.getName());
        }
        if (activity.getCreateBy() != null) {
            User creator = userMapper.selectById(activity.getCreateBy());
            if (creator != null) activityDetailVo.setCreateByName(creator.getName());
        }
        if (activity.getEditBy() != null) {
            User editor = userMapper.selectById(activity.getEditBy());
            if (editor != null) activityDetailVo.setEditByName(editor.getName());
        }

        // 3. 查询并设置该活动的备注列表
        List<ActivityRemarkVo> remarkVoList = activityRemarkService.getRemarkVoListByActivityId(id);
        activityDetailVo.setRemarkList(remarkVoList);


        return activityDetailVo;
    }

    @Override
    public void updateActivityById(ActivityUpdateDto activityUpdateDto, Integer editById) {
        Activity activity = activityMapper.selectById(activityUpdateDto.getId());
        if (activity == null) {
             throw new BusinessException(ResultCodeEnum.DATA_NOT_EXIST);
        }
        BeanUtils.copyProperties(activityUpdateDto,activity);

        activity.setEditTime(LocalDateTime.now());
        activity.setEditBy(editById);

        activityMapper.updateById(activity);
    }


}
