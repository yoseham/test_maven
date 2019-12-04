package cn.edu.szu.bigdata.rsp_platform.system.service;

import cn.edu.szu.bigdata.rsp_platform.system.model.RspAlgorithmParamter;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 算法参数表 服务类
 * </p>
 *
 * @author longhao
 * @since 2019-08-23
 */
public interface RspAlgorithmParamterService extends IService<RspAlgorithmParamter> {

    List<RspAlgorithmParamter> getRspAlgorithmParamtersByAlgorithmId(String AlgorithmId);

}
