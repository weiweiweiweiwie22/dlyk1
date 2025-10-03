package com.weiwei.weidlykserver.controller.clue;

import com.weiwei.weidlykserver.entity.ClueRemark;
import com.weiwei.weidlykserver.entity.User;
import com.weiwei.weidlykserver.result.Result;
import com.weiwei.weidlykserver.service.ClueRemarkService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 线索跟踪记录表 前端控制器
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
@RestController
@RequestMapping("/api")
public class ClueRemarkController {
    @Resource
    private ClueRemarkService clueRemarkService;

    @Operation(summary = "新增线索跟踪记录")
    @PostMapping("/clue/remark/add")
    public Result<String> addClueRemark(@RequestBody ClueRemark clueRemark, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        boolean isSuccess = clueRemarkService.saveClueRemark(clueRemark, currentUser.getId());
        if (isSuccess) {
            return Result.ok("添加成功");
        }
        return Result.fail();
    }
}
