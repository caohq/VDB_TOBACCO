package cn.csdb.portal.utils.dataSrc;

import cn.csdb.portal.model.Subject;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.sql.Connection;

/**
 * Created by shibaoping on 2019/6/13.
 * Druid连接池工具类
 */
public class DruidUtil {
    private static DruidUtil single = null;

    public static Map<String, DruidDataSource> map = new HashMap<>();

    public DruidUtil() {
    }

    /**
     * 获取实例 重启tomcat之后清空Druid连接池
     *
     * @return
     */
    @Bean(initMethod = "init", destroyMethod = "destroy")
    public static DruidUtil getInstance() {
        if (single == null) {
            synchronized (DruidUtil.class) {
                if (single == null) {
                    single = new DruidUtil();
                }
            }
        }
        return single;
    }

    //获取连接
    public Connection getConnection(String id) throws SQLException {
        DruidDataSource source = map.get(id);
        return source.getConnection();
    }

    //新增连接池功能
    public void addDruidDataSource(String host, String port, String username, String password, String databaseName) {
        Properties prop = new Properties();
        ClassPathResource classPathResource = new ClassPathResource("/jdbc.properties");
        Properties druidProperties = null;
        try {
            druidProperties = PropertiesLoaderUtils.loadProperties(classPathResource);
            prop.setProperty("driverClassName", druidProperties.getProperty("db.driver"));
            prop.setProperty("validationQuery", druidProperties.getProperty("druid.validationQuery"));
            prop.setProperty("url", "jdbc:mysql://" + host + ":" + port + "/" + databaseName);
            prop.setProperty("connectionProperties", druidProperties.getProperty("druid.connectionProperties"));
            prop.setProperty("username", username);
            prop.setProperty("password", password);
            prop.setProperty("initialSize", druidProperties.getProperty("druid.initialSize"));
            prop.setProperty("maxActive", druidProperties.getProperty("druid.maxActive"));
            prop.setProperty("minIdle", druidProperties.getProperty("druid.minIdle"));
            prop.setProperty("maxWait", druidProperties.getProperty("druid.maxWait"));
            prop.setProperty("filters", druidProperties.getProperty("druid.filters"));
            prop.setProperty("timeBetweenEvictionRunsMillis", druidProperties.getProperty("druid.timeBetweenEvictionRunsMillis"));
            prop.setProperty("minEvictableIdleTimeMillis", druidProperties.getProperty("druid.minEvictableIdleTimeMillis"));
            prop.setProperty("testWhileIdle", druidProperties.getProperty("druid.testWhileIdle"));
            prop.setProperty("testOnBorrow", druidProperties.getProperty("druid.testOnBorrow"));
            prop.setProperty("testOnReturn", druidProperties.getProperty("druid.testOnReturn"));
            prop.setProperty("poolPreparedStatements", druidProperties.getProperty("druid.poolPreparedStatements"));
            prop.setProperty("maxPoolPreparedStatementPerConnectionSize", druidProperties.getProperty("druid.maxPoolPreparedStatementPerConnectionSize"));
            prop.setProperty("removeAbandoned", druidProperties.getProperty("druid.removeAbandoned"));
            prop.setProperty("removeAbandonedTimeout", druidProperties.getProperty("druid.removeAbandonedTimeout"));
            prop.setProperty("logAbandoned", druidProperties.getProperty("druid.logAbandoned"));
            DruidDataSource druidDataSource = (DruidDataSource) DruidDataSourceFactory
                    .createDataSource(prop);
            map.put(host + port + databaseName, druidDataSource);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("新增数据库创建连接池失败！");
        }
    }

    //删除连接池
    public void removeDruidDataSource(Subject subject) {
        DruidDataSource source = map.get(subject.getDbHost() + subject.getDbPort() + subject.getDbName());
        source.close();
        map.remove(subject.getDbHost() + subject.getDbPort() + subject.getDbName());
    }

    //查询连接池是否存在
    public boolean containsId(String id) {
        return map.containsKey(id);
    }
}
