package cn.csdb.portal.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class ConfigUtil {
    public static String getConfigItem(String configFilePath, String key) {
        String value = null;
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(new File(configFilePath)));
            value = properties.getProperty(key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public static void setConfigItem(String configFilePath, String key, String value) {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(new File(configFilePath)));
            properties.setProperty(key, value);
            FileOutputStream fos = new FileOutputStream(new File(configFilePath));
            properties.store(fos, "");
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
