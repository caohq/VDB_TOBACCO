package cn.csdb.portal.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONReader;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadJsonFileUtil {
    public static void main(String[] args) throws IOException {
        String path="G:\\testdb5\\www\\db\\t_authoritybigbig.json";
//         readJsonByFastJson(path);
        System.out.println("lllllllll");
    }
    public  Map<String, List<List<String>>> readJsonByFastJson(String path) {
        File file = new File(path);//json文件路径
        Map<String, Object> map = new HashMap<>();
        Map<String, List<List<String>>> listMap = new HashMap<>();
        List<List<String>> dataList = new ArrayList<>();
        long start = System.currentTimeMillis();
        List<String> columnList = new ArrayList<>();

//        先解析出列明
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
            JSONReader reader = new JSONReader(isr);
            reader.startObject(); //开始取json对象
            while (reader.hasNext()) {
                String key = reader.readString();//json的key
                if ("TABLESCOLUMN".equals(key)) {
                    reader.startArray();
                    while (reader.hasNext()) {
                        reader.startObject();
                        while (reader.hasNext()) {
                            String key1 = reader.readString();
                            String value1 = reader.readObject().toString();
                            columnList.add(value1);
                        }
                        reader.endObject();
                    }
                    reader.endArray();
                }else  if ("DATA".equalsIgnoreCase(key)) {
                    reader.startArray();
                    while (reader.hasNext()) {
                        reader.startObject();
                        while (reader.hasNext()) {
                            String key1 = reader.readString();
                            Object val = reader.readObject();
                        }
                        reader.endObject();
                    }
                    reader.endArray();
                } else {
                    Object o = reader.readObject();
                }
            }
            reader.endObject();
            reader.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
            JSONReader reader = new JSONReader(isr);
            reader.startObject(); //开始取json对象
            while (reader.hasNext()) {
                String key = reader.readString();//json的key
                if ("DATA".equalsIgnoreCase(key)) {
                    reader.startArray();
                    while (reader.hasNext()) {
                        reader.startObject();
                        int i = 0;
                        List<String> list = new ArrayList<>(columnList);
                        while (reader.hasNext()) {
                            String key1 = reader.readString();
                            Object val = reader.readObject();
                            if (val == null) {
                                val = "";
                            }
                            if (columnList.get(i).equals(key1)) {
                                list.set(i, val.toString());
                            } else {
                                int k = 0;
                                for (int j = 0; j < columnList.size(); j++) {
                                    if (columnList.get(j).equals(key1)) {
                                        k = j;
                                        break;
                                    }
                                }
                                list.set(k, val.toString());
                            }
                            i++;
                        }
                        reader.endObject();
                        dataList.add(list);
                    }
                    reader.endArray();
                } else {
                    Object o = reader.readObject();
                    map.put(key, o);
                }
            }
            reader.endObject();
            reader.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        Object o2 = map.get("COLUMNNOTE");
        Object o3 = map.get("COLUMNTYPE");
        Object o4 = map.get("LENGTH");
        Object o5 = map.get("PRIMARYKEY");

        List<String> columnType = new ArrayList<>();
        List<String> columnLehgth = new ArrayList<>();
        List<String> columnPk = new ArrayList<>();
        List<String> columnComment = new ArrayList<>();

        JSONArray jsonArray2 = JSONArray.parseArray(o2.toString());
        JSONArray jsonArray3 = JSONArray.parseArray(o3.toString());
        JSONArray jsonArray4 = JSONArray.parseArray(o4.toString());
        JSONArray jsonArray5 = JSONArray.parseArray(o5.toString());

        for (int i = 0; i < columnList.size(); i++) {
            columnComment.add(jsonArray2.getJSONObject(0).get(columnList.get(i)).toString());
            columnType.add(jsonArray3.getJSONObject(0).get(columnList.get(i)).toString());
            columnLehgth.add(jsonArray4.getJSONObject(0).get(columnList.get(i)).toString());
            columnPk.add(jsonArray5.getJSONObject(0).get(columnList.get(i)).toString());
        }
        List<List<String>> tableTitle = new ArrayList<>();
        tableTitle.add(columnComment);
        tableTitle.add(columnList);
        tableTitle.add(columnType);
        tableTitle.add(columnLehgth);
        tableTitle.add(columnPk);

        listMap.put("title", tableTitle);
        listMap.put("tableDate", dataList);
        long end = System.currentTimeMillis();
        System.out.println("解析时间：" + dataList.size() + " 条数据需要：" + (end - start) / 1000.0);
        return listMap;
    }
}
