package cn.edu.szu.bigdata.rsp_platform.system.controller;


import cn.edu.szu.bigdata.rsp_platform.common.JsonResult;
import cn.edu.szu.bigdata.rsp_platform.common.PageParam;
import cn.edu.szu.bigdata.rsp_platform.common.PageResult;
import cn.edu.szu.bigdata.rsp_platform.common.hdfs.HdfsOperation;
import cn.edu.szu.bigdata.rsp_platform.common.log.BussinessLog;
import cn.edu.szu.bigdata.rsp_platform.common.utils.StringUtil;
import cn.edu.szu.bigdata.rsp_platform.system.model.BlockView;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspBlock;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspDataExplorationTask;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspDataset;
import cn.edu.szu.bigdata.rsp_platform.system.service.BlockViewService;
import cn.edu.szu.bigdata.rsp_platform.system.service.RspBlockService;
import cn.edu.szu.bigdata.rsp_platform.system.service.RspDataExplorationTaskService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.edu.szu.bigdata.rsp_platform.common.BaseController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 数据探索任务 前端控制器
 * </p>
 *
 * @author longhao
 * @since 2019-08-28
 */
@Api(value = "RSP模型管理", tags = "rspDataExplorationTask")
@RestController
@RequestMapping("${api.version}/rspDataExplorationTask")
public class RspDataExplorationTaskController extends BaseController {

    @Autowired
    RspDataExplorationTaskService rspDataExplorationTaskService;

    @Autowired
    BlockViewService blockViewService;

    @Autowired
    RspBlockService rspBlockService;


    @BussinessLog(operEvent = "添加数据探索任务",operType = 1)
    @ApiOperation(value = "添加数据探索任务")
    @PostMapping()
    public JsonResult createRspDataExplorationTask(RspDataExplorationTask rspDataExplorationTask) throws Exception {

//        if(rspDataExplorationTaskService.save(rspDataExplorationTask)){
            String rspDatasetId = rspDataExplorationTask.getDatasetId();
            List<String> blockIds =  new ArrayList<>(Arrays.asList(rspDataExplorationTask.getBlockId().split("\u0001")));
            String columnIds = rspDataExplorationTask.getColumnIds();
            List<Map<String,String>> result=new ArrayList<>();
            blockIds.add(rspDatasetId);
            for (String blockId : blockIds){
                Map<String,String> map=new HashMap<>();
                List<String> dataList = blockViewService.listByIds(rspDatasetId,blockId,columnIds);
                if(dataList.size()!=0){
                    map.put("blockId",blockId);

                    map.put("X",dataList.get(0));
                    map.put("Y",dataList.get(1));
                    if(blockId == rspDatasetId){
                        map.put("type","1");
                        map.put("blockName","AllBlock");
                    }else{
                        map.put("type","0");
                        map.put("blockName",rspBlockService.getById(blockId).getName());
                    }
                    result.add(map);
                }
            }
            return JsonResult.ok("成功").put("data",result);
        }
//        return JsonResult.error("失败");
//    }

    @BussinessLog(operEvent = "获取数据探索任务,分页",operType = 1)
    @ApiOperation(value = "获取数据探索任务,分页")
    @GetMapping()
    public PageResult<RspDataExplorationTask> getAllRspDataExplorationTask(HttpServletRequest request){
        PageParam pageParam = new PageParam(request);
        pageParam.setDefaultOrder(new String[]{"update_time"}, null);
        QueryWrapper<RspDataExplorationTask> queryWrapper = new QueryWrapper();
        String keyword =  pageParam.getString("keyword");
        if (StringUtil.isNotBlank(keyword)) {
            queryWrapper.like("source_data_path", keyword);
        }
        List<RspDataExplorationTask> list=rspDataExplorationTaskService.page(pageParam,queryWrapper).getRecords();
        return new PageResult<>(list,pageParam.getTotal());
    }

    @BussinessLog(operEvent = "删除数据探索任务",operType = 1)
    @ApiOperation(value = "删除数据探索任务")
    @DeleteMapping("{id}")
    public JsonResult deleteRspDataExplorationTaskById(@PathVariable("id") String id){
        if(rspDataExplorationTaskService.removeById(id)){
            return JsonResult.ok("成功");
        }
        return JsonResult.error("失败");
    }
}

