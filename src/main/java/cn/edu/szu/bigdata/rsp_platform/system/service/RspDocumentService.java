package cn.edu.szu.bigdata.rsp_platform.system.service;

import cn.edu.szu.bigdata.rsp_platform.system.model.RspDocument;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 数据目录 服务类
 * </p>
 *
 * @author longhao
 * @since 2019-08-09
 */
public interface RspDocumentService extends IService<RspDocument> {

    public boolean removeById(String id);
    public boolean removeByIds(List<String> ids);

    public boolean save(RspDocument rspDocument);

    public RspDocument selectByHdfsLocation(String hdfsLocation);
}
