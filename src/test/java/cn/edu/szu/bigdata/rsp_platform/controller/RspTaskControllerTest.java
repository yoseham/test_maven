package cn.edu.szu.bigdata.rsp_platform.controller;

import cn.edu.szu.bigdata.rsp_platform.service.InitData;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@Transactional
public class RspTaskControllerTest extends SecurityOauth2Test{
    /**
     * 测试查询所有RSPTask
     * @throws Exception
     */
    @Test
    public void listTest() throws Exception{
        ResultActions result=mockMvc.perform(
                get("/v1/rspTask")
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result.andReturn().getResponse().getContentAsString());
    }
    /**
     * 测试根据id删除RSPTask
     * @throws Exception
     */
    @Test
    public void deleteTest() throws Exception{
        ResultActions result=mockMvc.perform(
                delete("/v1/rspTask/{id}","1159710215550013441")
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result.andReturn().getResponse().getContentAsString());
    }
}
