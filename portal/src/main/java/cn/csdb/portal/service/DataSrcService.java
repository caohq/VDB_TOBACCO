package cn.csdb.portal.service;

import cn.csdb.portal.model.DataSrc;
import cn.csdb.portal.repository.DataSrcDao;
import cn.csdb.portal.utils.SqlUtil;
import cn.csdb.portal.utils.dataSrc.DataSourceFactory;
import cn.csdb.portal.utils.dataSrc.IDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by huangwei on 2016/4/6.
 */
@Service
@Transactional
public class DataSrcService {

    @Autowired
    private DataSrcDao dataSrcDao;

    private Logger logger = LoggerFactory.getLogger(DataSrcService.class);


    public List<String> relationalDatabaseTableList(DataSrc dataSrc) {
        IDataSource dataSource = DataSourceFactory.getDataSource(dataSrc.getDatabaseType());
        Connection connection = dataSource.getConnection(dataSrc.getHost(), dataSrc.getPort(), dataSrc.getUserName(), dataSrc.getPassword(), dataSrc.getDatabaseName());
        if (connection == null) {
            return null;
        }
        List<String> tableList = dataSource.getTableList(connection);
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
        return tableList;
    }


    public boolean validateSql(String sql, int dataSourceId) {
        if (!SqlUtil.validateSelectSql(sql)) {
            return false;
        }
        DataSrc dataSrc = dataSrcDao.findById(dataSourceId);
        IDataSource dataSource = DataSourceFactory.getDataSource(dataSrc.getDatabaseType());
        Connection connection = dataSource.getConnection(dataSrc.getHost(), dataSrc.getPort(), dataSrc.getUserName(), dataSrc.getPassword(), dataSrc.getDatabaseName());
        if (connection == null) {
            return false;
        }
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
        return dataSource.validateSql(connection, sql);
    }





}
