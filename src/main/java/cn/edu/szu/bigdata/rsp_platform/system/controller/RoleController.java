package cn.edu.szu.bigdata.rsp_platform.system.controller;

import cn.edu.szu.bigdata.rsp_platform.common.JsonResult;
import cn.edu.szu.bigdata.rsp_platform.common.PageParam;
import cn.edu.szu.bigdata.rsp_platform.common.PageResult;
import cn.edu.szu.bigdata.rsp_platform.common.log.BussinessLog;
import cn.edu.szu.bigdata.rsp_platform.common.utils.StringUtil;
import cn.edu.szu.bigdata.rsp_platform.system.model.Role;
import cn.edu.szu.bigdata.rsp_platform.system.service.RoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(value = "角色管理", tags = "role")
@RestController
@RequestMapping("${api.version}/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @BussinessLog(operEvent = "查询所有角色",operType = 1)
    @ApiOperation(value = "查询所有角色")
    @PreAuthorize("hasAuthority('get:/v1/role')")
    @GetMapping()
    public PageResult<Role> list(HttpServletRequest request) {
        PageParam pageParam = new PageParam(request);
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        String keywords = (String) pageParam.get("keyword");
        if (StringUtil.isNotBlank(keywords)) {
            queryWrapper.like("role_name", keywords).or().like("comments", keywords);
        }
        IPage pageRole = roleService.page(pageParam, queryWrapper);
        return new PageResult<>(pageRole.getRecords(), pageRole.getTotal());
    }


    @BussinessLog(operEvent = "添加角色",operType = 1)
    @ApiOperation(value = "添加角色")
    @PreAuthorize("hasAuthority('post:/v1/role')")
    @PostMapping()
    public JsonResult add(Role role) {
        if (roleService.save(role)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }


    @BussinessLog(operEvent = "修改角色",operType = 1)
    @ApiOperation(value = "修改角色")
    @PreAuthorize("hasAuthority('put:/v1/role')")
    @PutMapping()
    public JsonResult update(Role role) {
        if (roleService.updateById(role)) {
            return JsonResult.ok("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    @BussinessLog(operEvent = "删除角色",operType = 1)
    @ApiOperation(value = "删除角色")
    @PreAuthorize("hasAuthority('delete:/v1/role/{id}')")
    @DeleteMapping("/{id}")
    public JsonResult delete(@PathVariable("id") String roleId) {
        if (roleService.removeById(roleId)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }
}
