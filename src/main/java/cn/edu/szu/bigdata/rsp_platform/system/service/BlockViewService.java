package cn.edu.szu.bigdata.rsp_platform.system.service;

import cn.edu.szu.bigdata.rsp_platform.system.model.BlockView;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 前端绘图表 服务类
 * </p>
 *
 * @author longhao
 * @since 2019-08-09
 */
public interface BlockViewService extends IService<BlockView> {

    public List<String> listByIds(String datasetId, String blockId, String columnId);

    public boolean deleteByDatasetId(String datasetId);

    public boolean deleteByBlockId(String blockId);

    public boolean deleteByColumnId(String columnId);

}
