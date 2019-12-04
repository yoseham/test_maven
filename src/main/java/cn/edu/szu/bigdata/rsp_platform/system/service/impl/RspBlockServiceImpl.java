package cn.edu.szu.bigdata.rsp_platform.system.service.impl;

import cn.edu.szu.bigdata.rsp_platform.common.JsonResult;
import cn.edu.szu.bigdata.rsp_platform.common.hdfs.HdfsOperation;
import cn.edu.szu.bigdata.rsp_platform.common.utils.StringUtil;
import cn.edu.szu.bigdata.rsp_platform.system.model.*;
import cn.edu.szu.bigdata.rsp_platform.system.dao.RspBlockMapper;
import cn.edu.szu.bigdata.rsp_platform.system.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 数据块表 服务实现类
 * </p>
 *
 * @author longhao
 * @since 2019-08-09
 */
@Service
public class RspBlockServiceImpl extends ServiceImpl<RspBlockMapper, RspBlock> implements RspBlockService {

    @Autowired
    DatasetBlockService datasetBlockService;

    @Autowired
    BlockViewService blockViewService;

    @Autowired
    RspBlockService rspBlockService;

    @Autowired
    DatasetColumnService datasetColumnService;

    @Autowired
    RspColumnService rspColumnService;

    @Autowired
    RspDatasetService rspDatasetService;

    public boolean removeById(String id){
        return datasetBlockService.removeByBlockId(id)&&blockViewService.deleteByBlockId(id)&&super.removeById(id);
    }

    public boolean removeByIds(List<String>ids){
        for (String id:ids){
            removeById(id);
        }
        return true;
    }


    @Override
    public List<Map<String, String>> preview(String blockId) throws Exception {



        RspBlock rspBlock=baseMapper.selectById(blockId);
        String path=rspBlock.getHdfsLocation();
        //根据地址去获取数据

        DatasetBlock datasetBlock=datasetBlockService.getByBlockId(blockId);
        String datasetId = datasetBlock.getDatasetId();
        RspDataset rspDataset = rspDatasetService.getById(datasetId);
        String seq = rspDataset.getSeparatorFlag();

        seq= StringUtil.escapeQueryChars(seq);

        List<String> columnIds=datasetColumnService.listByDatasetId(datasetId);
        List<Map<String,String>> result=new ArrayList<>();

        if (columnIds.size()==0){
            return result;
        }
        List<RspColumn> columns= (List<RspColumn>) rspColumnService.listByIds(columnIds);
        List<String> dataList = HdfsOperation.get_100(path);
        for(String datalist : dataList){
            Map<String,String> mapT=new HashMap<>();
            String[] list = datalist.split(seq);
            for(int i = 0, l = columns.size(); i<l; i++){
                mapT.put(columns.get(i).getName(), list[i]);
            }
            result.add(mapT);
        }
        return result;

//        Random random=new Random();
//        for (int i=0;i<20;i++){
//            Map<String,String> map=new HashMap<>();
//            for (RspColumn col:columns){
//                int tmpInt=random.nextInt(1000);
//                map.put(col.getName(), Integer.toString(tmpInt));
//            }
//            result.add(map);
//        }
//        return result;
    }


}
