package cn.edu.szu.bigdata.rsp_platform.system.service;

import cn.edu.szu.bigdata.rsp_platform.system.model.Menu;
import com.baomidou.mybatisplus.extension.service.IService;


public interface MenuService extends IService<Menu> {
    void deleteTrash();
}
