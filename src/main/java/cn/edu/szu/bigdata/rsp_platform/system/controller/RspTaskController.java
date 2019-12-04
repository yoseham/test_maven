package cn.edu.szu.bigdata.rsp_platform.system.controller;

import cn.edu.szu.bigdata.rsp_platform.common.BaseController;
import cn.edu.szu.bigdata.rsp_platform.common.JsonResult;
import cn.edu.szu.bigdata.rsp_platform.common.PageParam;
import cn.edu.szu.bigdata.rsp_platform.common.PageResult;
import cn.edu.szu.bigdata.rsp_platform.common.eum.SparkJobState;
import cn.edu.szu.bigdata.rsp_platform.common.exception.BusinessException;
import cn.edu.szu.bigdata.rsp_platform.common.log.BussinessLog;
import cn.edu.szu.bigdata.rsp_platform.common.utils.JSONUtil;
import cn.edu.szu.bigdata.rsp_platform.common.utils.PropertiesUtil;
import cn.edu.szu.bigdata.rsp_platform.common.utils.StringUtil;
import cn.edu.szu.bigdata.rsp_platform.system.dto.RspJobDto;
import cn.edu.szu.bigdata.rsp_platform.system.model.*;
import cn.edu.szu.bigdata.rsp_platform.system.service.*;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * @author longhao
 * @date 2019/6/24 19:52
 */
@Api(value = "RSPTask管理", tags = "rspTask")
@RestController
@RequestMapping("${api.version}/rspTask")
public class RspTaskController extends BaseController {
    private static String RSP_HDFS_BASE_PATH;
    private static String RSP_HDFS_GRAPH_PATH;

    private static Logger logger = Logger.getLogger(RspTaskController.class);


    public RspTaskController(){
        try {
            Properties properties = PropertiesUtil.getProperties("livy.properties");
            RSP_HDFS_BASE_PATH = String.valueOf(properties.get("RSP_HDFS_BASE_PATH"));
            RSP_HDFS_GRAPH_PATH = String.valueOf(properties.get("RSP_HDFS_GRAPH_PATH"));
        } catch (IOException e) {
            logger.error("请检查配置文件，找不到 RSP_HDFS_GRAPH_PATH 或者 RSP_HDFS_BASE_PATH");
            e.printStackTrace();
        }
    }



    @Autowired
    RspTaskService rspTaskService;


    @Autowired
    RspService rspService;

    @Autowired
    DatasetService datasetService;

    @Autowired
    RspDocumentService rspDocumentService;

    @Autowired
    RspDatasetService rspDatasetService;

    @Autowired
    RspBlockService rspBlockService;

    @Autowired
    DatasetBlockService datasetBlockService;

    @Autowired
    RspColumnService rspColumnService;

    @Autowired
    DatasetColumnService datasetColumnService;


    @Autowired
    RspDataExplorationTaskService dataExplorationTaskService;

    @Autowired
    DocumentDatasetService documentDatasetService;


    @Autowired
    WaitTaskService waitTaskService;

    @Autowired
    RunningTaskService runningTaskService;


    @BussinessLog(operEvent = "查询所有RSPTask",operType = 1)
    @ApiOperation(value = "查询所有RSPTask")
//    @PreAuthorize("hasAuthority('get:/v1/rspTask')")
    @GetMapping()
    public PageResult<RspTask> list(HttpServletRequest request) {
        PageParam pageParam = new PageParam(request);
        pageParam.setDefaultOrder(null,new String[]{"create_time"});
        QueryWrapper<RspTask> queryWrapper = new QueryWrapper<>();
        String keywords = (String) pageParam.get("keyword");
        if (StringUtil.isNotBlank(keywords)) {
            queryWrapper.like("username", keywords);
        }
        IPage pageRspTask = rspTaskService.page(pageParam, queryWrapper);
        return new PageResult<>(pageRspTask.getRecords(), pageRspTask.getTotal());
    }


    @BussinessLog(operEvent = "删除rspTask",operType = 1)
    @ApiOperation(value = "删除rspTask")
//    @PreAuthorize("hasAuthority('delete:/v1/rspTask/{id}')")
    @DeleteMapping("/{id}")
    public JsonResult delete(@PathVariable("id") String rspTaskId) {
        if (rspTaskService.removeById(rspTaskId)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }


    @BussinessLog(operEvent = "添加RSP任务", operType = 1)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "separatorFlag", value = "分隔符", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "columns", value = "数据字典", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "sourcePath", value = "hdfs源路径", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "documentName", value = "数据目录名称",required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "datasetName", value = "数据集名称", required = true,paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "introduction", value = "数据集简介", required = true,paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "reduceNumber", value = "块数目", required = true, paramType = "query", dataType = "String"),
    })
    @ApiOperation(value = "添加RSP任务")
    @PostMapping()
    public JsonResult addRspTask(HttpServletRequest request) throws Exception{
        String separatorFlag = request.getParameter("separatorFlag");
        String documentName = request.getParameter("documentName");
        String datasetName = request.getParameter("datasetName");
        String reduceNumber = request.getParameter("reduceNumber");
        String columnString = request.getParameter("columns");
        String sourcePath = request.getParameter("sourcePath");
        String introduction = request.getParameter("introduction");


        RspJobDto rspJobDto=new RspJobDto();
        rspJobDto.setDatasetName(datasetName);
        rspJobDto.setDocumentName(documentName);
        rspJobDto.setIntroduction(introduction);
        rspJobDto.setReduceNumber(reduceNumber);
        rspJobDto.setSeparatorFlag(separatorFlag);
        rspJobDto.setSourcePath(sourcePath);


        String targetPath = RSP_HDFS_BASE_PATH + "/" + rspJobDto.getDocumentName() + "/" + rspJobDto.getDatasetName();
        String documentPath = RSP_HDFS_BASE_PATH + "/" + rspJobDto.getDocumentName();

        System.out.println("====================================================="+targetPath);
        System.out.println("============================================="+documentPath);
        List<RspColumn> columns=new ArrayList<>();
        if(StringUtil.isNotBlank(columnString)){
            try {
                columns = JSONUtil.parseArray(columnString, RspColumn.class);
            }catch (Exception e){
                throw new BusinessException(401,"columns填写错误");
            }
        }

        if(rspJobDto.getDocumentName().indexOf("/")!=-1){
            targetPath = rspJobDto.getDocumentName()+ "/" + rspJobDto.getDatasetName();
            documentPath = rspJobDto.getDocumentName();
        }

        //数据集为空，新建
        RspDataset dataset=rspDatasetService.selectByHdfsLocation(targetPath);
        if (dataset==null){
            dataset=new RspDataset();
            dataset.setName(rspJobDto.getDatasetName());
            dataset.setHdfsLocation(targetPath);
            rspDatasetService.save(dataset);
        }
        else{
            String datasetId = dataset.getId();
            datasetBlockService.removeByDatasetId(datasetId);
            datasetColumnService.removeByDatasetId(datasetId);
        }
        dataset.setIntroduction(rspJobDto.getIntroduction());
        dataset.setBlockNumber(rspJobDto.getReduceNumber());
        String vol=rspService.getPathCapacity(rspJobDto.getSourcePath());

        if (StringUtil.isBlank(vol)){
            dataset.setVolume("0");
        }else {
            dataset.setVolume(vol);
        }
        rspDatasetService.updateById(dataset);

        RspDocument rspDocument=rspDocumentService.selectByHdfsLocation(documentPath);

        //数据集目录为空，新建
        if (rspDocument==null){
            rspDocument=new RspDocument();
            rspDocument.setName(rspJobDto.getDocumentName());
            rspDocument.setVolume("0");
            rspDocument.setHdfsLocation(documentPath);
            rspDocumentService.save(rspDocument);
        }

        Long documentVolume=Long.parseLong(rspDocument.getVolume());
        Long datasetVolume=Long.parseLong(dataset.getVolume());
        rspDocument.setVolume(Long.toString(documentVolume+datasetVolume));
        rspDocumentService.updateById(rspDocument);


        //保存数据集目录与数据集关联
        DocumentDataset dd=new DocumentDataset();
        dd.setDocumentId(rspDocument.getId());
        dd.setDatasetId(dataset.getId());
        documentDatasetService.save(dd);


        //存储列信息，及数据集与列的关联表
        if (columns!=null && columns.size()>0){
            //先删除现有的列名
            List<String> ids=datasetColumnService.listByDatasetId(dataset.getId());
            rspColumnService.removeByIds(ids);
            //删除旧的关联
            datasetColumnService.removeByDatasetId(dataset.getId());

            //插入新的列名
            rspColumnService.saveBatch(columns);
            List<DatasetColumn> dcs=new ArrayList<>();
            for (RspColumn column:columns){
                DatasetColumn tmp=new DatasetColumn();
                tmp.setColumnId(column.getId());
                tmp.setDatasetId(dataset.getId());
                dcs.add(tmp);
            }
            //插入新的关联
            datasetColumnService.saveBatch(dcs);
        }

        //初始化sparkJob
        SparkJob sparkJob=new SparkJob();
        sparkJob.setProxyUser("longhao");
        sparkJob.setQueue("default");
        sparkJob.setExecutorCores(1);
        sparkJob.setExecutorMemory("2g");
        sparkJob.setDriverCores(1);

        List<String> params;
        //================将sourcePath数据 使用RSP转换到 targetPath====================
        RunningTask rspTask=new RunningTask();
        sparkJob.setName("rspTask_"+rspJobDto.getSourcePath()+"_"+targetPath);
        sparkJob.setFile("/tmp/sparkrsp-1.0.jar");
        sparkJob.setClassName("cn.edu.szu.bigdata.RSP");
        params=new ArrayList<>();
        params.add("-i");
        params.add(rspJobDto.getSourcePath());
        params.add("-o");
        params.add(targetPath);
        params.add("-n");
        params.add(rspJobDto.getReduceNumber());
        sparkJob.setArgs(params);
//        JSONObject jb2 = JSONObject.fromObject(sparkJob);
        rspTask.setParameter(JSONObject.toJSONString(sparkJob));
        //设置状态为待启动
        rspTask.setRunningState(SparkJobState.WAITING_SUBMIT.getDescription());
        runningTaskService.save(rspTask);

        //填写rspTask日志
        RspTask rspTaskLog=new RspTask();
        rspTaskLog.setUserId(getLoginUserId());
        rspTaskLog.setUsername(getLoginUser().getUsername());
        rspTaskLog.setParam(rspJobDto.getSourcePath()+"|"+targetPath+"|"+reduceNumber+"|"+rspTask.getTaskId());
        rspTaskService.save(rspTaskLog);
        boolean flag=false;
        if(flag && columnString!=null && columnString.length()>0) {
            System.out.println("============================" + columnString);
            //=============对targetDataset做数据预览================
            WaitTask dataExploreTask = new WaitTask();

            //初始化sparkJob
            SparkJob sparkJob1=new SparkJob();
            sparkJob1.setProxyUser("longhao");
            sparkJob1.setQueue("default");
            sparkJob1.setExecutorCores(1);
            sparkJob1.setExecutorMemory("4g");
            sparkJob1.setDriverCores(1);
            Map<String,Object> map1=new HashMap<>();
            map1.put("spark.yarn.appMasterEnv.PYSPARK_DRIVER_PYTHON","/opt/anaconda3/bin/python");
            map1.put("spark.yarn.appMasterEnv.PYSPARK_PYTHON","/opt/anaconda3/bin/python");
            map1.put("spark.driver.maxResultSize","20g");
            sparkJob1.setConf(map1);
            sparkJob1.setName("dataExploreTask_" + rspJobDto.getSourcePath() + "_" + targetPath);
            sparkJob1.setFile("/tmp/dataExplore.py");
            //执行程序的参数
            params = new ArrayList<>();
            //目标数据路径，分隔符，数据字典
            params.add("-i");
            params.add(targetPath);
            params.add("-s");
            params.add(rspJobDto.getSeparatorFlag());
            params.add("-c");
            params.add(columnString);
            sparkJob1.setArgs(params);
            dataExploreTask.setParameter(JSONObject.toJSONString(sparkJob1));
            dataExploreTask.setParentTaskId(rspTask.getTaskId());

            //===========将数据预览graph数据从hdfs存入mysql========
            WaitTask graphDataLoadTask = new WaitTask();
            String datasetId = dataset.getId();
            graphDataLoadTask.setTaskType(1);
            graphDataLoadTask.setParentTaskId(dataExploreTask.getTaskId());
            graphDataLoadTask.setParameter(datasetId);
            waitTaskService.save(graphDataLoadTask);


//        sparkJob.setName("graphDataLoad_"+rspJobDto.getSourcePath()+"_"+targetPath);
//        //执行程序的hdfs路径
//        sparkJob.setFile("/tmp/graphDataLoad.py");
            //执行程序的参数

            //数据路径
//        params.add(targetPath);
//        JSONObject jsonObject=JSONObject.fromObject(sparkJob);
//        graphDataLoadTask.setParameter(jsonObject.toString());
//        waitTaskService.save(graphDataLoadTask);

        dataExploreTask.setChildTaskId(graphDataLoadTask.getTaskId());
        waitTaskService.save(dataExploreTask);

        graphDataLoadTask.setParentTaskId(dataExploreTask.getTaskId());
        waitTaskService.updateById(graphDataLoadTask);

        rspTask.setChildTaskId(dataExploreTask.getTaskId());
        runningTaskService.updateById(rspTask);
        }
        return JsonResult.ok("任务提交成功");
    }

}
