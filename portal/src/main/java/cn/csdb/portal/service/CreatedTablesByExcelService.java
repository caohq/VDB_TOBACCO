//package cn.csdb.portal.service;
//
//import cn.csdb.portal.model.*;
//import cn.csdb.portal.repository.*;
//import cn.csdb.portal.utils.ExcelXlsReader;
//import cn.csdb.portal.utils.ExcelXlsxReaderWithDefaultHandler;
//import cn.csdb.portal.utils.dataSrc.DataSourceFactory;
//import cn.csdb.portal.utils.dataSrc.IDataSource;
//import com.alibaba.fastjson.JSONObject;
//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.xssf.usermodel.XSSFCell;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.Resource;
//import java.io.*;
//import java.sql.*;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.Date;
//
//@Service
//public class CreatedTablesByExcelService {
//
//
//    private Logger logger = LoggerFactory.getLogger(CreatedTablesByExcelService.class);
//
//    @Resource
//    private SubjectDao subjectDao;
//    @Resource
//    private CreatedTablesByExcelDao createdTablesByCSVDao;
//
//    @Resource
//    private DataSrcDao dataSrcDao;
//
//    @Resource
//    private CheckUserDao checkUserDao;
//
//    @Resource
//    private NodeDao nodeDao;
//
//    /**
//     * 根据当前用户获取相关连接信息
//     *
//     * @param subjectCode
//     * @param DatabaseType
//     * @return
//     */
//    private DataSrc getDataSrc(String subjectCode, String DatabaseType) {
//        Subject subject = checkUserDao.getSubjectByCode(subjectCode);
//        DataSrc datasrc = new DataSrc();
//        datasrc.setDatabaseName(subject.getDbName());
//        datasrc.setDatabaseType(DatabaseType);
//        datasrc.setHost(subject.getDbHost());
//        datasrc.setPort(subject.getDbPort());
//        datasrc.setUserName(subject.getDbUserName());
//        datasrc.setPassword(subject.getDbPassword());
//        return datasrc;
//    }
//
//
//    /**
//     * 获取数据库连接
//     *
//     * @param dataSrc
//     * @return
//     */
//    private Connection getConnection(DataSrc dataSrc) {
//        IDataSource dataSource = DataSourceFactory.getDataSource(dataSrc.getDatabaseType());
//        Connection connection = dataSource.getConnection(dataSrc.getHost(), dataSrc.getPort(), dataSrc.getUserName(), dataSrc.getPassword(), dataSrc.getDatabaseName());
//        return connection;
//    }
//
//    public JSONObject ergodicNodeList(String subjectCode){
//        JSONObject jsonObject = new JSONObject();
//        List<Node> nodes=nodeDao.findBySubjectCode(subjectCode);
//        for(Node node:nodes){
//            getDbFile(subjectCode,node);
//        }
//        return jsonObject;
//    }
//
//    //    根据 /home/ ThemeCode /节点名称/ db目录下的csv文件，
//    //    怎么判断哪些csv文件已经建表成功，哪些还未建表
//    /**
//    * @Description:
//    * @Param: [subjectCode, node]
//    * @return: com.alibaba.fastjson.JSONObject
//    * @Author: zcy
//    * @Date: 2019/8/21
//    */
//    public JSONObject getDbFile(String subjectCode,Node node) {
//        JSONObject jsonObject = new JSONObject();
//        Subject subject = subjectDao.findBySubjectCode(subjectCode);
////        Node node=nodeDao.findByNodeCode(nodeCode);
//        String dbFilePath;
//        if (subject != null) {
//            dbFilePath = node.getDbPath();
//            List<String> files = new ArrayList<String>();
//            File file = new File(dbFilePath);
//            File[] tempList = file.listFiles();
//            if(tempList!=null) {
//                for (int i = 0; i < tempList.length; i++) {
//                    if (tempList[i].isFile()) {
//                        files.add(tempList[i].toString());
//                        //文件名，不包含路径
//                        String fileName = tempList[i].getName();
////                 判断该csv文件是否已经建表,从MongoDB数据库中判断
//                        if (createdTablesByCSVDao.IsCreatedTable(node.getNodeCode(), subjectCode, fileName)) {
//                            jsonObject.put("tableIsExist", "表已存在");
////                        return jsonObject;   表已存在需要写进日志
//                        } else {
//                            //未建表，
//                            String tableName = fileName.substring(0, fileName.lastIndexOf("."));
////                        判断表名是否存在，数据库中
//                            if (tableIsExist(subject, tableName)) {
//                                jsonObject.put("tableIsExist", "表已存在");
////                            return jsonObject;
//                            }
////                      表不存在，建表
//                            jsonObject = excelVersion(fileName, subject, tableName, node);
////                        建表和导入数据成功，添加关联信息
//                            if (jsonObject.get("code").equals("success")) {
//                                CreatedTables createdTables = new CreatedTables();
//                                createdTables.setSubjectCode(subjectCode);
//                                createdTables.setNodeCode(node.getNodeCode());
//                                createdTables.setTableName(tableName);
//                                createdTables.setFileName(fileName);
//                                createdTablesByCSVDao.addCreatedTables(createdTables);
//                            }
//                        }
//                    }
//                    if (tempList[i].isDirectory()) {
//                        //这里就不递归了，
//                    }
//                }
//            }else{
//                jsonObject.put("result","该文件夹下没有文件");
//            }
//        }
//        return jsonObject;
//    }
//
//    /**
//     * @Description: 根据excel不同版本，调用不同的解析方法
//     * @Param: [fileName, subject, tableName]
//     * @return: com.alibaba.fastjson.JSONObject
//     * @Author: zcy
//     * @Date: 2019/8/15
//     */
//    public JSONObject excelVersion(String fileName, Subject subject, String tableName ,Node node) {
//        JSONObject jsonObject = new JSONObject();
//        Map<String, List<List<String>>> map = new HashMap<>();
//        List<List<String>> lists = new ArrayList<>();
//        List<List<String>> listDate = new ArrayList<>();
//        String dbFilePath=node.getDbPath()+"/"+fileName;
//        if (fileName.matches("^.+\\.(?i)(xls)$")) {
////            普通数据解析
////            map = parseExcelBy2003(fileName, subject,dbFilePath);
////            大文件数据解析
//            ExcelXlsReader excelXlsReader=new ExcelXlsReader();
//            try {
//              map=excelXlsReader.process(dbFilePath);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        } else if (fileName.matches("^.+\\.(?i)(xlsx)$")) { //@描述：是否是2007的excel，返回true是2007
////            解析普通excel2007版本及以上
////             map = parseExcelBy2007(dbFilePath);
////            POI读取大数据
//            ExcelXlsxReaderWithDefaultHandler excelXlsxReaderWithDefaultHandler=new ExcelXlsxReaderWithDefaultHandler();
////
////            ExcelXlsxReader excelXlsxReader=new ExcelXlsxReader();
//            try {
//                map=excelXlsxReaderWithDefaultHandler.process(dbFilePath);
////                map=excelXlsxReader.processSheet(dbFilePath);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            jsonObject.put("code", "文件格式错误,文件的扩展名只能是xls或xlsx!");
//            return jsonObject;
//        }
//        lists = map.get("title");
//        listDate = map.get("tableDate");
//        List<TableField> list = formatData(lists);
//        jsonObject = createTableAndInsertValue(tableName, list, subject.getSubjectCode(), listDate);
//        return jsonObject;
//    }
//
//    public List<TableField> formatData(List<List<String>> listTitle) {
//        List<TableField> list = new ArrayList<>();
//        for (int j = 0; j < listTitle.get(1).size(); j++) {
//            TableField tableField = new TableField();
//            tableField.setComment(listTitle.get(0).get(j));//注释
//            tableField.setField(listTitle.get(1).get(j));   //字段名
//            tableField.setType(listTitle.get(2).get(j));   //数据类型
//            tableField.setLength(listTitle.get(3).get(j));  //长度
//            tableField.setPk(listTitle.get(4).get(j));
//            list.add(tableField);
//        }
//        return list;
//    }
//
//    /**
//     * 基于XSSFWorkbook对象处理Excel生成 Map<表名，List<行值>>
//     * .xls格式的excel文件需要HSSF支持，需要相应的poi.jar，.xlsx格式的excel文件需要XSSF支持，需要poi-ooxml.jar，
//     *
//     * @param
//     * @return
//     */
//    public Map<String, List<List<String>>> parseExcelBy2007(String dbFilePath) {
////        File excelFile = new File(subject.getDbPath() + "\\" + fileName);
//        File excelFile = new File(dbFilePath);
//        XSSFWorkbook workbook = null;
//        Map<String, List<List<String>>> map = new HashMap<>();
//        List<List<String>> lists = new ArrayList<>();
//        List<List<String>> listDate = new ArrayList<>();
//        int realcellNum = 0;
//        List<String> typeList = new ArrayList<>();
//        try {
//            workbook = new XSSFWorkbook(new FileInputStream(excelFile));
//            int numberOfSheets = workbook.getNumberOfSheets();
//            for (int i = 0; i < numberOfSheets; i++) {
//                if (i == 0) {
////                    获得第i个sheet页
//                    XSSFSheet sheetAt = workbook.getSheetAt(i);
//                    int lastRowNum = sheetAt.getLastRowNum();
//                    if (lastRowNum > 0) {
//                        for (int r = 0; r <= lastRowNum; r++) {
//                            XSSFRow row = sheetAt.getRow(r);
//                            int lastCellNum = row.getLastCellNum();
//                            List<String> cellList = new ArrayList<>();
////                        根据第二行的字段名，获取最完整的列数
//                            realcellNum = sheetAt.getRow(1).getLastCellNum();
//                            for (int c = 0; c < realcellNum; c++) {
//                                XSSFCell cell = row.getCell(c);
//                                String s = "";
////                             excel的时间和日期格式需要单独处理
//                                if (r >= 5) {
//                                    if (typeList.size() > 0) {
//                                        String dateType = typeList.get(c);
//                                        s = formatDateExcel(cell, dateType);
//                                        System.out.println(s);
//                                    }
//                                } else {
//                                    s = cell == null ? "" : cell.toString();
//                                }
//                                cellList.add(s);
//                            }
//                            if (r < 5) {
//                                if (r == 2) {
//                                    typeList = cellList;
//                                }
//                                lists.add(cellList);
//                            } else {
//                                listDate.add(cellList);
//                            }
//                        }
//                    }
//                }
////                第二个sheet页仍从第六行开始读取数据
//                if (i > 0) {
////                    获得第i个sheet页
//                    XSSFSheet sheetAt = workbook.getSheetAt(i);
//                    int lastRowNum = sheetAt.getLastRowNum();
//                    if (lastRowNum > 0) {
//                        for (int r = 0; r <= lastRowNum; r++) {
//                            XSSFRow row = sheetAt.getRow(r);
//                            int lastCellNum = row.getLastCellNum();
//                            List<String> cellList = new ArrayList<>();
//                            for (int c = 0; c < realcellNum; c++) {
//                                XSSFCell cell = row.getCell(c);
////                                String s = cell == null ? "" : cell.toString();
//                                if (typeList.size() > 0) {
//                                    String dateType = typeList.get(c);
//                                    String s = formatDateExcel(cell, dateType);
//                                    cellList.add(s);
//                                }
//                            }
//                            listDate.add(cellList);
//                        }
//                    }
//                }
//            }
//            map.put("title", lists);
//            map.put("tableDate", listDate);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            logger.error("解析Excel异常！！！");
//        }
//        return map;
//    }
//
//    public String formatDateExcel(Cell cell, String dateType) {
//        String s;
//        if ("date".equalsIgnoreCase(dateType)) {
//            DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
//            s = formater.format(cell.getDateCellValue());
//        } else if ("time".equalsIgnoreCase(dateType)) {
//            DateFormat formater = new SimpleDateFormat("HH:mm:ss");
//            s = formater.format(cell.getDateCellValue());
//        } else if ("datetime".equalsIgnoreCase(dateType)) {
//            DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            s = formater.format(cell.getDateCellValue());
//        } else {
//            s = cell == null ? "" : cell.toString();
//        }
//        return s;
//    }
//
//    public String formatDateExcelBy2003(Cell cell, String dateType) {
//        String s = "";
//        if ("date".equalsIgnoreCase(dateType)) {
//            DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
//            try {
//                Date date = new SimpleDateFormat("MM/dd/yyyy").parse(cell.toString());
//                s = formater.format(date);
//                System.out.println();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        } else if ("datetime".equalsIgnoreCase(dateType)) {
//            DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            try {
//                Date date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(cell.toString());
//                s = formater.format(date);
//                System.out.println();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        } else {
//            s = cell == null ? "" : cell.toString();
//        }
//
//        return s;
//    }
//
//    public Map<String, List<List<String>>> parseExcelBy2003(String fileName, Subject subject,String dbFilePath) {
////        File excelFile = new File(subject.getDbPath() + "\\" + fileName);
//        File excelFile = new File(dbFilePath);
//        HSSFWorkbook workbook = null;
//        Map<String, List<List<String>>> map = new HashMap<>();
//        List<List<String>> lists = new ArrayList<>();
//        List<List<String>> listDate = new ArrayList<>();
//        List<String> typeList = new ArrayList<>();
//        int realcellNum = 0;
//        try {
//            workbook = new HSSFWorkbook(new FileInputStream(excelFile));
//            int numberOfSheets = workbook.getNumberOfSheets();
//            for (int i = 0; i < numberOfSheets; i++) {
//                if (i == 0) {
////                    获得第i个sheet页
//                    HSSFSheet sheetAt = workbook.getSheetAt(i);
//                    int lastRowNum = sheetAt.getLastRowNum();
//                    if (lastRowNum > 0) {
//                        for (int r = 0; r <= lastRowNum; r++) {
//                            HSSFRow row = sheetAt.getRow(r);
//                            int lastCellNum = row.getLastCellNum();
//                            List<String> cellList = new ArrayList<>();
////                        根据第二行的字段名，获取最完整的列数
//                            realcellNum = sheetAt.getRow(1).getLastCellNum();
//                            for (int c = 0; c < realcellNum; c++) {
//                                HSSFCell cell = row.getCell(c);
//                                String s = "";
////                                String s = cell == null ? "" : cell.toString();
//                                if (r >= 5) {
//                                    if (typeList.size() > 0) {
//                                        String dateType = typeList.get(c);
//                                        s = formatDateExcelBy2003(cell, dateType);
//                                        System.out.println(s);
//                                    }
//                                } else {
//                                    s = cell == null ? "" : cell.toString();
//                                }
//                                cellList.add(s);
////                                System.out.println(getCellValue(cell));
//                            }
//                            if (r < 5) {
//                                if (r == 2) {
//                                    typeList = cellList;
//                                }
//                                lists.add(cellList);
//                            } else {
//                                listDate.add(cellList);
//                            }
//                        }
//                    }
//                }
//                if (i > 0) {
////                    获得第i个sheet页
//                    HSSFSheet sheetAt = workbook.getSheetAt(i);
//                    int lastRowNum = sheetAt.getLastRowNum();
//                    if (lastRowNum > 0) {
//                        for (int r = 0; r <= lastRowNum; r++) {
//                            HSSFRow row = sheetAt.getRow(r);
//                            int lastCellNum = row.getLastCellNum();
//                            List<String> cellList = new ArrayList<>();
//                            for (int c = 0; c < realcellNum; c++) {
//                                HSSFCell cell = row.getCell(c);
////                                String s = cell == null ? "" : cell.toString();
//                                if (typeList.size() > 0) {
//                                    String dateType = typeList.get(c);
//                                    String s = formatDateExcelBy2003(cell, dateType);
//                                    cellList.add(s);
//                                }
//                            }
//                            listDate.add(cellList);
//                        }
//                    }
//                }
//            }
//            map.put("title", lists);
//            map.put("tableDate", listDate);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            logger.error("解析Excel异常！！！");
//        }
//        return map;
//    }
//
//    @Transactional
//    public JSONObject createTableAndInsertValue(String tableName, List<TableField> tableFields, String subjectCode, List<List<String>> listData) {
//        JSONObject jsonObject = new JSONObject();
//        // 获取当前用户的MySQL连接
//        DataSrc dataSrc = getDataSrc(subjectCode, "mysql");
//        Connection connection = null;
//        try {
//            connection = getConnection(dataSrc);
//            if (connection == null) {
//                jsonObject.put("code", "error");
//                jsonObject.put("message", "数据库连接异常");
//                return jsonObject;
//            }
//
//            List<String> typeList = new ArrayList<>();
//            Boolean createTable = false;
//            // 创建表
//            typeList = createTableSqlByMysql(connection, tableName, tableFields);
//            if (typeList.get(typeList.size() - 1).equals("false")) {
//                createTable = false;
//            } else {
//                createTable = true;
//            }
//            typeList.remove(typeList.size() - 1);
//            if (!createTable) {
//                jsonObject.put("code", "error");
//                jsonObject.put("message", "创建表失败");
//                return jsonObject;
//            }
//            // 插入数据
//            jsonObject = insertValue2(connection, tableName, listData, typeList);
//            if ("error".equals(jsonObject.get("code"))) {
//                return jsonObject;
//            }
//
//            jsonObject.put("code", "success");
//            jsonObject.put("message", tableName + "创建成功，数据增添成功");
//        } finally {
//            if (connection != null) {
//                try {
//                    connection.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return jsonObject;
//    }
//
//
//    /**
//     * 插入数据
//     *
//     * @param connection
//     * @param tableName
//     * @param value
//     * @return
//     */
//    private JSONObject insertValue2(Connection connection, String tableName, List<List<String>> value, List<String> typeList) {
//        JSONObject jsonObject = new JSONObject();
//        String insertSql = null;
//        boolean execute = true;
//        List<List<String>> lists = value.subList(2, value.size());
//        List<List<String>> l = new ArrayList<>(1024);
//        for (int i = 0; i < lists.size(); i++) {
//            l.add(lists.get(i));
//            if (l.size() % 1024 == 0) {
//                insertSql = getInsertSql(connection, tableName, l, typeList);
//                execute = executeSql(connection, tableName, insertSql);
//                l.clear();
//            } else if (i == lists.size() - 1) {
//                insertSql = getInsertSql(connection, tableName, l, typeList);
//                execute = executeSql(connection, tableName, insertSql);
//                l.clear();
//            }
//            if (!execute) {
//                dropTable(connection, tableName);
//                jsonObject.put("code", "error");
//                jsonObject.put("message", tableName + "导入数据失败");
//                return jsonObject;
//            }
//        }
//        jsonObject.put("code", "success");
//        jsonObject.put("message", tableName + "导入数据成功");
//        return jsonObject;
//    }
//
//    /**
//     * @param connection
//     * @param tableName
//     * @param value
//     * @return 获取分段insert语句
//     */
//    private String getInsertSql(Connection connection, String tableName, List<List<String>> value, List<String> typeList) {
//        StringBuilder sb = new StringBuilder("INSERT INTO ");
//        sb.append(tableName);
//        try {
//            DatabaseMetaData metaData = connection.getMetaData();
//            ResultSet columns = metaData.getColumns(null, null, tableName, null);
//            sb.append("(");
//            while (columns.next()) {
//                String column_name = columns.getString("COLUMN_NAME");
//                sb.append(column_name);
//                sb.append(" ,");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return "";
//        }
//        sb = sb.deleteCharAt(sb.length() - 1);
//        sb.append(") VALUES ");
//        Iterator<List<String>> iterator = value.iterator();
//        for (List<String> row : value) {
//            sb.append("(");
//            List<String> next = iterator.next();
//            Iterator<String> iterator1 = next.iterator();
//            int i = 0;
//            while (iterator1.hasNext()) {
//                String next1 = iterator1.next();
//                sb.append("'");
//                if ("".equals(next1.trim()) &&
//                        ("int".equalsIgnoreCase(typeList.get(i)) || "float".equalsIgnoreCase(typeList.get(i)) || "double".equalsIgnoreCase(typeList.get(i)) || "decimal".equalsIgnoreCase(typeList.get(i)))) {
//                    next1 = "0";
//                    sb.append(next1);
//                } else {
//                    sb.append(next1);
//                }
//                sb.append("' ,");
//                i++;
//            }
//            sb = sb.deleteCharAt(sb.length() - 1);
//            sb.append("),");
//        }
//        sb = sb.deleteCharAt(sb.length() - 1);
//        return sb.toString();
//    }
//
//
//    /**
//     * @param connection
//     * @param sql
//     * @return 执行sql结果
//     */
//    private boolean executeSql(Connection connection, String tableName, String sql) {
//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.execute();
//            return true;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            logger.error(tableName + "插入数据失败");
//            return false;
//        }
//    }
//
//
//    /**
//     * @param connection
//     * @param tableName  首次导入excel表数据失败 删除创建的表
//     */
//    private void dropTable(Connection connection, String tableName) {
//        StringBuilder sb = new StringBuilder("drop table if exists ");
//        sb.append(tableName);
//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement(sb.toString());
//            preparedStatement.execute();
//        } catch (SQLException e) {
//            logger.error(tableName + "初次创建数据表插入数据失败 且 删除表失败");
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * DDL create table 建表mysql+字段注释
//     *
//     * @param tableName
//     * @param tableFields
//     * @return
//     */
//    private List<String> createTableSqlByMysql(Connection connection, String tableName, List<TableField> tableFields) {
//        List<String> primaryKeys = new ArrayList<>();
//        List<String> typeList = new ArrayList<>();
//        StringBuffer sb = new StringBuffer("CREATE TABLE ");
//        sb.append(tableName);
//        sb.append("(");
//        Iterator<TableField> iterator = tableFields.iterator();
//        while (iterator.hasNext()) {
//            TableField next = iterator.next();
//            String field = next.getField();
//            String type = next.getType();
//            String length = next.getLength();
//            String comment = next.getComment();
//            String pk = next.getPk();
//            sb.append("`" + field + "`  ");
//            sb.append(type);
//            int len = Integer.parseInt(length.substring(0, length.length() - 2));
//            if ("float".equalsIgnoreCase(type) || "double".equalsIgnoreCase(type) || "decimal".equalsIgnoreCase(type)) {
//                len += 6;
//                sb.append("(" + len + ",6)");
//            } else if ("time".equalsIgnoreCase(type) || "date".equalsIgnoreCase(type) || "datetime".equalsIgnoreCase(type)) {
//
//            } else {
//                sb.append("(" + len + ")");
//            }
//            sb.append(" COMMENT '" + comment + "'");
//            if ("1".equals(pk) || "1.0".equals(pk)) {
//                primaryKeys.add(field);
//            }
//            typeList.add(type);
//            sb.append(",");
//        }
////        去掉PORTALID
//        if (primaryKeys.size() != 0) {
//            sb.append(" PRIMARY KEY (");
//            for (String s : primaryKeys) {
//                sb.append("`" + s + "`,");
//            }
//            sb = sb.deleteCharAt(sb.length() - 1);
//            sb.append(")) ENGINE=INNODB DEFAULT CHARSET = UTF8");
//        } else {
//            sb = sb.deleteCharAt(sb.length() - 1);
//            sb.append(") ENGINE=INNODB DEFAULT CHARSET = UTF8");
//        }
//        try {
////        sqlserver建表不成功
//            PreparedStatement preparedStatement = connection.prepareStatement(sb.toString());
//            preparedStatement.execute();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            logger.error("创建表" + tableName + "失败");
//            try {
//                connection.close();
//            } catch (SQLException ee) {
//                ee.printStackTrace();
//            }
//            typeList.add("false");
//            return typeList;
//        }
//        typeList.add("true");
//        return typeList;
//    }
//
//    /**
//     * 当前表是否存在
//     *
//     * @param table
//     * @return
//     */
//    private boolean tableIsExist(Subject subject, String table) {
//        StringBuilder sql = new StringBuilder("");
//        sql.append("select COUNT(1) AS num from information_schema.TABLES t WHERE t.TABLE_SCHEMA = '" + subject.getDbName() + "' AND t.TABLE_NAME = '" + table + "'");
//        String num = "";
//        DataSrc dataSrc = getDataSrc(subject.getSubjectCode(), "mysql");
//        Connection connection = getConnection(dataSrc);
//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
//            ResultSet res = preparedStatement.executeQuery();
//            while (res.next()) {
//                num = res.getString("num");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println("Error: could not retrieve data for the sql '" + sql + "' from the backend.");
//        }
//        return "1".equals(num);
//    }
//
//
//    /***
//     public void getDbCSV(String subjectCode){
//     Subject subject=subjectDao.findBySubjectCode(subjectCode);
//     String dbFilePath;
//     if(subject!=null){
//     dbFilePath=subject.getDbPath();
//     List<String> files = new ArrayList<String>();
//     File file = new File(dbFilePath);
//     File[] tempList = file.listFiles();
//
//     for (int i = 0; i < tempList.length; i++) {
//     if (tempList[i].isFile()) {
//     files.add(tempList[i].toString());
//     //文件名，不包含路径
//     String fileName = tempList[i].getName();
//     //                 判断该csv文件是否已经建表
//     if(createdTablesByCSVDao.IsCreatedTable(subject.getThemeCode(),subjectCode,fileName)){
//
//     }else{ //未建表，
//     Map<String,List<List<String>>> map=analysisCSV(fileName,subject);
//     //                       建表
//     List<List<String>>  lists1=map.get("title");
//     if(lists1.size()>0){
//
//     }
//     }
//     System.out.println(fileName);
//     }
//     if (tempList[i].isDirectory()) {
//     //这里就不递归了，
//     }
//     }
//     }
//     }
//
//     //   解析CSV文件
//     public Map<String,List<List<String>>> analysisCSV(String csvName, Subject subject){
//     // 1.声明CsvReader类用于csv文件读取
//     CsvReader nCsvReader = null;
//     Map<String,List<List<String>>> map=new HashMap<>();
//     List<List<String>> lists=new ArrayList<>();
//     List<List<String>> listDate=new ArrayList<>();
//     try {
//     // 2.实例化CsvReader类用于csv文件读取
//     nCsvReader = new CsvReader(subject.getDbPath()+"\\"+csvName);
//     // 3.循环reader的结果集
//     int n=0;
//     while (nCsvReader.readRecord()) {
//     List<String> list=new ArrayList<>();
//     int count=nCsvReader.getColumnCount();
//     for (int i = 0; i < nCsvReader.getColumnCount(); i++) {
//     String aa=nCsvReader.get(i);
//     byte[] bytes=aa.getBytes("ISO8859-1");
//     String s=new String(bytes,"gbk");
//     list.add(s);
//     System.out.println(s);
//     }
//     if(n<5){
//     lists.add(list);
//     }else{
//     listDate.add(list);
//     }
//     n++;
//     }
//     } catch (FileNotFoundException e) {
//     e.printStackTrace();
//     logger.error("解析csv异常！！！"+ e);
//     } catch (IOException e) {
//     e.printStackTrace();
//     } finally {
//     // 关闭读取连接
//     nCsvReader.close();
//     }
//     map.put("title",lists);
//     map.put("tableDate",listDate);
//     return map;
//     }
//
//     */
//}
