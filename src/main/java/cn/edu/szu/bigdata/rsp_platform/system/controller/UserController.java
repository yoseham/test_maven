package cn.edu.szu.bigdata.rsp_platform.system.controller;


import cn.edu.szu.bigdata.rsp_platform.common.*;
import cn.edu.szu.bigdata.rsp_platform.common.log.BussinessLog;
import cn.edu.szu.bigdata.rsp_platform.common.utils.StringUtil;
import cn.edu.szu.bigdata.rsp_platform.system.model.User;
import cn.edu.szu.bigdata.rsp_platform.system.service.UserRoleService;
import cn.edu.szu.bigdata.rsp_platform.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Api(value = "用户管理", tags = "user")
@RestController
@RequestMapping("${api.version}/user")
public class UserController extends BaseController {
    private static final String DEFAULT_PSW = "123456";  // 用户默认密码
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BussinessLog(operEvent = "查询所有用户",operType = 1)
    @PreAuthorize("hasAuthority('get:/v1/user')")
    @ApiOperation(value = "查询所有用户")
    @GetMapping()
    public PageResult<User> list(HttpServletRequest request) {
        return userService.listUser(new PageParam(request).setDefaultOrder(new String[]{"create_time"}, null));
    }

    @BussinessLog(operEvent = "添加用户",operType = 1)
    @ApiOperation(value = "添加用户")
    @PreAuthorize("hasAuthority('post:/v1/user')")
    @PostMapping()
    public JsonResult add(User user, String roleIds) {
        user.setState(0);
        user.setPassword(passwordEncoder.encode(DEFAULT_PSW));
        if (userService.addUser(user, getRoleIds(roleIds))) {
            return JsonResult.ok("添加成功，初始密码为" + DEFAULT_PSW);
        }
        return JsonResult.error("添加失败");
    }

    @BussinessLog(operEvent = "修改用户",operType = 1)
    @ApiOperation(value = "修改用户")
    @PreAuthorize("hasAuthority('put:/v1/user')")
    @PutMapping()
    public JsonResult update(User user, String roleIds) {
        if (userService.updateUser(user, getRoleIds(roleIds))) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    @BussinessLog(operEvent = "修改用户状态",operType = 1)
    @ApiOperation(value = "修改用户状态")
    @PreAuthorize("hasAuthority('put:/v1/user/state')")
    @PutMapping("/state")
    public JsonResult updateState(String userId, Integer state) {
        if (userId == null) {
            return JsonResult.error("参数userId不能为空");
        }
        if (state == null || (state != 0 && state != 1)) {
            return JsonResult.error("状态值不正确");
        }
        User user = new User();
        user.setUserId(userId);
        user.setState(state);
        if (userService.updateById(user)) {
            return JsonResult.ok();
        }
        return JsonResult.error();
    }

    @BussinessLog(operEvent = "修改自己密码",operType = 1)
    @ApiOperation(value = "修改自己密码")
    @PreAuthorize("hasAuthority('put:/v1/user/psw')")
    @PutMapping("/psw")
    public JsonResult updatePsw(String oldPsw, String newPsw) {
        if (StringUtil.isBlank(oldPsw, newPsw)) {
            return JsonResult.error("参数不能为空");
        }
        if (getLoginUser() == null) {
            return JsonResult.error("未登录");
        }
        if (!passwordEncoder.matches(oldPsw, getLoginUser().getPassword())) {
            return JsonResult.error("原密码输入不正确");
        }
        User user = new User();
        user.setUserId(getLoginUser().getUserId());
        user.setPassword(passwordEncoder.encode(newPsw));
        if (userService.updateById(user)) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    @BussinessLog(operEvent = "重置密码",operType = 1)
    @ApiOperation(value = "重置密码")
    @PreAuthorize("hasAuthority('put:/v1/user/psw/{id}')")
    @PutMapping("/psw/{id}")
    public JsonResult resetPsw(@PathVariable("id") String userId) {
        if (userId == null) {
            return JsonResult.error("参数userId不能为空");
        }
        User user = new User();
        user.setUserId(userId);
        user.setPassword(passwordEncoder.encode(DEFAULT_PSW));
        if (userService.updateById(user)) {
            return JsonResult.ok("重置成功，初始密码为" + DEFAULT_PSW);
        } else {
            return JsonResult.error("重置失败");
        }
    }

    @BussinessLog(operEvent = "删除用户",operType = 1)
    @ApiOperation(value = "删除用户")
    @PreAuthorize("hasAuthority('delete:/v1/user/{id}')")
    @DeleteMapping("/{id}")
    public JsonResult delete(@PathVariable("id") Long userId) {
        if (userId == null) {
            return JsonResult.error("参数userId不能为空");
        }
        if (userService.removeById(userId)) {
            return JsonResult.ok("删除成功");
        } else {
            return JsonResult.error("删除失败");
        }
    }

    /**
     * 用逗号分割角色
     */
    private List<String> getRoleIds(String rolesStr) {
        List<String> roleIds = new ArrayList<>();
        if (rolesStr != null) {
            String[] split = rolesStr.split(",");
            for (String t : split) {
                try {
                    roleIds.add(t);
                } catch (Exception e) {
                }
            }
        }
        return roleIds;
    }
}
