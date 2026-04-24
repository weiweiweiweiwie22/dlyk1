package com.weiwei.weidlykserver.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiwei.weidlykserver.entity.Clue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weiwei.weidlykserver.query.ClueQuery;
import com.weiwei.weidlykserver.vo.ChartDataVO;
import com.weiwei.weidlykserver.vo.ClueDetailVo;
import com.weiwei.weidlykserver.vo.CluePageVo;
import com.weiwei.weidlykserver.vo.ClueRemarkVo;

// 【关键修改】：使用 MyBatis 的 Param
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 线索表 Mapper 接口
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
public interface ClueMapper extends BaseMapper<Clue> {

    // 【关键修改】：给 page 和 query 都加上 MyBatis 的 @Param
    IPage<CluePageVo> selectCluePage(@Param("page") Page<Clue> page, @Param("query") ClueQuery query);

    ClueDetailVo selectDetailById(Long id);

    List<ClueRemarkVo> selectRemarkListByClueId(Long clueId);

    List<ChartDataVO> selectSourceData();
}