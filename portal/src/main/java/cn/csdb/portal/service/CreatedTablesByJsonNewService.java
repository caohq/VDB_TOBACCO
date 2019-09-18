package cn.csdb.portal.service;

import cn.csdb.portal.model.*;
import cn.csdb.portal.repository.*;
import cn.csdb.portal.utils.ReadJsonFileUtil;
import cn.csdb.portal.utils.dataSrc.DataSourceFactory;
import cn.csdb.portal.utils.dataSrc.IDataSource;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.google.gson.stream.JsonReader;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

@Service
public class CreatedTablesByJsonNewService {


    private Logger logger = LoggerFactory.getLogger(CreatedTablesByJsonService.class);

    @Resource
    private SubjectDao subjectDao;
    @Resource
    private CreatedTablesByExcelDao createdTablesByCSVDao;

    @Resource
    private CheckUserDao checkUserDao;

    @Resource
    private NodeDao nodeDao;

    /**
     * 根据当前用户获取相关连接信息
     *
     * @param subjectCode
     * @param DatabaseType
     * @return
     */
    private DataSrc getDataSrc(String subjectCode, String DatabaseType) {
        Subject subject = checkUserDao.getSubjectByCode(subjectCode);
        DataSrc datasrc = new DataSrc();
        datasrc.setDatabaseName(subject.getDbName());
        datasrc.setDatabaseType(DatabaseType);
        datasrc.setHost(subject.getDbHost());
        datasrc.setPort(subject.getDbPort());
        datasrc.setUserName(subject.getDbUserName());
        datasrc.setPassword(subject.getDbPassword());
        return datasrc;
    }


    /**
     * 获取数据库连接
     *
     * @param dataSrc
     * @return
     */
    private Connection getConnection(DataSrc dataSrc) {
        IDataSource dataSource = DataSourceFactory.getDataSource(dataSrc.getDatabaseType());
        Connection connection = dataSource.getConnection(dataSrc.getHost(), dataSrc.getPort(), dataSrc.getUserName(), dataSrc.getPassword(), dataSrc.getDatabaseName());
        return connection;
    }

    /**
     * @Description: 遍历主题库
     * @Param: []
     * @return: com.alibaba.fastjson.JSONObject
     * @Author: zcy
     * @Date: 2019/9/2
     */
    public JSONObject ergodicSubjectList() {
//        查询所有主题库
        List<Subject> list = subjectDao.findAllsubject();
        JSONObject jsonObject = new JSONObject();
        for (Subject subject : list) {
            jsonObject = ergodicNodeList(subject.getSubjectCode());
        }
        return jsonObject;
    }

    /**
     * @Description: 遍历节点
     * @Param: [subjectCode]
     * @return: com.alibaba.fastjson.JSONObject
     * @Author: zcy
     * @Date: 2019/9/2
     */
    public JSONObject ergodicNodeList(String subjectCode) {
        JSONObject jsonObject = new JSONObject();
        List<Node> nodes = nodeDao.findBySubjectCode(subjectCode);
        for (Node node : nodes) {
            getDbFile(subjectCode, node);
        }
        return jsonObject;
    }

    //    根据 /home/ ThemeCode /节点名称/ db目录下的csv文件，
    //    怎么判断哪些csv文件已经建表成功，哪些还未建表

    /**
     * @Description:
     * @Param: [subjectCode, node]
     * @return: com.alibaba.fastjson.JSONObject
     * @Author: zcy
     * @Date: 2019/8/21
     */
    public JSONObject getDbFile(String subjectCode, Node node) {
        JSONObject jsonObject = new JSONObject();
        Subject subject = subjectDao.findBySubjectCode(subjectCode);
        String dbFilePath;
        if (subject != null) {
            dbFilePath = node.getDbPath();
            List<String> files = new ArrayList<String>();
            File file = new File(dbFilePath);
            File[] tempList = file.listFiles();
            if (tempList != null) {
                for (int i = 0; i < tempList.length; i++) {
                    if (tempList[i].isFile()) {
                        files.add(tempList[i].toString());
                        //文件名，不包含路径
                        String fileName = tempList[i].getName();
//                 判断该csv文件是否已经建表,从MongoDB数据库中判断
                        if (createdTablesByCSVDao.IsCreatedTable(node.getNodeCode(), subjectCode, fileName)) {
                            jsonObject.put("tableIsExist", "表已存在");
//                        return jsonObject;   表已存在需要写进日志
                        } else {
                            //未建表，
                            String tableName = fileName.substring(0, fileName.lastIndexOf("."));
//                        判断表名是否存在，数据库中
                            if (tableIsExist(subject, tableName)) {
                                jsonObject.put("tableIsExist", "表已存在");
//                            return jsonObject;
                            }
//                      表不存在，建表
                            jsonObject = excelVersion(fileName, subject, tableName, node);
//                        建表和导入数据成功，添加关联信息
                            if (jsonObject.get("code").equals("success")) {
                                CreatedTables createdTables = new CreatedTables();
                                createdTables.setSubjectCode(subjectCode);
                                createdTables.setNodeCode(node.getNodeCode());
                                createdTables.setTableName(tableName);
                                createdTables.setFileName(fileName);
                                createdTablesByCSVDao.addCreatedTables(createdTables);
                            }
                        }
                    }
                    if (tempList[i].isDirectory()) {
                        //这里就不递归了，
                    }
                }
            } else {
                jsonObject.put("result", "该文件夹下没有文件");
            }
        }
        return jsonObject;
    }

    /**
     * @Description: 解析json建表
     * @Param: [fileName, subject, tableName]
     * @return: com.alibaba.fastjson.JSONObject
     * @Author: zcy
     * @Date: 2019/8/15
     */
    public JSONObject excelVersion(String fileName, Subject subject, String tableName, Node node) {
        JSONObject jsonObject = new JSONObject();
        Map<String, List<List<String>>> map = new HashMap<>();
        List<List<String>> lists = new ArrayList<>();
//        Linux
//        String dbFilePath=node.getDbPath()+"/"+fileName;
        String dbFilePath = node.getDbPath() + "\\" + fileName;

//        大文件的json
        // 获取当前用户的MySQL连接
        DataSrc dataSrc = getDataSrc(subject.getSubjectCode(), "mysql");
        Connection connection = null;
        try {
            connection = getConnection(dataSrc);
            if (connection == null) {
                jsonObject.put("code", "error");
                jsonObject.put("message", "数据库连接异常");
                return jsonObject;
            }

            lists = readJsonByFastJsonCreateTable(dbFilePath);
            List<String> columnList = lists.get(1);
            List<String> typeList=lists.get(2);
            List<TableField> list = formatData(lists);
            jsonObject = createTable(tableName, list,connection);
            if ("error".equals(jsonObject.get("code"))) {
                return jsonObject;
            }
//解析json数据，并插入表中
            jsonObject=readJsonByFastJsonTableData(dbFilePath, columnList, tableName,typeList,connection);
            if ("error".equals(jsonObject.get("code"))) {
                return jsonObject;
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonObject;
    }
    //    先解析出建表数据
    public  List<List<String>> readJsonByFastJsonCreateTable(String path) {
        File file = new File(path);//json文件路径
        Map<String, Object> map = new HashMap<>();

        List<String> columnList = new ArrayList<>();
        List<String> columnType = new ArrayList<>();
        List<String> columnLehgth = new ArrayList<>();
        List<String> columnPk = new ArrayList<>();
        List<String> columnComment = new ArrayList<>();

        List<TableField> list=new ArrayList<>();
        List<List<String>> tableTitle = new ArrayList<>();
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
//                            TableField tableField=new TableField();
                            String key1 = reader.readString();
                            String value1 = reader.readObject().toString();
                            String l[]=value1.split("','");
//                            tableField.setComment(l[1]);
//                            tableField.setField(key1);
//                            tableField.setType(l[2]);
//                            tableField.setLength(l[3]);
//                            tableField.setPk(l[4].substring(0,l[4].length()-1));
                            columnList.add(key1);//字段名
                            columnComment.add(l[1]);//注释
                            columnType.add(l[2]); //数据类型
                            columnLehgth.add(l[3]); //长度
                            columnPk.add(l[4].substring(0,l[4].length()-1));//是否是主键
//                            list.add(tableField);
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
                    map.put(key, o);
                }
            }
            reader.endObject();
            reader.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
//        Object o2 = map.get("COLUMNNOTE");
//        Object o3 = map.get("COLUMNTYPE");
//        Object o4 = map.get("LENGTH");
//        Object o5 = map.get("PRIMARYKEY");
//
//        List<String> columnType = new ArrayList<>();
//        List<String> columnLehgth = new ArrayList<>();
//        List<String> columnPk = new ArrayList<>();
//        List<String> columnComment = new ArrayList<>();
//
//        JSONArray jsonArray2 = JSONArray.parseArray(o2.toString());
//        JSONArray jsonArray3 = JSONArray.parseArray(o3.toString());
//        JSONArray jsonArray4 = JSONArray.parseArray(o4.toString());
//        JSONArray jsonArray5 = JSONArray.parseArray(o5.toString());
//
//        for (int i = 0; i < columnList.size(); i++) {
//            columnComment.add(jsonArray2.getJSONObject(0).get(columnList.get(i)).toString());
//            columnType.add(jsonArray3.getJSONObject(0).get(columnList.get(i)).toString());
//            columnLehgth.add(jsonArray4.getJSONObject(0).get(columnList.get(i)).toString());
//            columnPk.add(jsonArray5.getJSONObject(0).get(columnList.get(i)).toString());
//        }
//
        tableTitle.add(columnComment);
        tableTitle.add(columnList);
        tableTitle.add(columnType);
        tableTitle.add(columnLehgth);
        tableTitle.add(columnPk);
        return tableTitle;
    }

    public  JSONObject readJsonByFastJsonTableData(String path,List<String> columnList,String tableName,List<String> typeList,Connection connection) {
        JSONObject jsonObject = new JSONObject();
        File file = new File(path);//json文件路径
        Map<String, List<List<String>>> listMap = new HashMap<>();
        List<List<String>> dataList = new ArrayList<>();
        long start = System.currentTimeMillis();
//        解析表数据
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
                        if (dataList.size() % 1024 == 0) {
//                            一次插入1024条数据
                            String insertSql = getInsertSqlNew(tableName, dataList, typeList, columnList);
                            boolean execute = executeSql(connection, tableName, insertSql);
                            if (!execute) {
                                dropTable(connection, tableName);
                                jsonObject.put("code", "error");
                                jsonObject.put("message", tableName + "导入数据失败");
                                return jsonObject;
                            }
                            dataList.clear();
                        }
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
        if (dataList.size() > 0) {
            String insertSql = getInsertSqlNew(tableName, dataList, typeList, columnList);
            boolean execute = executeSql(connection, tableName, insertSql);
            if (!execute) {
                dropTable(connection, tableName);
                jsonObject.put("code", "error");
                jsonObject.put("message", tableName + "导入数据失败");
                return jsonObject;
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("解析时间和插入数据时间：" + dataList.size() + " 条数据需要：" + (end - start) / 1000.0);
        jsonObject.put("code", "success");
        jsonObject.put("message", tableName + "导入数据成功");
        return jsonObject;
    }


    /**
     * @Description: 格式化建表数据
     * @Param: [listTitle]
     * @return: java.util.List<cn.csdb.portal.model.TableField>
     * @Author: zcy
     * @Date: 2019/9/2
     */
    public List<TableField> formatData(List<List<String>> listTitle) {
        List<TableField> list = new ArrayList<>();
        for (int j = 0; j < listTitle.get(1).size(); j++) {
            TableField tableField = new TableField();
            tableField.setComment(listTitle.get(0).get(j));//注释
            tableField.setField(listTitle.get(1).get(j));   //字段名
            tableField.setType(listTitle.get(2).get(j));   //数据类型
            tableField.setLength(listTitle.get(3).get(j));  //长度
            tableField.setPk(listTitle.get(4).get(j));
            list.add(tableField);
        }
        return list;
    }

    /**
     * @Description: 建表和插入数据
     * @Param: [tableName, tableFields, subjectCode, listData]
     * @return: com.alibaba.fastjson.JSONObject
     * @Author: zcy
     * @Date: 2019/9/2
     */
    @Transactional
    public JSONObject createTable(String tableName, List<TableField> tableFields,Connection connection){
        JSONObject jsonObject = new JSONObject();
        List<String> typeList = new ArrayList<>();
        Boolean createTable = false;
        // 创建表
        typeList = createTableSqlByMysql(connection, tableName, tableFields);
        if (typeList.get(typeList.size() - 1).equals("false")) {
            createTable = false;
        } else {
            createTable = true;
        }
        typeList.remove(typeList.size() - 1);
        if (!createTable) {
            jsonObject.put("code", "error");
            jsonObject.put("message", "创建表失败");
            return jsonObject;
        }
        return jsonObject;
    }



    private String getInsertSqlNew(String tableName, List<List<String>> value, List<String> typeList,List<String> columnList) {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(tableName);
        sb.append("(");
        for (String column_name:columnList) {
            sb.append(column_name);
            sb.append(" ,");
        }
        sb = sb.deleteCharAt(sb.length() - 1);
        sb.append(") VALUES ");
        Iterator<List<String>> iterator = value.iterator();
        for (List<String> row : value) {
            sb.append("(");
            List<String> next = iterator.next();
            Iterator<String> iterator1 = next.iterator();
            int i = 0;
            while (iterator1.hasNext()) {
                String next1 = iterator1.next();
                sb.append("'");
                if ("".equals(next1.trim()) &&
                        ("int".equalsIgnoreCase(typeList.get(i)) || "float".equalsIgnoreCase(typeList.get(i)) || "double".equalsIgnoreCase(typeList.get(i)) || "decimal".equalsIgnoreCase(typeList.get(i)))) {
                    next1 = "0";
                    sb.append(next1);
                } else {
                    sb.append(next1);
                }
                sb.append("' ,");
                i++;
            }
            sb = sb.deleteCharAt(sb.length() - 1);
            sb.append("),");
        }
        sb = sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
    /**
     * @param connection
     * @param sql
     * @return 执行sql结果
     */
    private boolean executeSql(Connection connection, String tableName, String sql) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(tableName + "插入数据失败");
            return false;
        }
    }


    /**
     * @param connection
     * @param tableName  首次导入excel表数据失败 删除创建的表
     */
    private void dropTable(Connection connection, String tableName) {
        StringBuilder sb = new StringBuilder("drop table if exists ");
        sb.append(tableName);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sb.toString());
            preparedStatement.execute();
        } catch (SQLException e) {
            logger.error(tableName + "初次创建数据表插入数据失败 且 删除表失败");
            e.printStackTrace();
        }
    }

    /**
     * DDL create table 建表mysql+字段注释
     *
     * @param tableName
     * @param tableFields
     * @return
     */
    private List<String> createTableSqlByMysql(Connection connection, String tableName, List<TableField> tableFields) {
        List<String> primaryKeys = new ArrayList<>();
        List<String> typeList = new ArrayList<>();
        StringBuffer sb = new StringBuffer("CREATE TABLE ");
        sb.append(tableName);
        sb.append("(");
        Iterator<TableField> iterator = tableFields.iterator();
        while (iterator.hasNext()) {
            TableField next = iterator.next();
            String field = next.getField();
            String type = next.getType();
            String length = next.getLength();
            String comment = next.getComment();
            String pk = next.getPk();
            sb.append("`" + field + "`  ");
            sb.append(type);
//            excel普通版数据解析后带有.0，需要做特殊处理,json格式不用，excel大数据不用
//            int len = Integer.parseInt(length.substring(0,length.length()-2));
            int len = Integer.parseInt(length);
            if ("float".equalsIgnoreCase(type) || "double".equalsIgnoreCase(type) || "decimal".equalsIgnoreCase(type)) {
                len += 6;
                sb.append("(" + len + ",6)");
            } else if ("time".equalsIgnoreCase(type) || "date".equalsIgnoreCase(type) || "datetime".equalsIgnoreCase(type)) {

            } else {
                sb.append("(" + len + ")");
            }
            sb.append(" COMMENT '" + comment + "'");
            if ("1".equals(pk) || "1.0".equals(pk)) {
                primaryKeys.add(field);
            }
            typeList.add(type);
            sb.append(",");
        }
//        去掉PORTALID
        if (primaryKeys.size() != 0) {
            sb.append(" PRIMARY KEY (");
            for (String s : primaryKeys) {
                sb.append("`" + s + "`,");
            }
            sb = sb.deleteCharAt(sb.length() - 1);
            sb.append(")) ENGINE=INNODB DEFAULT CHARSET = UTF8");
        } else {
            sb = sb.deleteCharAt(sb.length() - 1);
            sb.append(") ENGINE=INNODB DEFAULT CHARSET = UTF8");
        }
        try {
//        sqlserver建表不成功
            PreparedStatement preparedStatement = connection.prepareStatement(sb.toString());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("创建表" + tableName + "失败");
            try {
                connection.close();
            } catch (SQLException ee) {
                ee.printStackTrace();
            }
            typeList.add("false");
            return typeList;
        }
        typeList.add("true");
        return typeList;
    }

    /**
     * 当前表是否存在
     *
     * @param table
     * @return
     */
    private boolean tableIsExist(Subject subject, String table) {
        StringBuilder sql = new StringBuilder("");
        sql.append("select COUNT(1) AS num from information_schema.TABLES t WHERE t.TABLE_SCHEMA = '" + subject.getDbName() + "' AND t.TABLE_NAME = '" + table + "'");
        String num = "";
        DataSrc dataSrc = getDataSrc(subject.getSubjectCode(), "mysql");
        Connection connection = getConnection(dataSrc);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
            ResultSet res = preparedStatement.executeQuery();
            while (res.next()) {
                num = res.getString("num");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error: could not retrieve data for the sql '" + sql + "' from the backend.");
        }
        return "1".equals(num);
    }
}
