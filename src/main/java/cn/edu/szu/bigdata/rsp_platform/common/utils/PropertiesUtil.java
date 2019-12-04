package cn.edu.szu.bigdata.rsp_platform.common.utils;

import org.apache.ibatis.io.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * Created by longhao on 2019/9/24
 */
public final class PropertiesUtil {
    private PropertiesUtil() {
    }

    public static Properties getProperties(String path) throws IOException {
        Properties properties = new Properties();
        try {
            InputStream in = Resources.getResourceAsStream(path);
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
