package cn.edu.szu.bigdata.rsp_platform.system.controller;


import cn.edu.szu.bigdata.rsp_platform.common.JsonResult;
import cn.edu.szu.bigdata.rsp_platform.common.log.BussinessLog;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspTrainMode;
import cn.edu.szu.bigdata.rsp_platform.system.service.RspTrainModeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.edu.szu.bigdata.rsp_platform.common.BaseController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 训练模式表 前端控制器
 * </p>
 *
 * @author longhao
 * @since 2019-08-23
 */
@Api(value = "RSP模型管理", tags = "rspTrainMode")
@RestController
@RequestMapping("/${api.version}/rspTrainMode")
public class RspTrainModeController extends BaseController {

    @Autowired
    private RspTrainModeService rspTrainModeService;


    //添加训练模式
    @BussinessLog(operEvent = "添加训练模式",operType = 1)
    @ApiOperation(value = "添加训练模式")
    @PostMapping()
    public JsonResult createTrainMode(RspTrainMode rspTrainMode){
        rspTrainModeService.save(rspTrainMode);
        return JsonResult.ok("成功");
    }

    //获取所有训练模式
    @BussinessLog(operEvent = "获取所有训练模式",operType = 1)
    @ApiOperation(value = "获取所有训练模式")
    @GetMapping()
    public JsonResult getAllTrainMode(HttpServletRequest request){
        List<RspTrainMode> trainModeList=rspTrainModeService.list();
        return JsonResult.ok("成功").put("data",trainModeList);
    }

    //根据id获取训练模式
    @BussinessLog(operEvent = "根据id获取训练模式",operType = 1)
    @ApiOperation(value = "根据id获取训练模式")
    @GetMapping("{id}")
    public JsonResult getTrainModeById(@PathVariable("id") String id){
        RspTrainMode result=rspTrainModeService.getById(id);
        return JsonResult.ok("成功").put("data",result);
    }


    @BussinessLog(operEvent = "修改训练模式",operType = 1)
    @ApiOperation(value = "修改训练模式")
    @PutMapping()
    public JsonResult updateTrainMode(RspTrainMode rspTrainMode){
        if(rspTrainModeService.updateById(rspTrainMode)){
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }


    @BussinessLog(operEvent = "删除训练模式",operType = 1)
    @ApiOperation(value = "删除训练模式")
    @DeleteMapping()
    public JsonResult deleteTrainMode(@PathVariable("id") String id){
        if (rspTrainModeService.removeById(id)){
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }
}

