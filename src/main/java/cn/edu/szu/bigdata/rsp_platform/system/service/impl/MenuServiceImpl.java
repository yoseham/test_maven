package cn.edu.szu.bigdata.rsp_platform.system.service.impl;

import cn.edu.szu.bigdata.rsp_platform.system.model.Menu;
import cn.edu.szu.bigdata.rsp_platform.system.dao.MenuMapper;
import cn.edu.szu.bigdata.rsp_platform.system.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Override
    public void deleteTrash() {
        baseMapper.deleteTrash();
    }
}
