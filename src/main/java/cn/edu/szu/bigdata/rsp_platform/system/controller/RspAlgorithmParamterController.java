package cn.edu.szu.bigdata.rsp_platform.system.controller;


import cn.edu.szu.bigdata.rsp_platform.common.JsonResult;
import cn.edu.szu.bigdata.rsp_platform.common.PageParam;
import cn.edu.szu.bigdata.rsp_platform.common.PageResult;
import cn.edu.szu.bigdata.rsp_platform.common.log.BussinessLog;
import cn.edu.szu.bigdata.rsp_platform.common.utils.StringUtil;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspAlgorithm;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspAlgorithmParamter;
import cn.edu.szu.bigdata.rsp_platform.system.service.RspAlgorithmParamterService;
import cn.edu.szu.bigdata.rsp_platform.system.service.RspAlgorithmService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.edu.szu.bigdata.rsp_platform.common.BaseController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 算法参数表 前端控制器
 * </p>
 *
 * @author longhao
 * @since 2019-08-23
 */
@Api(value = "RSP模型管理", tags = "rspAlgorithmParamter")
@RestController
@RequestMapping("/${api.version}/rspAlgorithmParamter")
public class RspAlgorithmParamterController extends BaseController {

    @Autowired
    RspAlgorithmParamterService algorithmParamterService;

    @Autowired
    RspAlgorithmService algorithmService;

    //添加算法参数
    @BussinessLog(operEvent = "添加算法参数",operType = 1)
    @ApiOperation(value = "添加算法参数")
    @PostMapping()
    public JsonResult createAlgorithmParamter(RspAlgorithmParamter rspAlgorithmParamter){
        algorithmParamterService.save(rspAlgorithmParamter);
        return JsonResult.ok("成功");
    }

    //获取所有算法参数，分页
    @BussinessLog(operEvent = "获取所有算法参数，分页",operType = 1)
    @ApiOperation(value = "获取所有算法参数，分页")
    @GetMapping()
    public PageResult<RspAlgorithmParamter> getAllAlgorithmParamter(HttpServletRequest request){
        PageParam pageParam = new PageParam(request);
        pageParam.setDefaultOrder(new String[]{"update_time"}, null);
        QueryWrapper<RspAlgorithmParamter> queryWrapper = new QueryWrapper();
        String keyword =  pageParam.getString("keyword");
        if (StringUtil.isNotBlank(keyword)) {
            queryWrapper.like("paramter_name", keyword);
        }
        List<RspAlgorithmParamter> list=algorithmParamterService.page(pageParam, queryWrapper).getRecords();

        List<String> ids=new ArrayList<>();
        for (RspAlgorithmParamter tmp:list){
            ids.add(tmp.getAlgorithmId());
        }
        if (ids.size()==0){
            return null;
        }
        List<RspAlgorithm> algorithms= (List<RspAlgorithm>) algorithmService.listByIds(ids);

        Map<String,String> algorithmsMap=new HashMap<>();
        for (RspAlgorithm tmp:algorithms){
            algorithmsMap.put(tmp.getId(),tmp.getAlgorithmName());
        }

        for (RspAlgorithmParamter tmp:list){
            if (algorithmsMap.containsKey(tmp.getAlgorithmId())){
                tmp.setAlgorithmName(algorithmsMap.get(tmp.getAlgorithmId()));
            }
        }
        //添加关联算法名称
        return new PageResult<>(list, pageParam.getTotal());
    }

    @BussinessLog(operEvent = "根据id获取算法参数",operType = 1)
    @ApiOperation(value = "根据算法id获取算法参数")
    @GetMapping("{id}")
    public JsonResult getAlgorithmParamterById(@PathVariable("id") String id){
        RspAlgorithmParamter result=algorithmParamterService.getById(id);
        return JsonResult.ok().put("data",result);
    }

    @BussinessLog(operEvent = "根据算法id获取全部参数",operType = 1)
    @ApiOperation(value = "根据算法id获取全部参数")
    @GetMapping("/algorithm/{id}")
    public JsonResult getAlgorithmParamtersByAlgorithmId(@PathVariable("id") String algorithmId){
        List<RspAlgorithmParamter> result=algorithmParamterService.getRspAlgorithmParamtersByAlgorithmId(algorithmId);
        return JsonResult.ok().put("data",result);
    }

    @BussinessLog(operEvent = "更新算法参数",operType = 1)
    @ApiOperation(value = "更新算法参数")
    @PutMapping()
    public JsonResult updateAlgorithmParamter(RspAlgorithmParamter algorithmParamter){
        if (algorithmParamterService.updateById(algorithmParamter)){
            return JsonResult.ok("更新成功");
        }
        return JsonResult.error("更新失败");
    }

    @BussinessLog(operEvent = "删除算法参数",operType = 1)
    @ApiOperation(value = "删除算法参数")
    @DeleteMapping()
    public JsonResult deleteAlgorithmParamter(@PathVariable("id") String id){
        if (algorithmParamterService.removeById(id)){
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }
}

