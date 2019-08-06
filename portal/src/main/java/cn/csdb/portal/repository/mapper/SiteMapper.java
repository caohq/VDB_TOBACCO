package cn.csdb.portal.repository.mapper;


import cn.csdb.portal.model.Site;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @program: DataSync
 * @description:
 * @author: huangwei
 * @create: 2018-10-15 14:38
 **/
public class SiteMapper implements RowMapper<Site> {

    @Override
    public Site mapRow(ResultSet resultSet, int i) throws SQLException {
        Site site = new Site();
        site.setId(resultSet.getInt("Id"));
        site.setSiteMarker(resultSet.getString("SiteMarker"));
        site.setFilePath(resultSet.getString("FilePath"));
        site.setDbName(resultSet.getString("DbName"));
        return site;
    }


}
