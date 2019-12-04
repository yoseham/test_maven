package cn.edu.szu.bigdata.rsp_platform.system.service.impl;

import cn.edu.szu.bigdata.rsp_platform.system.dao.UserRoleMapper;
import cn.edu.szu.bigdata.rsp_platform.system.model.UserRole;
import cn.edu.szu.bigdata.rsp_platform.system.service.UserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {


    @Override
    public String[] getRoleIds(String userId) {
        List<UserRole> userRoles = baseMapper.selectList(new QueryWrapper<UserRole>().eq("user_id", userId));
        String[] roleIds = new String[userRoles.size()];
        for (int i = 0; i < userRoles.size(); i++) {
            roleIds[i] = userRoles.get(i).getRoleId();
        }
        return roleIds;
    }

    @Override
    public void deleteTrash() {
        baseMapper.deleteTrash();
    }
}
