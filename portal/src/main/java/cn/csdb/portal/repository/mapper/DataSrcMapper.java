package cn.csdb.portal.repository.mapper;

import cn.csdb.portal.model.DataSrc;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * @program: DataSync
 * @description:
 * @author: hw
 * @create: 2018-10-22 16:42
 **/
public class DataSrcMapper implements RowMapper<DataSrc> {
    public DataSrc mapRow(ResultSet rs, int rowNum) throws SQLException {
        DataSrc dataSrc = new DataSrc();
        dataSrc.setDataSourceId(rs.getInt("DataSourceId"));
        dataSrc.setDataSourceName(rs.getString("DataSourceName"));
        dataSrc.setDataSourceType(rs.getString("DataSourceType"));
        dataSrc.setDatabaseName(rs.getString("DatabaseName"));
        dataSrc.setDatabaseType(rs.getString("DatabaseType"));
        dataSrc.setFilePath(rs.getString("FilePath"));
        dataSrc.setFileType(rs.getString("FileType"));
        dataSrc.setHost(rs.getString("Host"));
        dataSrc.setPort(rs.getString("Port"));
        dataSrc.setIsValid(rs.getString("IsValid"));
        dataSrc.setUserName(rs.getString("UserName"));
        dataSrc.setPassword(rs.getString("Password"));
        dataSrc.setCreateTime(rs.getTime("CreateTime"));
        dataSrc.setStat(rs.getInt("Stat"));
        return dataSrc;
    }
}
