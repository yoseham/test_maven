package cn.edu.szu.bigdata.rsp_platform.controller;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author SunHaotong
 * @date 2019/8/13 11:34
 */
@Transactional
public class RspBlockViewControllerTest extends  SecurityOauth2Test{
    /**
     * 测试创建块视图
     * @throws Exception
     */

    @Test
    public void createBlockViewTest() throws Exception{
        ResultActions result=mockMvc.perform(
                post("/v1/rspBlockView/")
                        .param("blockId",rspBlocks.get(0).getId())
                        .param("columnId",columns.get(0).getId())
                        .param("datasetId",datasets.get(0).getId())
                        .param("rowCount","2856")
                        .param("xAxisData","1,2,3,4,5,6,7,8")
                        .param("yAxisData","12,123,49,54,46,67,34,69")
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result.andReturn().getResponse().getContentAsString());
    }
    /**
     * 测试根据ID查询块视图
     * @throws Exception
     */
    @Test
    public void getBlockViewByIdTest() throws Exception{
        ResultActions result=mockMvc.perform(
                get("/v1/rspBlockView/{id}",blockViews.get(1).getId())
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result.andReturn().getResponse().getContentAsString());
    }
    /**
     * 测试条件查询块视图
     * @throws Exception
     */
    @Test
    public void searchBlockViewTest() throws Exception{
        ResultActions result1=mockMvc.perform(
                get("/v1/rspBlockView/search")
                        .param("datasetId",blockViews.get(0).getDatasetId())
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result1.andReturn().getResponse().getContentAsString());
        ResultActions result2=mockMvc.perform(
                get("/v1/rspBlockView/search")
                        .param("blockId",rspBlocks.get(0).getId())
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                 .andExpect(jsonPath("$.code").value(0))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result2.andReturn().getResponse().getContentAsString());
        ResultActions result3=mockMvc.perform(
                get("/v1/rspBlockView/search")
                        .param("columnId",columns.get(0).getId())
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result3.andReturn().getResponse().getContentAsString());
    }
    /**
     * 测试修改块视图
     * @throws Exception
     */
    @Test
    public void updateBlockViewTest() throws Exception{
        ResultActions result=mockMvc.perform(
                put("/v1/rspBlockView/")
                        .param("id",blockViews.get(0).getId())
                        .param("blockId",rspBlocks.get(1).getId())
                        .param("columnId",columns.get(1).getId())
                        .param("datasetId",datasets.get(1).getId())
                        .param("rowCount","2460")
                        .param("xAxisData","1,2,3,4,5,6,7,8")
                        .param("yAxisData","12,123,49,54,46,67,34,69")
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result.andReturn().getResponse().getContentAsString());
    }
    /**
     * 测试删除块视图
     * @throws Exception
     */
    @Test
    public void deleteBlockViewTest() throws Exception{
        ResultActions result=mockMvc.perform(
                delete("/v1/rspBlockView/{id}",blockViews.get(0).getId())
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result.andReturn().getResponse().getContentAsString());

    }
}
