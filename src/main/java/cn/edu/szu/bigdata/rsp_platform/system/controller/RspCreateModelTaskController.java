package cn.edu.szu.bigdata.rsp_platform.system.controller;
import cn.edu.szu.bigdata.rsp_platform.common.JsonResult;
import cn.edu.szu.bigdata.rsp_platform.common.PageParam;
import cn.edu.szu.bigdata.rsp_platform.common.PageResult;
import cn.edu.szu.bigdata.rsp_platform.common.log.BussinessLog;
import cn.edu.szu.bigdata.rsp_platform.common.utils.StringUtil;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspCreateModelTask;
import cn.edu.szu.bigdata.rsp_platform.system.service.RspCreateModelTaskService;
import cn.edu.szu.bigdata.rsp_platform.system.service.impl.RspCreateModelTaskServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 创建模型任务 前端控制器
 * @author SunHaotong
 * @since 2019-08-21
 */
@Api(value = "RSP模型管理", tags = "rspCreateModelTask")
@RestController
@RequestMapping("${api.version}/rspCreateModelTask")
public class RspCreateModelTaskController{
    @Autowired
    RspCreateModelTaskService rspCreateModelTaskService;
    //新增创建模型任务
    @BussinessLog(operEvent = "添加创建模型任务", operType = 1)
    @ApiOperation(value = "添加创建模型任务")
//    @PreAuthorize("hasAuthority('post:/v1/rspCreateModelTask')")
    @PostMapping()
    public JsonResult createRspCreateModelTask(RspCreateModelTask rspCreateModelTask) {
        System.out.println(rspCreateModelTask.toString());
        rspCreateModelTaskService.save(rspCreateModelTask);
        return JsonResult.ok("添加成功");
    }

    //查询创建模型任务
    @BussinessLog(operEvent = "查询创建模型任务", operType = 1)
    @ApiOperation(value = "查询创建模型任务")
//    @PreAuthorize("hasAuthority('get:/v1/rspCreateModelTask')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "model_name", value = "模型名",paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "document_name", value = "数据集目录名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "dataset_name", value = "数据集名", paramType = "query", dataType = "String"),
    })
    @GetMapping()
    public PageResult<RspCreateModelTask> getAllRspCreateModelTask(HttpServletRequest request) {
        PageParam pageParam = new PageParam(request);
        pageParam.setDefaultOrder(new String[]{"create_time"}, null);
        QueryWrapper<RspCreateModelTask> queryWrapper = new QueryWrapper();
        String keyword = pageParam.getString("keyword");
        if (StringUtil.isNotBlank(keyword)) {
            queryWrapper.like("model_name", keyword).or().like("document_id", keyword).or().like("dataset_id", keyword);
        }
        List<RspCreateModelTask> result = rspCreateModelTaskService.page(pageParam, queryWrapper).getRecords();

        return new PageResult<>(result, pageParam.getTotal());
    }


    //根据id查询创建模型任务
    @BussinessLog(operEvent = "根据id查询创建模型任务", operType = 1)
    @ApiOperation(value = "根据id查询创建模型任务")
//    @PreAuthorize("hasAuthority('get:/v1/rspCreateModelTask/{id}')")
    @GetMapping("{id}")
    public JsonResult getRspDocumentById(@PathVariable("id") String id) {
        RspCreateModelTask result = rspCreateModelTaskService.getById(id);
        return JsonResult.ok("成功").put("data", result);
    }
    //删除创建模型任务
    @BussinessLog(operEvent = "删除创建模型任务", operType = 1)
    @ApiOperation(value = "删除创建模型任务")
//    @PreAuthorize("hasAuthority('delete:/v1/rspCreateModelTask/{id}')")
    @DeleteMapping("/{id}")
    public JsonResult deleteRspDocument(@PathVariable("id") String id) {
        if (rspCreateModelTaskService.removeById(id)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }
}

