package cn.csdb.portal.utils.dataSrc;

import com.google.common.collect.Maps;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * @program: DataSync
 * @description:
 * @author: hw
 * @create: 2018-09-27 14:50
 **/
public class DataBaseSource {
    private static Logger logger = LoggerFactory.getLogger(DataBaseSource.class);

    private static Map<String, ComboPooledDataSource> DBSources = Maps.newConcurrentMap();
    private final static int MINPOOLSIZE = 1;
    private final static int ACQUIREINCREMENT = 2;
    private final static int MAXPOOLSIZE = 5;

    public static synchronized void createDataBaseSource(String driverClass, String host, String port, String user, String passwd, String db) {
        ComboPooledDataSource comboPooledDataSource = DBSources.get(getJdbcUrl(driverClass.trim(), host, port, db));
        if (comboPooledDataSource != null) {
            return;
        }
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass(driverClass); //loads the jdbc driver
        } catch (PropertyVetoException e) {
            logger.error("缺少驱动包", e);
        }
        String jdbcUrl = getJdbcUrl(driverClass.trim(), host, port, db);
        cpds.setJdbcUrl(jdbcUrl);
        cpds.setUser(user);
        cpds.setPassword(passwd);
        cpds.setMinPoolSize(MINPOOLSIZE);
        cpds.setAcquireIncrement(ACQUIREINCREMENT);
        cpds.setMaxPoolSize(MAXPOOLSIZE);
        DBSources.put(jdbcUrl, cpds);
    }


    public static Connection getConnection(String driverClass, String host, String port, String user, String passwd, String db) {
        ComboPooledDataSource comboPooledDataSource = DBSources.get(getJdbcUrl(driverClass.trim(), host, port, db));
        if (comboPooledDataSource == null) {
            createDataBaseSource(driverClass, host, port, user, passwd, db);
        }
        comboPooledDataSource = DBSources.get(getJdbcUrl(driverClass.trim(), host, port, db));
        try {
            return comboPooledDataSource.getConnection();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            logger.error("无法获取连接", e);
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return null;
    }

    private static String getJdbcUrl(String driverClass, String host, String port, String db) {

        if (StringUtils.startsWith(driverClass,"oracle.jdbc.")) {
            return "jdbc:oracle:thin:@" + host + ":" + port + ":" + db;
        }
        if (StringUtils.startsWith(driverClass,"com.mysql.jdbc.")) {
            return "jdbc:mysql://" + host + ":" + port + "/" +db+"?zeroDateTimeBehavior=convertToNull&Unicode=true&characterEncoding=UTF-8&useSSL=false";
        }
        return null;
    }
}
