package cn.csdb.portal.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.Map;
import java.util.Properties;

/**
 * Created by xiajl on 2018/9/19.
 */
public class PropertiesUtil {
    /**
     * 向properties文件中更新键值对
     *
     * @param map 新的键值对
     * @return jsonObject, 1表示成功，0表示出错
     */
    public static JSONObject addProperties(Map<String, String> map, String propertiesPath) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        JSONObject jsonObject = new JSONObject();
        Properties properties = new Properties();
        try {
            fis = new FileInputStream(propertiesPath);
            properties.load(fis);
            fis.close();
            properties.putAll(map);
            fos = new FileOutputStream(propertiesPath);
            properties.store(fos, "update");
            jsonObject.put("info", 1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            jsonObject.put("info", 0);
        } catch (IOException e) {
            e.printStackTrace();
            jsonObject.put("info", 0);
        } finally {
            try {
                if (fis == null)
                    fis.close();
                if (fos == null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    /**
     * 读取值
     * //String value = GetValueByKey("Test.properties", "name");
     */
    public static String GetValueByKey(String filePath, String key) {
        Properties pps = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            pps.load(in);
            String value = pps.getProperty(key);
            System.out.println(key + " = " + value);
            return value;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //写入Properties信息
    //WriteProperties("Test.properties","long", "212");
    public static void WriteProperties(String filePath, String pKey, String pValue) throws IOException {
        Properties pps = new Properties();

        InputStream in = new FileInputStream(filePath);
        //从输入流中读取属性列表（键和元素对）
        pps.load(in);
        //调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。
        //强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
        OutputStream out = new FileOutputStream(filePath);
        pps.setProperty(pKey, pValue);
        //以适合使用 load 方法加载到 Properties 表中的格式，
        //将此 Properties 表中的属性列表（键和元素对）写入输出流
        pps.store(out, "Update " + pKey + " name");
    }

    public String readValue(String fileName, String key) {
        Properties props = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(fileName));
            props.load(in);
            String value = props.getProperty(key);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
