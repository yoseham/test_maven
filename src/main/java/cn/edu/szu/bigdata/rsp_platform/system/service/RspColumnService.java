package cn.edu.szu.bigdata.rsp_platform.system.service;

import cn.edu.szu.bigdata.rsp_platform.system.model.RspColumn;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspDocument;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 数据集的属性表 服务类
 * </p>
 *
 * @author longhao
 * @since 2019-08-09
 */
public interface RspColumnService extends IService<RspColumn> {

    public boolean removeById(String id);

    public boolean removeByIds(List<String> ids);
}
