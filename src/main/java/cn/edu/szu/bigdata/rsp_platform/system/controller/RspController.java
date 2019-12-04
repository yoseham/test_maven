package cn.edu.szu.bigdata.rsp_platform.system.controller;

import cn.edu.szu.bigdata.rsp_platform.common.BaseController;
import cn.edu.szu.bigdata.rsp_platform.common.exception.BusinessException;
import cn.edu.szu.bigdata.rsp_platform.common.log.BussinessLog;
import cn.edu.szu.bigdata.rsp_platform.common.JsonResult;
import cn.edu.szu.bigdata.rsp_platform.common.utils.JSONUtil;
import cn.edu.szu.bigdata.rsp_platform.common.utils.StringUtil;
import cn.edu.szu.bigdata.rsp_platform.system.model.*;
import cn.edu.szu.bigdata.rsp_platform.system.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author longhao
 * @date 2019/6/21 22:21
 */
@Api(value = "RSP数据转换", tags = "rsp")
@RestController
@RequestMapping("${api.version}/rsp")
public class RspController extends BaseController {

    private String baseURL = "/tmp/rsp";

    @Autowired
    RspService rspService;

    @Autowired
    DatasetService datasetService;

    @Autowired
    RspDocumentService rspDocumentService;

    @Autowired
    RspDatasetService rspDatasetService;

    @Autowired
    RspColumnService rspColumnService;

    @Autowired
    DatasetColumnService datasetColumnService;

    @Autowired
    RspDataExplorationTaskService dataExplorationTaskService;

    @Autowired
    DocumentDatasetService documentDatasetService;


    @BussinessLog(operEvent = "RSP转换", operType = 0)
//    @PreAuthorize("hasAuthority('post:/v1/rsp/convert')")
//    @ApiIgnore
//    @ApiOperation(value = "rsp转换")
    @PostMapping("/convert")
    public JsonResult convert(HttpServletRequest request) {
        String sourcePath = request.getParameter("sourcePath");
        String targetPath = request.getParameter("targetPath");
        String reduceNumber = request.getParameter("reduceNumber");
        try {

            return rspService.execRsp(sourcePath, targetPath, reduceNumber);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(500, "系统异常");
        }
    }


    @BussinessLog(operEvent = "RSP转换test", operType = 0)
//    @PreAuthorize("hasAuthority('post:/v1/rsp/test')")
//    @ApiIgnore
//    @ApiOperation(value = "rsp转换test")
    @PostMapping("/test")
    public JsonResult test(HttpServletRequest request) throws Exception {
        String sourcePath = request.getParameter("sourcePath");
        String datasetName = request.getParameter("datasetName");
        String dataName = request.getParameter("dataName");
        String reduceNumber = request.getParameter("reduceNumber");
        String targetPath = baseURL + "/" + datasetName.trim() + "/" + dataName.trim();

        QueryWrapper<Dataset> queryWrapper = new QueryWrapper();
        //根据地址判断
        queryWrapper.eq("address",targetPath);
        List<Dataset> result=datasetService.list(queryWrapper);
        if (result.size()==0){
            String parentId = genParentId(datasetName);
            Dataset dataset = new Dataset();
            dataset.setParentId(parentId);
            dataset.setAddress(targetPath);
            dataset.setName(dataName);
            datasetService.mySave(dataset);
        }else{
            Dataset dataset=result.get(0);
            dataset.setCapacity("");
            dataset.setBlockNumber("");
            datasetService.updateById(dataset);
        }

        System.out.println(sourcePath + "==" + targetPath + "==" + reduceNumber);

        return rspService.execRsp(sourcePath, targetPath, reduceNumber);
    }


    @BussinessLog(operEvent = "RSP转换V1", operType = 0)
//    @PreAuthorize("hasAuthority('post:/v1/rsp/convertV1')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sourcePath", value = "hdfs源路径", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "documentName", value = "数据目录名称",required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "datasetName", value = "数据集名称", required = true,paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "introduction", value = "数据集简介", required = true,paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "reduceNumber", value = "块数目", required = true, paramType = "query", dataType = "String"),
    })
    @ApiOperation(value = "RSP转换V1")
    @PostMapping("/convertV1")
    public JsonResult rspConvertV1(HttpServletRequest request) throws Exception{
        String sourcePath = request.getParameter("sourcePath");
        String documentName = request.getParameter("documentName");
        String datasetName = request.getParameter("datasetName");
        String reduceNumber = request.getParameter("reduceNumber");
        String introduction = request.getParameter("introduction");
        String targetPath = baseURL + "/" + documentName.trim() + "/" + datasetName.trim();
        String documentPath = baseURL + "/" + documentName.trim();

        //数据集为空，新建
        RspDataset dataset=rspDatasetService.selectByHdfsLocation(targetPath);
        if (dataset==null){
            dataset=new RspDataset();
            dataset.setName(datasetName);
            dataset.setHdfsLocation(targetPath);
            rspDatasetService.save(dataset);
        }
        dataset.setIntroduction(introduction);
        dataset.setBlockNumber(reduceNumber);
        String vol=rspService.getPathCapacity(sourcePath);
        System.out.println("容量：---------"+vol);
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
            rspDocument.setName(documentName);
            rspDocument.setVolume("0");
            rspDocument.setHdfsLocation(documentPath);
            rspDocumentService.save(rspDocument);
        }
        Long a=Long.parseLong(rspDocument.getVolume());
        Long b=Long.parseLong(dataset.getVolume());
        rspDocument.setVolume(Long.toString(a+b));
        rspDocumentService.updateById(rspDocument);

        //保存关联表

        DocumentDataset dd=new DocumentDataset();
        dd.setDocumentId(rspDocument.getId());
        dd.setDatasetId(dataset.getId());
        documentDatasetService.save(dd);

        return rspService.execRsp(sourcePath, targetPath, reduceNumber);
    }




    @BussinessLog(operEvent = "RSP转换V2", operType = 0)
//    @PreAuthorize("hasAuthority('post:/v1/rsp/convertV2')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "separatorFlag", value = "分隔符", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "columns", value = "数据字典", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "sourcePath", value = "hdfs源路径", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "documentName", value = "数据目录名称",required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "datasetName", value = "数据集名称", required = true,paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "introduction", value = "数据集简介", required = true,paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "reduceNumber", value = "块数目", required = true, paramType = "query", dataType = "String"),
    })
    @ApiOperation(value = "RSP转换V2")
    @PostMapping("/convertV2")
    @Transactional
    public JsonResult rspConvertV2(HttpServletRequest request) throws Exception{
        String sourcePath = request.getParameter("sourcePath");
        String separatorFlag = request.getParameter("separatorFlag");
        String documentName = request.getParameter("documentName");
        String datasetName = request.getParameter("datasetName");
        String reduceNumber = request.getParameter("reduceNumber");
        String columnString= request.getParameter("columns");


        List<RspColumn> columns=new ArrayList<>();
        if(StringUtil.isNotBlank(columnString)){
            try {
                columns = JSONUtil.parseArray(columnString, RspColumn.class);
            }catch (Exception e){
                throw new BusinessException(401,"columns填写错误");
            }
        }
        String introduction = request.getParameter("introduction");
        String targetPath = baseURL + "/" + documentName.trim() + "/" + datasetName.trim();
        String documentPath = baseURL + "/" + documentName.trim();
        if(documentName.indexOf("/")!=-1){
            targetPath = documentName.trim() + "/" + datasetName.trim();
            documentPath = documentName.trim();
        }
        //数据集为空，新建
        RspDataset dataset=rspDatasetService.selectByHdfsLocation(targetPath);
        if (dataset==null){
            dataset=new RspDataset();
            dataset.setName(datasetName);
            dataset.setHdfsLocation(targetPath);
            rspDatasetService.save(dataset);
        }
        dataset.setIntroduction(introduction);
        dataset.setBlockNumber(reduceNumber);
        String vol=rspService.getPathCapacity(sourcePath);

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
            rspDocument.setName(documentName);
            rspDocument.setVolume("0");
            rspDocument.setHdfsLocation(documentPath);
            rspDocumentService.save(rspDocument);
        }
        Long a=Long.parseLong(rspDocument.getVolume());
        Long b=Long.parseLong(dataset.getVolume());
        rspDocument.setVolume(Long.toString(a+b));
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
        //存储数据探索任务
        RspDataExplorationTask dataExplorationTask=new RspDataExplorationTask();
        dataExplorationTask.setDatasetId(dataset.getId());
        dataExplorationTaskService.save(dataExplorationTask);

//        return rspService.execRsp(sourcePath, targetPath, reduceNumber);
        boolean result=rspService.submitRspJob(sourcePath, targetPath, reduceNumber);
        if (result){
            return JsonResult.ok("任务提交成功");
        }else{
            return JsonResult.error("任务提交失败");
        }
    }



    /**
     * @param name
     * @return
     */
    private String genParentId(String name) throws Exception{
        QueryWrapper<Dataset> queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id", "-1").eq("name", name);
        List<Dataset> list = datasetService.list(queryWrapper);
        if (null != list && list.size() > 0) {
            return list.get(0).getDatasetId();
        } else {
            Dataset dataset = new Dataset();
            dataset.setName(name);
            dataset.setAddress(baseURL + "/" + name);
            dataset.setParentId("-1");
            datasetService.mySave(dataset);
            return dataset.getDatasetId();
        }
    }


    @BussinessLog(operEvent = "RSP数据查询", operType = 0)
//    @PreAuthorize("hasAuthority('post:/v1/rsp/getSubpaths')")
    @ApiOperation(value = "获取路径地址的子路径")
    @ApiImplicitParam(name = "path", value = "hdfs路径", required = true, paramType = "query", dataType = "String")
    @PostMapping("/getSubpaths")
    public JsonResult getSubPaths(HttpServletRequest request) {
        String path = request.getParameter("path");
        try {
            List<String> names = rspService.getAllPathName(path);
            return JsonResult.ok(200, names);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("系统异常");
        }
    }
}
