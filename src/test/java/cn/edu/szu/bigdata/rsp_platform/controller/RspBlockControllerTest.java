package cn.edu.szu.bigdata.rsp_platform.controller;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author SunHaotong
 * @date 2019/8/14 13:34
 */
@Transactional
public class RspBlockControllerTest  extends  SecurityOauth2Test{
    /**
     * 测试增加数据块
     * @throws Exception
     */
    @Test
    public void createRspBlockTest() throws Exception{
        ResultActions result=mockMvc.perform(
                post("/v1/rspBlock")
                        .param("name","nanfgangdianwangg_test")
                        .param("volume","10")
                        .param("hdfsLocation","/tmp/rsp/nanfgangdianwangg_test")
                        .param("datasetId",datasets.get(0).getId())
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result.andReturn().getResponse().getContentAsString());
    }
    /**
     * 测试查询数据块
     * @throws Exception
     */
    @Test
    public void getRspBlockByIdTest() throws Exception{
        ResultActions result=mockMvc.perform(
                get("/v1/rspBlock/{id}",rspBlocks.get(0).getId())
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result.andReturn().getResponse().getContentAsString());
    }
    /**
     * 测试查询数据集下的数据块
     * @throws Exception
     */
    @Test
    public void getBatchRspBlockByDatasetIdTest() throws Exception{
        ResultActions result=mockMvc.perform(
                get("/v1/rspBlock/batch/{datasetId}",datasets.get(0).getId())
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result.andReturn().getResponse().getContentAsString());
    }
    /**
     * 测试修改数据块
     * @throws Exception
     */
    @Test
    public void updateRspBlockTest() throws Exception{
        ResultActions result=mockMvc.perform(
                put("/v1/rspBlock/")
                        .param("id",rspBlocks.get(1).getId())
                        .param("name","nanfgangdianwangg_test")
                        .param("volume","10")
                        .param("hdfsLocation","/tmp/rsp/nanfgangdianwangg_test")
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result.andReturn().getResponse().getContentAsString());
    }
    /**
     * 测试删除数据块
     * @throws Exception
     */
    @Test
    public void deleteRspBlockByIdTest() throws Exception{
        ResultActions result=mockMvc.perform(
                delete("/v1/rspBlock/{id}",rspBlocks.get(0).getId())
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result.andReturn().getResponse().getContentAsString());
    }
}
