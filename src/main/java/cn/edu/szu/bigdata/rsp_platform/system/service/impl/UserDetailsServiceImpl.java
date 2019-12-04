package cn.edu.szu.bigdata.rsp_platform.system.service.impl;


import cn.edu.szu.bigdata.rsp_platform.system.dao.AuthoritiesMapper;
import cn.edu.szu.bigdata.rsp_platform.system.dao.UserMapper;
import cn.edu.szu.bigdata.rsp_platform.system.dao.UserRoleMapper;
import cn.edu.szu.bigdata.rsp_platform.system.model.Authorities;
import cn.edu.szu.bigdata.rsp_platform.system.model.Role;
import cn.edu.szu.bigdata.rsp_platform.system.model.User;
import cn.edu.szu.bigdata.rsp_platform.system.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AuthoritiesMapper authoritiesMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("账号不存在");
        }

        //添加角色
        List<String> ids=new ArrayList<>();
        ids.add(user.getUserId());
        List<UserRole> userRoles = userRoleMapper.selectByUserIds(ids);
        List<Role> tempURs = new ArrayList<>();
        for (UserRole ur : userRoles) {
            Role tpRole=new Role();
            tpRole.setRoleId(ur.getRoleId());
            tpRole.setRoleName(ur.getRoleName());
            tempURs.add(tpRole);
        }
        user.setRoles(tempURs);

        List<String> authoritys = authoritiesMapper.listByUserId(user.getUserId());
        user.setAuthorities(getGrantedAuthority(authoritys));
        return user;
    }

    private List<Authorities> getGrantedAuthority(List<String> authoritys) {
        List<Authorities> grantedAuthorities = new ArrayList<>();
        for (String auth : authoritys) {
            Authorities ga = new Authorities();
            ga.setAuthority(auth);
            grantedAuthorities.add(ga);
        }
        return grantedAuthorities;
    }
}
