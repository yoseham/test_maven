package cn.edu.szu.bigdata.rsp_platform.controller;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author SunHaotong
 * @date 2019/8/20 14:26
 */
@Transactional
public class RspDatasetControllerTest  extends  SecurityOauth2Test{
    /**
     * 测试添加数据集
     * @throws Exception
     */
    @Test
    public void createRspDatasetTest() throws Exception{
        ResultActions result=mockMvc.perform(
                post("/v1/rspDataset")
                        .param("blockNumber","10")
                        .param("documentId",rspDocuments.get(0).getId())
                        .param("hdfsLocation","/tmp/rsp/996")
                        .param("name","test_996")
                        .param("volume","100")
                        .param("introduction","test intro")
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result.andReturn().getResponse().getContentAsString());
    }
    /**
     * 测试修改数据集
     * @throws Exception
     */
    @Test
    public void modifyRspDatasetTest() throws Exception{
        ResultActions result=mockMvc.perform(
                put("/v1/rspDataset")
                        .param("blockNumber","20")
                        .param("id",datasets.get(0).getId())
                        .param("hdfsLocation","/tmp/rsp/996")
                        .param("name","test_996")
                        .param("volume","50")
                        .param("introduction","test intro")
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result.andReturn().getResponse().getContentAsString());
    }
    /**
     * 测试查询数据集
     * @throws Exception
     */
    @Test
    public void getRspDatasetTest() throws Exception{
        ResultActions result=mockMvc.perform(
                get("/v1/rspDataset/{id}",datasets.get(0).getId())
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result.andReturn().getResponse().getContentAsString());
    }
    /**
     * 测试删除数据集
     * @throws Exception
     */
    @Test
    public void deleteRspDatasetTest() throws Exception{
        ResultActions result=mockMvc.perform(
                delete("/v1/rspDataset/{id}",datasets.get(0).getId())
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result.andReturn().getResponse().getContentAsString());
    }
    /**
     * 测试查询数据目录下的数据集
     * @throws Exception
     */
    @Test
    public void getRspDatasetFromRspDocumentTest() throws Exception{
        ResultActions result=mockMvc.perform(
                get("/v1/rspDataset/batch/{documentId}",rspDocuments.get(0).getId())
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result.andReturn().getResponse().getContentAsString());
    }
}
