package cn.csdb.portal.service;

import cn.csdb.portal.model.*;
import cn.csdb.portal.repository.CheckUserDao;
import cn.csdb.portal.repository.SynchronizationTablesDao;
import cn.csdb.portal.utils.Excel2ListIncludeNull;
import cn.csdb.portal.utils.SqlUtil;
import cn.csdb.portal.utils.dataSrc.DataSourceFactory;
import cn.csdb.portal.utils.dataSrc.IDataSource;
import com.alibaba.fastjson.JSONObject;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.SelectUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @ClassName FileImportService
 * @Description
 * @Author jinbao
 * @Date 2018/12/26 11:25
 * @Version 1.0
 **/
@Service
public class FileImportService {
    private Logger logger = LoggerFactory.getLogger(FileImportService.class);
    @Resource
    private CheckUserDao checkUserDao;
    @Resource
    private SynchronizationTablesDao synchronizationTablesDao;
    @Value("${synchronizeTableLogs}")
    private String synchronizeTableLogs;

    @Transactional
    public JSONObject processExcel(String tempFilePath, String subjectCode) {
        JSONObject jsonObject = new JSONObject();

        // 解析excel
        Map<String, List<List<String>>> mapSheet = parseExcel(tempFilePath);
        int sheetSize = mapSheet.keySet().size();
        if (sheetSize == 0) {
            jsonObject.put("code", "error");
            jsonObject.put("message", "excel数据为空");
            return jsonObject;
        } /*else if (sheetSize > 1) {
            jsonObject.put("code", "error");
            jsonObject.put("message", "excel中仅能有一个数据源");
            return jsonObject;
        }*/

        // 获取当前用户的MySQL连接
        Connection connection = null;
        try {
            DataSrc dataSrc = getDataSrc(subjectCode, "mysql");
            connection = getConnection(dataSrc);
            if (connection == null) {
                jsonObject.put("code", "error");
                jsonObject.put("message", "数据库连接异常");
                return jsonObject;
            }
            // sheet中  第一行为字段中文名称，第二行为数据库字段名称， sheet页名称为表名称
            //判断表是否存在 存在则判断字段是否对应   不存在则新增数据表
            List<Map<String, List<List<String>>>> resultList = new LinkedList<>();
            Iterator<Map.Entry<String, List<List<String>>>> iterator = mapSheet.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, List<List<String>>> next = iterator.next();
                String key = next.getKey();
                List<List<String>> value = next.getValue();
                if (value == null || value.size() == 0) {
                    jsonObject.put("code", "error");
                    jsonObject.put("message", key + "页签中数据为空，请查验");
                    return jsonObject;
                }

                // excel当前表存在比较数据库字段 与 excel字段 顺序、字段名称
                boolean tableIsExist = tableIsExist(connection, null, dataSrc.getDatabaseName(), key);
                Map<String, Map<String, List<String>>> tableResult = new LinkedHashMap<>();
                Map<String, List<String>> stringListMap = new LinkedHashMap<>();
                if (tableIsExist) {
                    stringListMap = tableField(key, connection);
                }
                // excel当前表不存在 选择字段类型、长度、是否主键
                stringListMap.put("excelField", value.get(1));
                stringListMap.put("excelComment", value.get(0));
                tableResult.put(key, stringListMap);
                Map<String, List<List<String>>> resultMap = formatterResult(tableResult);
                resultList.add(resultMap);
            }
            jsonObject.put("data", resultList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error:数据连接关闭错误");
            jsonObject.put("code", "error");
            jsonObject.put("message", "数据连接关闭错误");
            return jsonObject;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        jsonObject.put("code", "success");
        return jsonObject;
    }

    @Transactional
    public JSONObject createTableAndInsertValue(String tableName, List<TableField> tableFields, String tempFilePath, String subjectCode) {
        JSONObject jsonObject = new JSONObject();
        Map<String, List<List<String>>> mapSheet = new LinkedHashMap<>();
        mapSheet = parseExcel(tempFilePath);

        // 获取当前用户的MySQL连接
        DataSrc dataSrc = getDataSrc(subjectCode, "mysql");
        Connection connection = null;
        try {
            connection = getConnection(dataSrc);
            if (connection == null) {
                jsonObject.put("code", "error");
                jsonObject.put("message", "数据库连接异常");
                return jsonObject;

            }
            // 创建表
            Boolean createTable = createTableSql(connection, tableName, tableFields);
            if (!createTable) {
                jsonObject.put("code", "error");
                jsonObject.put("message", "创建表失败");
                return jsonObject;
            }

            // 插入数据
            Set<Map.Entry<String, List<List<String>>>> entries = mapSheet.entrySet();
            Iterator<Map.Entry<String, List<List<String>>>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, List<List<String>>> next = iterator.next();
                List<List<String>> value = next.getValue();
                String key = next.getKey();
                // sheet页名称需要与表名称匹配才能继续执行添加
                if (!key.equals(tableName)) {
                    continue;
                }
                jsonObject = insertValue2(connection, tableName, value);
                if ("error".equals(jsonObject.get("code"))) {
                    return jsonObject;
                }
            }
            jsonObject.put("code", "success");
            jsonObject.put("message", tableName + "创建成功，数据增添成功");
        } finally {
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

    @Transactional
    public JSONObject onlyInsertValue(String tableName, List<TableField> tableFields, String tempFilePath, String subjectCode) {
        JSONObject jsonObject = new JSONObject();
        Map<String, List<List<String>>> mapSheet = new LinkedHashMap<>();
        mapSheet = parseExcel(tempFilePath);
        // 获取当前用户的MySQL连接
        DataSrc dataSrc = getDataSrc(subjectCode, "mysql");
        Connection connection = null;
        try {
            connection = getConnection(dataSrc);
            if (connection == null) {
                jsonObject.put("code", "error");
                jsonObject.put("message", "数据库连接异常");
                return jsonObject;
            }
            Iterator<Map.Entry<String, List<List<String>>>> iterator = mapSheet.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, List<List<String>>> next = iterator.next();
                String key = next.getKey();
                // sheet页名称需要与表名称匹配才能继续执行添加
                if (!key.equals(tableName)) {
                    continue;
                }
                List<List<String>> value = next.getValue();
                jsonObject = onlyInsertSql(tableName, tableFields, value, connection);
                if ("error".equals(jsonObject.get("code"))) {
                    return jsonObject;
                }
            }
            jsonObject.put("code", "success");
        } finally {
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


    /**
     * 当前表是否存在
     *
     * @param connection
     * @param catalog
     * @param schema
     * @param table
     * @return
     */
    private boolean tableIsExist(Connection connection, String catalog, String schema, String table) {
        StringBuilder sql = new StringBuilder("");
        sql.append("select COUNT(1) AS num from information_schema.TABLES t WHERE t.TABLE_SCHEMA = '" + schema + "' AND t.TABLE_NAME = '" + table + "'");
        String num = "";
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


    /**
     * 基于事件处理excel生成 Map<表名，List<行值>>
     *
     * @param fileName
     * @return
     */
    private Map<String, List<List<String>>> parseExcel2(String fileName) {
        Excel2ListIncludeNull excel2ListIncludeNull = new Excel2ListIncludeNull();
        Map<String, List<List<String>>> map = null;
        try {
            map = excel2ListIncludeNull.processSheet(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    /**
     * 基于XSSFWorkbook对象处理Excel生成 Map<表名，List<行值>>
     *
     * @param fileName Excel全路径
     * @return
     */
    private Map<String, List<List<String>>> parseExcel(String fileName) {
        File excelFile = new File(fileName);
        XSSFWorkbook workbook = null;
        Map<String, List<List<String>>> map = new HashMap<>();
        List<List<String>> rowList = null;
        try {
            workbook = new XSSFWorkbook(new FileInputStream(excelFile));
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {
                XSSFSheet sheetAt = workbook.getSheetAt(i);
                int lastRowNum = sheetAt.getLastRowNum();
                rowList = new ArrayList<List<String>>(lastRowNum);
                for (int r = 0; r < lastRowNum; r++) {
                    XSSFRow row = sheetAt.getRow(r);
                    short lastCellNum = row.getLastCellNum();
                    List<String> cellList = new ArrayList<String>(lastCellNum);
                    for (int c = 0; c < lastCellNum; c++) {
                        XSSFCell cell = row.getCell(c);
                        String s = cell == null ? "" : cell.toString();
                        cellList.add(s);
                    }
                    rowList.add(cellList);
                }
                if (rowList.size() == 0) {
                    break;
                }
                String sheetName = sheetAt.getSheetName();
                map.put(sheetName, rowList);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("解析Excel异常！！！");
        }
        return map;
    }

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
     * 获取 已存在表的字段名称 & 字段注释 & 是否主键
     *
     * @param tableName
     * @param connection
     * @return
     * @throws SQLException
     */
    private Map<String, List<String>> tableField(String tableName, Connection connection) {
        Map<String, List<String>> tableField = new LinkedHashMap<>();
        List<String> fieldList = new LinkedList<>();
        List<String> commentList = new LinkedList<>();
        List<String> priKey = new LinkedList<>();
        StringBuilder sb = new StringBuilder("SHOW FULL COLUMNS FROM ");
        sb.append(tableName);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sb.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                fieldList.add(resultSet.getString("Field"));
                commentList.add(resultSet.getString("Comment"));
                priKey.add(resultSet.getString("Key"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("获取表" + tableName + "字段属性值错误！");
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        tableField.put("tableField", fieldList);
        tableField.put("tableComment", commentList);
        tableField.put("priKey", priKey);
        return tableField;
    }


    /**
     * 将table & excel 数据格式化输出 适合页面渲染
     *
     * @param jsonObject
     * @return
     */
    private Map<String, List<List<String>>> formatterResult(Map<String, Map<String, List<String>>> jsonObject) {
        Map<String, List<List<String>>> map = new LinkedHashMap<>();
        List<List<String>> resultList = new LinkedList<>();
        Set<String> strings = jsonObject.keySet();
        for (String s : strings) {
            Map<String, List<String>> tableMap = jsonObject.get(s);
            List<String> list = tableMap.get("tableField");
            List<String> list1 = tableMap.get("tableComment");
            List<String> list2 = tableMap.get("excelField");
            List<String> list3 = tableMap.get("excelComment");
            // 主键标识
            List<String> list4 = tableMap.get("priKey");
            int tableSize = list == null ? 0 : list.size();
            int excelSize = list2 == null ? 0 : list2.size();
            for (int i = 0; i < tableSize || i < excelSize; i++) {
                List<String> l = new LinkedList<>();
                if (i >= tableSize) {
                    l.add("");
                    l.add("");
                    l.add("");
                } else {
                    l.add(list.get(i));
                    l.add(list1.get(i));
                    l.add(list4.get(i));
                }
                if (i >= excelSize) {
                    l.add("");
                    l.add("");
                } else {
                    l.add(list2.get(i));
                    l.add(list3.get(i));
                }
                // l【0】：当前表是否存在标志位
                // l【1~length-2】：字段信息
                // l【length】：当前表的主键
                resultList.add(l);
            }

            // 根据表字段集合长度判断 当前表是否存在
            List<String> status = new LinkedList<>();
            if (list == null) {
                status.add("notExist");
                ((LinkedList<List<String>>) resultList).addFirst(status);
            } else {
                status.add("isExist");
                ((LinkedList<List<String>>) resultList).addFirst(status);
            }
            map.put(s, resultList);
        }
        return map;
    }


    /**
     * DDL create table
     *
     * @param tableName
     * @param tableFields
     * @return
     */
    private Boolean createTableSql(Connection connection, String tableName, List<TableField> tableFields) {
        List<String> primaryKeys = new ArrayList<>(3);
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
            sb.append("(" + length + ")");
            sb.append("COMMENT '" + comment + "'");
            if ("1".equals(pk)) {
                primaryKeys.add(field);
            }
            sb.append(",");
        }
        sb.append("PORTALID VARCHAR(36) COMMENT '中心端系统ID', PRIMARY KEY (");
        if (primaryKeys.size() != 0) {
            for (String s : primaryKeys) {
                sb.append("`" + s + "`,");
            }
        }
        sb.append("PORTALID)) ENGINE=INNODB DEFAULT CHARSET = UTF8");
        try {
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
            return false;
        }
        return true;
    }


    /**
     * 插入数据
     *
     * @param connection
     * @param tableName
     * @param value
     * @return
     */
    private JSONObject insertValue2(Connection connection, String tableName, List<List<String>> value) {
        JSONObject jsonObject = new JSONObject();
        String insertSql = null;
        boolean execute = true;
        List<List<String>> lists = value.subList(2, value.size());
        List<List<String>> l = new ArrayList<>(1024);
        for (int i = 0; i < lists.size(); i++) {
            l.add(lists.get(i));
            if (l.size() % 1024 == 0) {
                insertSql = getInsertSql(connection, tableName, l);
                execute = executeSql(connection, tableName, insertSql);
                l.clear();
            } else if (i == lists.size() - 1) {
                insertSql = getInsertSql(connection, tableName, l);
                execute = executeSql(connection, tableName, insertSql);
                l.clear();
            }
            if (!execute) {
                dropTable(connection, tableName);
                jsonObject.put("code", "error");
                jsonObject.put("message", tableName + "导入数据失败");
                return jsonObject;
            }
        }
        jsonObject.put("code", "success");
        jsonObject.put("message", tableName + "导入数据成功");
        return jsonObject;
    }

    /**
     * @param connection
     * @param tableName
     * @param value
     * @return 获取分段insert语句
     */
    private String getInsertSql(Connection connection, String tableName, List<List<String>> value) {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(tableName);
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, tableName, null);
            sb.append("(");
            while (columns.next()) {
                String column_name = columns.getString("COLUMN_NAME");
                if ("PORTALID".equals(column_name)) {
                    continue;
                }
                sb.append(column_name);
                sb.append(" ,");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
        sb.replace(sb.length() - 1, sb.length(), ",PORTALID) VALUES");
        Iterator<List<String>> iterator = value.iterator();
        for (List<String> row : value) {
            sb.append("(");
            List<String> next = iterator.next();
            Iterator<String> iterator1 = next.iterator();
            while (iterator1.hasNext()) {
                String next1 = iterator1.next();
                sb.append("'");
                sb.append(next1);
                sb.append("' ,");
            }
            String s = UUID.randomUUID().toString();
            sb.append("'");
            sb.append(s);
            sb.append("'");
            sb.append("),");
        }
        sb.replace(sb.length() - 1, sb.length(), "");
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


    private JSONObject onlyInsertSql(String tableName, List<TableField> tableFields, List<List<String>> value, Connection connection) {
        JSONObject jsonObject = new JSONObject();

        // 插入的数据
        List<List<String>> insertValue = value.subList(2, value.size());
        // excel字段名称
        List<String> excelField = value.get(1);
        // k:表字段名->v:excel字段名
        Map<String, String> fieldMapping = new LinkedHashMap<>();
        // 前端数据 原始表字段顺序
        List<String> tableFiled = new ArrayList<>(64);
        // 前端数据 原始表字段对应excel字段顺序
        List<String> newField = new ArrayList<>(1024);

        try {
            String sql = "";
            List<List<String>> l = new ArrayList<>(1024);
            connection.setAutoCommit(false);
            for (int flg = 0; flg < insertValue.size(); flg++) {
                l.add(insertValue.get(flg));
                if (flg != 0 && flg % 1024 == 0) {
                    sql = onlyInsertGetInsertSql(tableFields, excelField, tableName, l);
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.execute();
                    l.clear();
                } else if (flg == insertValue.size() - 1) {
                    sql = onlyInsertGetInsertSql(tableFields, excelField, tableName, l);
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.execute();
                    l.clear();
                }
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            jsonObject.put("code", "error");
            jsonObject.put("message", e.getMessage());
            logger.error(e.getMessage());
            logger.error("ERROR: " + tableName + "插入数据失败");
            return jsonObject;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("ERROR: " + tableName + "插入数据关闭数据连接错误");
                logger.error(e.getMessage());
            }
        }
        jsonObject.put("message", tableName + "插入数据成功");
        return jsonObject;
    }

    private String onlyInsertGetInsertSql(List<TableField> tableFields, List<String> excelField, String tableName, List<List<String>> insertValue) {
        // excel数据位置
        List<String> orderNum = new ArrayList<>(64);

        // k:表字段名->v:excel字段名
        Map<String, String> fieldMapping = new LinkedHashMap<>();
        // 前端数据 原始表字段顺序
        List<String> tableFiled = new ArrayList<>(64);
        // 前端数据 原始表字段对应excel字段顺序
        List<String> newField = new ArrayList<>(1024);
        StringBuffer sb = new StringBuffer("INSERT INTO `" + tableName + "`");
        for (TableField t : tableFields) {
            tableFiled.add(t.getOldField());
            newField.add(t.getField());
            fieldMapping.put(t.getOldField(), t.getField());
        }

        Set<String> strings = fieldMapping.keySet();
        sb.append("( ");
        for (String k : strings) {
            String v = fieldMapping.get(k);
            if (!"-1".equals(v)) {
                sb.append(k + ",");
            }
        }

        for (String n : newField) {
            for (int i = 0; i < excelField.size(); i++) {
                String s = excelField.get(i);
                if (n.equals(s)) {
                    orderNum.add(Integer.valueOf(i).toString());
                    break;
                }
            }
        }
        if (sb.toString().endsWith(",")) {
            sb.replace(sb.length() - 1, sb.length(), " ,PORTALID)");
        }
        sb.append(" VALUES ");
        for (List<String> row : insertValue) {
            sb.append("( ");
            for (String i : orderNum) {
                int j = Integer.parseInt(i);
                sb.append("'" + row.get(j) + "',");
            }
            String s = UUID.randomUUID().toString();
            sb.append("'");
            sb.append(s);
            sb.append("'),");
        }
        if (sb.toString().endsWith(",")) {
            sb.replace(sb.length() - 1, sb.length(), "");
        }
        String sql = sb.toString();
        return sql;
    }

    public JSONObject deleteTableName(String tableName, String subjectCode) {
        JSONObject jsonObject = new JSONObject();
        DataSrc mysql = getDataSrc(subjectCode, "mysql");
        Connection connection = null;
        try {
            connection = getConnection(mysql);
            if (connection == null) {
                jsonObject.put("code", "error");
                jsonObject.put("message", "数据库连接异常");
            } else {
                String sql = "drop table ".concat(tableName);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.execute();
                jsonObject.put("code", "success");
                jsonObject.put("message", "“" + tableName + "”表删除成功");
            }
        } catch (SQLException e) {
            jsonObject.put("code", "error");
            jsonObject.put("message", "执行异常");
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    jsonObject.put("code", "error");
                    jsonObject.put("message", "关闭异常");
                    e.printStackTrace();
                }
            }
        }
        return jsonObject;
    }

    public String validateSqlString(String sqlString, String subjectCode) {
        String message = "true";
        DataSrc dataSrc = getDataSrc(subjectCode, "mysql");
        boolean b = false;
        try {
            b = SqlUtil.validateSelectSql(sqlString);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return "请输入查询语句";
        }

        if (!b) {
            return "查询语句错误";
        }

        // 不允许 使用 * 查询
        boolean find = Pattern.compile(" \\* ").matcher(sqlString).find();
        if (find) {
            return "请明确查询的字段";
        }
        Connection connection = getConnection(dataSrc);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            message = e.getMessage();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return message;
    }

    public String validateTableName(String tableName, String subjectCode) {
        DataSrc dataSrc = getDataSrc(subjectCode, "mysql");
        Connection connection = getConnection(dataSrc);
        boolean tableIsExist = tableIsExist(connection, null, dataSrc.getDatabaseName(), tableName);
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableIsExist ? tableName + ":已经存在" : "true";
    }

    public JSONObject previewSqlData(String sqlString, String subjectCode) {
        JSONObject jsonObject = new JSONObject();
        String validateSqlString = validateSqlString(sqlString, subjectCode);
        if (!"true".equals(validateSqlString)) {
            return null;
        }

        DataSrc dataSrc = getDataSrc(subjectCode, "mysql");
        Connection connection = getConnection(dataSrc);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            ResultSet previewResult = preparedStatement.executeQuery();

            // 获取字段名称列表
            List<String> columnName = new ArrayList<>();
            ResultSetMetaData metaData = previewResult.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String c = metaData.getColumnLabel(i);
                if (!"PORTALID".equalsIgnoreCase(c)) {
                    columnName.add(c);
                }
            }

            // 取得实际数据
            ResultSet resultSet = preparedStatement.executeQuery();
            List<ArrayList<String>> dataList = new ArrayList<ArrayList<String>>();
            while (resultSet.next() && dataList.size() < 11) {
                ArrayList<String> datas = new ArrayList<>();
                for (String name : columnName) {
                    String data = resultSet.getString(name);
                    datas.add(data);
                }
                dataList.add(datas);
            }
            jsonObject.put("columnName", columnName);
            if (dataList.size() > 10) {
                jsonObject.put("range", "out");
                jsonObject.put("data", dataList.subList(0, 10));
            } else {
                jsonObject.put("range", "in");
                jsonObject.put("data", dataList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            jsonObject.put("code", "error");
            jsonObject.put("message", "预览sql异常");
            return jsonObject;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        jsonObject.put("code", "success");
        return jsonObject;
    }

    public String createTableBySql(String newSql, String newName, String subjectCode) throws SQLException {
        String validateSqlString = validateSqlString(newSql, subjectCode);
        if (!"true".equals(validateSqlString)) {
            return "请检查sql语句";
        }
        Select select = null;
        try {
            select = (Select) CCJSqlParserUtil.parse(newSql);
            SelectUtils.addExpression(select, new Column("UUID() as PORTALID"));

        } catch (JSQLParserException e) {
            e.printStackTrace();
            return "请检查sql语句";
        }
        String createBySelect = "CREATE TABLE " + newName + " ( " + select.toString() + " )";
        DataSrc dataSrc = getDataSrc(subjectCode, "mysql");
        Connection connection = getConnection(dataSrc);
        PreparedStatement preparedStatement = connection.prepareStatement(createBySelect);
        preparedStatement.execute();
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
            }
        }
        return newName + "：创建成功";
    }

    public void createSynchronizeTask(String newSql, String newName, String subjectCode, Long periodTime, String loginId) {
        SynchronizationTable synchronizationTable = new SynchronizationTable();
        synchronizationTable.setFrequency(periodTime);
        synchronizationTable.setLastModifyTime(new Date().getTime());
        synchronizationTable.setSqlString(newSql);
        synchronizationTable.setSubjectCode(subjectCode);
        synchronizationTable.setTableName(newName);
        synchronizationTable.setLoginId(loginId);
        synchronizationTable.setSystemName("DataAssembler");
        synchronizationTablesDao.save(synchronizationTable);
    }

    public void SynchronizeTask(String sqlString, String tempTableName, String subjectCode, String tableName) {
        DataSrc dataSrc = getDataSrc(subjectCode, "mysql");
        Connection connection = getConnection(dataSrc);
        PrintWriter printWriter = null;
        try {
            printWriter = getPrintWriter(tableName);
            Date start = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String startTimeString = sdf.format(start);
            printWriter.println("======== 开始执行同步表操作： " + tableName + "======== 时间：" + startTimeString);
            printWriter.println("-------- 创建中间表 --------");
            Select select = (Select) CCJSqlParserUtil.parse(sqlString);
            SelectUtils.addExpression(select, new Column("UUID() as PORTALID"));
            String createTempTableBySelect = "CREATE TABLE " + tempTableName + " ( " + select.toString() + " )";
            printWriter.println(createTempTableBySelect);
            connection.prepareStatement(createTempTableBySelect).execute();

            printWriter.println("-------- 删除原始表 --------");
            String deleteTempTable = "DROP TABLE IF EXISTS " + tableName;
            connection.prepareStatement(deleteTempTable).execute();

            printWriter.println("-------- 中间表重命名为同步表 --------");
            String renameTable = "RENAME TABLE " + tempTableName + " TO " + tableName;
            connection.prepareStatement(renameTable).execute();
            Date end = new Date();
            String endTimeString = sdf.format(end);
            printWriter.println("======== 结束完成同步表操作： " + tableName + "======== 时间：" + endTimeString);
        } catch (SQLException e) {
            e.printStackTrace();
            printWriter.println("========error========");
            printWriter.println(e.getMessage());
            printWriter.println("========error========");
        } catch (JSQLParserException e) {
            e.printStackTrace();
            printWriter.println("========error========");
            printWriter.println(e.getMessage());
            printWriter.println("========error========");
        } finally {
            printWriter.flush();
            printWriter.close();
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    printWriter.println("========error========");
                    printWriter.println(e.getMessage());
                    printWriter.println("========error========");
                }
            }
        }
    }

    private PrintWriter getPrintWriter(String tableName) {
        PrintWriter printWriter = null;
        FileWriter fileWriter;
        String logFilePath = synchronizeTableLogs + "/" + tableName + ".log";
        File logFile = new File(logFilePath);
        if (!logFile.getParentFile().exists()) {
            logFile.getParentFile().mkdirs();
        }

        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
                System.out.println("创建：" + logFilePath);
            }
            fileWriter = new FileWriter(logFile, true);
            printWriter = new PrintWriter(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return printWriter;
    }

    public JSONObject selectSynchronizeInfo(String loginId, String subjectCode) {
        JSONObject jsonObject = new JSONObject();
        List<SynchronizationTable> synchronizationTables = synchronizationTablesDao.selectSynchronizeInfo(loginId, subjectCode);
        Map<String, Period> map = new HashMap<>(16);
        for (SynchronizationTable synchronizationTable : synchronizationTables) {
            Period period = Period.selectPeriodByDataTime(synchronizationTable.getFrequency());
            map.put(synchronizationTable.getId(), period);
        }
        jsonObject.put("list", synchronizationTables);
        jsonObject.put("select", map);
        return jsonObject;
    }

    public void updateSynchronizeTable(String synchronizeId, String frequency) {
        synchronizationTablesDao.updateByIdAndFrequency(synchronizeId, frequency);
    }
}
