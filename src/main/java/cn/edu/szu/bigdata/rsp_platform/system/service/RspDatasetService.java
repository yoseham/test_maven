package cn.edu.szu.bigdata.rsp_platform.system.service;

import cn.edu.szu.bigdata.rsp_platform.system.model.RspDataset;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.poi.ss.formula.functions.T;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据集 服务类
 * </p>
 *
 * @author longhao
 * @since 2019-08-09
 */
public interface RspDatasetService extends IService<RspDataset> {

    public boolean removeById(String datasetId);
    public boolean removeByIds(List<String> ids);

    public boolean save(RspDataset rspDataset);

    public RspDataset selectByHdfsLocation(String hdfsLocation);

    public List<Map<String,String>> preview(String rspDatasetId) throws Exception;
}
