package cn.edu.szu.bigdata.rsp_platform.system.controller;


import cn.edu.szu.bigdata.rsp_platform.common.JsonResult;
import cn.edu.szu.bigdata.rsp_platform.common.PageParam;
import cn.edu.szu.bigdata.rsp_platform.common.PageResult;
import cn.edu.szu.bigdata.rsp_platform.common.log.BussinessLog;
import cn.edu.szu.bigdata.rsp_platform.common.utils.StringUtil;
import cn.edu.szu.bigdata.rsp_platform.system.dao.RunningTaskMapper;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspAlgorithm;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspAlgorithmParamter;
import cn.edu.szu.bigdata.rsp_platform.system.model.RunningTask;
import cn.edu.szu.bigdata.rsp_platform.system.service.RunningTaskService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.edu.szu.bigdata.rsp_platform.common.BaseController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 艹ni🐎
 * @since 2019-09-30
 */
@RestController
@RequestMapping("/v1/runningTask")
public class RunningTaskController extends BaseController {

    @Autowired
    RunningTaskService runningTaskService;

    @BussinessLog(operEvent = "runningTask表",operType = 1)
    @ApiOperation(value = "runningTask表")
    @GetMapping()
    public PageResult<RunningTask> runningTask(HttpServletRequest request){
        PageParam pageParam = new PageParam(request);
        pageParam.setDefaultOrder(null,new String[]{"create_time"});
        QueryWrapper<RunningTask> queryWrapper = new QueryWrapper();
        String keyword =  pageParam.getString("keyword");
        if (StringUtil.isNotBlank(keyword)) {
            queryWrapper.like("batch_id", keyword).or().like("application_id", keyword).or().like("parent_task_id",keyword).or().like("child_task_id",keyword);
        }
        List<RunningTask> list = runningTaskService.page(pageParam, queryWrapper).getRecords();
        return new PageResult<>(list,pageParam.getTotal());
    }

    @BussinessLog(operEvent = "根据列ID删除正在进行的任务",operType = 1)
    @ApiOperation(value = "删除正在进行的任务")
    @DeleteMapping("/{id}")
    public JsonResult deleteRunningTask(@PathVariable("id") String task_id){
        if (runningTaskService.removeById(task_id)){
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

//    @BussinessLog(operEvent = "根据数据集id搜索",operType = 1)
//    @ApiOperation(value = "根据数据集id搜索")
//    @GetMapping("{id}")
//    public JsonResult getAlgorithmParamterById(@PathVariable("id") String columnId){
//        RunningTask runningTask=runningTaskService.getById(columnId);
//        return JsonResult.ok().put("data",runningTask);
//    }

}

