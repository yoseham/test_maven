package cn.edu.szu.bigdata.rsp_platform.common.utils;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 字符串工具类
 *
 * @author 王帆
 * @date 2017-11-10 上午11:03:50
 */
public class StringUtil {

    /**
     * 是否为空
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        if (str == null || str.isEmpty() || str.replaceAll(" ", "").isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isBlank(String... strs) {
        for (int i = 0; i < strs.length; i++) {
            if (isBlank(strs[i])) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean contains(String str, String key) {
        if (str != null && str.contains(key)) {
            return true;
        }
        return false;
    }

    public static boolean contains(String... strs) {
        for (int i = 0; i < strs.length - 1; i++) {
            if (contains(strs[i], strs[strs.length - 1])) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(String[] strs, String key) {
        for (int i = 0; i < strs.length; i++) {
            if (contains(strs[i], key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 首字母大写
     *
     * @param in
     * @return
     */
    public static String upperHeadChar(String in) {
        String head = in.substring(0, 1);
        String out = head.toUpperCase() + in.substring(1, in.length());
        return out;
    }

    /**
     * 字符串XSS过滤，JavaScript过滤，Sql过滤
     *
     * @param str 传入的字符串
     *
     * @return 转义后的字符串
     * */
    public static String xssEncode(String str){
        String s = StringEscapeUtils.escapeHtml(str);
//		s = StringEscapeUtils.escapeJavaScript(s);
//		s = StringEscapeUtils.escapeSql(s);
        return s;
    }

    /**
     * 转换特殊字符
     * @param s
     * @return
     */
    public static String escapeQueryChars(String s) {
        if (StringUtils.isBlank(s)) {
            return s;
        }
        StringBuilder sb = new StringBuilder();
        //查询字符串一般不会太长，挨个遍历也花费不了多少时间
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            // These characters are part of the query syntax and must be escaped
            if (c == '\\' || c == '+' || c == '-' || c == '!' || c == '(' || c == ')'
                    || c == ':' || c == '^'	|| c == '[' || c == ']' || c == '\"'
                    || c == '{' || c == '}' || c == '~' || c == '*' || c == '?'
                    || c == '|' || c == '&' || c == ';' || c == '/' || c == '.'
                    || c == '$' || Character.isWhitespace(c)) {
                sb.append('\\');
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
