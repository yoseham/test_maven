package cn.edu.szu.bigdata.rsp_platform.system.service;

import cn.edu.szu.bigdata.rsp_platform.common.JsonResult;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspBlock;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据块表 服务类
 * </p>
 *
 * @author longhao
 * @since 2019-08-09
 */
public interface RspBlockService extends IService<RspBlock> {

    public boolean removeById(String id);
    boolean removeByIds(List<String> ids);
    List<Map<String,String>> preview(String blockId) throws Exception;
}
