package cn.edu.szu.bigdata.rsp_platform.common;

import cn.edu.szu.bigdata.rsp_platform.common.eum.SparkJobState;
import cn.edu.szu.bigdata.rsp_platform.common.hdfs.HdfsOperation;
import cn.edu.szu.bigdata.rsp_platform.common.utils.RedisUtil;
import cn.edu.szu.bigdata.rsp_platform.system.model.*;
import cn.edu.szu.bigdata.rsp_platform.system.service.*;
import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by longhao on 2019/9/26
 */

@Component
public class PrintTask {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static Logger logger = Logger.getLogger(PrintTask.class);

    @Autowired
    private RedisUtil redisUtil;

    private static String UNDEFINE_MAP="undefineMap";
    private static String STARTING_MAP="startingMap";


    @Autowired
    RunningTaskService runningTaskService;

    @Autowired
    WaitTaskService waitTaskService;

    @Autowired
    FinishedTaskService finishedTaskService;

    @Autowired
    LivyService livyService;

    @Autowired
    BlockViewService blockViewService;

    @Autowired
    RspDatasetService rspDatasetService;

    @Autowired
    RspBlockService rspBlockService;


    @Autowired
    DatasetBlockService datasetBlockService;

    @Autowired
    RspColumnService rspColumnService;

    @Autowired
    DatasetColumnService datasetColumnService;


    public boolean addBlockView(String datasetId) throws Exception {
        List<BlockView> blockViewList = new ArrayList<>();
        List<String> blockIds = datasetBlockService.listByDatasetId(datasetId);
        List<String> columnIds = datasetColumnService.listByDatasetId(datasetId);
        RspDataset rspDataset = rspDatasetService.getById(datasetId);
        //如果还没有生成，则等待
        if(!HdfsOperation.isDir(rspDataset.getHdfsLocation().replace("rsp","rsp_graph")+"/all")||
                HdfsOperation.blocksList(rspDataset.getHdfsLocation().replace("rsp","rsp_graph")).size()<blockIds.size()){
            return false;
        }
        for(String columnId:columnIds){
            BlockView blockView = new BlockView();
            blockView.setDatasetId(datasetId);
            blockView.setBlockId(datasetId);
            blockView.setColumnId(columnId);
            RspColumn rspColumn= rspColumnService.getById(columnId);
            String path = rspDataset.getHdfsLocation().replace("rsp","rsp_graph")+"/all/"+rspColumn.getName();
            if(HdfsOperation.isFile(path)){
                List<String> dataList = HdfsOperation.get_100(path);
                blockView.setxAxisData(dataList.get(0));
                blockView.setyAxisData(dataList.get(1));
                blockViewList.add(blockView);
            }
        }
        for(String blockId : blockIds) {
            for (String columnId : columnIds) {
                BlockView blockView = new BlockView();
                blockView.setDatasetId(datasetId);
                blockView.setBlockId(blockId);
                blockView.setColumnId(columnId);
                blockView.setRowCount("10");
                RspBlock rspBlock = rspBlockService.getById(blockId);
                String blockPath = rspBlock.getHdfsLocation();
                RspColumn rspColumn= rspColumnService.getById(columnId);
                rspColumn.setDataExploration(1);
                rspColumnService.updateById(rspColumn);
                String colName = rspColumn.getName();
                String path = blockPath.replace("rsp","rsp_graph")+"/"+colName;
                if(HdfsOperation.isFile(path)){

                    List<String> dataList = HdfsOperation.get_100(path);
                    blockView.setxAxisData(dataList.get(0));
                    blockView.setyAxisData(dataList.get(1));
                    blockViewList.add(blockView);
                }

            }
        }
        blockViewService.saveBatch(blockViewList);
        return true;
    }

    public static String add(String str1, String str2) {
        if(str1 == null)
            return str2;
        if(str2 == null)
            return str1;
        StringBuffer s1 = new StringBuffer(str1).reverse();
        StringBuffer s2 = new StringBuffer(str2).reverse();
        StringBuffer res = new StringBuffer();
        int len1 = s1.length();
        int len2 = s2.length();
        int len;
        if(len1 < len2) {
            len = len2;
            int count = len2 - len1;
            while(count-- > 0)
                s1.append('0');
        } else {
            len = len1;
            int count = len1 - len2;
            while(count-- > 0)
                s2.append('0');
        }
        int overflow = 0;
        int num;
        for(int i = 0; i < len; i++) {
            num = s1.charAt(i) - '0' + s2.charAt(i) - '0' + overflow;
            if(num >= 10) {
                overflow = 1;
                num -= 10;
            } else {
                overflow = 0;
            }
            res.append(String.valueOf(num));
        }
        if(overflow == 1)
            res.append(1);

        return res.reverse().toString();
    }

    @Scheduled(fixedDelay = 10000)
    public void reportCurrentTime() throws Exception {
//        System.out.println("===================="+redisUtil.randomKey()+"===========");
        List<RunningTask> tasks = runningTaskService.list();
        for (RunningTask task : tasks) {
            if(task.getTaskType()==1){
                task.setRunningState("running");
                String datasetId = task.getParameter();
                int count=task.getCount();
                ++count;
                task.setCount(count);
                if(addBlockView(datasetId)){
                    task.setRunningState("success");
                    runningTaskMoveToFinishTask(task);
                }
                else if(count>50){
                    task.setRunningState("error");
                    runningTaskMoveToFinishTask(task);
                }else{
                    runningTaskService.updateById(task);
                }
            }
            //如果运行状态为WAITING_SUBMIT提交任务,成功后状态修改为SUBMITED
            else{
                //统计SpringTask调用次数
                int count=task.getCount();
                ++count;
                task.setCount(count);

                //新任务
                if (task.getRunningState().equals(SparkJobState.WAITING_SUBMIT.getDescription())) {
                    SparkJob sparkJob = JSON.parseObject(task.getParameter(), SparkJob.class);
                    System.out.println(sparkJob);
                    System.out.println("================================"+sparkJob.getArgs());
                    logger.debug("================================"+sparkJob.getArgs());

                    int batchId = livyService.startSparkJob(sparkJob);
                    task.setBatchId(batchId);
                    if (task.getBatchId() == -1){//任务提交失败
                        runningTaskMoveToFinishTask(task);
                    }else{//任务提交成功
                        task.setRunningState(SparkJobState.SUBMITED.getDescription());//任务状态设置为已提交
                        redisUtil.hPut(STARTING_MAP,task.getTaskId(),Integer.toString(0));//任务id，加载到启动列表中
                        runningTaskService.updateById(task);
                    }
                    continue;
                }

                //获取当前任务的状态,并更新
                SparkJobState state = livyService.getSparkJobState(task.getBatchId());
                if (state==null){
                    task.setRunningState("UNDEFINE_STATE");

                    if(redisUtil.hExists(UNDEFINE_MAP,task.getTaskId())){
                        int ct=Integer.parseInt((String)redisUtil.hGet(UNDEFINE_MAP,task.getTaskId()));
                        ct++;
                        if (ct>10){//尝试10次依然没有成功 //这里的循环依赖的是SpringTask
                            redisUtil.hDelete(UNDEFINE_MAP,task.getTaskId());
                            runningTaskMoveToFinishTask(task);
                        }else{
                            redisUtil.hPut(UNDEFINE_MAP,task.getTaskId(),Integer.toString(ct));
                        }
                    }else{
                        redisUtil.hPut(UNDEFINE_MAP,task.getTaskId(),Integer.toString(0));
                    }
                    runningTaskService.updateById(task);
                    continue;
                }else{
                    task.setRunningState(state.getDescription());
                    logger.debug(sdf.format(new Date()) + " 当前运行状态: " + state.getDescription());
                    int ct=Integer.parseInt((String) redisUtil.hGet(STARTING_MAP,task.getTaskId()));
                    //为运行状态
                    if(task.getApplicationId()==null||task.getApplicationId().length()==0){
                        Map<String,Object> result=livyService.getSparkJobInfo(task.getBatchId());
                        String applicationId=(String)result.get("appId");
                        task.setApplicationId(applicationId);
                    }

                    if(task.getRunningState().equals(SparkJobState.STARTING.getDescription())){
                        //为 starting 状态
                        ct++;
                        redisUtil.hPut(STARTING_MAP,task.getTaskId(),Integer.toString(ct));
                    }
                    else if (task.getRunningState().equals(SparkJobState.SUCCESS.getDescription())) {
                        //为成功状态, 如果job执行完成, 启动子job,把子job从wait_task移动到running_task，并将当前job移动到finished_task中
                        if (task.getChildTaskId() != null && task.getChildTaskId().length() > 0) {
                            WaitTask waitTask = waitTaskService.getById(task.getChildTaskId());
                            RunningTask runningTask = new RunningTask();
                            runningTask.setTaskId(waitTask.getTaskId());
                            runningTask.setRunningState(SparkJobState.WAITING_SUBMIT.getDescription());
                            runningTask.setParameter(waitTask.getParameter());
                            runningTask.setChildTaskId(waitTask.getChildTaskId());
                            runningTask.setTaskType(waitTask.getTaskType());
                            runningTaskService.save(runningTask);
                            waitTaskService.removeById(task.getChildTaskId());
                        }
                        runningTaskMoveToFinishTask(task);
                        livyService.deleteSparkJob(task.getBatchId());
                    }
                    else if (ct > 10 || task.getRunningState().equals(SparkJobState.DEAD.getDescription())) {
                        //为不正常状态, 如果job死亡，或者各种原因不正常，移出kill 掉job,将其移动到finished
                        livyService.deleteSparkJob(task.getBatchId());
                        runningTaskMoveToFinishTask(task);
                    }
                    else{
                        runningTaskService.updateById(task);
                    }
                }
            }
        }
    }


    private void runningTaskMoveToFinishTask(RunningTask runningTask){
        FinishedTask finishedTask=new FinishedTask();
        finishedTask.setTaskId(runningTask.getTaskId());
        finishedTask.setBatchId(runningTask.getBatchId());
        finishedTask.setApplicationId(runningTask.getApplicationId());
        finishedTask.setChildTaskId(runningTask.getChildTaskId());
        finishedTask.setRunningState(runningTask.getRunningState());
        finishedTask.setParameter(runningTask.getParameter());
        finishedTask.setTaskType(runningTask.getTaskType());
        finishedTaskService.save(finishedTask);
        //移出运行任务
        runningTaskService.removeById(runningTask.getTaskId());
        //有子任务，且当前任务未运行成功，删除这些子任务
        if (runningTask.getChildTaskId()!=null&&!runningTask.getRunningState().equals(SparkJobState.SUCCESS.getDescription()))
        {
            WaitTask temp=waitTaskService.getById(runningTask.getChildTaskId());
            iterDelete(temp);
        }

//        在运行表中移出
        if(redisUtil.hExists(UNDEFINE_MAP,runningTask.getTaskId())){
            redisUtil.hDelete(UNDEFINE_MAP,runningTask.getTaskId());
        }
        if(redisUtil.hExists(STARTING_MAP,runningTask.getTaskId())){
            redisUtil.hDelete(STARTING_MAP,runningTask.getTaskId());
        }
    }

    private boolean iterDelete(WaitTask task){
        List<String> taskIds=new ArrayList<>();
        WaitTask current=task;
        while (current!=null){
            taskIds.add(current.getTaskId());
            if (current.getChildTaskId()!=null){
                current=waitTaskService.getById(current.getChildTaskId());
            }else{
                break;
            }
        }
        return waitTaskService.removeByIds(taskIds);
    }



}
