package cn.edu.szu.bigdata.rsp_platform.system.controller;

import cn.edu.szu.bigdata.rsp_platform.common.JsonResult;
import cn.edu.szu.bigdata.rsp_platform.common.PageParam;
import cn.edu.szu.bigdata.rsp_platform.common.PageResult;
import cn.edu.szu.bigdata.rsp_platform.common.log.BussinessLog;
import cn.edu.szu.bigdata.rsp_platform.common.utils.StringUtil;
import cn.edu.szu.bigdata.rsp_platform.system.model.*;
import cn.edu.szu.bigdata.rsp_platform.system.service.DatasetColumnService;
import cn.edu.szu.bigdata.rsp_platform.system.service.RspColumnService;
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
import java.util.List;

/**
 * @author longhao
 * @date 2019/8/10 10:28
 */
@Api(value = "RSP数据管理", tags = "rspColumn")
@RestController
@RequestMapping("${api.version}/rspColumn")
public class RspColumnController {

    @Autowired
    RspColumnService rspColumnService;

    @Autowired
    DatasetColumnService datasetColumnService;

    //新增列
    @BussinessLog(operEvent = "新增列",operType = 1)
    @ApiOperation(value = "新增列")
//    @PreAuthorize("hasAuthority('post:/v1/rspColumn')")
    @PostMapping()
    public JsonResult createRspColumn(RspColumn rspColumn,String datasetId){
        rspColumnService.save(rspColumn);
        DatasetColumn datasetColumn=new DatasetColumn();
        datasetColumn.setColumnId(rspColumn.getId());
        datasetColumn.setDatasetId(datasetId);
        datasetColumnService.save(datasetColumn);
        return JsonResult.ok("添加成功");
    }
    //查询列
    @BussinessLog(operEvent = "查询列",operType = 1)
    @ApiOperation(value = "查询列")
//    @PreAuthorize("hasAuthority('get:/v1/rspColumn/{id}')")
    @GetMapping("/{id}")
    public JsonResult getRspColumnById(@PathVariable("id") String columnId){
        RspColumn result=rspColumnService.getById(columnId);
        return JsonResult.ok("成功").put("data",result);
    }

    //查询数据集的列
    @BussinessLog(operEvent = "查询数据集的列",operType = 1)
    @ApiOperation(value = "查询数据集的列")
//    @PreAuthorize("hasAuthority('get:/v1/rspColumn/batch/{datasetId}')")
    @GetMapping("batch/{datasetId}")
    public PageResult<RspColumn> getBatchRspColumnByDatasetId(HttpServletRequest request, @PathVariable("datasetId") String datasetId){
        PageParam pageParam = new PageParam(request);
        pageParam.setDefaultOrder(new String[]{"name"}, null);
        QueryWrapper<RspColumn> queryWrapper = new QueryWrapper();
        String keyword =  pageParam.getString("keyword");
        if (StringUtil.isNotBlank(keyword)) {
            queryWrapper.like("name", keyword);
        }
        List<String> columnIds=datasetColumnService.listByDatasetId(datasetId);
        queryWrapper.in("id",columnIds);
//        List<RspColumn> result=rspColumnService.page(pageParam, queryWrapper).getRecords();
        List<RspColumn> result = (List<RspColumn>)rspColumnService.listByIds(columnIds);
        return new PageResult<>(result, pageParam.getTotal());
    }

    //更新列
    @BussinessLog(operEvent = "更新列",operType = 1)
    @ApiOperation(value = "更新列")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "name", value = "列名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "type", value = "列类型", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "comment", value = "列描述", paramType = "query", dataType = "String"),
    })
//    @PreAuthorize("hasAuthority('put:/v1/rspColumn')")
    @PutMapping()
    public JsonResult updateRspColumn(@ApiIgnore  RspColumn rspColumn){
        if (rspColumnService.updateById(rspColumn)){
            return JsonResult.ok("更新成功");
        }
        return JsonResult.error("更新失败");
    }
    //删除列
    @BussinessLog(operEvent = "删除列",operType = 1)
    @ApiOperation(value = "删除列")
//    @PreAuthorize("hasAuthority('delete:/v1/rspColumn/{id}')")
    @DeleteMapping("/{id}")
    public JsonResult deleteRspColumn(@PathVariable("id") String columnId){
        if (rspColumnService.removeById(columnId)){
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }
}
