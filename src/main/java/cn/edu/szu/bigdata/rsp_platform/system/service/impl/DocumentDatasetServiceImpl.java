package cn.edu.szu.bigdata.rsp_platform.system.service.impl;

import cn.edu.szu.bigdata.rsp_platform.system.model.DocumentDataset;
import cn.edu.szu.bigdata.rsp_platform.system.dao.DocumentDatasetMapper;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspDataset;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspDocument;
import cn.edu.szu.bigdata.rsp_platform.system.service.DocumentDatasetService;
import cn.edu.szu.bigdata.rsp_platform.system.service.RspDatasetService;
import cn.edu.szu.bigdata.rsp_platform.system.service.RspDocumentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据目录与数据集关联表 服务实现类
 * </p>
 *
 * @author longhao
 * @since 2019-08-09
 */
@Service
public class DocumentDatasetServiceImpl extends ServiceImpl<DocumentDatasetMapper, DocumentDataset> implements DocumentDatasetService {

    @Autowired
    RspDatasetService rspDatasetService;

    @Autowired
    RspDocumentService rspDocumentService;

    @Override
    public List<String> listByDocumentId(String documentId) {
        QueryWrapper<DocumentDataset> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("document_id",documentId);
        List<DocumentDataset> result=baseMapper.selectList(queryWrapper);
        List<String> datasetIds=new ArrayList<>();
        for (DocumentDataset dd:result
        ) {
            datasetIds.add(dd.getDatasetId());
        }

        return datasetIds;
    }

    @Override
    public boolean removeByDocumentId(String documentId) {
        List<String> datasetIds=listByDocumentId(documentId);
        QueryWrapper<DocumentDataset> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("document_id",documentId);
        if ((baseMapper.delete(queryWrapper)>=0)&&rspDatasetService.removeByIds(datasetIds)){
         return true;
        }
        return false;
    }

    @Override
    public boolean removeByDatasetId(String datasetId) {
        QueryWrapper<DocumentDataset> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("dataset_id",datasetId);
        if (baseMapper.delete(queryWrapper)>=0){
            return true;
        }
        return false;
    }

    public List<Map<String,Object>> rspTree(){
        List<RspDocument> documents=rspDocumentService.list();


        List<Map<String,Object>> result=new ArrayList<>();
        Map<String,Object> mp1=new HashMap<>();
        mp1.put("id","-1");
        mp1.put("title","RSP数据管理平台");
        mp1.put("address","");
        mp1.put("volume","");
        List<Map<String,Object>> mapList=new ArrayList<>();
        for (RspDocument rd:documents){
            Map<String,Object> tmpMap=new HashMap<>();
            tmpMap.put("id",rd.getId());
            tmpMap.put("title",rd.getName());
            tmpMap.put("address",rd.getHdfsLocation());
            tmpMap.put("volume",rd.getVolume());
            List<String> ids=listByDocumentId(rd.getId());
            if (ids==null||ids.size()==0){
                continue;
            }
            List<RspDataset> tmp= (List<RspDataset>) rspDatasetService.listByIds(ids);
            System.out.println("====================大小：：："+tmp.size());
            List<Map<String,Object>> stag=new ArrayList<>();
            for (RspDataset rdt:tmp){
                Map<String,Object> mp=new HashMap<>();
                mp.put("id",rdt.getId());
                mp.put("title",rdt.getName());
                mp.put("address",rdt.getHdfsLocation());
                mp.put("volume",rdt.getVolume());
                stag.add(mp);
            }
            tmpMap.put("children",stag);
            mapList.add(tmpMap);
        }
        mp1.put("children",mapList);
        result.add(mp1);
        return result;
    }

    public boolean save(DocumentDataset documentDataset){
        QueryWrapper<DocumentDataset> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("document_id",documentDataset.getDocumentId()).eq("dataset_id",documentDataset.getDatasetId());
        List<DocumentDataset> dds=baseMapper.selectList(queryWrapper);
        boolean flag=false;
        if (dds.size()==0){
            flag=true;
        }
        return flag&&super.save(documentDataset);
    }

}
