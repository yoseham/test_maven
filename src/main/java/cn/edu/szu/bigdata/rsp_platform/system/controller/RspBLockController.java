package cn.edu.szu.bigdata.rsp_platform.system.controller;

import cn.edu.szu.bigdata.rsp_platform.common.JsonResult;
import cn.edu.szu.bigdata.rsp_platform.common.PageParam;
import cn.edu.szu.bigdata.rsp_platform.common.PageResult;
import cn.edu.szu.bigdata.rsp_platform.common.hdfs.HdfsOperation;
import cn.edu.szu.bigdata.rsp_platform.common.log.BussinessLog;
import cn.edu.szu.bigdata.rsp_platform.common.utils.StringUtil;
import cn.edu.szu.bigdata.rsp_platform.system.model.DatasetBlock;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspBlock;
//import cn.edu.szu.bigdata.rsp_platform.system.vo.RspBlock;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspDataset;
import cn.edu.szu.bigdata.rsp_platform.system.service.DatasetBlockService;
import cn.edu.szu.bigdata.rsp_platform.system.service.RspBlockService;
import cn.edu.szu.bigdata.rsp_platform.system.service.RspDatasetService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.hadoop.fs.Hdfs;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author longhao
 * @date 2019/8/10 1:34
 */
@Api(value = "RSP数据管理", tags = "rspBlock")
@RestController
@RequestMapping("${api.version}/rspBlock")
public class RspBLockController {
    @Autowired
    RspBlockService rspBlockService;

    @Autowired
    DatasetBlockService datasetBlockService;

    @Autowired
    RspDatasetService rspDatasetService;

    //增加数据块
    @BussinessLog(operEvent = "增加数据块",operType = 1)
    @ApiOperation(value = "增加数据块")
//    @PreAuthorize("hasAuthority('post:/v1/rspBlock')")
    @PostMapping()
    public JsonResult createRspBlock(RspBlock rspBlock,String datasetId){

        rspBlockService.save(rspBlock);
        DatasetBlock datasetBlock=new DatasetBlock();
        datasetBlock.setBlockId(rspBlock.getId());
        datasetBlock.setDatasetId(datasetId);
        datasetBlockService.save(datasetBlock);
        RspDataset rspDataset = rspDatasetService.getById(datasetId);
        rspDataset.setBlockNumber(String.valueOf(Integer.parseInt(rspDataset.getBlockNumber())+1));
        rspDatasetService.updateById(rspDataset);
        System.out.println(rspDataset.getBlockNumber());
        return JsonResult.ok("添加成功");
    }
    //查询数据块
    @BussinessLog(operEvent = "查询数据块",operType = 1)
    @ApiOperation(value = "查询数据块")
//    @PreAuthorize("hasAuthority('get:/v1/rspBlock/{id}')")
    @GetMapping("/{id}")
    public JsonResult getRspBlockById(@PathVariable("id") String blockId){
        RspBlock result=rspBlockService.getById(blockId);
        return JsonResult.ok("查询成功").put("data",result);
    }

    //查询数据集下的数据块
    @BussinessLog(operEvent = "查询数据集下的数据块",operType = 1)
    @ApiOperation(value = "查询数据集下的数据块")
//    @PreAuthorize("hasAuthority('get:/v1/rspBlock/batch/{datasetId}')")
    @GetMapping("batch/{datasetId}")
    public JsonResult getBatchRspBlockByDatasetId(HttpServletRequest request, @PathVariable("datasetId") String datasetId) throws Exception {
//        PageParam pageParam = new PageParam(request);
//        pageParam.setDefaultOrder(new String[]{"create_time"}, null);
        QueryWrapper<RspBlock> queryWrapper = new QueryWrapper();
//        String keyword =  pageParam.getString("keyword");
//        if (StringUtil.isNotBlank(keyword)) {
//            queryWrapper.like("name", keyword);
//        }

        List<String> blockIds=datasetBlockService.listByDatasetId(datasetId);
        List<RspBlock> rspBlockList=new ArrayList<>();
        System.out.println(datasetId);
        System.out.println(blockIds);
        if(blockIds.size()==0){
            RspDataset rspDataset = rspDatasetService.getById(datasetId);
            String path = rspDataset.getHdfsLocation();
            rspBlockList = HdfsOperation.blocksList(path);
            System.out.println(rspBlockList);
            rspBlockService.saveBatch(rspBlockList);
            blockIds=process(rspBlockList,datasetId);
        }else{
            rspBlockList= (List<RspBlock>) rspBlockService.listByIds(blockIds);
        }
        return JsonResult.ok("").put("data",rspBlockList);
    }

    private List<String> process(List<RspBlock> blockList,String datasetId){
        List<DatasetBlock> datasetBlockList=new ArrayList<>();
        List<String> blockIds=new ArrayList<>();
        for (RspBlock blk:blockList){
            blockIds.add(blk.getId());
            DatasetBlock datasetBlock=new DatasetBlock();
            datasetBlock.setBlockId(blk.getId());
            datasetBlock.setDatasetId(datasetId);
            datasetBlockList.add(datasetBlock);
        }
        datasetBlockService.removeByDatasetId(datasetId);
        datasetBlockService.saveBatch(datasetBlockList);
//        RspDataset rspDataset = rspDatasetService.getById(datasetId);
//        rspDataset.setBlockNumber(Integer.toString(blockList.size()));
//        rspDatasetService.updateById(rspDataset);
        return blockIds;
    }

    //修改数据块
    @BussinessLog(operEvent = "修改数据块",operType = 1)
    @ApiOperation(value = "修改数据块")
//    @PreAuthorize("hasAuthority('put:/v1/rspBlock')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "name", value = "块名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "volume", value = "块大小", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "hdfsLocation", value = "hdfs路径", paramType = "query", dataType = "String"),
    })
    @PutMapping()
    public JsonResult updateRspBlock(@ApiIgnore RspBlock rspBlock){
        if (rspBlockService.updateById(rspBlock)){
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    //删除数据块
    @BussinessLog(operEvent = "删除数据块",operType = 1)
    @ApiOperation(value = "删除数据块")
//    @PreAuthorize("hasAuthority('delete:/v1/rspBlock/{id}')")
    @DeleteMapping("/{id}")
    public JsonResult deleteRspBlockById(@PathVariable("id") String blockId){
        if (rspBlockService.removeById(blockId)){
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    //块数据预览
    @BussinessLog(operEvent = "块数据预览",operType= 1)
    @ApiOperation("块数据预览")
    @GetMapping("/preview/{id}")
    public JsonResult preview(@PathVariable("id") String blockId) throws Exception {
        List<Map<String,String>> result=rspBlockService.preview(blockId);
        return JsonResult.ok().put("data",result);
    }
}
