package cn.csdb.portal.repository.mapper;

import cn.csdb.portal.model.DataTask;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @program: DataSync
 * @author: huangwei
 * @create: 2018-10-12 16:18
 **/
public class DataTaskMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        DataTask dataTask = new DataTask();
        dataTask.setDataTaskId(resultSet.getString("DataTaskId"));
        dataTask.setDataSourceId(resultSet.getInt("DataSourceId"));
        dataTask.setSubjectCode(resultSet.getString("SubjectCode"));
        dataTask.setDataTaskType(resultSet.getString("DataTaskType"));
        dataTask.setTableName(resultSet.getString("TableName"));
        dataTask.setSqlString(resultSet.getString("SqlString"));
        dataTask.setSqlTableNameEn(resultSet.getString("SqlTableNameEn"));
        dataTask.setSqlFilePath(resultSet.getString("SqlFilePath"));
        dataTask.setFilePath(resultSet.getString("FilePath"));
        dataTask.setCreator(resultSet.getString("Creator"));
        dataTask.setCreateTime(resultSet.getTimestamp("CreateTime"));
        dataTask.setStatus(resultSet.getString("Status"));
        return dataTask;
    }
}
