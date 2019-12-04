package cn.edu.szu.bigdata.rsp_platform.system.service.impl;

import cn.edu.szu.bigdata.rsp_platform.system.dao.RoleMapper;
import cn.edu.szu.bigdata.rsp_platform.system.model.Role;
import cn.edu.szu.bigdata.rsp_platform.system.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public void deleteTrash() {
        baseMapper.deleteTrash();
    }
}
