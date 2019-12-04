package cn.edu.szu.bigdata.rsp_platform.common.livy;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by longhao on 2019/9/20
 */
public class LivyApp {

    static String host = "http://172.31.238.104:8998";
    private static Logger logger= LoggerFactory.getLogger(LivyApp.class);

    //提交rsp任务
    public static int submitRSPJob(RSPParameter parameter){
        JSONObject data = new JSONObject();

        if(parameter.getJobName()==null){
            data.put("name","RSP_job");
        }else {
            data.put("name",parameter.getJobName());
        }

        if(parameter.getQueue()==null){
            data.put("queue", "default");
        }else {
            data.put("queue", parameter.getQueue());
        }

        if (parameter.getProxyUser()==null){
            data.put("proxyUser", "longhao");
        }else {
            data.put("proxyUser", parameter.getProxyUser());
        }

        if(parameter.getRspJarPath()==null){
            data.put("file","/tmp/sparkrsp-1.0.jar");
        }else {
            data.put("file", parameter.getRspJarPath());
        }
        data.put("className","cn.edu.szu.bigdata.RSP");

        if(parameter.getInputPath()==null||parameter.getOutputPath()==null||parameter.getBlockNumber()==null){
            return -1;
        }
        data.put("args",new String[]{"-i",parameter.getInputPath(),"-o",parameter.getOutputPath(),"-n",parameter.getBlockNumber()});
        String res = HttpUtils.postAccess(host + "/batches", data);
        JSONObject resjson = JSON.parseObject(res);
        logger.info(Integer.toString(resjson.getIntValue("id")));
        return resjson.getIntValue("id");

    }

    public static int submitJob()  {
        JSONObject data = new JSONObject();
        data.put("proxyUser","longhao");
//        data.put("file","/tmp/spark-examples_2.11-2.4.0.cloudera2.jar");// 指定执行的spark jar (hdfs路径)
//        data.put("jars",new String[]{"/kong/data/jar/dbscan-on-spark_2.11-0.2.0-SNAPSHOT.jar"});//指定spark jar依赖的外部jars
        data.put("file","/tmp/sparkrsp-1.0.jar");
        data.put("className", "cn.edu.szu.bigdata.RSP");
        data.put("name","RSP_longhao");
//        data.put("executorCores",3);
//        data.put("executorMemory","2g");
//        data.put("driverCores",1);
//        data.put("driverMemory","4g");
//        data.put("numExecutors",6);
        data.put("queue","default");
        data.put("args",new String[]{"-i","/user/longhao/call_data.csv","-o","/tmp/longhao/test111","-n","10"});//传递参数

        String res = HttpUtils.postAccess(host + "/batches", data);

        JSONObject resjson = JSON.parseObject(res);
//        System.out.println("id:"+resjson.getIntValue("id"));
        return resjson.getIntValue("id");
    }

    //获取appId
    public static String getApplicationId(int id){
        JSONObject batch = HttpUtils.getAccess(host + "/batches/" + id);
        String appId = batch.getString("appId");
//        System.out.println("appId:" + appId);
        logger.info(appId);
        return appId;
    }

    // 可以直接kill掉spark任务
    public static void killJob(int id){
        if(HttpUtils.deleteAccess(host+"/batches/"+id)) {
            System.out.println("kill spark job success");
            logger.info("kill spark job success");
        }

    }

    //获取任务状态
    public static String getJobState(int id){
        JSONObject state = HttpUtils.getAccess(host + "/batches/"+id+"/state");
//        System.out.println(state.getString("state"));
        logger.info(state.getString("state"));
        return state.getString("state");
    }

    //获取任务日志
    public static String getJobLogs(int id){
        JSONObject log = HttpUtils.getAccess(host + "/batches/"+id+"/log");
//        System.out.println(log.toJSONString());
        logger.info(log.toJSONString());
        return log.toJSONString();
    }

    public static void main(String[] args) {
//        int count=0;
//        RSPParameter parameter=new RSPParameter();
//        parameter.setInputPath("/user/longhao/zdt_ssl_1301_order");
//        parameter.setOutputPath("/tmp/longhao/test");
//        parameter.setBlockNumber("1000");
//        int id=submitRSPJob(parameter);
//        if(id==-1){
//            System.out.println("运行失败");
//        }else{
//            System.out.println("id:"+id);
//            while(true) {
//                try {
//                    String state=getJobState(id);
//                    System.out.println(state);
//                    if(state.equals("starting")){
//                        count++;
//                    }else{
//                        String appId=getApplicationId(id);
//                        System.out.println("appId:"+appId);
//                    }
//                    if(count>20||state.equals("dead")||state.equals("success")){
//                        System.out.println("结束进程，当前batch运行状态为："+state);
//                        System.out.println(getJobLogs(id));
//                        break;
//                    }
//                    Thread.sleep(10000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                    killJob(id);
//                }
//            }
//            killJob(id);
//        }
    }
}