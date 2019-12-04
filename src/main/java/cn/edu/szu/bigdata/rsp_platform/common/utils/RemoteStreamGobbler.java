package cn.edu.szu.bigdata.rsp_platform.common.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * @author longhao
 * @date 2019/6/22 23:55
 */
public class RemoteStreamGobbler extends Thread {
    String in;
    String type;
    String logName;

    RemoteStreamGobbler(String in, String type, String logName)
    {
        this.in = in;
        this.type = type;
        this.logName = logName;
    }

    public void run()
    {
        Writer out=null;
        try
        {
            if (FileUtils.isFileExist(logName)){
                FileUtils.deleteFile(logName);
            }
            File file=FileUtils.makeFile(logName);
            out =new FileWriter(file);

            String[] lines=in.split("\n");
            for (String line:lines){

            }

        } catch (IOException ioe)
        {
            ioe.printStackTrace();
        }finally {
         if (out!=null){
             try {
                 out.close();
             }catch (Exception e){
                 e.printStackTrace();
             }
         }
        }
    }
}
