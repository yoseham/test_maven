package cn.edu.szu.bigdata.rsp_platform.system.dao;

import cn.edu.szu.bigdata.rsp_platform.system.model.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


public interface RoleMapper extends BaseMapper<Role> {

    boolean deleteTrash();
}
