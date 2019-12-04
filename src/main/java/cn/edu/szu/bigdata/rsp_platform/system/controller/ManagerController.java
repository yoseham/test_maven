package cn.edu.szu.bigdata.rsp_platform.system.controller;


import cn.edu.szu.bigdata.rsp_platform.common.BaseController;
import cn.edu.szu.bigdata.rsp_platform.common.JsonResult;
import cn.edu.szu.bigdata.rsp_platform.common.log.BussinessLog;
import cn.edu.szu.bigdata.rsp_platform.system.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author longhao
 * @date 2019/6/23 17:06
 */
@Api(value = "数据管理", tags = "data")
@RestController
@RequestMapping("${api.version}/sys")
public class ManagerController extends BaseController {
    @Autowired
    private MenuService menuService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleAuthoritiesService roleAuthoritiesService;

    @BussinessLog(operEvent = "删除数据库中status=1的记录",operType = 1)
    @ApiOperation(value = "删除数据库中status=1的记录")
//    @PreAuthorize("hasAuthority('get:/v1/sys/deleteTrash')")
    @GetMapping("/deleteTrash")
    public JsonResult deleteTrash(){
        menuService.deleteTrash();
        roleAuthoritiesService.deleteTrash();
        roleService.deleteTrash();
        userRoleService.deleteTrash();
        userService.deleteTrash();
        System.out.println("==================================");
        return JsonResult.ok("已删除");
    }
}
