package cn.csdb.portal.utils.dataSrc;

/**
 * @program: DataSync
 * @description:
 * @author: hw
 * @create: 2018-10-22 16:42
 **/
public class DataSourceFactory {
    public static IDataSource getDataSource(String databaseType) {
        if (databaseType.equalsIgnoreCase("mysql")) {
            return new MySqlDataSource();
        } else if (databaseType.equals("oracle")) {
            return new OracleDataSource();
        }
        return null;
    }
}
