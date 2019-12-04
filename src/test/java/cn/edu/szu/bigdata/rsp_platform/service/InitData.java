package cn.edu.szu.bigdata.rsp_platform.service;

import cn.edu.szu.bigdata.rsp_platform.RspPlatformApplication;
import cn.edu.szu.bigdata.rsp_platform.system.model.*;
import cn.edu.szu.bigdata.rsp_platform.system.service.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author longhao
 * @date 2019/8/11 19:13
 */
@Transactional//支持数据回滚，避免测试数据污染环境
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RspPlatformApplication.class)
public class InitData {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitData.class);

    @Autowired
    RspDocumentService rspDocumentService;

    @Autowired
    RspDatasetService rspDatasetService;

    @Autowired
    RspBlockService rspBlockService;

    @Autowired
    RspColumnService rspColumnService;

    @Autowired
    DocumentDatasetService documentDatasetService;

    @Autowired
    DatasetBlockService datasetBlockService;

    @Autowired
    DatasetColumnService datasetColumnService;

    @Autowired
    BlockViewService blockViewService;


    public List<RspDocument> rspDocuments;

    public List<RspDataset> datasets;

    public List<RspColumn> columns;

    public List<RspBlock> rspBlocks;

    public List<BlockView> blockViews;


    @Before
    public void initCreateData(){
        //创建5个数据集目录
        rspDocuments=new ArrayList<>();
        String[] names={"南方电网_test","HIGGS_test","四川电信_test","Taxi_test","天虹超市_test"};
        for (int i=0;i<5;i++){
            RspDocument rspDocument=new RspDocument();
            rspDocument.setName(names[i]);
            rspDocument.setHdfsLocation("/tmp/rsp/"+rspDocument.getName());
            rspDocument.setVolume("10240");
            rspDocuments.add(rspDocument);
        }
        rspDocumentService.saveBatch(rspDocuments);
        //每个数据集目录创建5个数据集
        String[] ds={"东莞2013年","higgs_atx_","通信月_","taxi_city_","购物篮_"};
        datasets=new ArrayList<>();
        int j=0;
        for (RspDocument rspDocument:rspDocuments){
            for (int i=0;i<5;i++) {
                RspDataset dataset = new RspDataset();
                dataset.setName(ds[j]+Integer.toString(i));
                dataset.setHdfsLocation(rspDocument.getHdfsLocation()+"/"+dataset.getName());
                dataset.setVolume("2048");
                dataset.setBlockNumber("10");
                dataset.setIntroduction("no 996 no ICU");
                rspDatasetService.save(dataset);
                DocumentDataset documentDataset=new DocumentDataset();
                documentDataset.setDocumentId(rspDocument.getId());
                documentDataset.setDatasetId(dataset.getId());
                documentDatasetService.save(documentDataset);
                datasets.add(dataset);
            }
            j++;
        }
        LOGGER.info("========================dataset 数据集数目："+datasets.size()+"==============================");
        System.out.println("dataset 数据集数目："+datasets.size());
        //每个数据集创建6个列
        columns=new ArrayList<>();
        for (RspDataset dataset:datasets){
            for (int i=0;i<6;i++) {
                RspColumn rspColumn = new RspColumn();
                rspColumn.setName(dataset.getName() + "_column_"+Integer.toString(i));
                rspColumn.setType("String");
                rspColumn.setComment("test");
                rspColumnService.save(rspColumn);
                DatasetColumn datasetColumn = new DatasetColumn();
                datasetColumn.setColumnId(rspColumn.getId());
                datasetColumn.setDatasetId(dataset.getId());
                datasetColumnService.save(datasetColumn);
                columns.add(rspColumn);
            }
        }
        //每个数据集创建10个块
        rspBlocks=new ArrayList<>();
        for (RspDataset dataset:datasets){
            for (int i=0;i<10;i++){
                RspBlock rspBlock=new RspBlock();
                rspBlock.setName("partition_00"+Integer.toString(i));
                rspBlock.setVolume("204");
                rspBlock.setHdfsLocation(dataset.getHdfsLocation()+"/"+rspBlock.getName());
                rspBlockService.save(rspBlock);
                DatasetBlock datasetBlock=new DatasetBlock();
                datasetBlock.setBlockId(rspBlock.getId());
                datasetBlock.setDatasetId(dataset.getId());
                datasetBlockService.save(datasetBlock);
                rspBlocks.add(rspBlock);
            }
        }
        //每个数据集创建3个块视图
        blockViews=new ArrayList<>();
        for (RspDataset dataset:datasets){
            for (int i=0;i<3;i++){
                BlockView blockView=new BlockView();
                blockView.setDatasetId(dataset.getId());
                List<String> blockIds=datasetBlockService.listByDatasetId(dataset.getId());
                blockView.setBlockId(blockIds.get(i));
                List<String> columnIds=datasetColumnService.listByDatasetId(dataset.getId());
                blockView.setColumnId(columnIds.get(i));
                blockView.setRowCount(Integer.toString(new Random().nextInt(3000)));
                blockView.setxAxisData("1,2,3,4,5,6,7,8");
                blockView.setyAxisData("12,123,49,54,46,67,34,69");
                blockViewService.save(blockView);
                blockViews.add(blockView);
            }
        }
    }
}
