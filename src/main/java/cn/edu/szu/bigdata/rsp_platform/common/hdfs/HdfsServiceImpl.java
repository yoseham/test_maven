package cn.edu.szu.bigdata.rsp_platform.common.hdfs;

import cn.edu.szu.bigdata.rsp_platform.common.exception.BusinessException;
import cn.edu.szu.bigdata.rsp_platform.system.vo.RspBlock;
import cn.edu.szu.bigdata.rsp_platform.system.vo.RspDir;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class HdfsServiceImpl {

    private  String ClusterName = "hdfs-production";
    private  String HADOOP_URL = "hdfs://" + ClusterName;
    private  Configuration conf;
    private  FileSystem fs;

    private static final Logger logger=LoggerFactory.getLogger(HdfsServiceImpl.class);


    public HdfsServiceImpl(){
        conf = new Configuration();
        conf.set("fs.defaultFS", HADOOP_URL);
        conf.set("dfs.nameservices", ClusterName);
        conf.set("dfs.ha.namenodes." + ClusterName, "Medusa002,Medusa001");
        conf.set("dfs.namenode.rpc-address." + ClusterName + ".Medusa001", "172.31.238.103:8020");
        conf.set("dfs.namenode.rpc-address." + ClusterName + ".Medusa002", "172.31.238.104:8020");
        //conf.setBoolean(name, value);
        conf.set("dfs.client.failover.proxy.provider." + ClusterName,
                "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
        String BASE_URL = HADOOP_URL + ":8020";
        try {
            fs = FileSystem.get(new URI(BASE_URL), conf, "longhao");
        }
        catch (Exception e){
            logger.error("HDFS 连接错误",e);
            System.out.println("HDFS 连接错误");
            throw new BusinessException(401,"HDFS连接错误");
        }
    }


    /*** 从hdfs下载文件 ***/
    public  boolean get(boolean delSrc, String srcStr, String dstStr, boolean useRawLocalFileSystem) throws IOException {
        Path srcPath = new Path(srcStr);
        Path dstPath = new Path(dstStr);

        if (!fs.exists(srcPath)) {
            logger.error("源文件不存在");
            return false;
        }

        if (!new File(dstStr).exists()) {
            new File(dstStr).mkdirs();
        }

        String fileName = srcPath.getName();
        //判断同名文件是否存在
        if (new File(dstStr + "\\" + fileName).exists()) {
            logger.error("本地文件已存在！");
            return false;
        }

        fs.copyToLocalFile(delSrc, srcPath, dstPath, useRawLocalFileSystem);
        return true;
    }


    /*** 删除文件 ***/
    public boolean rmFile(String filePath) throws IOException {
        Path path = new Path(filePath);
        boolean deleteOnExit = fs.delete(path, true);
        return deleteOnExit;
    }

    /*** 文件重命名 ***/
    public boolean renameFile(String oldNamePath, String newNamePath) throws IOException {
        Path srcPath = new Path(oldNamePath);
        Path newPath = new Path(newNamePath);
        boolean isSuccess = fs.rename(srcPath, newPath);
        return isSuccess;
    }


    /**
     * 获取某路径下的所有子路径
     *
     * @param path
     * @return
     * @throws Exception
     */
    public  List<String> getAllSubPath(String path) throws IOException {
        Path p = new Path(path);

        List<String> result = new ArrayList<>();
        if (!fs.exists(p)) {
            throw new BusinessException(402,"路径不存在");
        } else {
            FileStatus[] listStatus = fs.listStatus(p);
            for (FileStatus f : listStatus) {
                result.add(f.getPath().getName());
            }
        }
        return result;
    }



    /**
     * 获取路径的capacity,返回值为xxMB,XXGB.
     */

    public  String getPathCapacity(String inputPath) throws IOException {
        Path p = new Path(inputPath);
        long size = fs.getContentSummary(p).getLength();
        return getPrintSize(size);
    }

    public  String getPathCapacity(Path path) throws IOException {
        long size = fs.getContentSummary(path).getLength();
        return getPrintSize(size);
    }

    /*** 创建文件夹 ***/
    public  boolean mkdir(String dirPath) throws IOException {
        Path path = new Path(dirPath);
        boolean exists = fs.exists(path);
        if (exists) {
            System.out.println("目录已存在");
            logger.warn("路径已存在");
            return false;
        }

        if (fs.mkdirs(path)) {
                System.out.println("目录创建成功: " + dirPath);
                logger.info("目录创建成功: " + dirPath);
                return true;
        }
        return false;
    }


    /**
     * 判断路径是否为目录
     */
    public  boolean isDir(String path) throws IOException {
        Path p = new Path(path);
        if (fs.getFileStatus(p).isDirectory()) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为文件
     *
     * @param path
     * @return
     * @throws Exception
     */
    public  boolean isFile(String path) throws IOException {
        Path p = new Path(path);
        if (fs.isFile(p)) {
            return true;
        }
        return false;
    }


    /**
     * 获取路径详细信息，名称，大小，创建时间，下级路径数目等
     * @param path
     * @return
     * @throws Exception
     */
    public  RspDir pathDetail(String path) throws IOException{
        Path p=new Path(path);
        RspDir result=new RspDir();
        if (!fs.exists(p)) {
            return result;
        }else{
            FileStatus[] listStatus = fs.listStatus(p);
            int blockNumber=listStatus.length;
            if (blockNumber>0){
                blockNumber=blockNumber-1;
            }
            result.setBlockNumber(blockNumber);
            result.setName(p.getName());
            long size = fs.getContentSummary(p).getLength();
            result.setCapacity(getPrintSize(size));
            result.setCreateTime(fs.getFileStatus(p).getModificationTime());
            return result;
        }
    }

    /**
     * 路径下 各个文件的大小，文件名，创建时间等
     * @param path
     * @return
     */

    public  List<RspBlock> subPathDetail(String path) throws Exception{
        Path p=new Path(path);

        List<RspBlock> result=new ArrayList<>();
        if (!fs.exists(p)) {
            return result;
        }else{
            FileStatus[] listStatus = fs.listStatus(p);
            for (FileStatus fl:listStatus){
                RspBlock tmp=new RspBlock();
                tmp.setCreateTime(fl.getModificationTime());
                tmp.setCapacity(getPrintSize(fl.getLen()));
                tmp.setName(fl.getPath().getName());
                result.add(tmp);
            }
            return result;
        }
    }


    private  String arrayToString(String[] arr) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String item : arr
        ) {
            stringBuilder.append(item + ";");
        }
        return stringBuilder.toString();
    }

    /**
     * 字节 转换为B MB GB
     *
     * @param size 字节大小
     * @return
     */
    public  String getPrintSize(long size) {
        long rest = 0;
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size /= 1024;
        }

        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            rest = size % 1024;
            size /= 1024;
        }

        if (size < 1024) {
            size = size * 100;
            return String.valueOf((size / 100)) + "." + String.valueOf((rest * 100 / 1024 % 100)) + "MB";
        } else {
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "GB";
        }
    }



    public static void main(String[] args) {
        try {
            HdfsServiceImpl hdfsServiceImpl =new HdfsServiceImpl();
//            HdfsOperation.getAllBlocks("/user/longhao/zdt_ssl_1301_order");
            hdfsServiceImpl.get(false,"/tmp/longhao/test/part-r-00009","D:/part-r-00003",true);
//            List<String> paths=new ArrayList<>();
//            HdfsOperation.iteratorShowFiles(paths,new Path("/tmp"));
//            for (String p:paths){
//                System.out.println(p);
//            }
//            HdfsOperation.mkdir("/tmp/test1/11111");
//            RspDir result=HdfsOperation.pathInformation("/tmp/longhao/test");
//            System.out.println(result.toString());
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("错误");
        }
    }

}