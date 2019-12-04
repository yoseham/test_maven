package cn.edu.szu.bigdata.rsp_platform.system.service.impl;

import cn.edu.szu.bigdata.rsp_platform.system.model.RspAlgorithmParamter;
import cn.edu.szu.bigdata.rsp_platform.system.dao.RspAlgorithmParamterMapper;
import cn.edu.szu.bigdata.rsp_platform.system.service.RspAlgorithmParamterService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 算法参数表 服务实现类
 * </p>
 *
 * @author longhao
 * @since 2019-08-23
 */
@Service
public class RspAlgorithmParamterServiceImpl extends ServiceImpl<RspAlgorithmParamterMapper, RspAlgorithmParamter> implements RspAlgorithmParamterService {

    @Override
    public List<RspAlgorithmParamter> getRspAlgorithmParamtersByAlgorithmId(String algorithmId) {
        QueryWrapper<RspAlgorithmParamter> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("algorithm_id",algorithmId);
        List<RspAlgorithmParamter> result=baseMapper.selectList(queryWrapper);
        return result;
    }
}
