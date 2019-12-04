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

/**
 * @author longhao
 * @date 2019/6/24 19:14
 */
@Service
@Component
public class RspLogProcess implements Runnable {
    private Logger logger= LoggerFactory.getLogger(RspLogProcess.class);

    private RspTaskService rspTaskService;

    private RspTask rspTask;

    private String logName;

    private String jobUrl;

    private RspLogProcess(){

    }

    public RspLogProcess(String logName,RspTask rspTask){
        this.logName=logName;
        this.rspTask=rspTask;
        this.rspTaskService= SpringBeanUtil.getBean(RspTaskService.class);
    }
    @Override
    public void run() {
        try {
            int count=5;

            do {
                Thread.currentThread().sleep(3000);//毫秒
                jobUrl = RemoteShellUtil.getJobUrlFromLog(logName);
                count--;
            }
            while (StringUtil.isBlank( jobUrl )&& count > 0);
            rspTask.setJobUrl(jobUrl);
            rspTaskService.save(rspTask);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new BusinessException("日志写入异常");
        }
    }
}
