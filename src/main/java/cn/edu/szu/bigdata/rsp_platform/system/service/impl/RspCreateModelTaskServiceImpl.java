package cn.edu.szu.bigdata.rsp_platform.system.service.impl;

import cn.edu.szu.bigdata.rsp_platform.system.model.RspAlgorithm;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspAlgorithmParamter;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspCreateModelTask;
import cn.edu.szu.bigdata.rsp_platform.system.dao.RspCreateModelTaskMapper;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspTrainMode;
import cn.edu.szu.bigdata.rsp_platform.system.service.*;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 创建模型任务 服务实现类
 * </p>
 *
 * @author Haotong Sun
 * @since 2019-08-23
 */
@Service
public class RspCreateModelTaskServiceImpl extends ServiceImpl<RspCreateModelTaskMapper, RspCreateModelTask> implements RspCreateModelTaskService {
    @Autowired
    RspDatasetService rspDatasetService;

    @Autowired
    RspDocumentService rspDocumentService;

    @Autowired
    RspColumnService rspColumnService;

    @Autowired
    RspAlgorithmService rspAlgorithmService;

    @Autowired
    RspTrainModeService rspTrainModeService;

    @Autowired
    RspAlgorithmParamterService rspAlgorithmParamterService;
}
