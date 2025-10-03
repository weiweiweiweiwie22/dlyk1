package com.weiwei.weidlykserver.service;

import com.weiwei.weidlykserver.entity.ClueRemark;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 线索跟踪记录表 服务类
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
public interface ClueRemarkService extends IService<ClueRemark> {

    boolean saveClueRemark(ClueRemark clueRemark, Integer id);
}
