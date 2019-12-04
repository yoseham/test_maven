package cn.edu.szu.bigdata.rsp_platform.service;

import cn.edu.szu.bigdata.rsp_platform.RspPlatformApplication;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspColumn;
import cn.edu.szu.bigdata.rsp_platform.system.service.RspColumnService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author longhao
 * @date 2019/8/9 23:47
 */
@Transactional//支持数据回滚，避免测试数据污染环境
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RspPlatformApplication.class)
public class RspColumnServiceTest extends InitData{

    @Autowired
    RspColumnService rspColumnService;

    @Test
    public void crudTest(){
        //create
        RspColumn rspColumn=new RspColumn();
        rspColumn.setName("column_test");
        rspColumn.setType("String");
        rspColumnService.save(rspColumn);
        Assert.assertNotNull(rspColumn.getId());
        //read
        rspColumn=rspColumnService.getById(rspColumn.getId());
        Assert.assertNotNull(rspColumn);

        //update
        rspColumn.setName("new_column_test");
        rspColumnService.updateById(rspColumn);
        rspColumn=rspColumnService.getById(rspColumn.getId());
        Assert.assertTrue(rspColumn.getName().equals("new_column_test"));

        //delete
        rspColumnService.removeById(rspColumn.getId());
        rspColumn=rspColumnService.getById(rspColumn.getId());
        Assert.assertNull(rspColumn);
    }
}
