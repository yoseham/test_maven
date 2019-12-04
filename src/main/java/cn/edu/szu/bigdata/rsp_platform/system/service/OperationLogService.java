package cn.edu.szu.bigdata.rsp_platform.system.service;

import cn.edu.szu.bigdata.rsp_platform.system.model.OperationLog;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * @author longhao
 * @date 2019/6/23 22:55
 */
public interface OperationLogService extends IService<OperationLog> {
    void deleteTrash();
}
