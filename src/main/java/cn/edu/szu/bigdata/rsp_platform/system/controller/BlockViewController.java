package cn.edu.szu.bigdata.rsp_platform.system.controller;

import cn.edu.szu.bigdata.rsp_platform.common.JsonResult;
import cn.edu.szu.bigdata.rsp_platform.common.PageParam;
import cn.edu.szu.bigdata.rsp_platform.common.PageResult;
import cn.edu.szu.bigdata.rsp_platform.common.log.BussinessLog;
import cn.edu.szu.bigdata.rsp_platform.common.utils.StringUtil;
import cn.edu.szu.bigdata.rsp_platform.system.model.BlockView;
import cn.edu.szu.bigdata.rsp_platform.system.service.BlockViewService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author longhao
 * @date 2019/8/10 10:57
 */
@Api(value = "RSP数据管理", tags = "rspBlockView")
@RestController
@RequestMapping("${api.version}/rspBlockView")
public class BlockViewController {

    @Autowired
    BlockViewService blockViewService;

    //新增块视图
    @BussinessLog(operEvent = "新增块视图",operType = 1)
    @ApiOperation(value = "新增块视图")
    @PostMapping()
    public JsonResult createBlockView(BlockView blockView){
        blockViewService.save(blockView);
        return JsonResult.ok("添加成功");
    }
    //查询块视图
    @BussinessLog(operEvent = "查询块视图",operType = 1)
    @ApiOperation(value = "查询块视图")
//    @PreAuthorize("hasAuthority('get:/v1/rspBlockView/{id}')")
    @GetMapping("/{id}")
    public JsonResult getBlockViewById(@PathVariable("id") String blockViewId){
        BlockView result=blockViewService.getById(blockViewId);
        return JsonResult.ok("成功").put("data",result);
    }

    //条件查询块视图，条件为，datasetId,blockId,columnId
    @BussinessLog(operEvent = "条件查询块视图",operType = 1)
    @ApiOperation(value = "条件查询块视图")
//    @PreAuthorize("hasAuthority('get:/v1/rspBlockView/search')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "datasetId", value = "数据集id", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "blockId", value = "块id", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "columnId", value = "列Id", paramType = "query", dataType = "String"),
    })
    @GetMapping("/search")
    public PageResult<BlockView> searchBlockView(HttpServletRequest request){
        QueryWrapper<BlockView> queryWrapper=new QueryWrapper<>();
        PageParam pageParam = new PageParam(request);
        String datasetId=request.getParameter("datasetId");
        String blockId=request.getParameter("blockId");
        String columnId=request.getParameter("columnId");
        if (StringUtil.isNotBlank(datasetId)){
            queryWrapper.eq("dataset_Id",datasetId);
        }
        if(StringUtil.isBlank(blockId)){
            queryWrapper.eq("block_id",blockId);
        }
        if (StringUtil.isBlank(columnId)){
            queryWrapper.eq("column_id",columnId);
        }
        List<BlockView> result=blockViewService.page(pageParam,queryWrapper).getRecords();
        return new PageResult<>(result,pageParam.getTotal());
    }

    //修改块视图
    @BussinessLog(operEvent = "修改块视图",operType = 1)
    @ApiOperation(value = "修改块视图")
//    @PreAuthorize("hasAuthority('put:/v1/rspBlockView')")
    @PutMapping()
    //    @PreAuthorize("hasAuthority('post:/v1/rspBlockView')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "datasetId", value = "数据集id", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "blockId", value = "块id", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "columnId", value = "列Id", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "rowCount", value = "行数", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "xAxisData", value = "x轴", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "yAxisData", value = "y轴", paramType = "query", dataType = "String")

    })
    public JsonResult updateBlockView(@ApiIgnore BlockView blockView){
        if (blockViewService.updateById(blockView)){
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }
    //删除块视图
    @BussinessLog(operEvent = "删除块视图",operType = 1)
    @ApiOperation(value = "删除块视图")
//    @PreAuthorize("hasAuthority('delete:/v1/rspBlockView/{id}')")
    @DeleteMapping("/{id}")
    public JsonResult deleteBlockView(@PathVariable("id") String blockViewId){
        if(blockViewService.removeById(blockViewId)){
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }
}
