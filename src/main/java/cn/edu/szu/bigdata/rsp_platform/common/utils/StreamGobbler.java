package cn.edu.szu.bigdata.rsp_platform.common.utils;

import java.io.*;

/**
 * @author longhao
 * @date 2019/6/22 23:55
 */
public class StreamGobbler extends Thread {
    InputStream is;
    String type;
    String logName;

    StreamGobbler(InputStream is, String type,String logName)
    {
        this.is = is;
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
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder result=new StringBuilder();
            String line=null;
            while ( (line = br.readLine()) != null) {
                out.write(line+"\n");
                System.out.println(type + ">" + line);
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
