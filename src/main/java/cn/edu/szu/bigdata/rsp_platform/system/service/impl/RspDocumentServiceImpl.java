package cn.edu.szu.bigdata.rsp_platform.system.service.impl;

import cn.edu.szu.bigdata.rsp_platform.system.model.RspDocument;
import cn.edu.szu.bigdata.rsp_platform.system.dao.RspDocumentMapper;
import cn.edu.szu.bigdata.rsp_platform.system.service.DocumentDatasetService;
import cn.edu.szu.bigdata.rsp_platform.system.service.RspDatasetService;
import cn.edu.szu.bigdata.rsp_platform.system.service.RspDocumentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 数据目录 服务实现类
 * </p>
 *
 * @author longhao
 * @since 2019-08-09
 */
@Service
public class RspDocumentServiceImpl extends ServiceImpl<RspDocumentMapper, RspDocument> implements RspDocumentService {

    @Autowired
    DocumentDatasetService documentDatasetService;

    @Autowired
    RspDatasetService rspDatasetService;


    public boolean removeById(String id){
        List<String> datasetIds=documentDatasetService.listByDocumentId(id);

        return
                documentDatasetService.removeByDocumentId(id)&&
                rspDatasetService.removeByIds(datasetIds)&&
                super.removeById(id);
    }

    public boolean removeByIds(List<String> ids){
        for (String id:ids){
            removeById(id);
        }
        return true;
    }

    public boolean save(RspDocument rspDocument){
        //不能插入重复名
        QueryWrapper<RspDocument> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("hdfs_location",rspDocument.getHdfsLocation()).or().eq("name",rspDocument.getName());
        List<RspDocument> resultList=baseMapper.selectList(queryWrapper);
        boolean flag=false;
        if (resultList.size()==0){
            flag=true;
        }
        return flag&&super.save(rspDocument);
    }

    @Override
    public RspDocument selectByHdfsLocation(String hdfsLocation) {
        QueryWrapper<RspDocument> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("hdfs_location",hdfsLocation);
        return baseMapper.selectOne(queryWrapper);
    }
}
