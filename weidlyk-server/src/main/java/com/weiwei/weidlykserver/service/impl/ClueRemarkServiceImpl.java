package com.weiwei.weidlykserver.service.impl;

import com.weiwei.weidlykserver.entity.ClueRemark;
import com.weiwei.weidlykserver.mapper.ClueRemarkMapper;
import com.weiwei.weidlykserver.service.ClueRemarkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 线索跟踪记录表 服务实现类
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
@Service
public class ClueRemarkServiceImpl extends ServiceImpl<ClueRemarkMapper, ClueRemark> implements ClueRemarkService {

    @Override
    public boolean saveClueRemark(ClueRemark clueRemark, Integer currentUserId) {
        clueRemark.setCreateBy(currentUserId);
        clueRemark.setCreateTime(LocalDateTime.now());
        // deleted 字段可以根据你的业务逻辑设置默认值，例如 0
        clueRemark.setDeleted(0);
        return this.save(clueRemark);
    }
}
