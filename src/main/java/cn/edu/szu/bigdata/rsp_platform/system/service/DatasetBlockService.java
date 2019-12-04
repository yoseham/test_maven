package cn.edu.szu.bigdata.rsp_platform.system.service;

import cn.edu.szu.bigdata.rsp_platform.system.model.DatasetBlock;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 数据集与数据块表 服务类
 * </p>
 *
 * @author longhao
 * @since 2019-08-09
 */
public interface DatasetBlockService extends IService<DatasetBlock> {
    public List<String> listByDatasetId(String datasetId);

    public boolean removeByDatasetId(String datasetId);

    public boolean removeByBlockId(String blockId);

    public DatasetBlock getByBlockId(String blockId);
}
