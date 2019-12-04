package cn.edu.szu.bigdata.rsp_platform.system.service;

import cn.edu.szu.bigdata.rsp_platform.system.model.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserRoleService extends IService<UserRole> {

    String[] getRoleIds(String userId);

    void deleteTrash();
}
