package cn.edu.szu.bigdata.rsp_platform.system.service.impl;

import cn.edu.szu.bigdata.rsp_platform.system.model.DatasetColumn;
import cn.edu.szu.bigdata.rsp_platform.system.dao.DatasetColumnMapper;
import cn.edu.szu.bigdata.rsp_platform.system.service.DatasetColumnService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 数据集与属性关联表 服务实现类
 * </p>
 *
 * @author longhao
 * @since 2019-08-09
 */
@Service
public class DatasetColumnServiceImpl extends ServiceImpl<DatasetColumnMapper, DatasetColumn> implements DatasetColumnService {
    @Override
    public List<String> listByDatasetId(String datasetId) {
        QueryWrapper<DatasetColumn> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("dataset_id",datasetId);
        List<DatasetColumn> result=baseMapper.selectList(queryWrapper);
        List<String> columnIds=new ArrayList<>();
        for (DatasetColumn dd:result
        ) {
            columnIds.add(dd.getColumnId());
        }

        return columnIds;
    }

    @Override
    public boolean removeByDatasetId(String datasetId) {
        QueryWrapper<DatasetColumn> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("dataset_id",datasetId);
        if (baseMapper.delete(queryWrapper)>=0){
            return true;
        }
        return false;
    }

    @Override
    public boolean removeByColumnId(String columnId) {
        QueryWrapper<DatasetColumn> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("column_id",columnId);
        if (baseMapper.delete(queryWrapper)>=0){
            return true;
        }
        return false;
    }
}
