package cn.edu.szu.bigdata.rsp_platform.system.controller;

import cn.edu.szu.bigdata.rsp_platform.common.JsonResult;
import cn.edu.szu.bigdata.rsp_platform.common.PageParam;
import cn.edu.szu.bigdata.rsp_platform.common.PageResult;
import cn.edu.szu.bigdata.rsp_platform.common.log.BussinessLog;
import cn.edu.szu.bigdata.rsp_platform.common.utils.StringUtil;
import cn.edu.szu.bigdata.rsp_platform.system.model.OperationLog;
import cn.edu.szu.bigdata.rsp_platform.system.service.OperationLogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author longhao
 * @date 2019/6/24 10:37
 */
@Api(value = "日志管理", tags = "operationLog")
@RestController
@RequestMapping("${api.version}/operationLog")
public class OperationLogController {
    @Autowired
    private OperationLogService operationLogService;


    @ApiOperation(value = "查询所有日志")
//    @PreAuthorize("hasAuthority('get:/v1/operationLog')")
    @GetMapping()
    public PageResult<OperationLog> list(HttpServletRequest request) {
        PageParam pageParam = new PageParam(request);
        pageParam.setDefaultOrder(null,new String[]{"create_time"});
        QueryWrapper<OperationLog> queryWrapper = new QueryWrapper();
        String keyword =  pageParam.getString("keyword");

        if (StringUtil.isNotBlank(keyword)) {
            queryWrapper.like("username", keyword).or().like("oper_event", keyword);
        }
        List<OperationLog> list = operationLogService.page(pageParam, queryWrapper).getRecords();
        return new PageResult<>(list, pageParam.getTotal());
    }


    @BussinessLog(operEvent = "删除日志",operType = 1)
    @ApiOperation(value = "删除日志")
//    @PreAuthorize("hasAuthority('delete:/v1/operationLog/{id}')")
    @DeleteMapping("/{id}")
    public JsonResult delete(@PathVariable("id") String OperationLogId) {
        if (operationLogService.removeById(OperationLogId)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }
}
