package cn.edu.szu.bigdata.rsp_platform.common.aop;

import cn.edu.szu.bigdata.rsp_platform.common.exception.BusinessException;
import cn.edu.szu.bigdata.rsp_platform.common.exception.MyExceptionHandler;
import cn.edu.szu.bigdata.rsp_platform.common.log.BussinessLog;
import cn.edu.szu.bigdata.rsp_platform.common.filter.RequestWrapper;
import cn.edu.szu.bigdata.rsp_platform.common.log.LogManager;
import cn.edu.szu.bigdata.rsp_platform.common.utils.UserUtil;
import cn.edu.szu.bigdata.rsp_platform.system.model.OperationLog;
import cn.edu.szu.bigdata.rsp_platform.system.model.User;
import cn.edu.szu.bigdata.rsp_platform.system.service.OperationLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.TimerTask;

/**
 * @author longhao
 * @date 2019/6/23 19:33
 */
@Aspect
@Component
public class LogAop {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    public final String string = "execution(*  *..*.*.controller..*.*(..))";
    private static final String UTF_8 = "utf-8";

    @Autowired
    OperationLogService operationLogService;

    @Autowired
    MyExceptionHandler myExceptionHandler;

    @Pointcut(value = "@annotation(cn.edu.szu.bigdata.rsp_platform.common.log.BussinessLog)")
    public void cutService() {
    }

    /**
     * 切面 配置通知
     */
    @Before("cutService()")
    public void saveOperation(JoinPoint joinPoint) {
//
//        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
//        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
//        HttpServletRequest request = sra.getRequest();
//        OperationLog operationLog = new OperationLog();
//        //请求方法
//        String method = request.getMethod();
//        operationLog.setReqType(method);
//
//        //URL
//        String uri = request.getRequestURI();
//        operationLog.setOperUrl(uri);
//
//        // 客户端ip
//        String ip = request.getRemoteAddr();
//        operationLog.setClientIp(ip);
//
//        // 操作人,user_id、账号、姓名
//        User user = UserUtil.getLoginUser();
//        if (user == null) {
//            //无用户模式下的请求
//            operationLog.setUserId("-1");
//            operationLog.setUsername("匿名用户");
//            operationLog.setNickName("匿名用户");
//        } else {
//            operationLog.setUserId(user.getUserId());
//            operationLog.setUsername(user.getUsername());
//            operationLog.setNickName(user.getNickName());
//        }
//        try {
//            RequestWrapper requestWrapper = new RequestWrapper(request);
//            String body = requestWrapper.getStreamBody();
//            operationLog.setReqParam(body);
//        }catch (IOException e){
//            log.error("请求读写异常",e);
//            throw new BusinessException(500,"系统异常");
//        }
//
//
//        Signature signature = joinPoint.getSignature();
//
//        MethodSignature msig = (MethodSignature) signature;
//        Method currentMethod = msig.getMethod();
//        BussinessLog annotation = currentMethod.getAnnotation(BussinessLog.class);
//
//        //事件
//        operationLog.setOperEvent(annotation.operEvent());
//        //事件类型
//        operationLog.setOperType(annotation.operType());
//        //请求时间
//        operationLog.setCreateTime(new Date());
//        request.setAttribute("operationLog", operationLog);
    }


    @Around("cutService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable{
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        HttpServletResponse response=sra.getResponse();

        OperationLog operationLog = new OperationLog();
        //请求方法
        String method = request.getMethod();
        operationLog.setReqType(method);

        //URL
        String uri = request.getRequestURI();
        operationLog.setOperUrl(uri);

        // 客户端ip
        String ip = request.getRemoteAddr();
        operationLog.setClientIp(ip);

        // 操作人,user_id、账号、姓名
        User user = UserUtil.getLoginUser();
        if (user == null) {
            //无用户模式下的请求
            operationLog.setUserId("-1");
            operationLog.setUsername("匿名用户");
            operationLog.setNickName("匿名用户");
        } else {
            operationLog.setUserId(user.getUserId());
            operationLog.setUsername(user.getUsername());
            operationLog.setNickName(user.getNickName());
        }
        try {
            RequestWrapper requestWrapper = new RequestWrapper(request);
            String body = requestWrapper.getStreamBody();
            operationLog.setReqParam(body);
        }catch (IOException e){
            log.error("请求读写异常",e);
            throw new BusinessException(500,"系统异常");
        }


        Signature signature = pjp.getSignature();

        MethodSignature msig = (MethodSignature) signature;
        Method currentMethod = msig.getMethod();
        BussinessLog annotation = currentMethod.getAnnotation(BussinessLog.class);

        //事件
        operationLog.setOperEvent(annotation.operEvent());
        //事件类型
        operationLog.setOperType(annotation.operType());
        //请求时间
        operationLog.setCreateTime(new Date());

        try {
              return pjp.proceed();
        } catch (Exception e) {
            operationLog.setMessage(e.getMessage());
            return myExceptionHandler.errorHandler(e,request,response);
        }finally {
            LogManager.me().executeLog(bussinessLog(operationLog));
        }
    }

    public TimerTask bussinessLog(OperationLog operationLog) {
        return new TimerTask() {
            @Override
            public void run() {
                operationLogService.save(operationLog);
            }
        };
    }
}
