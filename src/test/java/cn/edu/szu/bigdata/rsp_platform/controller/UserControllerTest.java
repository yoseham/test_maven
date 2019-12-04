package cn.edu.szu.bigdata.rsp_platform.controller;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Created by longhao on 2019/8/7
 */


public class UserControllerTest extends SecurityOauth2Test {


    /**
     * 测试 获取全部用户
     * @throws Exception
     */
    @Test
    public void getUserList() throws Exception{
        ResultActions result=mockMvc.perform(
                get("/v1/user")
                        .param("page","1")
                        .param("limit","10")
                        .param("order","desc")
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(CONTENT_TYPE));
        logger.info(result.andReturn().getResponse().getContentAsString());
    }


    /**
     * 测试新增用户
     * @throws Exception
     */
    @Transactional
    @Test
    public void createUserTest() throws Exception{
        String result=mockMvc.perform(
                post("/v1/user")
                        .param("username","test")
                        .param("nickName","test")
                        .param("phone","13691745061")
                        .param("password","password_test")
                        .param("roleIds","2")
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$.code").value(200))
                .andReturn().getResponse().getContentAsString();
        logger.info(result);
    }


    /**
     * 测试修改用户--成功
     * @throws Exception
     */
    @Transactional
    @Test
    public void changeUserTestOK() throws Exception{
        String result=mockMvc.perform(
                put("/v1/user")
                        .param("userId","2")
                        .param("nickName","test1")
                        .param("phone","1234567890")
                        .param("password","password_test")
                        .param("roleIds","2")
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$.code").value(200))
                .andReturn().getResponse().getContentAsString();
        logger.info(result);
    }

    /**
     * 测试修改用户--失败
     * @throws Exception
     */
    @Transactional
    @Test
    public void changeUserTestERROR() throws Exception{
        String result=mockMvc.perform(
                put("/v1/user")
                        .param("userId","6324")
                        .param("nickName","sb")
                        .param("phone","233")
                        .param("password","2b")
                        .param("roleIds","4396")
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andReturn().getResponse().getContentAsString();
        logger.info(result);
    }

    /**
     * 测试修改用户状态 -- 成功
     * @throws Exception
     */
    @Transactional
    @Test
    public void changeUserStateTest() throws Exception{
        String result=mockMvc.perform(
                delete("/v1/user/{id}",2)
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$.code").value(200))
                .andReturn().getResponse().getContentAsString();
        logger.info(result);
    }

    /**
     * 测试重置用户密码 -- 成功
     * @throws Exception
     */
    @Transactional
    @Test
    public void resetUserPwdTest() throws Exception{
        String result=mockMvc.perform(
                put("/v1/user/psw/{id}",2)
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$.code").value(200))
                .andReturn().getResponse().getContentAsString();
        logger.info(result);
    }

    /**
     * 测试修改自己密码
     * @throws Exception
     */
    @Transactional
    @Test
    public void changeMyPwdTest() throws Exception{
        String result=mockMvc.perform(
                put("/v1/user/psw/","admin")
                        .param("oldPsw","admin")
                        .param("newPsw","test")
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$.code").value(200))
                .andReturn().getResponse().getContentAsString();
        logger.info(result);
    }

    /**
     * 测试删除用户
     * @throws Exception
     */
    @Transactional
    @Test
    public void deleteUserTest() throws Exception{
        String result=mockMvc.perform(
                delete("/v1/user/{id}",2)
                        .header("Authorization", "bearer " + accessToken)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$.code").value(200))
                .andReturn().getResponse().getContentAsString();
        logger.info(result);
    }


}
