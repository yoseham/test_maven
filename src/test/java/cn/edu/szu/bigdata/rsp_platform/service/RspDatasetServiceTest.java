package cn.edu.szu.bigdata.rsp_platform.service;

import cn.edu.szu.bigdata.rsp_platform.RspPlatformApplication;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspDataset;
import cn.edu.szu.bigdata.rsp_platform.system.service.RspDatasetService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author longhao
 * @date 2019/8/9 23:46
 */
@Transactional//支持数据回滚，避免测试数据污染环境
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RspPlatformApplication.class)
public class RspDatasetServiceTest extends InitData{

    @Autowired
    RspDatasetService rspDatasetService;

    @Test
    public void crudTest(){
        RspDataset rspDataset=new RspDataset();
        rspDataset.setName("rspDataSet_test");
        rspDataset.setBlockNumber("100");
        rspDataset.setHdfsLocation("/tmp/rsp/test/rspDataSet_test");
        rspDataset.setVolume("2048000");
        rspDatasetService.save(rspDataset);
        Assert.assertNotNull(rspDataset.getId());
        //read
        rspDataset=rspDatasetService.getById(rspDataset.getId());
        Assert.assertNotNull(rspDataset.getId());
        //update
        rspDataset.setName("new_rspDataSet_test");
        rspDatasetService.updateById(rspDataset);
        rspDataset=rspDatasetService.getById(rspDataset.getId());
        Assert.assertTrue(rspDataset.getName().equals("new_rspDataSet_test"));
        //delete
        rspDatasetService.removeById(rspDataset.getId());
        rspDataset=rspDatasetService.getById(rspDataset.getId());
        Assert.assertNull(rspDataset);
    }

    List<RspDataset> res;

    @Before
    public void setup(){
        res=new ArrayList<>();
        RspDataset rspDataset;
        for (int i=0;i<10;i++){
            rspDataset=new RspDataset();
            rspDataset.setName("rspDataSet_test_"+Integer.toString(i));
            rspDataset.setBlockNumber("100");
            rspDataset.setHdfsLocation("/tmp/rsp/test/rspDataSet_test_"+Integer.toString(i));
            rspDataset.setVolume("204800");
            res.add(rspDataset);
        }
        rspDatasetService.saveBatch(res);
    }

    @Test
    public void listByIdsTest(){
        List<String> ids=new ArrayList<>();
        Assert.assertTrue(res.size()==10);
        for (RspDataset rd:res){
            ids.add(rd.getId());
        }
        List<RspDataset> rspDatasets= (List<RspDataset>) rspDatasetService.listByIds(ids);
        Assert.assertTrue(rspDatasets.size()==10);
    }
}
