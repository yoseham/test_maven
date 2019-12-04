package cn.edu.szu.bigdata.rsp_platform.common.hdfs;

import cn.edu.szu.bigdata.rsp_platform.common.exception.BusinessException;
//import cn.edu.szu.bigdata.rsp_platform.system.vo.RspBlock;
import cn.edu.szu.bigdata.rsp_platform.system.model.RspBlock;
import cn.edu.szu.bigdata.rsp_platform.system.vo.RspDir;
import com.google.inject.internal.util.$Objects;
import org.apache.commons.io.FileExistsException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class HdfsOperation {
    public static Configuration conf;
    public static FileSystem fs;

    private static String ClusterName = "hdfs-production";
    private static final String HADOOP_URL = "hdfs://" + ClusterName;

    static {
        conf = new Configuration();
        conf.set("fs.defaultFS", HADOOP_URL);
        conf.set("dfs.nameservices", ClusterName);
        conf.set("dfs.ha.namenodes." + ClusterName, "Medusa002,Medusa005");
        conf.set("dfs.namenode.rpc-address." + ClusterName + ".Medusa005", "172.31.238.107:8020");
        conf.set("dfs.namenode.rpc-address." + ClusterName + ".Medusa002", "172.31.238.104:8020");
        //conf.setBoolean(name, value);
        conf.set("dfs.client.failover.proxy.provider." + ClusterName,
                "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");

        String BASE_URL = HADOOP_URL + ":8020";
        try {
            fs = FileSystem.get(new URI(BASE_URL), conf, "longhao");
        }catch (Exception e){
            throw new BusinessException();
        }
    }

    /*** 查看文件内容 ***/
    public static void cat(String filePath) throws Exception {
        Path path = new Path(filePath);
        FSDataInputStream inputStream = null;
        try {
            inputStream = fs.open(path);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            IOUtils.copyBytes(inputStream, System.out, 4096, false);
        } catch (IOException e) {
            throw e;
        } finally {
            IOUtils.closeStream(inputStream);
        }

    }

    public static List<String> get_100(String filePath) throws Exception{
        Path path = new Path(filePath);
        FSDataInputStream inputStream = null;
        try {
            inputStream = fs.open(path);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String data = bufferedReader.readLine();
            List<String> datalist=new ArrayList<String>();
            int line=0;
            while(data!=null&&line<100){
                line++;
                datalist.add(data);
                data = bufferedReader.readLine();
            }
            return datalist;

        } catch (IOException e) {
            throw e;
        } finally {
            IOUtils.closeStream(inputStream);
        }

    }

    /*** 上传文件到hdfs ***/
    public static boolean put(boolean delSrcFile, boolean overwrite, String srcStr, String dstStr) throws IOException {
        Path srcPath = new Path(srcStr);
        Path dstPath = new Path(dstStr);

        // 判断源文件是否存在
        if (!(new File(srcStr)).exists()) {
            System.out.println("上传的源文件不存在！");
            return false;
        }

        // 判断上传目录是否存在
        if (!fs.exists(dstPath)) {
            System.out.println("上传路径不存在！");
            return false;
        }

        fs.copyFromLocalFile(delSrcFile, overwrite, srcPath, dstPath);

        System.out.println("上传到 :" + conf.get("fs.defaultFS.name") + dstStr);
        // 列出该目录下的文件
        System.out.println("-------list-----------");
        FileStatus[] listStatus = fs.listStatus(dstPath);

        for (FileStatus file : listStatus) {
            System.out.println(file.getPath());
        }
        return true;

    }

    /*** 从hdfs下载文件 ***/
    public static boolean get(boolean delSrc, String srcStr, String dstStr, boolean useRawLocalFileSystem)
            throws IOException {
        Path srcPath = new Path(srcStr);
        Path dstPath = new Path(dstStr);

        // 判断源文件是否存在
        if (!fs.exists(srcPath)) {
            System.out.println("源文件不存在");
            return false;
        }

        // 判断目标路径是否存在,不存则创建
        else if (!new File(dstStr).exists()) {
            new File(dstStr).mkdirs();
            System.out.println();
        } else {
            String fileName = srcPath.getName();
            // 判断同名文件是否存在
            if (new File(dstStr + "\\" + fileName).exists()) {
                System.out.println("本地文件已存在！");
                return false;
            } else {
                // 下载文件
                fs.copyToLocalFile(delSrc, srcPath, dstPath, useRawLocalFileSystem);
                System.out.println("download successful! At:" + dstStr);
                return true;
            }
        }
        return true;
    }

    /*** 删除文件 ***/
    public static boolean rmFile(String filePath) throws IOException {

        Path path = new Path(filePath);
        boolean deleteOnExit = fs.delete(path, true);
        if (deleteOnExit) {
            System.out.println("删除成功");
        } else {
            System.out.println("删除失败,文件不存在");
        }
        return deleteOnExit;
    }

    /*** 文件重命名 ***/
    public static boolean renameFile(String oldNamePath, String newNamePath) throws IOException {
        Path srcPath = new Path(oldNamePath);
        Path newPath = new Path(newNamePath);
        boolean isSuccess = fs.rename(srcPath, newPath);
        if (isSuccess) {
            System.out.println("重命名: " + oldNamePath + " 为  " + newNamePath);
        } else {
            System.out.println("重命名失败！");
        }
        return isSuccess;
    }

    /*** 创建文件,并且写入内容 ***/
    public static boolean touch(String filePath, String content) throws IOException {
        Path path = new Path(filePath);
        byte[] contens = content.getBytes();

        boolean overwrite;
        if (fs.exists(path)) {
            // 文件已经存在
            while (true) {
                System.out.println("文件已经存在,是否覆盖(y/n)?");
                Scanner sc = new Scanner(System.in);
                String input = sc.next();
                if ("y".equals(input.trim()) || "Y".equals(input.trim())) {
                    FSDataOutputStream out = fs.create(path, true);
                    out.write(contens);
                    out.close();
                    System.out.println("文件创建成功！");
                    return true;

                } else if ("n".equals(input) || "N".equals(input)) {
                    System.out.println("文件写入失败，文件已存在");
                    return false;
                } else {
                    continue;
                }
            }

        } else {
            // 文件不存在
            FSDataOutputStream out = fs.create(path);
            out.write(contens);
            out.close();
            System.out.println("文件创建成功！");
            return true;
        }

    }


    /**
     * 获取路径下的所有子文件路径
     **/
    public static List<Path> ls(String path) throws Exception {
        Path p = new Path(path);
        List<Path> result = new ArrayList<>();
        if (!fs.exists(p)) {
            System.out.println("路径：" + path + " 不存在！");
            throw new FileNotFoundException("路径不存在");
        } else {
            FileStatus[] listStatus = fs.listStatus(p);
            System.out.println("under path:" + path);
            for (FileStatus f : listStatus) {
                result.add(f.getPath());
            }
        }
        return result;
    }

    /**
     * 获取某路径下的所有路径
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static List<String> getAllSubPath(String path) throws Exception {
        Path p = new Path(path);
        List<String> result = new ArrayList<>();
        if (!fs.exists(p)) {
            throw new FileNotFoundException("路径不存在");
        } else {
            FileStatus[] listStatus = fs.listStatus(p);
            for (FileStatus f : listStatus) {
                result.add(f.getPath().getName());
            }
        }
        return result;
    }


    /**
     * @param path
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void getFileStatus(String path) throws IOException, FileNotFoundException {
        Path p = new Path(path);
        if (!fs.exists(p)) {
            System.out.println("路径：" + path + " 不存在！");
            throw new FileNotFoundException("路径不存在");
        } else {
            FileStatus[] listStatus = fs.listStatus(p);
            System.out.println("under path:" + path);
            for (FileStatus f : listStatus) {
                System.out.println(getPrintSize(f.getBlockSize()));
            }
        }
    }


    /**
     * 获取路径的capacity,返回值为xxMB,XXGB.
     */

    public static String getPathCapacity(String inputPath) throws Exception {
        Path p = new Path(inputPath);
        long size = fs.getContentSummary(p).getLength();
        return getPrintSize(size);
    }

    public static String getPathCapacity(Path path) throws Exception {
        long size = fs.getContentSummary(path).getLength();
        return getPrintSize(size);
    }

    /*** 创建文件夹 ***/
    public static void mkdir(String dirPath) throws Exception {
        Path path = new Path(dirPath);
        boolean exists = fs.exists(path);
        if (exists) {
            System.out.println("目录已存在");
//            throw new FileExistsException("目录已存在");
        }else {
            if (fs.mkdirs(path)) {
                System.out.println("目录创建成功: " + dirPath);
            }
        }
    }


    /**
     * 判断路径是否为目录
     */
    public static boolean isDir(String path) throws Exception {
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
    public static boolean isFile(String path) throws Exception {
        Path p = new Path(path);
        if (fs.isFile(p)) {
            return true;
        }
        return false;
    }


    /**
     * 获取文件所有的block
     */
    public static void getAllBlocks(String path) throws IOException {
        Path p = new Path(path);
//        BlockLocation[] result=null;
        if (!fs.exists(p)) {
            System.out.println("路径：" + path + " 不存在！");
//        } else if(fs.isDirectory(p)){
//            System.out.println("路径不是文件，请输入文件路径");
//        }else{
        } else {
            FileStatus[] listStatus = fs.listStatus(p);
            System.out.println("under path:" + path);
            for (FileStatus f : listStatus) {
                BlockLocation[] result = fs.getFileBlockLocations(f, 0, f.getLen());
                for (BlockLocation block : result
                ) {
                    System.out.println("----------------------------------------------------");
                    System.out.println("block 所在的节点：" + arrayToString(block.getNames()));
                    System.out.println("与此块关联的文件的起始偏移量：" + block.getOffset());
                    System.out.println("block 大小：" + getPrintSize(block.getLength()));
                    System.out.println("block 拓扑路径：" + arrayToString(block.getTopologyPaths()));
                    System.out.println("block 块的stirageId: " + arrayToString(block.getStorageIds()));
                    System.out.println("----------------------------------------------------");
                }
            }
        }
    }

    /**
     * 获取路径详细信息，名称，大小，创建时间，下级路径数目等
     * @param path
     * @return
     * @throws Exception
     */
    public static RspDir pathInformation(String path) throws Exception{
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


    public static String getPathVolume(String pathString) throws Exception{
        Path path=new Path(pathString);
        if (!fs.exists(path)){
            return null;
        }
        long size = fs.getContentSummary(path).getLength();
        return Long.toString(size);

    }


    /**
     * 路径下 各个文件的大小，文件名，创建时间等
     * @param path
     * @return
     */

    public static List<RspBlock> blocksList(String path) throws Exception{
        Path p=new Path(path);
        List<RspBlock> result=new ArrayList<>();
        if (!fs.exists(p)) {
            return result;
        }else{
            FileStatus[] listStatus = fs.listStatus(p);
            for (FileStatus fl:listStatus){
                if(fl.getPath().getName().equals("_SUCCESS")){
                    continue;
                }
                RspBlock tmp=new RspBlock();
                tmp.setHdfsLocation(path+'/'+fl.getPath().getName());
                tmp.setVolume(getPrintSize(fl.getLen()));
                tmp.setName(fl.getPath().getName());
                result.add(tmp);
            }
            return result;
        }
    }


    private static String arrayToString(String[] arr) {
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
    public static String getPrintSize(long size) {
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


    /**
     * 添加内容到文件
     **/
    public static void append(String content, String filePath) throws IOException {
        Path path = new Path(filePath);
        // 将字符串转为输入流
        ByteArrayInputStream in = new ByteArrayInputStream(content.getBytes());
        // 获得添加 输出流
        FSDataOutputStream out = fs.append(path, 4096);
        IOUtils.copyBytes(in, out, 4096, true);

    }


    /**
     * 迭代遍历（不使用）
     *
     * @param
     * @param path
     */
    public static void iteratorShowFiles(List<String> paths, Path path) {
        try {
            if (path == null) {
                return;
            }
            //获取文件列表
            FileStatus[] files = fs.listStatus(path);

            //展示文件信息
            for (int i = 0; i < files.length; i++) {
                try {
                    if (files[i].isDirectory()) {
                        paths.add(files[i].getPath().toString());
                        //递归调用
                        iteratorShowFiles(paths, files[i].getPath());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
//            HdfsOperation.ls("/user/longhao");
//            HdfsOperation.getAllBlocks("/user/longhao/zdt_ssl_1301_order");
//            HdfsOperation.get(false,"/tmp/longhao/test/part-r-00009","D:/part-r-00003",true);r
//            List<String> paths=new ArrayList<>();
//            HdfsOperation.iteratorShowFiles(paths,new Path("/tmp"));
//            for (String p:paths){
//                System.out.println(p);
//            }
//            HdfsOperation.mkdir("/tmp/test1/11111");
//            String result=HdfsOperation.getPathCapacity("/user/tamer/AirOnTimeCSV");
//            System.out.println(result);
//            List<List<String>> datalist = HdfsOperation.get_100("/user/hive/warehouse/power.db/zdt_ssl_1306/part-m-00000");
//            System.out.println(datalist);
//            List<Path> filelist = HdfsOperation.ls("/tmp/rsp/四川电信_demo/通信月_0");
//            for(Path file : filelist){
//                System.out.println(file);
//            }
            if(put(false,true,"D:\\nginx-1.17.2\\dataExplore.py","/tmp/")){
                System.out.println("success!");
            }
//            if(rmFile("/tmp/dataExplore.py")){
//                System.out.println("success!");
//            }


//            List<RspBlock> blocklist = new ArrayList<>();
//
//            blocklist = HdfsOperation.blocksList("/tmp/rsp/四川电信_demo/通信月_0");
//            System.out.println(blocklist);
//            HdfsOperation.getAllBlocks("/user/hive/warehouse/power.db/zdt_ssl_1301");

//            List<RspBlock> result=HdfsOperation.blocksList("/tmp/longhao/test");
//            for (RspBlock rspBlock:result){
//                System.out.println(rspBlock.toString());
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}