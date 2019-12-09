package cn.edu.szu.bigdata.rsp_platform;

import cn.edu.szu.bigdata.rsp_platform.common.utils.JSONUtil;
import cn.edu.szu.bigdata.rsp_platform.common.utils.StringUtil;
import cn.edu.szu.bigdata.rsp_platform.system.model.Menu;
import com.alibaba.fastjson.JSON;


import java.util.*;

public class Test {

    @org.junit.Test
    public void test() {
        String json = "[{\"authority\":\"\",\"createTime\":1553577548000,\"menuIcon\":\"layui-icon layui-icon-set\",\"menuId\":1,\"menuName\":\"系统管理\",\"menuUrl\":\"\",\"parentId\":-1,\"sortNumber\":1,\"updateTime\":1553588288000},{\"authority\":\"get:/v1/oauth/client\",\"createTime\":1553588271000,\"menuIcon\":\"\",\"menuId\":2,\"menuName\":\"客户端管理\",\"menuUrl\":\"\",\"parentId\":1,\"sortNumber\":2,\"updateTime\":1553588271000},{\"authority\":\"post:/v1/user/query\",\"createTime\":1553588477000,\"menuIcon\":\"\",\"menuId\":3,\"menuName\":\"用户管理\",\"menuUrl\":\"#/system/user\",\"parentId\":1,\"sortNumber\":3,\"updateTime\":1553588477000},{\"authority\":\"get:/v1/role\",\"createTime\":1553588523000,\"menuIcon\":\"\",\"menuId\":4,\"menuName\":\"角色管理\",\"menuUrl\":\"#/system/role\",\"parentId\":1,\"sortNumber\":4,\"updateTime\":1553588523000},{\"authority\":\"get:/v1/authorities\",\"createTime\":1553588548000,\"menuIcon\":\"\",\"menuId\":5,\"menuName\":\"权限管理\",\"menuUrl\":\"#/system/authorities\",\"parentId\":1,\"sortNumber\":5,\"updateTime\":1553588548000},{\"authority\":\"get:/v1/menu\",\"createTime\":1553588894000,\"menuIcon\":\"\",\"menuId\":6,\"menuName\":\"菜单管理\",\"menuUrl\":\"#/system/menu\",\"parentId\":1,\"sortNumber\":6,\"updateTime\":1553588894000}]";
        List<Map<String, Object>> menuTree = getMenuTree(JSONUtil.parseArray(json, Menu.class), "-1");
        removeEmptyMenu(menuTree);
        System.out.println(JSON.toJSONString(menuTree));





    }


    // 递归转化树形菜单
    private List<Map<String, Object>> getMenuTree(List<Menu> menus, String parentId) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < menus.size(); i++) {
            Menu temp = menus.get(i);
            if (parentId == temp.getParentId()) {
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

    private void removeEmptyMenu(List<Map<String, Object>> menuTree) {
        Iterator<Map<String, Object>> mapIterator = menuTree.iterator();
        while (mapIterator.hasNext()) {
            Map<String, Object> next = mapIterator.next();
            List<Map<String, Object>> subMenus = (List<Map<String, Object>>) next.get("subMenus");
            if (subMenus.size() <= 0 && "javascript:;".equals((String) next.get("url"))) {
                mapIterator.remove();
            } else {
                removeEmptyMenu(subMenus);
            }
        }
    }

}
