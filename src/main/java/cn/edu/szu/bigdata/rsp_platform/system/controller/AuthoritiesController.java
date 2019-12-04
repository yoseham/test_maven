package cn.edu.szu.bigdata.rsp_platform.system.controller;

import cn.edu.szu.bigdata.rsp_platform.common.BaseController;
import cn.edu.szu.bigdata.rsp_platform.common.JsonResult;
import cn.edu.szu.bigdata.rsp_platform.common.PageParam;
import cn.edu.szu.bigdata.rsp_platform.common.PageResult;
import cn.edu.szu.bigdata.rsp_platform.common.log.BussinessLog;
import cn.edu.szu.bigdata.rsp_platform.common.utils.JSONUtil;
import cn.edu.szu.bigdata.rsp_platform.common.utils.StringUtil;
import cn.edu.szu.bigdata.rsp_platform.system.model.Authorities;
import cn.edu.szu.bigdata.rsp_platform.system.model.RoleAuthorities;
import cn.edu.szu.bigdata.rsp_platform.system.service.AuthoritiesService;
import cn.edu.szu.bigdata.rsp_platform.system.service.RoleAuthoritiesService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(value = "权限管理", tags = "authorities")
@RestController
@RequestMapping("${api.version}/authorities")
public class AuthoritiesController extends BaseController {
    @Autowired
    private AuthoritiesService authoritiesService;
    @Autowired
    private RoleAuthoritiesService roleAuthoritiesService;


    @BussinessLog(operEvent = "同步权限",operType = 1)
    @ApiOperation(value = "同步权限")
    @PreAuthorize("hasAuthority('post:/v1/authorities/sync')")
    @PostMapping("/sync")
    public JsonResult add(String json) {
        List<Authorities> list = JSONUtil.parseArray(json, Authorities.class);
        authoritiesService.remove(null);
        authoritiesService.saveBatch(list);
        roleAuthoritiesService.deleteTrash();
        return JsonResult.ok("同步成功");
    }

    @BussinessLog(operEvent = "查询所有权限",operType = 1)
    @ApiOperation(value = "查询所有权限")
    @PreAuthorize("hasAuthority('get:/v1/authorities')")
    @GetMapping
    public PageResult<Authorities> list(HttpServletRequest request) {
        PageParam pageParam = new PageParam(request);
        pageParam.setDefaultOrder(new String[]{"sort"}, null);
        String roleId = pageParam.getString("roleId");
        String keyword = pageParam.getString("keyword");
        QueryWrapper<Authorities> queryWrapper = new QueryWrapper<>();
        if (StringUtil.isNotBlank(keyword)) {
            queryWrapper.like("authority_name", keyword).or().like("parent_name", keyword).or().like("authority", keyword);
        }
        List<Authorities> authorities = authoritiesService.page(pageParam, queryWrapper).getRecords();
        // 回显选中状态
        List<String> roleAuths = authoritiesService.listByRoleId(roleId);
        for (Authorities one : authorities) {
            one.setChecked(0);
            for (String roleAuth : roleAuths) {
                if (one.getAuthority().equals(roleAuth)) {
                    one.setChecked(1);
                    break;
                }
            }
        }
        return new PageResult<>(authorities, pageParam.getTotal());
    }


    @BussinessLog(operEvent = "给角色添加权限",operType = 1)
    @ApiOperation(value = "给角色添加权限")
    @PreAuthorize("hasAuthority('post:/v1/authorities/role')")
    @PostMapping("/role")
    public JsonResult addRoleAuth(String roleId, String authId) {
        RoleAuthorities roleAuth = new RoleAuthorities();
        roleAuth.setRoleId(roleId);
        roleAuth.setAuthority(authId);
        if (roleAuthoritiesService.save(roleAuth)) {
            return JsonResult.ok();
        }
        return JsonResult.error();
    }

    @BussinessLog(operEvent = "移除角色权限",operType = 1)
    @ApiOperation(value = "移除角色权限")
    @PreAuthorize("hasAuthority('delete:/v1/authorities/role')")
    @DeleteMapping("/role")
    public JsonResult deleteRoleAuth(Long roleId, String authId) {
        if (roleAuthoritiesService.remove(new UpdateWrapper<RoleAuthorities>().eq("role_id", roleId).eq("authority", authId))) {
            return JsonResult.ok();
        }
        return JsonResult.error();
    }
}
