package cn.edu.szu.bigdata.rsp_platform.system.service;

import cn.edu.szu.bigdata.rsp_platform.system.model.Role;
import com.baomidou.mybatisplus.extension.service.IService;


public interface RoleService extends IService<Role> {
    void deleteTrash();
}
