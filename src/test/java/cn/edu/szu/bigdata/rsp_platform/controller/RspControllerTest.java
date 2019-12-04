package cn.edu.szu.bigdata.rsp_platform.controller;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@Transactional
public class RspControllerTest extends SecurityOauth2Test{
    /**
     * 测试RSP转换V1
     * @throws Exception
     */
    @Test
    public void convertV1Test() throws Exception{
        ResultActions result=mockMvc.perform(
                post("/v1/rsp/convertV1")
                        .param("datasetName",datasets.get(0).getName())
                        .param("documentName",rspDocuments.get(0).getName())
                        .param("reduceNumber","10")
                        .param("sourcePath",rspDocuments.get(0).getHdfsLocation())
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result.andReturn().getResponse().getContentAsString());
    }
    //获取路径地址的子路径
    @Test
    public void getSubPathsTest() throws Exception{
        ResultActions result=mockMvc.perform(
                post("/v1/rsp/getSubpaths")
                        .param("path",rspDocuments.get(0).getHdfsLocation())
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result.andReturn().getResponse().getContentAsString());
    }
}
