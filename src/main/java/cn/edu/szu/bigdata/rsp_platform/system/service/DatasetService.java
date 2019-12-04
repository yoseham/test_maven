package cn.edu.szu.bigdata.rsp_platform.system.service;

import cn.edu.szu.bigdata.rsp_platform.system.model.Dataset;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * <p>
 * 数据集 服务类
 * </p>
 *
 * @author longhao
 * @since 2019-06-25
 */
public interface DatasetService extends IService<Dataset> {
    void rebuildRspTree() throws Exception;

    void removeByMyId(String id) throws Exception;

    boolean updateByMyId(Dataset dataset) throws Exception;

    boolean mySave(Dataset entity) throws Exception;
}
