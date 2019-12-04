package cn.edu.szu.bigdata.rsp_platform.service;

import cn.edu.szu.bigdata.rsp_platform.RspPlatformApplication;
import cn.edu.szu.bigdata.rsp_platform.system.model.User;
import cn.edu.szu.bigdata.rsp_platform.system.service.RoleService;
import cn.edu.szu.bigdata.rsp_platform.system.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by longhao on 2019/8/7
 */
@Transactional//支持数据回滚，避免测试数据污染环境
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RspPlatformApplication.class)
public class UserServiceTest {

    @Autowired
    public UserService userService;

    @Autowired
    public RoleService roleService;

    @Test
    public void CRUDTest(){
        //create
        User user=new User();
        user.setUsername("unit_test");
        user.setNickName("nickName_test");
        user.setPassword("password_test");
        List<String> ids=new ArrayList<>();
        ids.add("2");
        userService.addUser(user,ids);
        Assert.assertNotNull(user.getUserId());
        //read
        user=userService.getById(user.getUserId());
        Assert.assertNotNull(user.getUserId());

        //update
        user.setUsername("new_unit_test");
        userService.updateById(user);
        user=userService.getById(user.getUserId());
        Assert.assertTrue(user.getUsername().equals("new_unit_test"));

        //delete
        userService.removeById(user.getUserId());
        user=userService.getById(user.getUserId());
        Assert.assertNull(user);
    }
}
