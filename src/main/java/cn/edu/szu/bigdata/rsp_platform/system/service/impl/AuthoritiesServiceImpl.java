package cn.edu.szu.bigdata.rsp_platform.system.service.impl;

import cn.edu.szu.bigdata.rsp_platform.system.dao.AuthoritiesMapper;
import cn.edu.szu.bigdata.rsp_platform.system.model.Authorities;
import cn.edu.szu.bigdata.rsp_platform.system.service.AuthoritiesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthoritiesServiceImpl extends ServiceImpl<AuthoritiesMapper, Authorities> implements AuthoritiesService {

    @Override
    public List<String> listByUserId(String userId) {
        return baseMapper.listByUserId(userId);
    }

    @Override
    public List<String> listByRoleId(String roleId) {
        List<String> roleIds = new ArrayList<>();
        if (roleId != null) {
            roleIds.add(roleId);
        }
        return listByRoleIds(roleIds);
    }

    @Override
    public List<String> listByRoleIds(List<String> roleIds) {
        if (roleIds == null || roleIds.size() == 0) {
            return new ArrayList<>();
        }
        return baseMapper.listByRoleId(roleIds);
    }

}
