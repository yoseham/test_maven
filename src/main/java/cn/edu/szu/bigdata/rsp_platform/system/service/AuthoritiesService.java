package cn.edu.szu.bigdata.rsp_platform.system.service;

import cn.edu.szu.bigdata.rsp_platform.system.model.Authorities;
import com.baomidou.mybatisplus.extension.service.IService;


import java.util.List;

public interface AuthoritiesService extends IService<Authorities> {

    List<String> listByUserId(String userId);

    List<String> listByRoleId(String roleId);

    List<String> listByRoleIds(List<String> roleIds);

}
