package cn.edu.szu.bigdata.rsp_platform.system.service.impl;

import cn.edu.szu.bigdata.rsp_platform.common.exception.BusinessException;
import cn.edu.szu.bigdata.rsp_platform.common.hdfs.HdfsOperation;
import cn.edu.szu.bigdata.rsp_platform.system.dao.DatasetMapper;
import cn.edu.szu.bigdata.rsp_platform.system.model.Dataset;
import cn.edu.szu.bigdata.rsp_platform.system.service.DatasetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 数据集 服务实现类
 * </p>
 *
 * @author longhao
 * @since 2019-06-25
 */
@Service
public class DatasetServiceImpl extends ServiceImpl<DatasetMapper, Dataset> implements DatasetService {
    private String baseURL = "/tmp/rsp";

    /**
     * 重构数据集表
     */
    public void rebuildRspTree() throws Exception {
        //清空数据表
        remove(null);
        //重新填充表单
        List<Dataset> datasets = new ArrayList<>();
        Dataset temp;
        int i = 0;
        List<String> datasetNames = HdfsOperation.getAllSubPath(baseURL);
        for (String str : datasetNames) {
            String tmpId = Integer.toString(i++);
            temp = new Dataset();
            temp.setParentId("-1");
            temp.setDatasetId(tmpId);
            temp.setParentName("数据集");
            temp.setName(str);
            String nd = baseURL + "/" + str;
            temp.setAddress(nd);
            datasets.add(temp);
            List<String> dataNames = HdfsOperation.getAllSubPath(nd);
            for (String tmpStr : dataNames) {
                temp = new Dataset();
                temp.setParentId(tmpId);
                temp.setDatasetId(Integer.toString(i++));
                temp.setParentName(str);
                temp.setName(tmpStr);
                temp.setAddress(nd + "/" + tmpStr);
                datasets.add(temp);
            }
        }
        saveBatch(datasets);
    }

    /**
     * 删除数据或者数据集
     */
    public void removeByMyId(String id) throws Exception{
        Dataset dataset = baseMapper.selectById(id);
        HdfsOperation.rmFile(dataset.getAddress());
        baseMapper.deleteById(id);
    }

    /**
     * 新增数据或数据集
     */
    public boolean mySave(Dataset dataset) throws Exception{
        Dataset parent;
        if (dataset.getParentId().equals("-1")) {
            parent = new Dataset();
            parent.setDatasetId("-1");
            parent.setAddress(baseURL);
        } else {
            parent = baseMapper.selectById(dataset.getParentId());
        }
        String address = parent.getAddress() + "/" + dataset.getName();
        dataset.setAddress(address);
        //优先操作hdfs
        HdfsOperation.mkdir(dataset.getAddress());
        return save(dataset);
    }

    /**
     * 修改数据或数据集
     */
    public boolean updateByMyId(Dataset dataset){
            Dataset old = baseMapper.selectById(dataset.getDatasetId());
            System.out.println(old.getAddress() + "======================================");
            String[] temp = old.getAddress().split("/");
            temp[temp.length - 1] = dataset.getName();
            String address = StringUtils.join(temp, "/");
            dataset.setAddress(address);

            try {
                HdfsOperation.renameFile(old.getAddress(), dataset.getAddress());
            }catch (Exception e){
                throw new BusinessException(401,"重命名失败");
            }
            baseMapper.updateById(dataset);
        return true;
    }
}
