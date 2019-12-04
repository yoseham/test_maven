package cn.edu.szu.bigdata.rsp_platform.system.service;

import cn.edu.szu.bigdata.rsp_platform.system.model.RspCreateModelTask;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 创建模型任务 服务类
 * </p>
 *
 * @author longhao
 * @since 2019-08-22
 */
public interface RspCreateModelTaskService extends IService<RspCreateModelTask> {

    @Override
    boolean save(RspCreateModelTask rspCreateModelTask);
}
