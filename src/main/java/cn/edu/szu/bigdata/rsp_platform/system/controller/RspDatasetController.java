package cn.edu.szu.bigdata.rsp_platform.system.controller;

import cn.edu.szu.bigdata.rsp_platform.common.JsonResult;
import cn.edu.szu.bigdata.rsp_platform.common.PageParam;
import cn.edu.szu.bigdata.rsp_platform.common.PageResult;
import cn.edu.szu.bigdata.rsp_platform.common.exception.BusinessException;
import cn.edu.szu.bigdata.rsp_platform.common.log.BussinessLog;
import cn.edu.szu.bigdata.rsp_platform.common.utils.StringUtil;
import cn.edu.szu.bigdata.rsp_platform.system.model.DocumentDataset;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspDataset;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspDocument;
import cn.edu.szu.bigdata.rsp_platform.system.service.DocumentDatasetService;
import cn.edu.szu.bigdata.rsp_platform.system.service.RspDatasetService;
import cn.edu.szu.bigdata.rsp_platform.system.service.RspService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author longhao
 * @date 2019/8/10 0:41
 */
@Api(value = "RSP数据管理", tags = "rspDataset")
@RestController
@RequestMapping("${api.version}/rspDataset")
public class RspDatasetController {

    @Autowired
    RspDatasetService rspDatasetService;

    @Autowired
    DocumentDatasetService documentDatasetService;

    @Autowired
    RspService rspService;


    //新增数据集
    @BussinessLog(operEvent = "添加数据集",operType = 1)
    @ApiOperation(value = "添加数据集")
//    @PreAuthorize("hasAuthority('post:/v1/rspDataset')")
    @PostMapping()
    public JsonResult createRspDataset(RspDataset rspDataset,String documentId){
        rspDatasetService.save(rspDataset);
        DocumentDataset documentDataset=new DocumentDataset();
        documentDataset.setDatasetId(rspDataset.getId());
        documentDataset.setDocumentId(documentId);
        documentDatasetService.save(documentDataset);
        return JsonResult.ok("添加成功");
    }
    //查询数据集
    @BussinessLog(operEvent = "查询数据集",operType = 1)
    @ApiOperation(value = "查询数据集")
//    @PreAuthorize("hasAuthority('get:/v1/rspDataset/{id}')")
    @GetMapping("/{id}")
    public JsonResult getDatasetById(@PathVariable("id") String datasetId){
        RspDataset result=rspDatasetService.getById(datasetId);
        return JsonResult.ok("成功").put("data",result);
    }

    //查询数据目录下的数据集
    @BussinessLog(operEvent = "查询数据目录下的数据集",operType = 1)
    @ApiOperation(value = "查询数据目录下的数据集")
//    @PreAuthorize("hasAuthority('get:/v1/rspDataset/batch/{documentId}')")
    @GetMapping("batch/{documentId}")
    public PageResult<RspDataset> getBatchRspDatasetByDocumentId(HttpServletRequest request, @PathVariable("documentId") String documentId){
        PageParam pageParam = new PageParam(request);
        pageParam.setDefaultOrder(new String[]{"create_time"}, null);
        QueryWrapper<RspDataset> queryWrapper = new QueryWrapper();
        String keyword =  pageParam.getString("keyword");
        if (StringUtil.isNotBlank(keyword)) {
            queryWrapper.like("name", keyword);
        }
        List<String> rspDatasetIds=documentDatasetService.listByDocumentId(documentId);
        queryWrapper.in("id",rspDatasetIds);
        List<RspDataset> result = rspDatasetService.page(pageParam, queryWrapper).getRecords();
        return new PageResult<>(result, pageParam.getTotal());
    }

    //修改数据集
    @BussinessLog(operEvent = "修改数据集",operType = 1)
    @ApiOperation(value = "修改数据集")
//    @PreAuthorize("hasAuthority('put:/v1/rspDataset}')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "name", value = "数据集名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "volume", value = "数据集大小", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "blockNumber", value = "块数目", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "hdfsLocation", value = "hdfs路径", paramType = "query", dataType = "String"),
    })
    @PutMapping()
    public JsonResult updateRspDataset(@ApiIgnore RspDataset rspDataset){
        if (rspDatasetService.updateById(rspDataset)){
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    //删除数据集
    @BussinessLog(operEvent = "删除数据集",operType = 1)
    @ApiOperation(value = "删除数据集")
//    @PreAuthorize("hasAuthority('delete:/v1/rspDataset/{id}')")
    @DeleteMapping("/{id}")
    public JsonResult deleteRspDataset(@PathVariable("id") String datasetId){
        if (rspDatasetService.removeById(datasetId)){
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    //数据预览
    @BussinessLog(operEvent = "数据预览",operType = 1)
    @ApiOperation(value = "数据预览")
    @GetMapping("/preview/{id}")
    public JsonResult dataPreview(@PathVariable("id") String datasetId) throws Exception {
        List<Map<String,String>> result=rspDatasetService.preview(datasetId);
        return JsonResult.ok().put("data",result);
    }
}
