package cn.edu.szu.bigdata.rsp_platform.system.service.impl;

import cn.edu.szu.bigdata.rsp_platform.common.exception.BusinessException;
import cn.edu.szu.bigdata.rsp_platform.system.model.DatasetBlock;
import cn.edu.szu.bigdata.rsp_platform.system.dao.DatasetBlockMapper;
import cn.edu.szu.bigdata.rsp_platform.system.service.DatasetBlockService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 数据集与数据块表 服务实现类
 * </p>
 *
 * @author longhao
 * @since 2019-08-09
 */
@Service
public class DatasetBlockServiceImpl extends ServiceImpl<DatasetBlockMapper, DatasetBlock> implements DatasetBlockService {

    @Override
    public List<String> listByDatasetId(String datasetId) {
        QueryWrapper<DatasetBlock> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("dataset_id",datasetId);
        List<DatasetBlock> result=baseMapper.selectList(queryWrapper);
        List<String> blockIds=new ArrayList<>();
        for (DatasetBlock db:result
        ) {
            blockIds.add(db.getBlockId());
        }
        return blockIds;
    }

    @Override
    public boolean removeByDatasetId(String datasetId) {
        QueryWrapper<DatasetBlock> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("dataset_id",datasetId);
        if (baseMapper.delete(queryWrapper)>=0){
            return true;
        }
        return false;
    }



    @Override
    public boolean removeByBlockId(String blockId) {
        QueryWrapper<DatasetBlock> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("block_id",blockId);
        if (baseMapper.delete(queryWrapper)>=0){
            return true;
        }
        return false;
    }

    @Override
    public DatasetBlock getByBlockId(String blockId) {
        QueryWrapper<DatasetBlock> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("block_id",blockId);
        List<DatasetBlock> blocks=baseMapper.selectList(queryWrapper);
        if (blocks.size()>1){
            throw new BusinessException("数据异常，一个blockId只能对应一个DatasetId");
        }
        if (blocks.size()==0){
            return null;
        }else {
            return blocks.get(0);
        }
    }


}
