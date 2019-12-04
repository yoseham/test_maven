package cn.edu.szu.bigdata.rsp_platform.common.utils;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import cn.edu.szu.bigdata.rsp_platform.common.JsonResult;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author longhao
 * @date 2019/6/24 12:50
 */
public class RemoteShellUtil {
    private static Connection conn;
    /**
     * 远程机器IP
     */
//    private static String ip="gnueqpp80r.51tcp.cc";
    private static String ip="172.31.238.104";
    /**
     * 远程机器端口
     */
//    private static int port=43949;
    private static int port=22;
    /**
     * 用户名
     */
    private static String username="longhao";
    /**
     * 密码
     */
    private static String password="wkofc!2015";
    private static String charset = Charset.defaultCharset().toString();

    private static final int TIME_OUT = 1000 * 5 * 60;

    /**
     * 登录
     *
     * @return
     * @throws IOException
     */
    private static boolean getConnection() throws Exception {
        conn = new Connection(ip, port);
        conn.connect();
        return conn.authenticateWithPassword(username, password);
    }

    /**
     * 执行脚本
     *
     * @param cmds
     * @return
     * @throws Exception
     */
    public static JsonResult exec(String cmds) throws Exception {
        InputStream stdOut = null;
        InputStream stdErr = null;
        String outStr = "";
        String outErr = "";
        int ret = -1;
        try {
            if (getConnection()) {
                // Open a new {@link Session} on this connection
                Session session = conn.openSession();
                // Execute a command on the remote machine.
                session.execCommand(cmds);
                stdOut = new StreamGobbler(session.getStdout());
                outStr = processStream(stdOut, charset);
//                System.out.println("outStr:" + outStr);
                stdErr = new StreamGobbler(session.getStderr());
                outErr = processStream(stdErr, charset);
//                System.out.println("outErr" + outErr);
                session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);
                ret = session.getExitStatus();
            } else {
                throw new Exception("登录远程机器失败" + ip); // 自定义异常类 实现略
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
            if (stdOut != null) {
                stdOut.close();
            }
            if (stdErr != null) {
                stdErr.close();
            }
        }
        return JsonResult.ok(ret, outStr);
    }


    /**
     * 执行在远程主机执行RSP命令，并将该进程设置为后台运行，快速返回状态。
     *
     * @param cmd
     * @return
     * @throws Exception
     */
    public static int execRspCmd(String cmd) throws Exception {
        int ret = -1;
        try {
            if (getConnection()) {
                Session session = conn.openSession();
                // Execute a command on the remote machine.
                session.execCommand(cmd);
                session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);
                ret = session.getExitStatus();
            }
        } catch (Exception e) {
            throw new Exception("登陆远程机器失败", e);
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return ret;
    }

    /**
     * 从RSP日志获取jobUrl
     *
     * @param logName
     * @return
     * @throws Exception
     */
    public static String getJobUrlFromLog(String logName) throws Exception {
        String cmd = "cat " + logName;
        JsonResult jsonResult = RemoteShellUtil.exec(cmd);
        String msg = (String) jsonResult.get("msg");
        String jobUrl = null;
        if (StringUtil.isNotBlank(msg)) {
            String[] lines = msg.split("\n");
            for (String line : lines) {
                if (line.indexOf("The url to track the job:") != -1) {
                    String[] temp = line.split("The url to track the job:");
                    jobUrl = temp[temp.length - 1];
                    break;
                }
            }
        }
        return jobUrl;
    }


    /**
     * @param in
     * @param charset
     * @return
     * @throws Exception
     */
    private static String processStream(InputStream in, String charset) throws Exception {
        byte[] buf = new byte[1024];
        StringBuilder sb = new StringBuilder();
        while (in.read(buf) != -1) {
            sb.append(new String(buf, charset));
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        try {
//            JsonResult jsonResult=RemoteShellUtil.exec("cat /home/longhao/RSP_HOME/run_rsp.log");
//            String msg=(String)jsonResult.get("msg");
//            System.out.println(msg);
            RemoteShellUtil.execRspCmd("cat /home/longhao/RSP_HOME/run_rsp.log");
//            String jobUrl=getJobUrlFromLog("cat /home/longhao/RSP_HOME/run_rsp.log");
//            System.out.println(jobUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
