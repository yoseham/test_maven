package cn.edu.szu.bigdata.rsp_platform.system.service.impl;

import cn.edu.szu.bigdata.rsp_platform.system.dao.RspTaskMapper;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspTask;
import cn.edu.szu.bigdata.rsp_platform.system.service.RspTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

/**
 * @author longhao
 * @date 2019/6/23 22:56
 */
@Service
public class RspTaskServiceImpl extends ServiceImpl<RspTaskMapper, RspTask> implements RspTaskService {
    @Override
    public void deleteTrash() {
        baseMapper.deleteTrash();
    }

}
