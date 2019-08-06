package cn.csdb.portal.repository;

import cn.csdb.portal.model.DataTask;
import cn.csdb.portal.repository.mapper.DataTaskMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.*;
import java.util.Date;
import java.util.List;

/**
 * @program: DataSync
 * @author: huangwei
 * @create: 2018-10-12 16:18
 **/

@Repository
public class DataTaskDao {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private MongoTemplate mongoTemplate;

    public DataTask get(int id) {
        return  mongoTemplate.findById(id,DataTask.class);
    }

    public void insertDataTask(final DataTask dataTask) {
        /*String sql = "insert into t_datatask(" +
                "dataSourceId,dataTaskType,tableName," +
                "SqlString,SqlTableNameEn,SqlFilePath," +
                "FilePath,creator,status,SubjectCode,CreateTime) values " +
                "(?,?,?,?,?,?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int i = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"dataTaskId"});
                ps.setInt(1, dataTask.getDataSourceId());
                ps.setString(2, dataTask.getDataTaskType());
                ps.setString(3, dataTask.getTableName());
                ps.setString(4, dataTask.getSqlString());
                ps.setString(5, dataTask.getSqlTableNameEn());
                ps.setString(6, dataTask.getSqlFilePath());
                ps.setString(7, dataTask.getFilePath());
                ps.setString(8, dataTask.getCreator());
                ps.setString(9, dataTask.getStatus());
                ps.setString(10, dataTask.getSubjectCode());
                ps.setTimestamp(11, new Timestamp(new Date().getTime()));
                return ps;
            }
        }, keyHolder);
        if (i > 0) {
            return keyHolder.getKey().intValue();
        }
        return -1;*/

         mongoTemplate.save(dataTask);
    }


}
