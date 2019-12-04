package cn.edu.szu.bigdata.rsp_platform.system.dao;

import cn.edu.szu.bigdata.rsp_platform.system.model.RoleAuthorities;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


public interface RoleAuthoritiesMapper extends BaseMapper<RoleAuthorities> {

    int deleteTrash();  // 删除垃圾数据(未关联权限的)
}
