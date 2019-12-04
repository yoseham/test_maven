package cn.edu.szu.bigdata.rsp_platform.system.service;

import cn.edu.szu.bigdata.rsp_platform.system.model.RoleAuthorities;
import com.baomidou.mybatisplus.extension.service.IService;


public interface RoleAuthoritiesService extends IService<RoleAuthorities> {

    void deleteTrash();
}
