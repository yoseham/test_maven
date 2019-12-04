package cn.edu.szu.bigdata.rsp_platform.system.controller;

import cn.edu.szu.bigdata.rsp_platform.common.JsonResult;
import cn.edu.szu.bigdata.rsp_platform.common.PageParam;
import cn.edu.szu.bigdata.rsp_platform.common.PageResult;
import cn.edu.szu.bigdata.rsp_platform.common.log.BussinessLog;
import cn.edu.szu.bigdata.rsp_platform.common.utils.StringUtil;
import cn.edu.szu.bigdata.rsp_platform.system.model.Menu;
import cn.edu.szu.bigdata.rsp_platform.system.service.MenuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(value = "菜单管理", tags = "menu")
@RestController
@RequestMapping("${api.version}/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @BussinessLog(operEvent = "查询所有菜单",operType = 1)
    @ApiOperation(value = "查询所有菜单")
    @PreAuthorize("hasAuthority('get:/v1/menu')")
    @GetMapping()
    public PageResult<Menu> list(HttpServletRequest request) {
        PageParam pageParam = new PageParam(request);
        pageParam.setDefaultOrder(new String[]{"sort_number"}, null);
        QueryWrapper<Menu> queryWrapper = new QueryWrapper();
        String keyword =  pageParam.getString("keyword");
        System.out.println("======================================keyword: "+keyword);
        if (StringUtil.isNotBlank(keyword)) {
            queryWrapper.like("menu_name", keyword).or().like("authority", keyword);
        }
        List<Menu> list = menuService.page(pageParam, queryWrapper).getRecords();
        List<Menu> allMenuList=menuService.list(queryWrapper);
        for (Menu one : list) {
            System.out.println(one.getMenuName());
            for (Menu t : allMenuList) {
                if (one.getParentId().equals(t.getMenuId())) {
                    one.setParentName(t.getMenuName());
                }
            }
        }
        return new PageResult<>(list, pageParam.getTotal());
    }

    @BussinessLog(operEvent = "查询所有菜单",operType = 1)
    @ApiOperation(value = "查询所有菜单")
    @PreAuthorize("hasAuthority('get:/v1/menu/getAllMenu')")
    @GetMapping("/getAllMenu")
    public PageResult<Menu> listNoPage(HttpServletRequest request) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper();
        List<Menu> list = menuService.list(queryWrapper);
        System.out.println("======================"+list.size()+"=================");
        return new PageResult<>(list);
    }

    @BussinessLog(operEvent = "添加菜单",operType = 1)
    @ApiOperation(value = "添加菜单")
    @PreAuthorize("hasAuthority('post:/v1/menu')")
    @PostMapping()
    public JsonResult add(Menu menu) {
        if (menuService.save(menu)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }


    @BussinessLog(operEvent = "修改菜单",operType = 1)
    @ApiOperation(value = "修改菜单")
    @PreAuthorize("hasAuthority('put:/v1/menu')")
    @PutMapping()
    public JsonResult update(Menu menu) {
        if (menuService.updateById(menu)) {
            return JsonResult.ok("修改成功！");
        }
        return JsonResult.error("修改失败！");
    }


    @BussinessLog(operEvent = "删除菜单",operType = 1)
    @ApiOperation(value = "删除菜单")
    @PreAuthorize("hasAuthority('delete:/v1/menu/{id}')")
    @DeleteMapping("/{id}")
    public JsonResult delete(@PathVariable("id") String menuId) {
        if (menuService.removeById(menuId)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }
}
