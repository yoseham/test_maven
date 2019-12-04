package cn.edu.szu.bigdata.rsp_platform.system.controller;

import cn.edu.szu.bigdata.rsp_platform.common.JsonResult;
import cn.edu.szu.bigdata.rsp_platform.common.PageParam;
import cn.edu.szu.bigdata.rsp_platform.common.PageResult;
import cn.edu.szu.bigdata.rsp_platform.common.log.BussinessLog;
import cn.edu.szu.bigdata.rsp_platform.common.utils.StringUtil;
import cn.edu.szu.bigdata.rsp_platform.system.model.*;
import cn.edu.szu.bigdata.rsp_platform.system.service.DocumentDatasetService;
import cn.edu.szu.bigdata.rsp_platform.system.service.RspDocumentService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author longhao
 * @date 2019/8/10 0:24
 */

@Api(value = "RSP数据管理", tags = "rspDocument")
@RestController
@RequestMapping("${api.version}/rspDocument")
public class RspDocumentController {


    @Autowired
    RspDocumentService rspDocumentService;

    @Autowired
    DocumentDatasetService documentDatasetService;


    //新增数据集目录
    @BussinessLog(operEvent = "添加数据集目录",operType = 1)
    @ApiOperation(value = "添加数据集目录")
//    @PreAuthorize("hasAuthority('post:/v1/rspDocument')")
    @PostMapping()
    public JsonResult createRspDocument(RspDocument rspDocument){
        rspDocumentService.save(rspDocument);
        return JsonResult.ok("添加成功");
    }

    //查询所有数据集目录
    @BussinessLog(operEvent = "查询所有数据集目录",operType = 1)
    @ApiOperation(value = "查询所有数据集目录")
//    @PreAuthorize("hasAuthority('get:/v1/rspDocument')")
    @GetMapping()
    public PageResult<RspDocument> getAllRspDocument(HttpServletRequest request){
        PageParam pageParam = new PageParam(request);
        pageParam.setDefaultOrder(new String[]{"create_time"}, null);
        QueryWrapper<RspDocument> queryWrapper = new QueryWrapper();
        String keyword =  pageParam.getString("keyword");
        if (StringUtil.isNotBlank(keyword)) {
            queryWrapper.like("name", keyword);
        }
        List<RspDocument> result = rspDocumentService.page(pageParam, queryWrapper).getRecords();
        return new PageResult<>(result, pageParam.getTotal());
    }

    //构建树形数据集目录
    @BussinessLog(operEvent = "构建树形数据集目录",operType = 1)
    @ApiOperation(value = "构建树形数据集目录")
//    @PreAuthorize("hasAuthority('get:/v1/rspDocument/rspTree')")
    @GetMapping("/rspTree")
    public JsonResult rspTree(){
        return JsonResult.ok("").put("data",documentDatasetService.rspTree());
    }


    //查询数据目录
    @BussinessLog(operEvent = "查询数据集目录",operType = 1)
    @ApiOperation(value = "查询数据集目录")
//    @PreAuthorize("hasAuthority('get:/v1/rspDocument/{id}')")
    @GetMapping("{id}")
    public JsonResult getRspDocumentById(@PathVariable("id") String documentId){
        RspDocument result=rspDocumentService.getById(documentId);
        return JsonResult.ok("成功").put("data",result);
    }


    //修改数据目录
    @BussinessLog(operEvent = "修改数据集目录",operType = 1)
    @ApiOperation(value = "修改数据集目录")
//    @PreAuthorize("hasAuthority('put:/v1/rspDocument')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "name", value = "数据集目录名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "volume", value = "数据集目录大小", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "hdfsLocation", value = "hdfs路径", paramType = "query", dataType = "String"),
    })
    @PutMapping()
    public JsonResult updateRspDocument(@ApiIgnore RspDocument rspDocument){
        if(rspDocumentService.updateById(rspDocument)){
            return JsonResult.ok("更新成功");
        }
        return JsonResult.error("更新失败");
    }
    //删除数据目录
    @BussinessLog(operEvent = "删除数据集目录",operType = 1)
    @ApiOperation(value = "删除数据集目录")
//    @PreAuthorize("hasAuthority('delete:/v1/rspDocument/{id}')")
    @DeleteMapping("/{id}")
    public JsonResult deleteRspDocument(@PathVariable("id") String documentId){
        //级联删除
        if (rspDocumentService.removeById(documentId)){
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }
}
