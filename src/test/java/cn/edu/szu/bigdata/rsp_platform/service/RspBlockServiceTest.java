package cn.edu.szu.bigdata.rsp_platform.service;

import cn.edu.szu.bigdata.rsp_platform.RspPlatformApplication;
import cn.edu.szu.bigdata.rsp_platform.common.hdfs.HdfsOperation;
import cn.edu.szu.bigdata.rsp_platform.system.controller.RspBLockController;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspBlock;
import cn.edu.szu.bigdata.rsp_platform.system.service.RspBlockService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author longhao
 * @date 2019/8/9 23:46
 */
@Transactional//支持数据回滚，避免测试数据污染环境
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RspPlatformApplication.class)
public class RspBlockServiceTest extends InitData{

    @Autowired
    RspBlockService rspBlockService;

    @Test
    public void crudTest() throws Exception {

        //create
        RspBlock rspBlock=new RspBlock();
        rspBlock.setName("block_test");
        rspBlock.setHdfsLocation("/tmp/rsp/block_test");
        rspBlock.setVolume("128MB");
        rspBlockService.save(rspBlock);
        Assert.assertNotNull(rspBlock.getId());
        //read
        rspBlock=rspBlockService.getById(rspBlock.getId());
        Assert.assertNotNull(rspBlock);

        //update
        rspBlock.setName("new_block_test");
        rspBlockService.updateById(rspBlock);
        rspBlock=rspBlockService.getById(rspBlock.getId());
        Assert.assertTrue(rspBlock.getName().equals("new_block_test"));


        //preview

        rspBlockService.preview("1162280960796405762");
        rspBlock=rspBlockService.getById(rspBlock.getId());
        Assert.assertNull(rspBlock);




        //delete
        rspBlockService.removeById(rspBlock.getId());
        rspBlock=rspBlockService.getById(rspBlock.getId());
        Assert.assertNull(rspBlock);


    }
}
