package cn.csdb.portal.utils.dataSrc;

import cn.csdb.portal.model.TableInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * @program: DataSync
 * @description:
 * @author: hw
 * @create: 2018-10-22 16:42
 **/
public abstract class IDataSource {

    private Logger logger = LoggerFactory.getLogger(IDataSource.class);

    /**
     * 获取数据库连接
     *
     * @param host         数据库地址
     * @param port         开放端口
     * @param userName     用户名
     * @param password     密码
     * @param databaseName 数据库名称
     * @return 数据库连接
     */
    public abstract Connection getConnection(String host, String port, String userName, String password, String databaseName);

    /**
     * 获取数据库内所有表名称
     *
     * @param connection 数据库连接
     * @return 所有表名称
     */
    public abstract List<String> getTableList(Connection connection);

    /**
     * 获取表字段
     *
     * @param connection 数据库连接
     * @param tableName  表名称
     * @return 字段信息
     */
    public abstract Map<String, Map<String, String>> getTableColumns(Connection connection, String databaseName, String tableName);

    /**
     * 验证sql语句是否正确
     *
     * @param connection 数据库连接
     * @param sql        sql语句
     * @return 正确返回true
     */
    public boolean validateSql(Connection connection, String sql) {
        try (Statement statement = connection.createStatement()) {
            return statement.execute(sql);
        } catch (SQLException e) {
            logger.error("执行sql语句错误", e);
            return false;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("数据连接关闭错误", e);
            }
        }
    }

    /**
     * 获取分页查询SQL
     *
     * @param connection 数据库连接
     * @param sql        sql语句
     * @param params     参数值
     * @param start      分页起始位置
     * @param pageSize   每页记录数
     * @return 分页查询SQL
     */
    public abstract PreparedStatement getPaginationSql(Connection connection, String sql, List<Object> params, int start, int pageSize);

    /**
     * 获取查询SQL
     *
     * @param connection 数据库连接
     * @param sql        sql语句
     * @param params     参数值
     * @return 查询SQL
     */
    public PreparedStatement getBySql(Connection connection, String sql, List<Object> params) {
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            if (params == null || params.size() == 0) {
                return ps;
            }
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            return ps;
        } catch (SQLException e) {
            logger.error("无法获取分页sql", e);
            return null;
        }
    }

    /**
     * 验证数据连接信息是否正确
     *
     * @param host     数据库地址
     * @param port     端口
     * @param userName 用户名
     * @param password 密码
     * @param database 数据库名称
     * @return
     */
    public boolean validateConnection(String host, String port, String userName, String password, String database) {
        Connection connection = getConnection(host, port, userName, password, database);
        try {
            if (connection == null) {
                return false;
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("数据连接关闭错误", e);
            }
        }
        return true;
    }

    /**
     * 获取表的字段名称、字段注释、数据类型
     *
     * @param connection
     * @param tableName
     * @return
     * @throws SQLException
     */
    public List<TableInfo> getTableFieldComs(Connection connection, String databaseName, String tableName) {
        List<TableInfo> list = Lists.newArrayList();
        Map<String, Map<String, String>> tableColumns = getTableColumns(connection, databaseName, tableName);
        for (String key : tableColumns.keySet()) {
            Map<String, String> value = tableColumns.get(key);
            String comment = value.get("comment");
            TableInfo tableInfo = new TableInfo();
            tableInfo.setColumnComment(StringUtils.isBlank(comment) ? "" : comment);
            tableInfo.setColumnName(key);
            tableInfo.setColumnNameLabel("");
            tableInfo.setDataType(value.get("type"));
            list.add(tableInfo);
        }
        return list;
    }
}
