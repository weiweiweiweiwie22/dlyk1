package com.weiwei.weidlykserver.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.weiwei.weidlykserver.dto.clue.ClueImportDTO;
import com.weiwei.weidlykserver.entity.Activity;
import com.weiwei.weidlykserver.entity.Clue;
import com.weiwei.weidlykserver.entity.User;
import com.weiwei.weidlykserver.exception.BusinessException;
import com.weiwei.weidlykserver.mapper.ActivityMapper;
import com.weiwei.weidlykserver.mapper.ClueMapper;
import com.weiwei.weidlykserver.mapper.DicValueMapper; // 引入字典Mapper
import com.weiwei.weidlykserver.mapper.UserMapper;
import com.weiwei.weidlykserver.result.Result;
import com.weiwei.weidlykserver.result.ResultCodeEnum;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class ClueImportListener implements ReadListener<ClueImportDTO> {

    // 批量处理阈值
    private static final int BATCH_COUNT = 100;
    // 缓存转换好的Clue实体
    private List<Clue> cachedClueList = new ArrayList<>(BATCH_COUNT);

    private final ClueMapper clueMapper;
    private final UserMapper userMapper;
    private final ActivityMapper activityMapper;
    private final DicValueMapper dicValueMapper; // 字典Mapper
    private final TransactionTemplate transactionTemplate;
    private final Validator validator;

    // 用于收集所有错误信息
    private final List<String> errorMessages = new ArrayList<>();
    private int totalCount = 0;
    private int successCount = 0;

    public ClueImportListener(ClueMapper clueMapper, UserMapper userMapper,
                              ActivityMapper activityMapper, DicValueMapper dicValueMapper,
                              TransactionTemplate transactionTemplate, Validator validator) {
        this.clueMapper = clueMapper;
        this.userMapper = userMapper;
        this.activityMapper = activityMapper;
        this.dicValueMapper = dicValueMapper; // 接收字典Mapper
        this.transactionTemplate = transactionTemplate;
        this.validator = validator;
    }

    @Override
    public void invoke(ClueImportDTO data, AnalysisContext context) {
        totalCount++;
        int currentRow = context.readRowHolder().getRowIndex() + 1;

        // 1. JSR 303 数据格式校验
        Set<ConstraintViolation<ClueImportDTO>> violations = validator.validate(data);
        if (!violations.isEmpty()) {
            String errorMsg = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("; "));
            addError(currentRow, errorMsg);
            return;
        }

        Clue clue = new Clue();

        // 2. 业务数据校验与转换
        // 验证负责人是否存在 (按登录账号查询)
        User owner = userMapper.selectByUsername(data.getOwner());
        if (owner == null) {
            addError(currentRow, String.format("负责人'%s'不存在", data.getOwner()));
            return;
        }
        clue.setOwnerId(owner.getId());
        clue.setCreateBy(owner.getId()); // 将创建人也设置为负责人

        // 验证活动是否存在 (按活动名称查询)
        if (StringUtils.hasText(data.getActivityName())) {
            Activity activity = activityMapper.selectByName(data.getActivityName());
            if (activity == null) {
                addError(currentRow, String.format("活动'%s'不存在", data.getActivityName()));
                return;
            }
            clue.setActivityId(activity.getId());
        }

        // 3. 使用字典表进行智能转换
        clue.setAppellation(getDicValueId("appellation", data.getAppellation(), currentRow));
        clue.setState(getDicValueId("clueState", data.getClueState(), currentRow));
        clue.setSource(getDicValueId("source", data.getSource(), currentRow));
        clue.setIntentionState(getDicValueId("intentionState", data.getIntentionState(), currentRow));

//         意向产品在您的字典表中没有，如果您有，请添加type_code为'intentionProduct'的数据，否则暂时注释
         clue.setIntentionProduct(getDicValueId("intentionProduct", data.getIntentionProduct(), currentRow));

        // 对"是否贷款"做特殊处理
        Integer needLoanId = getDicValueId("needLoan", data.getIsLoan(), currentRow);
        if (needLoanId != null) {
            clue.setNeedLoan(needLoanId);
        } else {
            // 如果找不到，可以根据肯定/否定词义进行猜测
            if ("是".equals(data.getIsLoan()) || "需要".equals(data.getIsLoan())) {
                clue.setNeedLoan(49); // 对应数据库中"需要"的ID
            } else {
                clue.setNeedLoan(50); // 对应数据库中"不需要"的ID
            }
        }

        // 4. 其他字段直接映射
        clue.setFullName(data.getFullName());
        clue.setPhone(data.getPhone());
        clue.setWeixin(data.getWechat());
        clue.setQq(data.getQq());
        clue.setEmail(data.getEmail());
        clue.setAge(data.getAge());
        clue.setJob(data.getJob());
        clue.setAddress(data.getAddress());
        clue.setDescription(data.getDescription());
        clue.setNextContactTime(data.getNextContactTime());
        clue.setCreateTime(LocalDateTime.now());

        // 年收入转换，增加异常处理
        if (StringUtils.hasText(data.getAnnualIncome())) {
            try {
                clue.setYearIncome(new BigDecimal(data.getAnnualIncome()));
            } catch (NumberFormatException e) {
                addError(currentRow, "年收入格式不正确，请输入纯数字");
                return;
            }
        }

        // 如果所有校验和转换都通过，才加入缓存列表
        cachedClueList.add(clue);

        // 达到批量处理阈值，执行保存
        if (cachedClueList.size() >= BATCH_COUNT) {
            saveData();
            cachedClueList.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 保存最后一批不足BATCH_COUNT的数据
        if (!cachedClueList.isEmpty()) {
            saveData();
        }
        log.info("所有数据解析完成！总行数: {}, 成功: {}, 失败: {}", totalCount, successCount, errorMessages.size());
    }

    // 保存数据到数据库
    private void saveData() {
        if (cachedClueList.isEmpty()) {
            return;
        }
        try {
            // 使用事务模板保证整批数据要么全部成功，要么全部失败
            transactionTemplate.execute(status -> {
                // 这里可以使用MyBatis Plus的批量插入，如果ClueMapper继承了BaseMapper
                // clueMapper.insertBatch(cachedClueList);
                // 或者循环插入
                for (Clue clue : cachedClueList) {
                    clueMapper.insert(clue);
                }
                return true;
            });
            successCount += cachedClueList.size();
        } catch (Exception e) {
            log.error("批量保存数据失败", e);
            // 批量失败时，为这批数据中的每一行都记录一个通用错误
            int startRow = totalCount - cachedClueList.size() + 1;
            for(int i = 0; i < cachedClueList.size(); i++) {
                addError(startRow + i, "数据保存至数据库时发生错误，该批次已回滚");
            }
        }
    }

    // 统一的错误信息添加方法
    private void addError(int row, String message) {
        errorMessages.add(String.format("第%d行: %s", row, message));
    }

    // 辅助方法：查询字典表获取ID
    private Integer getDicValueId(String typeCode, String typeValue, int currentRow) {
        if (!StringUtils.hasText(typeValue)) {
            return null;
        }
        Integer id = dicValueMapper.selectIdByTypeCodeAndValue(typeCode, typeValue);
        if (id == null) {
            addError(currentRow, String.format("字典值'%s' (类型: %s) 无效或不存在", typeValue, typeCode));
        }
        return id;
    }

    // 获取最终处理结果
    public Result<String> getResult() {
        if (errorMessages.isEmpty()) {
            return Result.ok(String.format("导入成功！共处理%d条数据。", totalCount));
        } else {
            String finalMessage = String.format("导入完成。总计%d条，成功%d条，失败%d条。详情如下：\n%s",
                    totalCount, successCount, errorMessages.size(), String.join(";\n", errorMessages));
            throw new BusinessException(ResultCodeEnum.IMPORT_ERROR.getCode(), finalMessage);
        }
    }
}