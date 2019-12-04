package cn.edu.szu.bigdata.rsp_platform.system.service.impl;

import cn.edu.szu.bigdata.rsp_platform.common.PageParam;
import cn.edu.szu.bigdata.rsp_platform.common.PageResult;
import cn.edu.szu.bigdata.rsp_platform.common.exception.BusinessException;
import cn.edu.szu.bigdata.rsp_platform.system.dao.UserMapper;
import cn.edu.szu.bigdata.rsp_platform.system.dao.UserRoleMapper;
import cn.edu.szu.bigdata.rsp_platform.system.model.Role;
import cn.edu.szu.bigdata.rsp_platform.system.model.User;
import cn.edu.szu.bigdata.rsp_platform.system.model.UserRole;
import cn.edu.szu.bigdata.rsp_platform.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public User getByUsername(String username) {
        return baseMapper.selectByUsername(username);
    }

    @Override
    public PageResult<User> listUser(PageParam pageParam) {
        List<User> userList = baseMapper.listFull(pageParam);
        // 查询user的角色
        if (userList != null && userList.size() > 0) {
            List<UserRole> userRoles = userRoleMapper.selectByUserIds(getUserIds(userList));
            for (User one : userList) {
                List<Role> tempURs = new ArrayList<>();
                for (UserRole ur : userRoles) {
                    if (one.getUserId().equals(ur.getUserId())) {
                        tempURs.add(new Role(ur.getRoleId(), ur.getRoleName(), null));
                    }
                }
                one.setRoles(tempURs);
            }
        }
        return new PageResult<>(userList, pageParam.getTotal());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addUser(User user, List<String> roleIds) {
        if (baseMapper.selectByUsername(user.getUsername()) != null) {
            throw new BusinessException(401,"账号已经存在");
        }
        boolean result = baseMapper.insert(user) > 0;
        if (result) {
            for (String roleId:roleIds){
                UserRole userRole=new UserRole();
                userRole.setUserId(user.getUserId());
                userRole.setRoleId(roleId);
                userRoleMapper.insert(userRole);
            }
//            if (userRoleMapper.insertBatch(user.getUserId(), roleIds) < roleIds.size()) {
//                throw new BusinessException("添加失败");
//            }
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateUser(User user, List<String> roleIds) {
        user.setUsername(null);  // 账号不能修改
        boolean result = baseMapper.updateById(user) > 0;
        if (result) {
            userRoleMapper.delete(new UpdateWrapper<UserRole>().eq("user_id", user.getUserId()));
            for (String roleId:roleIds){
                UserRole userRole=new UserRole();
                userRole.setUserId(user.getUserId());
                userRole.setRoleId(roleId);
                userRoleMapper.insert(userRole);
            }
//            if (userRoleMapper.insertBatch(user.getUserId(), roleIds) < roleIds.size()) {
//                throw new BusinessException("修改失败");
//            }
        }
        return result;
    }

    /**
     * 获取用户id
     */
    private List<String> getUserIds(List<User> userList) {
        List<String> userIds = new ArrayList<>();
        for (User one : userList) {
            userIds.add(one.getUserId());
        }
        return userIds;
    }

    @Override
    public void deleteTrash() {
        baseMapper.deleteTrash();
    }
}
