package cn.edu.szu.bigdata.rsp_platform.controller;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspBlock;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspColumn;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * @author SunHaotong
 * @date 2019/8/14 14:03
 */
@Transactional
public class RspColumnControllerTest extends  SecurityOauth2Test{
    /**
     * 测试新增列
     * @throws Exception
     */
    @Test
    public void createRspColumnTest() throws Exception{
        ResultActions result=mockMvc.perform(
                post("/v1/rspColumn")
                        .param("comment","test_6324")
                        .param("datasetId",datasets.get(0).getId())
                        .param("name","test_col_001")
                        .param("type","varchar")
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result.andReturn().getResponse().getContentAsString());
    }
    /**
     * 测试查询列
     * @throws Exception
     */
    @Test
    public void getRspColumnByIdTest() throws Exception{
        ResultActions result=mockMvc.perform(
                get("/v1/rspColumn/{id}",columns.get(0).getId())
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result.andReturn().getResponse().getContentAsString());
    }
    /**
     * 测试查询数据集的列
     * @throws Exception
     */
    @Test
    public void getBatchRspColumnByDatasetIdTest() throws Exception{
        ResultActions result=mockMvc.perform(
                get("/v1/rspColumn/batch/{datasetId}",datasets.get(0).getId())
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result.andReturn().getResponse().getContentAsString());
    }
    /**
     * 测试更新列
     * @throws Exception
     */
    @Test
    public void updateRspColumnTest() throws Exception{
        ResultActions result=mockMvc.perform(
                put("/v1/rspColumn")
                        .param("id",columns.get(0).getId())
                        .param("name","test_996")
                        .param("comment","modifyNameTest")
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result.andReturn().getResponse().getContentAsString());
    }
    /**
     * 测试删除列
     * @throws Exception
     */
    @Test
    public void deleteRspColumnTest() throws Exception{
        ResultActions result=mockMvc.perform(
                delete("/v1/rspColumn/{id}",columns.get(0).getId())
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result.andReturn().getResponse().getContentAsString());
    }
}
