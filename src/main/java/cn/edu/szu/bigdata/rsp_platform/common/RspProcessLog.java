package cn.edu.szu.bigdata.rsp_platform.common;


import cn.edu.szu.bigdata.rsp_platform.common.exception.BusinessException;
import cn.edu.szu.bigdata.rsp_platform.common.utils.SpringBeanUtil;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspTask;
import cn.edu.szu.bigdata.rsp_platform.system.service.LivyService;
import cn.edu.szu.bigdata.rsp_platform.system.service.RspTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author longhao
 * @date 2019/6/24 19:14
 */
@Service
@Component
public class RspProcessLog implements Runnable {
    private Logger logger= LoggerFactory.getLogger(RspProcessLog.class);

    private RspTaskService rspTaskService;

    private LivyService livyService;

    private RspTask rspTask;

    private int batchId;

    private String jobUrl;

    private RspProcessLog(){

    }

    public RspProcessLog(int batchId, RspTask rspTask){
        this.batchId=batchId;
        this.rspTask=rspTask;
        this.rspTaskService= SpringBeanUtil.getBean(RspTaskService.class);
        this.livyService = SpringBeanUtil.getBean(LivyService.class);
    }
    @Override
    public void run() {
        try {
            int count=0;
            String appId=null;
            while(true) {
                if(count>20){
                    break;
                }
                try {
                    Map<String, Object> info=livyService.getSparkJobInfo(batchId);
                    if(info.get("appId").equals("null")){
                        count++;
                    }else{
                        appId=(String)info.get("appId");
                        logger.debug("appId:" + appId);
                        break;
                    }
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }
            }
            jobUrl=appId;
            rspTask.setJobUrl(jobUrl);
            rspTaskService.save(rspTask);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new BusinessException("日志写入异常");
        }
    }
}
