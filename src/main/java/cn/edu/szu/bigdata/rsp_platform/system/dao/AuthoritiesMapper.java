package cn.edu.szu.bigdata.rsp_platform.system.dao;

import cn.edu.szu.bigdata.rsp_platform.system.model.Authorities;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AuthoritiesMapper extends BaseMapper<Authorities> {

    List<String> listByUserId(String userId);

    List<String> listByRoleId(@Param("roleIds") List<String> roleIds);
}
