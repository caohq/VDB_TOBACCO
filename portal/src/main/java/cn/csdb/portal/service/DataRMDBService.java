package cn.csdb.portal.service;

import cn.csdb.portal.model.DataSrc;
import cn.csdb.portal.model.Subject;
import cn.csdb.portal.model.TableInfo;
import cn.csdb.portal.repository.CheckUserDao;
import cn.csdb.portal.repository.DataSrcDao;
import cn.csdb.portal.utils.dataSrc.DataSourceFactory;
import cn.csdb.portal.utils.dataSrc.IDataSource;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2017/4/17 0017.
 */
@Service
public class DataRMDBService {

    private Logger logger = LoggerFactory.getLogger(DataRMDBService.class);

    @Resource
    private DataSrcDao dataSrcDao;

    @Resource
    private CheckUserDao checkUserDao;

    @Value("#{systemPro['tableDataFilePath']}")
    private String tableDataFilePath;

    public List<List<Object>> getDataBySql(String sql, Map<String, List<TableInfo>> tableComsMap,
                                           String curDataSubjectCode, int start, int limit) {
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
        IDataSource dataSource = DataSourceFactory.getDataSource(datasrc.getDatabaseType());
        Connection connection = dataSource.getConnection(datasrc.getHost(), datasrc.getPort(),
                datasrc.getUserName(), datasrc.getPassword(), datasrc.getDatabaseName());
        PreparedStatement paginationSql = dataSource.getPaginationSql(connection, sql, null, start, limit);
        List<List<Object>> datas = Lists.newArrayList();
        try {
            ResultSet resultSet = paginationSql.executeQuery();
            while (resultSet.next()) {
                ArrayList<Object> objects = Lists.newArrayList();
                Set<String> keySet = tableComsMap.keySet();
                for (String key : keySet) {
                    List<TableInfo> tableInfos = tableComsMap.get(key);
                    for (TableInfo tableInfo : tableInfos) {
                        if (StringUtils.isNoneBlank(tableInfo.getColumnNameLabel())) {
                            Object object1 = resultSet.getObject(tableInfo.getColumnNameLabel());
                            objects.add(object1);
                        } else {
                            Object object1 = resultSet.getObject(tableInfo.getColumnName());
                            objects.add(object1);
                        }
                    }
                }
                datas.add(objects);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
            }
        }
        return datas;
    }

    public List<List<Object>> getDataByTable(String tableName, HashMap<String, List<TableInfo>> tableComsMap,
                                             String curDataSubjectCode, int start, int limit) {
        return getDataBySql("select * from " + tableName, tableComsMap, curDataSubjectCode, start, limit);
    }


    private static final ExecutorService executor = Executors.newFixedThreadPool(4);


}
