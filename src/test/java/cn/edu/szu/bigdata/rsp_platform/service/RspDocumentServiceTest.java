package cn.edu.szu.bigdata.rsp_platform.service;

import cn.edu.szu.bigdata.rsp_platform.RspPlatformApplication;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspDocument;
import cn.edu.szu.bigdata.rsp_platform.system.service.RspDocumentService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author longhao
 * @date 2019/8/9 23:26
 */
@Transactional//支持数据回滚，避免测试数据污染环境
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RspPlatformApplication.class)
public class RspDocumentServiceTest extends InitData{

    @Autowired
    RspDocumentService rspDocumentService;


    @Test
    public void crudTest(){
        //create
        RspDocument rspDocument=new RspDocument();
        rspDocument.setName("南方电网");
        rspDocument.setVolume("2048000MB");
        rspDocument.setHdfsLocation("/tmp/rsp/南方电网");
        rspDocumentService.save(rspDocument);
        Assert.assertNotNull(rspDocument.getId());
        //read
        rspDocument=rspDocumentService.getById(rspDocument.getId());

        Assert.assertNotNull(rspDocument.getId());

        //update
        rspDocument.setName("new南方电网");
        rspDocumentService.updateById(rspDocument);
        rspDocument=rspDocumentService.getById(rspDocument.getId());
        Assert.assertTrue(rspDocument.getName().equals("new南方电网"));

        //delete
        Assert.assertTrue(rspDocumentService.removeById(rspDocument.getId()));
        rspDocument=rspDocumentService.getById(rspDocument.getId());
        Assert.assertNull(rspDocument);
    }
}
