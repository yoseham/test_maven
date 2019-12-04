package cn.edu.szu.bigdata.rsp_platform.system.dao;

import cn.edu.szu.bigdata.rsp_platform.common.PageParam;
import cn.edu.szu.bigdata.rsp_platform.system.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    User selectByUsername(String username);

    List<User> listFull(@Param("page") PageParam page);

    boolean deleteTrash();
}
