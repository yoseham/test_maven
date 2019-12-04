package cn.edu.szu.bigdata.rsp_platform.system.service.impl;

import cn.edu.szu.bigdata.rsp_platform.common.hdfs.HdfsOperation;
import cn.edu.szu.bigdata.rsp_platform.common.utils.StringUtil;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspBlock;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspColumn;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspDataset;
import cn.edu.szu.bigdata.rsp_platform.system.dao.RspDatasetMapper;
import cn.edu.szu.bigdata.rsp_platform.system.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 数据集 服务实现类
 * </p>
 *
 * @author longhao
 * @since 2019-08-09
 */
@Service
public class RspDatasetServiceImpl extends ServiceImpl<RspDatasetMapper, RspDataset> implements RspDatasetService {
    @Autowired
    DatasetBlockService datasetBlockService;

    @Autowired
    DatasetColumnService datasetColumnService;

    @Autowired
    RspBlockService rspBlockService;

    @Autowired
    RspColumnService rspColumnService;

    @Autowired
    BlockViewService blockViewService;

    @Autowired
    RspDatasetService rspDatasetService;

    public boolean removeById(String datasetId){

        //找出所有的与datasetId相关的所有blockId,columnId
        List<String> blockIds=datasetBlockService.listByDatasetId(datasetId);
        List<String> columnIds=datasetColumnService.listByDatasetId(datasetId);

        // 在关联表中删除与datasetId相关的blockId,columnId
        boolean result1=datasetBlockService.removeByDatasetId(datasetId);
        boolean result2=datasetColumnService.removeByDatasetId(datasetId);

        //在block表中删除相关的所有block,columnId
        boolean result3=rspBlockService.removeByIds(blockIds);
        boolean result4=rspColumnService.removeByIds(columnIds);

        //删除前端绘图
        boolean result5=blockViewService.deleteByDatasetId(datasetId);

        //删除datasetId
        boolean result6=super.removeById(datasetId);
        return result1&&result2&&result3&&result4&&result5&&result6;
    }

    public boolean removeByIds(List<String> ids) {
        for (String id:ids){
            removeById(id);
        }
        return true;
    }

    public boolean save(RspDataset rspDataset){
        QueryWrapper<RspDataset> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("hdfs_location",rspDataset.getHdfsLocation());
        List<RspDataset> result=baseMapper.selectList(queryWrapper);
        boolean flag=false;
        if (result.size()==0){
            flag=true;
        }
        return flag&&super.save(rspDataset);
    }

    public RspDataset selectByHdfsLocation(String hdfsLocation){
        QueryWrapper<RspDataset> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("hdfs_location",hdfsLocation);
        return baseMapper.selectOne(queryWrapper);
    }

    public List<Map<String,String>> preview(String rspDatasetId) throws Exception {
        List<String> blockIds=datasetBlockService.listByDatasetId(rspDatasetId);
        List<String> columnIds=datasetColumnService.listByDatasetId(rspDatasetId);

        RspDataset rspDataset = rspDatasetService.getById(rspDatasetId);
        String seq = rspDataset.getSeparatorFlag();
        seq=StringUtil.escapeQueryChars(seq);
        //默认读取第一个块
        RspBlock block=rspBlockService.getById(blockIds.get(0));
        String path=block.getHdfsLocation();
        List<Map<String,String>> result=new ArrayList<>();
        if (columnIds.size()==0){
            return result;
        }
        List<RspColumn> columns= (List<RspColumn>) rspColumnService.listByIds(columnIds);
        List<String> dataList = HdfsOperation.get_100(path);
        for(String datalist : dataList){
            Map<String,String> map=new HashMap<>();
//            List<String> list = Arrays.asList(datalist.split(seq));
            String[] list = datalist.split(seq);
            for(int i = 0, l = columns.size(); i<l; i++){
                map.put(columns.get(i).getName(), list[i]);
            }
            result.add(map);
        }
        return result;
//        //构造假数据
//        List<Map<String,String>> result=new ArrayList<>();
//        if (columnIds.size()==0){
//            return result;
//        }
//        List<RspColumn> rspColumns= (List<RspColumn>) rspColumnService.listByIds(columnIds);
//        Random random=new Random();
//
//        for (int i=0;i<20;i++){
//            Map<String,String> map=new HashMap<>();
//            for (RspColumn col:rspColumns){
//                int tmpInt=random.nextInt(1000);
//                map.put(col.getName(), Integer.toString(tmpInt));
//            }
//            result.add(map);
//        }
//        return result;
    }



}
