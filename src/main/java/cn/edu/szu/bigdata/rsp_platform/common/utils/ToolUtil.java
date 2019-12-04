package cn.edu.szu.bigdata.rsp_platform.common.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author longhao
 * @date 2019/6/23 18:54
 */
public class ToolUtil {
    /**
     * 获取异常的具体信息
     *
     * @author fengshuonan
     * @Date 2017/3/30 9:21
     * @version 2.0
     */
    public static String getExceptionMsg(Throwable e) {
        StringWriter sw = new StringWriter();
        try {
            e.printStackTrace(new PrintWriter(sw));
        } finally {
            try {
                sw.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return sw.getBuffer().toString().replaceAll("\\$", "T");
    }
}
