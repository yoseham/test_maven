package cn.edu.szu.bigdata.rsp_platform.system.service.impl;

import cn.edu.szu.bigdata.rsp_platform.system.dao.RoleAuthoritiesMapper;
import cn.edu.szu.bigdata.rsp_platform.system.model.RoleAuthorities;
import cn.edu.szu.bigdata.rsp_platform.system.service.RoleAuthoritiesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

@Service
public class RoleAuthoritiesServiceImpl extends ServiceImpl<RoleAuthoritiesMapper, RoleAuthorities> implements RoleAuthoritiesService {

    @Override
    public void deleteTrash() {
        baseMapper.deleteTrash();
    }

}
