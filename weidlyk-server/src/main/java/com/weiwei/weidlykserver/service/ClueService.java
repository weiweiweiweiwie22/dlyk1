package com.weiwei.weidlykserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiwei.weidlykserver.entity.Clue;
import com.baomidou.mybatisplus.extension.service.IService;
import com.weiwei.weidlykserver.entity.Customer;
import com.weiwei.weidlykserver.query.ClueQuery;
import com.weiwei.weidlykserver.query.CustomerQuery;
import com.weiwei.weidlykserver.vo.ClueDetailVo;
import com.weiwei.weidlykserver.vo.CluePageVo;
import com.weiwei.weidlykserver.vo.CustomerExportVo;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * <p>
 * 线索表 服务类
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
public interface ClueService extends IService<Clue> {

    IPage<CluePageVo> getCluePage(Page<Clue> page, ClueQuery query);

    boolean isPhoneUnique(String phone);

    ClueDetailVo getClueDetailById(Long id);

    boolean saveClue(Clue clue, Integer currentUserId);

    boolean updateClue(Clue clue, Integer currentUserId);

    boolean convertToCustomer(Customer customer, Integer currentUserId);

}
