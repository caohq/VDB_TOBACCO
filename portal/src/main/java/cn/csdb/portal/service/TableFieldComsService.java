package cn.csdb.portal.service;


import cn.csdb.portal.model.DataSrc;
import cn.csdb.portal.model.Subject;
import cn.csdb.portal.model.TableFieldComs;
import cn.csdb.portal.model.TableInfo;
import cn.csdb.portal.repository.CheckUserDao;
import cn.csdb.portal.repository.TableFieldComsDao;
import cn.csdb.portal.utils.dataSrc.DataSourceFactory;
import cn.csdb.portal.utils.dataSrc.IDataSource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.google.common.hash.Hashing;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


/**
 * Created by Administrator on 2017/4/17 0017.
 */
@Service
public class TableFieldComsService {
    private Logger logger = LoggerFactory.getLogger(TableFieldComsService.class);

    /*@Resource
    private DataSrcDao dataSrcDao;*/

    @Resource
    private TableFieldComsDao tableFieldComsDao;

    @Resource
    private CheckUserDao checkUserDao;

    private final static String URISPLIT = "#";

    @Transactional(readOnly = true)
    public Map<String, List<TableInfo>> getDefaultFieldComsByTableName(String subjectCode, String tableName) {


        if (StringUtils.isBlank(tableName)) {
            return null;
        }
        tableName = tableName.trim();

/*
        DataSrc dataSrc = dataSrcDao.findById(dataSourceId);
*/
        Subject subject = checkUserDao.getSubjectByCode(subjectCode);
        DataSrc datasrc = new DataSrc();
        datasrc.setDatabaseName(subject.getDbName());
        datasrc.setDatabaseType("mysql");
        datasrc.setHost(subject.getDbHost());
        datasrc.setPort(subject.getDbPort());
        datasrc.setUserName(subject.getDbUserName());
        datasrc.setPassword(subject.getDbPassword());
        if (datasrc == null) {
            return null;
        }
        Map<String, List<TableInfo>> maps = Maps.newHashMap();
        TableFieldComs tableFieldComs = getTableFieldComsByUriEx(datasrc, tableName);
        List<TableInfo> tableInfosCur = null;
        if (tableFieldComs != null) {
            String fieldComs = tableFieldComs.getFieldComs();
            tableInfosCur = JSON.parseArray(fieldComs, TableInfo.class);
        }

        IDataSource dataSource = DataSourceFactory.getDataSource(datasrc.getDatabaseType());
        Connection connection = dataSource.getConnection(datasrc.getHost(), datasrc.getPort(), datasrc.getUserName(), datasrc.getPassword(), datasrc.getDatabaseName());
        List<TableInfo> tableInfos = null;
        try {
            tableInfos = dataSource.getTableFieldComs(connection, datasrc.getDatabaseName(), tableName);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {

            }
        }
        if (tableInfos != null && tableInfosCur != null) {
            List intersectionList = ListUtils.intersection(tableInfos, tableInfosCur);//1 获取交集，取tableInfosCur中元素
            tableInfos.removeAll(tableInfosCur);//2 去除重复元素
            tableInfos.addAll(intersectionList);//3 然后取并集
            maps.put(tableName, tableInfos);
        } else if (tableInfos != null) {
            maps.put(tableName, tableInfos);
        }
        return maps;

    }

    @Transactional
    public boolean insertTableFieldComs(String curDataSubjectCode, String tableName, List<TableInfo> tableInfos, String state) {
/*
        DataSrc dataSrc = dataSrcDao.findById(dataSourceId);
*/
        /*DataSrc datasrc = new DataSrc();
        datasrc.setDataSourceName("10.0.86.78_usdr");
        datasrc.setDataSourceType("db");
        datasrc.setDatabaseName("drsrnew");
        datasrc.setDatabaseType("mysql");
        datasrc.setHost("10.0.86.78");
        datasrc.setPort("3306");
        datasrc.setUserName("root");
        datasrc.setPassword("");*/
        Subject subject = checkUserDao.getSubjectByCode(curDataSubjectCode);
        DataSrc datasrc = new DataSrc();
        datasrc.setDatabaseName(subject.getDbName());
        datasrc.setDatabaseType("mysql");
        datasrc.setHost(subject.getDbHost());
        datasrc.setPort(subject.getDbPort());
        datasrc.setUserName(subject.getDbUserName());
        datasrc.setPassword(subject.getDbPassword());
        if (datasrc == null) {
            return false;
        }
        TableFieldComs tableFieldComs = getTableFieldComsByUriEx(datasrc, tableName);
        if (tableFieldComs == null) {
            String fieldComs = JSON.toJSONString(tableInfos, new PropertyFilter() {
                @Override
                public boolean apply(Object object, String name, Object value) {
                    if (name.equalsIgnoreCase("columnNameLabel")) {
                        return false;
                    }
                    return true;
                }
            });
            tableFieldComs = new TableFieldComs();
            String uriEx = getUriEx(datasrc, tableName);
            int uriHash = getUriHash(datasrc, tableName);
            tableFieldComs.setFieldComs(fieldComs);
            tableFieldComs.setStatus(0);
            tableFieldComs.setCreateTime(new Date());
            tableFieldComs.setUriEx(uriEx);
            tableFieldComs.setUriHash(uriHash);
            tableFieldComsDao.saveTableFieldComs(tableFieldComs, subject.getSubjectCode(), subject.getDbName(), tableName, state);
            return true;
        }
        tableFieldComs.setUpdateTime(new Date());
        String fieldComs = tableFieldComs.getFieldComs();
        if (StringUtils.isBlank(fieldComs)) {
            tableFieldComs.setFieldComs(fieldComs);
            tableFieldComsDao.updateFieldComs(tableFieldComs, subject.getSubjectCode(), subject.getDbName(), tableName, state);
            return true;
        }
        List<TableInfo> tableInfosPre = JSON.parseArray(fieldComs, TableInfo.class);
        tableInfosPre.removeAll(tableInfos);
        tableInfosPre.addAll(tableInfos);
        tableFieldComs.setFieldComs(JSON.toJSONString(tableInfosPre, new PropertyFilter() {
            @Override
            public boolean apply(Object object, String name, Object value) {
                if (name.equalsIgnoreCase("columnNameLabel")) {
                    return false;
                }
                return true;
            }
        }));
        tableFieldComsDao.updateFieldComs(tableFieldComs, subject.getSubjectCode(), subject.getDbName(), tableName, state);
        return true;
    }

    private TableFieldComs getTableFieldComsByUriEx(DataSrc dataSrc, String tableName) {
        TableFieldComs tableFieldComs = null;
        int uriHash = getUriHash(dataSrc, tableName);
        List<TableFieldComs> tableFieldComsList = tableFieldComsDao.getTableFieldComsByUriEx(uriHash);
        if (tableFieldComsList.size() == 1) {
            tableFieldComs = tableFieldComsList.get(0);
        }
        if (tableFieldComsList.size() > 1) {
            for (TableFieldComs tableFieldComsCur : tableFieldComsList) {
                if (StringUtils.equals(tableFieldComsCur.getUriEx(), getUriEx(dataSrc, tableName))) {
                    tableFieldComs = tableFieldComsCur;
                    break;
                }
            }
        }
        return tableFieldComs;
    }

    private int getUriHash(DataSrc dataSrc, String tableName) {
        String uriEx = getUriEx(dataSrc, tableName);
        return Hashing.murmur3_32().hashBytes(uriEx.getBytes()).asInt();
    }

    private String getUriEx(DataSrc dataSrc, String tableName) {
        String host = dataSrc.getHost();
        String port = dataSrc.getPort();
        String databaseName = dataSrc.getDatabaseName();
        return Joiner.on(URISPLIT).skipNulls().join(host, port, databaseName, tableName);
    }

    /**
     * 同步注释到MySQL
     *
     * @param curDataSubjectCode
     * @param tableName
     * @param tableInfosList
     * @param realPath
     * @return
     */
    public JSONObject syncMySQLComment(String curDataSubjectCode, String tableName, List<TableInfo> tableInfosList, String realPath) {
        JSONObject jsonObject = new JSONObject();
        Subject subject = checkUserDao.getSubjectByCode(curDataSubjectCode);
        DataSrc datasrc = new DataSrc();
        datasrc.setDatabaseName(subject.getDbName());
        datasrc.setDatabaseType("mysql");
        datasrc.setHost(subject.getDbHost());
        datasrc.setPort(subject.getDbPort());
        datasrc.setUserName(subject.getDbUserName());
        datasrc.setPassword(subject.getDbPassword());
        Connection connection = null;

        try {
            IDataSource dataSource = DataSourceFactory.getDataSource(datasrc.getDatabaseType());
            connection = dataSource.getConnection(datasrc.getHost(), datasrc.getPort(), datasrc.getUserName(), datasrc.getPassword(), datasrc.getDatabaseName());
            String scriptName = getSqlScript(curDataSubjectCode, tableName, connection, realPath, tableInfosList);
            executeScript(connection, scriptName, realPath);
        } catch (FileNotFoundException f) {
            f.printStackTrace();
            logger.error("MySQL注释同步失败：执行脚本失败");
        } catch (IOException i) {
            i.printStackTrace();
            logger.error("MySQL注释同步失败：修改注释的脚本文件生成失败");
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("MySQL注释同步失败：获取数据库连接失败");
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                logger.warn("MySQL注释同步成功：但关闭数据库连接失败");
            }
        }
        return jsonObject;
    }

    /**
     * 生成 修改注释的语句脚本
     *
     * @param curDataSubjectCode 当前库
     * @param tableName          当前表
     * @param connection         连接
     * @throws SQLException
     */
    private String getSqlScript(String curDataSubjectCode, String tableName, Connection connection, String realPath, List<TableInfo> tableInfosList) throws SQLException, IOException {
        StringBuffer sb = new StringBuffer();
        String getTableMate = "SELECT COLUMN_NAME `name`, COLUMN_TYPE `type`, COLUMN_COMMENT `comment`  FROM information_schema.`COLUMNS` WHERE TABLE_SCHEMA=? AND TABLE_NAME=?";
        PreparedStatement preparedStatement = connection.prepareStatement(getTableMate);
        preparedStatement.setString(1, curDataSubjectCode);
        preparedStatement.setString(2, tableName);
        ResultSet resultSet = preparedStatement.executeQuery();
        // 生成alert 语句
        Map<String, String> map = new HashMap<>(16);
        for (TableInfo info : tableInfosList) {
            map.put(info.getColumnName(), info.getColumnComment());
        }
        while (resultSet.next()) {
            sb.append("alter table " + tableName + " modify ");
            String name = resultSet.getString("name");
            sb.append(" " + name + " ");
            String type = resultSet.getString("type");
            sb.append(" " + type + " ");
            String comment = map.get(name);
            sb.append(" comment '" + comment + "'; \n ");
        }

        // 写入临时文件
        File f = new File(realPath);
        if (!f.exists()) {
            f.mkdir();
        }
        String scriptName = UUID.randomUUID().toString() + ".sql";
        File tempFile = new File(realPath + scriptName);
        if (!tempFile.exists()) {
            tempFile.createNewFile();
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(sb.toString().getBytes("UTF-8"));
        FileOutputStream fos = new FileOutputStream(tempFile);
        byte[] b = new byte[1];
        int i = 0;
        while ((i = bis.read(b)) != -1) {
            fos.write(b);
        }
        fos.flush();
        fos.close();
        return scriptName;
    }

    /**
     * 执行注释修改脚本，完成后删除脚本
     *
     * @param connection
     * @param scriptName
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    private void executeScript(Connection connection, String scriptName, String realPath) throws IOException {
        ScriptRunner runner = new ScriptRunner(connection);
        // 下面配置不要随意更改，否则会出现各种问题
        // 自动提交
        runner.setAutoCommit(true);
        runner.setFullLineDelimiter(false);
        // 每条命令间的分隔符
        runner.setDelimiter(";");
        runner.setSendFullScript(false);
        runner.setStopOnError(false);
        FileInputStream fileInputStream = new FileInputStream(realPath + scriptName);
        runner.runScript(new InputStreamReader(fileInputStream, "utf-8"));
        fileInputStream.close();
        File f = new File(realPath + scriptName);
        boolean delete = f.delete();
        System.out.println(delete);
    }
}
