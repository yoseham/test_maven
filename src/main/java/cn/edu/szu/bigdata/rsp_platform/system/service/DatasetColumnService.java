package cn.edu.szu.bigdata.rsp_platform.system.service;

import cn.edu.szu.bigdata.rsp_platform.system.model.DatasetColumn;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 数据集与属性关联表 服务类
 * </p>
 *
 * @author longhao
 * @since 2019-08-09
 */
public interface DatasetColumnService extends IService<DatasetColumn> {
    public List<String> listByDatasetId(String datasetId);

    public boolean removeByDatasetId(String datasetId);

    public boolean removeByColumnId(String columnId);
}
