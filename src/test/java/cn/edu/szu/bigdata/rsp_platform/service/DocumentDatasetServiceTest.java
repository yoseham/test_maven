package cn.edu.szu.bigdata.rsp_platform.service;

import cn.edu.szu.bigdata.rsp_platform.RspPlatformApplication;
import cn.edu.szu.bigdata.rsp_platform.system.model.DocumentDataset;
import cn.edu.szu.bigdata.rsp_platform.system.service.DocumentDatasetService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import org.apache.hadoop.hdfs.web.JsonUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author longhao
 * @date 2019/8/10 12:47
 */
@Transactional//支持数据回滚，避免测试数据污染环境
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RspPlatformApplication.class)
public class DocumentDatasetServiceTest extends InitData{

    @Autowired
    DocumentDatasetService documentDatasetService;


    @Test
    public void listByDocumentIdTest(){
        List<String> result=documentDatasetService.listByDocumentId(rspDocuments.get(0).getId());
        Assert.assertTrue(result.size()>=0);
    }

    @Test
    public void removeByDocumentIdTest(){
        documentDatasetService.removeByDocumentId(rspDocuments.get(0).getId());
        List<String> result=documentDatasetService.listByDocumentId(rspDocuments.get(0).getId());
        Assert.assertTrue(result.size()==0);
    }

    @Test
    public void removeByDatasetIdTest(){
        documentDatasetService.removeByDatasetId(datasets.get(0).getId());
        QueryWrapper<DocumentDataset> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("dataset_id",datasets.get(0).getId());
        List<DocumentDataset> result=documentDatasetService.list(queryWrapper);
        Assert.assertTrue(result.size()==0);
    }

    @Test
    public void rspTreeTest(){
        List<Map<String,Object>> res=documentDatasetService.rspTree();
        Gson gson = new Gson();
        String json = gson.toJson(res);
        System.out.println(json);
    }
}
