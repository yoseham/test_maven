package cn.edu.szu.bigdata.rsp_platform.system.dao;

import cn.edu.szu.bigdata.rsp_platform.system.model.OperationLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


/**
 * @author longhao
 * @date 2019/6/23 18:34
 */
public interface OperationLogMapper extends BaseMapper<OperationLog> {
    boolean deleteTrash();
}
