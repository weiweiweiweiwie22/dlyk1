package com.weiwei.weidlykserver.controller.clue;

import com.alibaba.excel.EasyExcel;
import com.weiwei.weidlykserver.dto.clue.ClueImportDTO;
import com.weiwei.weidlykserver.excel.ClueImportListener;
import com.weiwei.weidlykserver.exception.BusinessException; // 引入 BusinessException
import com.weiwei.weidlykserver.mapper.ActivityMapper;
import com.weiwei.weidlykserver.mapper.ClueMapper;
import com.weiwei.weidlykserver.mapper.DicValueMapper; // 引入字典Mapper
import com.weiwei.weidlykserver.mapper.UserMapper;
import com.weiwei.weidlykserver.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "线索导入接口")
@RestController
@RequestMapping("/api/clue")
@RequiredArgsConstructor
@Slf4j
public class ClueImportController {

    private final ClueMapper clueMapper;
    private final UserMapper userMapper;
    private final ActivityMapper activityMapper;
    private final DicValueMapper dicValueMapper; // 注入字典Mapper
    private final TransactionTemplate transactionTemplate;
    private final Validator validator;

    @Operation(summary = "导入线索Excel")
    @PreAuthorize("hasAuthority('clue:import')")
    @PostMapping("/import")
    public Result<String> importClue(@RequestPart("file") MultipartFile file) {
        try {
            ClueImportListener listener = new ClueImportListener(
                    clueMapper, userMapper, activityMapper, dicValueMapper, // 传入字典Mapper
                    transactionTemplate, validator);

            EasyExcel.read(file.getInputStream(), ClueImportDTO.class, listener)
                    .sheet()
                    .doRead();

            // 根据Listener的结果返回更详细的信息
            return listener.getResult();
        } catch (BusinessException e) {
            // 捕获我们在Listener中主动抛出的业务异常，将详细错误信息返回给前端
            return Result.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他未知异常
            log.error("Excel导入失败，发生未知错误", e);
            return Result.fail(500, "文件解析失败，请检查文件格式或联系管理员");
        }
    }
}