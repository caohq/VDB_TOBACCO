package cn.csdb.portal.repository.mapper;

import cn.csdb.portal.model.ResCatalog;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by pirate on 2018/10/22.
 */
public class ResCatalogMapper implements RowMapper<ResCatalog> {
    public ResCatalog mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResCatalog resCatalog = new ResCatalog();
        resCatalog.setId(rs.getInt("id"));
        resCatalog.setParentid(rs.getInt("parentid"));
        resCatalog.setName(rs.getString("name"));
        resCatalog.setLevel(rs.getInt("level"));
        resCatalog.setNodeorder(rs.getInt("nodeorder"));
        resCatalog.setUpdatetime(rs.getTimestamp("updatetime"));
        resCatalog.setCreatetime(rs.getTimestamp("createtime"));
        return resCatalog;
    }

}
