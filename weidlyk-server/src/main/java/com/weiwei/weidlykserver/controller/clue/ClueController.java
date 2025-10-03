package com.weiwei.weidlykserver.controller.clue;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiwei.weidlykserver.entity.Clue;
import com.weiwei.weidlykserver.entity.Customer;
import com.weiwei.weidlykserver.entity.User;
import com.weiwei.weidlykserver.query.ClueQuery;
import com.weiwei.weidlykserver.result.Result;
import com.weiwei.weidlykserver.service.ClueService;
import com.weiwei.weidlykserver.vo.ClueDetailVo;
import com.weiwei.weidlykserver.vo.CluePageVo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 线索表 前端控制器
 * </p>
 *
 * @author weiwei
 * @since 2025-09-22
 */
@RestController
@RequestMapping("/api")
public class ClueController {
    @Resource
    private ClueService clueService;

    @Operation(summary = "分页筛选获取线索列表")
    @PreAuthorize("hasAuthority('clue:list')")
    @GetMapping("/clues")
    public Result<IPage<CluePageVo>> getCluesPage(Page<Clue> page, ClueQuery query) {
        IPage<CluePageVo> cluePageVoIPage = clueService.getCluePage(page, query);
        return Result.ok(cluePageVoIPage);
    }

    @Operation(summary = "添加线索")
    @PreAuthorize("hasAuthority('clue:add')")
    @PostMapping("/clue/add")
    public Result<String> addClue(@RequestBody Clue clue, Authentication  authentication) {
        User currentUser = (User) authentication.getPrincipal();
        Integer currentUserId = currentUser.getId();
        boolean save = clueService.saveClue(clue, currentUserId);
        if (save) {
            return Result.ok("添加成功");
        }
        return Result.fail();
    }

    @Operation(summary = "校验手机号唯一性")
    @GetMapping("/clue/phone/check")
    public Result<Map<String, Boolean>> checkPhoneUnique(@RequestParam("phone") String phone) {
        // 调用 Service 层的方法进行查询
        boolean isUnique = clueService.isPhoneUnique(phone);
        // 返回一个包含布尔值的Map，方便前端判断
        return Result.ok(Collections.singletonMap("isUnique", isUnique));
    }

    @Operation(summary = "根据ID获取线索详情")
    @GetMapping("/clue/detail/{id}")
    public Result<ClueDetailVo> getClueDetail(@PathVariable("id") Long id) {
        ClueDetailVo clueDetail = clueService.getClueDetailById(id);
        if (clueDetail != null) {
            return Result.ok(clueDetail);
        }
        return Result.fail();
    }

    @Operation(summary = "更新线索信息")
    @PostMapping("/clue/update")
    @PreAuthorize("hasAuthority('clue:edit')")
// 1. 新增 Authentication 参数
    public Result<String> updateClue(@RequestBody Clue clue, Authentication authentication) {
        // 2. 获取当前登录用户
        User currentUser = (User) authentication.getPrincipal();

        // 3. 将clue对象和用户ID都传递给Service层
        boolean isSuccess = clueService.updateClue(clue, currentUser.getId());

        if (isSuccess) {
            return Result.ok("更新成功");
        }
        return Result.fail();
    }

    @Operation(summary = "根据ID获取线索原始信息(用于编辑)")
    @GetMapping("/clue/edit/{id}")
    public Result<Clue> getClueForEdit(@PathVariable("id") Long id) {
        Clue clue = clueService.getById(id);
        if (clue != null) {
            return Result.ok(clue);
        }
        return Result.fail();
    }

    @Operation(summary = "删除线索")
    @PostMapping("/clue/delete/{id}")
    @PreAuthorize("hasAuthority('clue:delete')")
    public Result<String> deleteClue(@PathVariable("id") Long id) {
        boolean isSuccess = clueService.removeById(id);
        if (isSuccess) {
            return Result.ok("删除成功");
        }
        return Result.fail();
    }

    @Operation(summary = "批量删除线索")
    @PreAuthorize("hasAuthority('clue:delete')")
    @PostMapping("/clue/delete/batch") // 匹配前端的 POST /api/clue/delete/batch
    public Result<String> deleteClueByIds(@RequestBody List<Integer> ids) {
        // 使用 Mybatis-Plus 自带的 removeByIds 方法
        boolean deleted = clueService.removeByIds(ids);
        if (deleted) {
            return Result.ok("批量删除成功");
        }
        return Result.fail();
    }

    @Operation(summary = "转换为客户")
    @PostMapping("/clue/convert")
    public Result<String> convertToCustomer(@RequestBody Customer customer, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        Integer currentUserId = currentUser.getId();
        boolean convertSuccess = clueService.convertToCustomer(customer, currentUserId);
        if (convertSuccess) {
            return Result.ok("转换成功");
        }
        return Result.fail();
    }


}
