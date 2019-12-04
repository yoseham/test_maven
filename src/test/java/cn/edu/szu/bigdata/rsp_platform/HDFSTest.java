package cn.edu.szu.bigdata.rsp_platform;



import cn.edu.szu.bigdata.rsp_platform.system.service.RspService;

import java.io.IOException;
import java.util.List;

/**
 * @author longhao
 * @date 2019/6/27 17:08
 */

public class HDFSTest {

    private static RspService service=new RspService();

//    @org.junit.Test
    public void testGetAllPathName(){
        try{
            List<String> paths=service.getAllPathName("/tmp/longhao/test");
            for (String item : paths){
                System.out.println(item);
            }
        }catch (IOException e){
            System.out.println("HADOOP_HOME or hadoop.home.dir are not set");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

//    @org.junit.Test
    public void TestGetRSPBlockNumber(){
        try{
            int count=service.getRSPBlockNumber("/tmp/longhao/test");
            System.out.println(count);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    @org.junit.Test
    public void TestDownloadRSPBlock(){
        try{
            service.DownloadRSPBlock("/tmp/longhao/test/part-r-00003","D:\\test",false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    @org.junit.Test
    public void TestDownloadRSPBlock_num(){
        try{
            service.DownloadRSPBlock("/tmp/longhao/test","D:\\test",true,2);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    @org.junit.Test
    public void TestGetPathCapacity(){
        try{
            String result=service.getPathCapacity("/tmp/longhao/test");
            System.out.println(result);
        }catch (Exception e){

        }
    }

    public static void main(String[] args) {
        try{
            service.DownloadRSPBlock("/tmp/longhao/test","D:\\test",true,2);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
