package cn.edu.szu.bigdata.rsp_platform.system.dao;

import cn.edu.szu.bigdata.rsp_platform.system.model.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


public interface MenuMapper extends BaseMapper<Menu> {
    boolean deleteTrash();
}
