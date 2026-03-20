package com.weiwei.weidlykserver.service;

import com.weiwei.weidlykserver.entity.Tran;
import com.baomidou.mybatisplus.extension.service.IService;

public interface TranService extends IService<Tran> {

    /**
     * 保存交易并记录初始历史
     */
    boolean saveTran(Tran tran);

    /**
     * 更新交易阶段并记录变更历史
     */
    boolean updateTranStage(Tran tran);
}