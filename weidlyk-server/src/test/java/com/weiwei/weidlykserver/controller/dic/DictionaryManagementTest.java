package com.weiwei.weidlykserver.controller.dic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiwei.weidlykserver.entity.DicType;
import com.weiwei.weidlykserver.entity.DicValue;
import com.weiwei.weidlykserver.result.Result;
import com.weiwei.weidlykserver.service.DicTypeService;
import com.weiwei.weidlykserver.service.DicValueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 字典管理模块集成测试
 */
@SpringBootTest
@AutoConfigureMockMvc
public class DictionaryManagementTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DicTypeService dicTypeService;

    @Autowired
    private DicValueService dicValueService;

    private DicType testDicType;
    private DicValue testDicValue;

    @BeforeEach
    public void setUp() {
        // 清理测试数据
        dicTypeService.remove(null);
        dicValueService.remove(null);

        // 创建测试数据
        testDicType = new DicType();
        testDicType.setTypeCode("test_type");
        testDicType.setTypeName("测试类型");
        testDicType.setRemark("这是一个测试类型");

        testDicValue = new DicValue();
        testDicValue.setTypeCode("test_type");
        testDicValue.setTypeValue("测试值");
        testDicValue.setOrder(1);
        testDicValue.setRemark("这是一个测试值");
    }

    // ==================== 字典类型测试 ====================

    @Test
    public void testAddDicType() throws Exception {
        String json = objectMapper.writeValueAsString(testDicType);

        mockMvc.perform(post("/api/dicType/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    public void testGetAllDicTypes() throws Exception {
        dicTypeService.save(testDicType);

        mockMvc.perform(get("/api/dicType/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testListDicTypesByPage() throws Exception {
        dicTypeService.save(testDicType);

        mockMvc.perform(get("/api/dicType/list")
                .param("current", "1")
                .param("size", "10")
                .param("typeCode", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.records").isArray());
    }

    @Test
    public void testEditDicType() throws Exception {
        dicTypeService.save(testDicType);
        Integer id = testDicType.getId();

        testDicType.setTypeName("修改后的测试类型");
        String json = objectMapper.writeValueAsString(testDicType);

        mockMvc.perform(put("/api/dicType/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    public void testDeleteDicType() throws Exception {
        dicTypeService.save(testDicType);
        Integer id = testDicType.getId();

        mockMvc.perform(delete("/api/dicType/delete/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    public void testBatchDeleteDicType() throws Exception {
        DicType type1 = new DicType();
        type1.setTypeCode("test_type_1");
        type1.setTypeName("测试类型1");

        DicType type2 = new DicType();
        type2.setTypeCode("test_type_2");
        type2.setTypeName("测试类型2");

        dicTypeService.save(type1);
        dicTypeService.save(type2);

        String json = objectMapper.writeValueAsString(new Integer[]{type1.getId(), type2.getId()});

        mockMvc.perform(delete("/api/dicType/batchDelete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    // ==================== 字典值测试 ====================

    @Test
    public void testAddDicValue() throws Exception {
        dicTypeService.save(testDicType);

        String json = objectMapper.writeValueAsString(testDicValue);

        mockMvc.perform(post("/api/dicValue/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    public void testGetAllDicValues() throws Exception {
        dicTypeService.save(testDicType);
        dicValueService.save(testDicValue);

        mockMvc.perform(get("/api/dicValue/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testListDicValuesByPage() throws Exception {
        dicTypeService.save(testDicType);
        dicValueService.save(testDicValue);

        mockMvc.perform(get("/api/dicValue/list")
                .param("current", "1")
                .param("size", "10")
                .param("typeCode", "test_type"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.records").isArray());
    }

    @Test
    public void testListDicValuesByTypeCode() throws Exception {
        dicTypeService.save(testDicType);
        dicValueService.save(testDicValue);

        mockMvc.perform(get("/api/dicValue/listByTypeCode/test_type"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testEditDicValue() throws Exception {
        dicTypeService.save(testDicType);
        dicValueService.save(testDicValue);
        Integer id = testDicValue.getId();

        testDicValue.setTypeValue("修改后的测试值");
        String json = objectMapper.writeValueAsString(testDicValue);

        mockMvc.perform(put("/api/dicValue/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    public void testDeleteDicValue() throws Exception {
        dicTypeService.save(testDicType);
        dicValueService.save(testDicValue);
        Integer id = testDicValue.getId();

        mockMvc.perform(delete("/api/dicValue/delete/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    public void testBatchDeleteDicValue() throws Exception {
        dicTypeService.save(testDicType);

        DicValue value1 = new DicValue();
        value1.setTypeCode("test_type");
        value1.setTypeValue("测试值1");
        value1.setOrder(1);

        DicValue value2 = new DicValue();
        value2.setTypeCode("test_type");
        value2.setTypeValue("测试值2");
        value2.setOrder(2);

        dicValueService.save(value1);
        dicValueService.save(value2);

        String json = objectMapper.writeValueAsString(new Integer[]{value1.getId(), value2.getId()});

        mockMvc.perform(delete("/api/dicValue/batchDelete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    // ==================== 业务接口测试 ====================

    @Test
    public void testGetAppellationList() throws Exception {
        mockMvc.perform(get("/api/dicValue/appellation/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testGetClueStatusList() throws Exception {
        mockMvc.perform(get("/api/dicValue/status/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testGetIntentionProductList() throws Exception {
        mockMvc.perform(get("/api/dicValue/intentionProduct/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testGetNeedsLoanList() throws Exception {
        mockMvc.perform(get("/api/dicValue/needsLoan/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testGetIntentionStatusList() throws Exception {
        mockMvc.perform(get("/api/dicValue/intentionStatus/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testGetSourceList() throws Exception {
        mockMvc.perform(get("/api/dicValue/source/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testGetNoteWayList() throws Exception {
        mockMvc.perform(get("/api/dicValue/noteWay/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }
}
