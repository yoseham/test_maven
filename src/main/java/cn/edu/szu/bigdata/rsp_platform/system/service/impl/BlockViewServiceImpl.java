package cn.edu.szu.bigdata.rsp_platform.system.service.impl;

import cn.edu.szu.bigdata.rsp_platform.system.model.BlockView;
import cn.edu.szu.bigdata.rsp_platform.system.dao.BlockViewMapper;
import cn.edu.szu.bigdata.rsp_platform.system.service.BlockViewService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端绘图表 服务实现类
 * </p>
 *
 * @author longhao
 * @since 2019-08-09
 */
@Service
public class BlockViewServiceImpl extends ServiceImpl<BlockViewMapper, BlockView> implements BlockViewService {

    @Override
    public List<String> listByIds(String datasetId, String blockId, String columnId){
        QueryWrapper<BlockView> queryWrapper=new QueryWrapper<>();
        Map<String,String> param = new HashMap<String, String>();
        param.put("dataset_id",datasetId);
        param.put("block_id",blockId);
        param.put("column_id",columnId);
        queryWrapper.allEq(param);
        List<BlockView> result=baseMapper.selectList(queryWrapper);
        List<String> dataList=new ArrayList<>();
        for (BlockView db:result
        ) {
            dataList.add(db.getxAxisData());
            dataList.add(db.getyAxisData());
        }
        return dataList;
    }

    @Override
    public boolean deleteByDatasetId(String datasetId) {
        QueryWrapper<BlockView> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("dataset_id",datasetId);
        if (baseMapper.delete(queryWrapper)>=0)
            return true;
        return false;
    }

    @Override
    public boolean deleteByBlockId(String blockId) {
        QueryWrapper<BlockView> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("block_id",blockId);
        if (baseMapper.delete(queryWrapper)>=0)
            return true;
        return false;
    }

    @Override
    public boolean deleteByColumnId(String columnId) {
        QueryWrapper<BlockView> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("column_id",columnId);
        if (baseMapper.delete(queryWrapper)>=0)
            return true;
        return false;
    }
}
