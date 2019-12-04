package cn.edu.szu.bigdata.rsp_platform.service;

import cn.edu.szu.bigdata.rsp_platform.RspPlatformApplication;
import cn.edu.szu.bigdata.rsp_platform.system.model.BlockView;
import cn.edu.szu.bigdata.rsp_platform.system.service.BlockViewService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author longhao
 * @date 2019/8/9 23:47
 */
@Transactional//支持数据回滚，避免测试数据污染环境
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RspPlatformApplication.class)
public class RspBlockViewServiceTest extends InitData{

    @Autowired
    BlockViewService blockViewService;

    @Test
    public void crudTest(){
        //create
        BlockView blockView=new BlockView();
        blockView.setBlockId("bv_1");
        blockView.setColumnId("bv_1");
        blockView.setRowCount("200");
        blockView.setDatasetId("bv_1");
        blockView.setxAxisData("1,2,3,4,5,6");
        blockView.setyAxisData("1,2,3,4,5,6");
        blockViewService.save(blockView);
        Assert.assertNotNull(blockView.getBlockId());


        //read
        blockView=blockViewService.getById(blockView.getId());
        Assert.assertNotNull(blockView);

        //update
        blockView.setRowCount("300");
        blockViewService.updateById(blockView);
        blockView=blockViewService.getById(blockView.getId());
        Assert.assertTrue(blockView.getRowCount().equals("300"));

        //delete
        blockViewService.removeById(blockView.getId());
        blockView=blockViewService.getById(blockView.getId());
        Assert.assertNull(blockView);

    }
    @Test
    public void Test(){
        List<String> list = blockViewService.listByIds("1162280953569619970","1162280962750951426","1162280956803428354");
        System.out.println(list);
    }
}
