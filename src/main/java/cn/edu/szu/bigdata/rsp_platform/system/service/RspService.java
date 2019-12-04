package cn.edu.szu.bigdata.rsp_platform.system.service;


import cn.edu.szu.bigdata.rsp_platform.common.JsonResult;
import cn.edu.szu.bigdata.rsp_platform.common.NewRspLogProcess;
import cn.edu.szu.bigdata.rsp_platform.common.RspLogProcess;
import cn.edu.szu.bigdata.rsp_platform.common.exception.BusinessException;
import cn.edu.szu.bigdata.rsp_platform.common.hdfs.HdfsOperation;
import cn.edu.szu.bigdata.rsp_platform.common.livy.RSPParameter;
import cn.edu.szu.bigdata.rsp_platform.common.utils.FileUtils;
import cn.edu.szu.bigdata.rsp_platform.common.utils.RemoteShellUtil;
import cn.edu.szu.bigdata.rsp_platform.common.utils.UserUtil;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspBlock;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspTask;
import cn.edu.szu.bigdata.rsp_platform.system.model.User;
import cn.edu.szu.bigdata.rsp_platform.system.vo.RspDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static cn.edu.szu.bigdata.rsp_platform.common.livy.LivyApp.*;

/**
 * @author longhao
 * @date 2019/6/22 23:55
 */
@Service
public  class RspService {
    private Logger logger= LoggerFactory.getLogger(RspService.class);
 public JsonResult execRsp(String source_path, String target_path, String reduce_number) throws Exception{
        User user= UserUtil.getLoginUser();
        String log_name="/home/longhao/RSP_HOME/logs/"+user.getUsername()+"_"+System.currentTimeMillis()+".log";
        logger.debug("===========logname: "+log_name);
        String cmd2="nohup hadoop jar /home/longhao/RSP_HOME/rsp.jar RSP -i "
                +source_path+" -o "+target_path+" -n "+reduce_number+
                " -p 0 > "+log_name+" 2>&1 &";
        logger.debug("===========cmd: "+cmd2);
        int state= RemoteShellUtil.execRspCmd(cmd2);
        if (state==-1){
            System.out.println("远程命令执行错误");
            logger.debug("远程命令执行错误");
            throw new BusinessException(401,"远程命令执行错误");
        }else{
            RspTask rspTask=new RspTask();
            rspTask.setUserId(user.getUserId());
            rspTask.setUsername(user.getUsername());
            rspTask.setParam(source_path+"|"+target_path+"|"+reduce_number+"|"+log_name);
            //延时3s,尝试5次,读取日志数据,解析jobUrl 然后写入数据库。
            RspLogProcess rspLogProcess=new RspLogProcess(log_name,rspTask);
            new Thread(rspLogProcess).start();
            return JsonResult.ok("任务提交成功");
        }
    }

 public boolean submitRspJob(String source_path, String target_path, String reduce_number){
        RSPParameter parameter=new RSPParameter();
        parameter.setInputPath(source_path);
        parameter.setOutputPath(target_path);
        parameter.setBlockNumber(reduce_number);
        int id=submitRSPJob(parameter);
        String appId=null;
        boolean result=false;
        if(id==-1){
            logger.error("source_path,target_path,reduce_number等参数缺失");
        }else{
            User user= UserUtil.getLoginUser();
            RspTask rspTask=new RspTask();
            rspTask.setUserId(user.getUserId());
            rspTask.setUsername(user.getUsername());
            rspTask.setParam(source_path+"|"+target_path+"|"+reduce_number+"|"+id);
            NewRspLogProcess rspLogProcess=new NewRspLogProcess(id,rspTask);
            new Thread(rspLogProcess).start();
            result=true;
        }
        return result;
    }


    /**
     * 获取指定路径下，所有文件或文件夹名称
     * @param path
     * @return
     */
    public List<String> getAllPathName(String path) throws Exception{
        List<String> paths= HdfsOperation.getAllSubPath(path);
        List<String> pathName=new ArrayList<>();
        for (String item:paths){
            String[] temp1=item.split(path);
            String realName=temp1[temp1.length-1];
            if (!realName.equals("_SUCCESS")) {
                pathName.add(realName);
            }
        }
        return pathName;
    }


    /**
     * 下载RSP_BLOCK文件,到指定路径
     */
    public void DownloadRSPBlock(String srcPath,String destDir,boolean delDest) throws Exception{
        //路径存在，且不是目录
        if(FileUtils.isFileExist(destDir)){
            return;
        }
        if (delDest){
            FileUtils.deleteFolder(destDir);
        }
        HdfsOperation.get(false,srcPath,destDir,true);
    }

    /**
     * 获取若干个RSP块，到指定路径
     */
    public boolean DownloadRSPBlock(String srcPath,String destDir,boolean delDest,int number) throws Exception{
        if (!HdfsOperation.isDir(srcPath)){
            return false;
        }
        List<String> paths = HdfsOperation.getAllSubPath(srcPath);

        if (delDest) {//清空本地目录
            FileUtils.deleteFolder(destDir);
        }

        List<Integer> nums = new ArrayList<>();
        if (paths.size()>number){
            //生成指定范围的number个随机整数
            nums = getRandomData(nums, number);
        }else{
            for (int i=0;i<paths.size();i++){
                nums.add(i);
            }
        }

        for (Integer item : nums) {
            String src_p = paths.get(item);
            HdfsOperation.get(false, src_p, destDir, true);
        }
        return true;
    }

    private List<Integer> getRandomData(List<Integer> nums,int number){
        Random rand = new Random();
        while (number>0) {
            int temp=rand.nextInt(number);
            if (!nums.contains(temp)){
                nums.add(temp);
                number--;
            }
        }
        return nums;
    }

    /**
     * 获取RSP数据块数目
     */
    public int getRSPBlockNumber(String path) throws Exception{
        //这里 -1 原因是 目录中会产生 _SUCCESS文件
        return HdfsOperation.ls(path).size()-1;
    }

    /**
     * 获取目录大小，块数目，创建时间
     */
    public RspDir rspDirInformation(String path) throws Exception{
        return HdfsOperation.pathInformation(path);
    }


    /**
     * 获取目录下各个文件的名称，大小，创建时间
     */

    public List<RspBlock> rspBlockList(String path) throws Exception{
        return HdfsOperation.blocksList(path);
    }


    /**
     *删除路径
     */
    public void deletePath(String path) throws Exception{
        HdfsOperation.rmFile(path);
    }

    /**
     * 获取路径大小,byte形式
     */
    public String getPathCapacity(String path) throws Exception{
        return HdfsOperation.getPathVolume(path);
    }

    /**
     * 创建文件夹
     */
    public void createDir(String path) throws Exception{
        HdfsOperation.mkdir(path);
    }
}
