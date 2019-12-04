package cn.edu.szu.bigdata.rsp_platform.system.service.impl;

import cn.edu.szu.bigdata.rsp_platform.system.dao.OperationLogMapper;
import cn.edu.szu.bigdata.rsp_platform.system.model.OperationLog;
import cn.edu.szu.bigdata.rsp_platform.system.service.OperationLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

/**
 * @author longhao
 * @date 2019/6/23 22:56
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {
    @Override
    public void deleteTrash() {
        baseMapper.deleteTrash();
    }
}
