package cn.edu.szu.bigdata.rsp_platform.system.service;

import cn.edu.szu.bigdata.rsp_platform.system.model.RspTask;
import com.baomidou.mybatisplus.extension.service.IService;


public interface RspTaskService extends IService<RspTask> {
    void deleteTrash();
}
