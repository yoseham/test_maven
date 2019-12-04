package cn.edu.szu.bigdata.rsp_platform.system.service.impl;

import cn.edu.szu.bigdata.rsp_platform.system.model.RspColumn;
import cn.edu.szu.bigdata.rsp_platform.system.dao.RspColumnMapper;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspDocument;
import cn.edu.szu.bigdata.rsp_platform.system.service.BlockViewService;
import cn.edu.szu.bigdata.rsp_platform.system.service.DatasetColumnService;
import cn.edu.szu.bigdata.rsp_platform.system.service.RspColumnService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 数据集的属性表 服务实现类
 * </p>
 *
 * @author longhao
 * @since 2019-08-09
 */
@Service
public class RspColumnServiceImpl extends ServiceImpl<RspColumnMapper, RspColumn> implements RspColumnService {

    @Autowired
    DatasetColumnService datasetColumnService;

    @Autowired
    BlockViewService blockViewService;

    public boolean removeById(String id){
        return datasetColumnService.removeByColumnId(id)&&blockViewService.deleteByColumnId(id)&&super.removeById(id);
    }

    public boolean removeByIds(List<String> ids){
        for (String id:ids){
            removeById(id);
        }
        return true;
    }

}
