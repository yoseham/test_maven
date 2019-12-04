package cn.edu.szu.bigdata.rsp_platform.common;


import cn.edu.szu.bigdata.rsp_platform.common.exception.BusinessException;
import cn.edu.szu.bigdata.rsp_platform.common.utils.RemoteShellUtil;
import cn.edu.szu.bigdata.rsp_platform.common.utils.SpringBeanUtil;
import cn.edu.szu.bigdata.rsp_platform.common.utils.StringUtil;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspTask;
import cn.edu.szu.bigdata.rsp_platform.system.service.RspTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static cn.edu.szu.bigdata.rsp_platform.common.livy.LivyApp.*;

/**
 * @author longhao
 * @date 2019/6/24 19:14
 */
@Service
@Component
public class NewRspLogProcess implements Runnable {
    private Logger logger= LoggerFactory.getLogger(NewRspLogProcess.class);

    private RspTaskService rspTaskService;

    private RspTask rspTask;

    private int batchId;

    private String jobUrl;

    private NewRspLogProcess(){

    }

    public NewRspLogProcess(int batchId, RspTask rspTask){
        this.batchId=batchId;
        this.rspTask=rspTask;
        this.rspTaskService= SpringBeanUtil.getBean(RspTaskService.class);
    }
    @Override
    public void run() {
        try {
            int count=0;
            String appId=null;
            while(true) {
                try {
                    String state=getJobState(batchId);
                    if(state.equals("starting")){
                        count++;
                    }else{
                        appId=getApplicationId(batchId);
                        if(appId!=null) {
                            jobUrl=appId;
                            logger.debug("appId:" + appId);
                            break;
                        }
                    }
                    if(count>20||state.equals("dead")||state.equals("success")){
                        logger.debug("结束进程，当前batch id:"+batchId+"appId:"+appId+"运行状态为："+state+"\n"+getJobLogs(batchId));
                        killJob(batchId);
                        break;
                    }
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }
            }
            rspTask.setJobUrl(jobUrl);
            rspTaskService.save(rspTask);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new BusinessException("日志写入异常");
        }
    }
}
