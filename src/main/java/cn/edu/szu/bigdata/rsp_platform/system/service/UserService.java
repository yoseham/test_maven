package cn.edu.szu.bigdata.rsp_platform.system.service;

import cn.edu.szu.bigdata.rsp_platform.common.PageParam;
import cn.edu.szu.bigdata.rsp_platform.common.PageResult;
import cn.edu.szu.bigdata.rsp_platform.system.model.User;
import com.baomidou.mybatisplus.extension.service.IService;


import java.util.List;

public interface UserService extends IService<User> {

    User getByUsername(String username);

    PageResult<User> listUser(PageParam pageParam);

    boolean addUser(User user, List<String> roleIds);

    boolean updateUser(User user, List<String> roleIds);

    void deleteTrash();

}
