package cn.edu.szu.bigdata.rsp_platform.controller;

import cn.edu.szu.bigdata.rsp_platform.service.InitData;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author longhao
 * @date 2019/8/11 20:50
 */
public class RspDocumentControllerTest extends SecurityOauth2Test {
    /**
     * 测试添加数据集目录
     * @throws Exception
     */
    @Test
    public void createRspDocumentTest() throws Exception{
        ResultActions result=mockMvc.perform(
                post("/v1/rspDocument")
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
     * 测试获取所有数据集目录
     * @throws Exception
     */
    @Test
    public void getAllRspDocumentTest() throws Exception{
        ResultActions result=mockMvc.perform(
                get("/v1/rspDocument")
                        .param("keyword","南方")
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result.andReturn().getResponse().getContentAsString());
    }

    /**
     * 测试获取RspTree
     * @throws Exception
     */
    @Test
    public void rspTreeTest() throws Exception{
        ResultActions result=mockMvc.perform(
                get("/v1/rspDocument/rspTree")
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result.andReturn().getResponse().getContentAsString());
    }

    /**
     * 测试根据id获取数据集目录
     * @throws Exception
     */
    @Test
    public void getRspDocumentByIdTest() throws Exception{
        ResultActions result = mockMvc.perform(
                get("/v1/rspDocument/{id}",rspDocuments.get(0).getId())
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result.andReturn().getResponse().getContentAsString());
    }

    /**
     * 测试更新数据集目录
     * @throws Exception
     */
    @Test
    public void updateRspDocumentTest() throws Exception{
        ResultActions result=mockMvc.perform(
                put("/v1/rspDocument")
                        .param("id",rspDocuments.get(0).getId())
                        .param("name","newName")
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result.andReturn().getResponse().getContentAsString());
    }

    /**
     * 测试删除数据集目录
     * @throws Exception
     */
    @Test
    public void deleteRspDocumentTest()throws Exception{
        ResultActions result=mockMvc.perform(
                delete("/v1/rspDocument/{id}",rspDocuments.get(0).getId())
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result.andReturn().getResponse().getContentAsString());
    }
}
