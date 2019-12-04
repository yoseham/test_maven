package cn.edu.szu.bigdata.rsp_platform.system.service;

import cn.edu.szu.bigdata.rsp_platform.system.model.DocumentDataset;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据目录与数据集关联表 服务类
 * </p>
 *
 * @author longhao
 * @since 2019-08-09
 */
public interface DocumentDatasetService extends IService<DocumentDataset> {


    //获取数据目录下的数据集
    public List<String> listByDocumentId(String DocumentId);

    public boolean removeByDocumentId(String DocumentId);

    public boolean removeByDatasetId(String datasetId);

    public List<Map<String,Object>> rspTree();

    public boolean save(DocumentDataset documentDataset);
}
