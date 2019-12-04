package cn.edu.szu.bigdata.rsp_platform.system.controller;

import cn.edu.szu.bigdata.rsp_platform.common.BaseController;
import cn.edu.szu.bigdata.rsp_platform.common.JsonResult;
import cn.edu.szu.bigdata.rsp_platform.common.log.BussinessLog;
import cn.edu.szu.bigdata.rsp_platform.common.utils.StringUtil;
import cn.edu.szu.bigdata.rsp_platform.system.model.Menu;
import cn.edu.szu.bigdata.rsp_platform.system.model.User;
import cn.edu.szu.bigdata.rsp_platform.system.service.MenuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Api(value = "个人信息", tags = "main")
@RequestMapping("${api.version}/")
@RestController
public class MainController extends BaseController {
    @Autowired
    private MenuService menuService;

    @BussinessLog(operEvent = "获取个人信息",operType = 1)
    @ApiOperation(value = "获取个人信息")
    @GetMapping("/user/info")
    public JsonResult userInfo() {
        return JsonResult.ok().put("user", getLoginUser());
    }

    @BussinessLog(operEvent = "获取所有菜单",operType = 1)
    @ApiOperation(value = "获取所有菜单")
    @GetMapping("/user/menu")
    public JsonResult userMenu() {
        // 获取当前用户的权限
        List<String> auths = new ArrayList<>();
        User loginUser = getLoginUser();
        if (loginUser != null && loginUser.getAuthorities() != null) {
            Iterator<? extends GrantedAuthority> iterator = loginUser.getAuthorities().iterator();
            while (iterator.hasNext()) {
                GrantedAuthority authority = iterator.next();
                auths.add(authority.getAuthority());
            }
        }
        // 查询所有的菜单
        List<Menu> menus = menuService.list(new QueryWrapper<Menu>().orderByAsc("sort_number"));
        // 移除没有权限的菜单
        Iterator<Menu> iterator = menus.iterator();
        while (iterator.hasNext()) {
            Menu next = iterator.next();
            boolean haveAuth = false;
            for (String auth : auths) {
                if (StringUtil.isBlank(next.getAuthority()) || next.getAuthority().equals(auth)) {
                    haveAuth = true;
                }
            }
            if (!haveAuth) {
                iterator.remove();
            }
        }
        // 去除空的目录
        iterator = menus.iterator();
        while (iterator.hasNext()) {
            Menu next = iterator.next();
            if (StringUtil.isBlank(next.getMenuUrl())) {
                boolean haveSub = false;
                for (Menu t : menus) {
                    if (t.getParentId().equals(next.getMenuId())) {
                        haveSub = true;
                        break;
                    }
                }
                if (!haveSub) {
                    iterator.remove();
                }
            }
        }
        return JsonResult.ok().put("data", getMenuTree(menus, "-1"));
    }

    /**
     * 递归转化树形菜单
     */
    private List<Map<String, Object>> getMenuTree(List<Menu> menus, String parentId) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < menus.size(); i++) {
            Menu temp = menus.get(i);
            if (parentId.equals(temp.getParentId())) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", temp.getMenuName());
                map.put("icon", temp.getMenuIcon());
                map.put("url", StringUtil.isBlank(temp.getMenuUrl()) ? "javascript:;" : temp.getMenuUrl());
                map.put("subMenus", getMenuTree(menus, menus.get(i).getMenuId()));
                list.add(map);
            }
        }
        return list;
    }




}
