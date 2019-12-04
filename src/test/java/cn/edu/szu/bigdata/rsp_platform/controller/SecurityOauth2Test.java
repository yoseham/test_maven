package cn.edu.szu.bigdata.rsp_platform.controller;

import cn.edu.szu.bigdata.rsp_platform.RspPlatformApplication;
import cn.edu.szu.bigdata.rsp_platform.service.InitData;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by longhao on 2019/8/8
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = RspPlatformApplication.class)
public class SecurityOauth2Test extends InitData{

    protected Logger logger= LoggerFactory.getLogger(getClass());

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    protected FilterChainProxy springSecurityFilterChain;

    protected  MockMvc mockMvc;

    protected String accessToken;

    //clientId
    private final static String CLIENT_ID = "397fd05f-3bfd-4205-a641-aaf0f8522744";
    //clientSecret
    private final static String CLIENT_SECRET = "2777e2f6-60a7-4a92-b02b-be92b52ab763";
    //用户名
    private final static String USERNAME = "admin";
    //密码
    private final static String PASSWORD = "admin";

    private final static String GRANT_TYPE="password";

    private final static String SCOPE="DEFAULT";

    protected static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    @Before
    public void setup(){
        mockMvc= MockMvcBuilders.webAppContextSetup(wac).addFilter(springSecurityFilterChain).build();
    }

    @Before
    public void obtainAccessToken() throws Exception {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", CLIENT_ID);
        params.add("client_secret",CLIENT_SECRET);
        params.add("username", USERNAME);
        params.add("password", PASSWORD);
        params.add("grant_type",GRANT_TYPE);
        params.add("scope",SCOPE);

        // @formatter:off

        ResultActions result = mockMvc.perform(post("/oauth/token")
                .params(params)
                .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE));

        // @formatter:on

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        accessToken=jsonParser.parseMap(resultString).get("access_token").toString();
    }

    @Test
    public void getAccessToken() throws Exception{
        logger.info("access_token={}", accessToken);
    }

}
