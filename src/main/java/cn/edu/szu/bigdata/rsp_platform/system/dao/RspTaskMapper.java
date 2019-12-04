package cn.edu.szu.bigdata.rsp_platform.system.dao;

import cn.edu.szu.bigdata.rsp_platform.system.model.RspTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


public interface RspTaskMapper extends BaseMapper<RspTask> {
    boolean deleteTrash();
}
