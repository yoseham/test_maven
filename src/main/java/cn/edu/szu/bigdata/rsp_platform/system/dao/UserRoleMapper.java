package cn.edu.szu.bigdata.rsp_platform.system.dao;

import cn.edu.szu.bigdata.rsp_platform.system.model.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRoleMapper extends BaseMapper<UserRole> {

    List<UserRole> selectByUserIds(@Param("userIds") List<String> userIds);

    int insertBatch(@Param("userId") String userId, @Param("roleIds") List<String> roleIds);

    boolean deleteTrash();
}
