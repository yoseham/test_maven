package cn.edu.szu.bigdata.rsp_platform.common.utils;

/**
 * @author longhao
 * @date 2019/6/23 0:03
 */
public class LocalShellUtil {

    /**
     * 执行本地shell命令
     * @param cmd
     * @param logName
     * @throws Exception
     */
    public static int runLocalShell(String cmd,String logName) throws Exception{
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(cmd);
        StreamGobbler errorGobbler = new
                StreamGobbler(proc.getErrorStream(), "ERROR",logName);
        StreamGobbler outputGobbler = new
                StreamGobbler(proc.getInputStream(), "OUTPUT",logName);
        // kick them off
        errorGobbler.start();
        outputGobbler.start();
        // any error???
        int exitVal = proc.waitFor();
        System.out.println("ExitValue: " + exitVal);
        return exitVal;
    }




    public static void main(String[] args) {
        try {
            LocalShellUtil.runLocalShell("ipconfig","2019062401.log");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
