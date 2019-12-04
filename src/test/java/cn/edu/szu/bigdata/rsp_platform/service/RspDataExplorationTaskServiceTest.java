package cn.edu.szu.bigdata.rsp_platform.service;

import cn.edu.szu.bigdata.rsp_platform.RspPlatformApplication;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspDataExplorationTask;
import cn.edu.szu.bigdata.rsp_platform.system.service.RspDataExplorationTaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by longhao on 2019/8/28
 */

@Transactional//支持数据回滚，避免测试数据污染环境
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RspPlatformApplication.class)
public class RspDataExplorationTaskServiceTest {

    @Autowired
    RspDataExplorationTaskService rspDataExplorationTaskService;

    @Test
    public void test(){
        RspDataExplorationTask rspDataExplorationTask=new RspDataExplorationTask();
        rspDataExplorationTask.setDatasetId("123456577");
        rspDataExplorationTask.setStatus(0);
        rspDataExplorationTaskService.save(rspDataExplorationTask);
        System.out.println(rspDataExplorationTask);
    }
}
