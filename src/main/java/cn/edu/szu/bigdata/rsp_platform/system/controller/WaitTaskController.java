package cn.edu.szu.bigdata.rsp_platform.system.controller;


import cn.edu.szu.bigdata.rsp_platform.common.JsonResult;
import cn.edu.szu.bigdata.rsp_platform.common.PageParam;
import cn.edu.szu.bigdata.rsp_platform.common.PageResult;
import cn.edu.szu.bigdata.rsp_platform.common.log.BussinessLog;
import cn.edu.szu.bigdata.rsp_platform.common.utils.StringUtil;
import cn.edu.szu.bigdata.rsp_platform.system.dao.WaitTaskMapper;
import cn.edu.szu.bigdata.rsp_platform.system.model.FinishedTask;
import cn.edu.szu.bigdata.rsp_platform.system.model.WaitTask;
import cn.edu.szu.bigdata.rsp_platform.system.service.WaitTaskService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
 * @author longhao
 * @since 2019-09-30
 */
@RestController
@RequestMapping("/v1/waitTask")
public class WaitTaskController extends BaseController {

    @Autowired
    WaitTaskService waitTaskService;


    @BussinessLog(operEvent = "waitTask表",operType = 1)
    @ApiOperation(value = "waitTask表")
    @GetMapping()
    public PageResult<WaitTask> finishedTask(HttpServletRequest request){
        PageParam pageParam = new PageParam(request);
        pageParam.setDefaultOrder(null,new String[]{"create_time"});
        QueryWrapper<WaitTask> queryWrapper = new QueryWrapper();
        String keyword =  pageParam.getString("keyword");
        if (StringUtil.isNotBlank(keyword)) {
            queryWrapper.like("child_task_id", keyword).or().like("parent_task_id",keyword);
        }//进行模糊搜索
        List<WaitTask> list =waitTaskService.page(pageParam, queryWrapper).getRecords();
        return new PageResult<>(list,pageParam.getTotal());
    }


    @BussinessLog(operEvent = "根据列ID删除正在进行的任务",operType = 1)
    @ApiOperation(value = "删除正在进行的任务")
    @DeleteMapping("/{id}")
    public JsonResult deletewaitTask(@PathVariable("id") String task_id){
        if (waitTaskService.removeById(task_id)){
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

}

