package cn.edu.szu.bigdata.rsp_platform.system.controller;


import cn.edu.szu.bigdata.rsp_platform.common.JsonResult;
import cn.edu.szu.bigdata.rsp_platform.common.PageParam;
import cn.edu.szu.bigdata.rsp_platform.common.PageResult;
import cn.edu.szu.bigdata.rsp_platform.common.log.BussinessLog;
import cn.edu.szu.bigdata.rsp_platform.common.utils.StringUtil;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspAlgorithm;
import cn.edu.szu.bigdata.rsp_platform.system.service.RspAlgorithmService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.edu.szu.bigdata.rsp_platform.common.BaseController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 算法库表 前端控制器
 * </p>
 *
 * @author Sun Haotong
 * @since 2019-08-23
 */
@Api(value = "RSP模型管理", tags = "rspAlgorithm")
@RestController
@RequestMapping("${api.version}/rspAlgorithm")
public class RspAlgorithmController extends BaseController {

    @Autowired
    RspAlgorithmService rspAlgorithmService;

    //创建算法
    @BussinessLog(operEvent = "创建算法", operType = 1)
    @ApiOperation(value = "创建算法")
    @PostMapping()
    public JsonResult createRspAlgorithm(RspAlgorithm rspAlgorithm){
        if (rspAlgorithmService.save(rspAlgorithm)){
            return JsonResult.ok("成功");
        }
        return JsonResult.error("失败");
    }

    //获取所有算法
    @BussinessLog(operEvent = "获取所有算法", operType = 1)
    @ApiOperation(value = "获取所有算法")
    @GetMapping()
    public PageResult<RspAlgorithm> getAllRspAlgorithm(HttpServletRequest request){
        PageParam pageParam = new PageParam(request);
        pageParam.setDefaultOrder(new String[]{"create_time"}, null);
        QueryWrapper<RspAlgorithm> queryWrapper = new QueryWrapper();
        String keyword =  pageParam.getString("keyword");
        if (StringUtil.isNotBlank(keyword)) {
            queryWrapper.like("algorithm_name", keyword).or().like("algorithm_type", keyword);
        }
        List<RspAlgorithm> list = rspAlgorithmService.page(pageParam, queryWrapper).getRecords();
        return new PageResult<>(list,pageParam.getTotal());
    }

    //根据算法类型获取算法

    @BussinessLog(operEvent = "根据算法类型获取算法", operType = 1)
    @ApiOperation(value = "根据算法类型获取算法")
    @ApiImplicitParam(name = "algorithmType", value = "算法类型",paramType = "query", dataType = "String")
    @PostMapping("/getByAlgorithmType")
    public JsonResult getAlgorithmByAlgorithmType(HttpServletRequest request){
        String algorithmType=request.getParameter("algorithmType");
        QueryWrapper<RspAlgorithm> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("algorithm_type",algorithmType);
        List<RspAlgorithm> list=rspAlgorithmService.list(queryWrapper);
        return JsonResult.ok("成功").put("data",list);
    }


    //获取所有算法类型
    @BussinessLog(operEvent = "获取所有算法类型", operType = 1)
    @ApiOperation(value = "获取所有算法类型")
    @GetMapping("/algorithmType")
    public JsonResult getAllAlgorithmType(){
        QueryWrapper<RspAlgorithm> queryWrapper=new QueryWrapper<>();
        queryWrapper.groupBy("algorithm_type").select("algorithm_type");
        List<RspAlgorithm> results=rspAlgorithmService.list(queryWrapper);
        List<String> algorithmTypes=new ArrayList<>();
        for (RspAlgorithm rspAlgorithm:results){
            algorithmTypes.add(rspAlgorithm.getAlgorithmType());
            System.out.println(rspAlgorithm);
        }
        return JsonResult.ok().put("data",algorithmTypes);
    }

    @BussinessLog(operEvent = "根据id获取算法", operType = 1)
    @ApiOperation(value = "根据id获取算法")
    @GetMapping("{id}")
    public JsonResult getRspAlgorithmById(@PathVariable("id") String id){
        RspAlgorithm result=rspAlgorithmService.getById(id);
        return JsonResult.ok("成功").put("data",result);
    }


    @BussinessLog(operEvent = "修改算法", operType = 1)
    @ApiOperation(value = "修改算法")
    @PutMapping()
    public JsonResult updateRspAlgorithm(RspAlgorithm rspAlgorithm){
        if(rspAlgorithmService.updateById(rspAlgorithm)){
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }


    @BussinessLog(operEvent = "删除算法", operType = 1)
    @ApiOperation(value = "删除算法")
    @DeleteMapping("{id}")
    public JsonResult deleteRspAlgorithmById(@PathVariable("id") String id){
        if(rspAlgorithmService.removeById(id)){
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }
}

