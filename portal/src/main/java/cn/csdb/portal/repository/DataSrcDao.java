package cn.csdb.portal.repository;

import cn.csdb.portal.model.DataSrc;
import cn.csdb.portal.repository.mapper.DataSrcMapper;
import cn.csdb.portal.utils.dataSrc.DataSourceFactory;
import cn.csdb.portal.utils.dataSrc.IDataSource;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.File;
import java.sql.*;
import java.util.Date;
import java.util.*;

/**
 * Created by huangwei on 2016/4/7.
 */
@Repository
public class DataSrcDao {

    private Logger logger = LoggerFactory.getLogger(DataSrcDao.class);
    private int numPerPage;
    private int totalRows;
    private int totalPages;
    private int currentPage;
    private int startIndex;
    private int lastIndex;
    @Resource
    private JdbcTemplate jdbcTemplate;

    public JSONObject pagination(int currentPage, int numPerPage, String dataSourceType, String... databaseTypes) {
        StringBuilder filter = new StringBuilder();
        List<Object> params = new ArrayList<Object>();
        List<Object> params1 = new ArrayList<Object>();
        JSONObject jsonObject = new JSONObject();
        List<DataSrc> dataSrcs = new ArrayList<DataSrc>();
        String sql = "select * from t_datasource where DataSourceType=?";
        params.add(dataSourceType);
        String sql1 = "select count(*) from t_datasource where DataSourceType=?";
        params1.add(dataSourceType);
        if (databaseTypes != null && databaseTypes.length > 0) {
            filter.append("(");
            for (String databaseType : databaseTypes) {
                filter.append("?,");
                params.add(databaseType);
                params1.add(databaseType);
            }
            filter.deleteCharAt(filter.lastIndexOf(","));
            filter.append(")");
        }
        if (!Strings.isNullOrEmpty(filter.toString())) {
            sql += " and databasetype in " + filter.toString();
            sql1 += " and databasetype in " + filter.toString();
        }
        sql += " order by DataSourceId DESC";
        totalRows = jdbcTemplate.queryForObject(sql1, Integer.class, params1.toArray());
        startIndex = (currentPage - 1) * numPerPage;
        if (totalRows % numPerPage == 0) {
            this.totalPages = totalRows / numPerPage;
        } else {
            this.totalPages = (totalRows / numPerPage) + 1;
        }
        if (totalRows < numPerPage) {
            this.lastIndex = totalRows;
        } else if ((totalRows % numPerPage == 0) || (totalRows % numPerPage != 0 && currentPage < totalPages)) {
            this.lastIndex = currentPage * numPerPage;
        } else if (totalRows % numPerPage != 0 && currentPage == totalPages) {//最后一页
            this.lastIndex = totalRows;
        }
        sql += " limit ?,?";
        params.add(startIndex);
        params.add(numPerPage);
        List list = jdbcTemplate.queryForList(sql, params.toArray());
        Iterator it = list.iterator();
        DataSrc dataSrc = null;
        while (it.hasNext()) {
            Map map4DataSrc = (Map) it.next();
            dataSrc = new DataSrc();
            dataSrc.setDataSourceId(((Integer) map4DataSrc.get("DataSourceId")).intValue());
            dataSrc.setDataSourceName((String) map4DataSrc.get("DataSourceName"));
            dataSrc.setDataSourceType((String) map4DataSrc.get("DataSourceType"));
            dataSrc.setDatabaseName((String) map4DataSrc.get("DatabaseName"));
            dataSrc.setDatabaseType((String) map4DataSrc.get("DatabaseType"));
            dataSrc.setHost((String) map4DataSrc.get("Host"));
            dataSrc.setPort((String) map4DataSrc.get("Port"));
            dataSrc.setUserName((String) map4DataSrc.get("UserName"));
            dataSrc.setPassword((String) map4DataSrc.get("Password"));
            dataSrc.setIsValid((String) map4DataSrc.get("IsValid"));
            dataSrc.setFileType((String) map4DataSrc.get("FileType"));
            dataSrc.setFilePath((String) map4DataSrc.get("FilePath"));
            dataSrc.setCreateTime((Date)map4DataSrc.get("CreateTime"));
            dataSrc.setStat((Integer)map4DataSrc.get("Stat"));
            dataSrcs.add(dataSrc);
        }
        jsonObject.put("dataSrcs", dataSrcs);
        jsonObject.put("totalPages", this.totalPages);
        return jsonObject;
    }
    public JSONObject pagination(int currentPage, int numPerPage, String dataSourceType) {
        JSONObject jsonObject = new JSONObject();
        List<Object> params = new ArrayList<Object>();
        List<Object> params1 = new ArrayList<Object>();
        List<DataSrc> dataSrcs = new ArrayList<DataSrc>();
        StringBuffer sql = new StringBuffer("select * from t_datasource where DataSourceType=? order by DataSourceId DESC");
        StringBuffer sql1 = new StringBuffer("select count(*) from t_datasource where DataSourceType=?");
        totalRows = jdbcTemplate.queryForObject(sql1.toString(), Integer.class, dataSourceType);
        startIndex = (currentPage - 1) * numPerPage;
        if (totalRows % numPerPage == 0) {
            this.totalPages = totalRows / numPerPage;
        } else {
            this.totalPages = (totalRows / numPerPage) + 1;
        }
        if (totalRows < numPerPage) {
            this.lastIndex = totalRows;
        } else if ((totalRows % numPerPage == 0) || (totalRows % numPerPage != 0 && currentPage < totalPages)) {
            this.lastIndex = currentPage * numPerPage;
        } else if (totalRows % numPerPage != 0 && currentPage == totalPages) {//最后一页
            this.lastIndex = totalRows;
        }
        sql.append(" limit ?,?");
        List list = jdbcTemplate.queryForList(sql.toString(), dataSourceType, startIndex, numPerPage);
        Iterator it = list.iterator();
        DataSrc dataSrc = null;
        while (it.hasNext()) {
            Map map4DataSrc = (Map) it.next();
            dataSrc = new DataSrc();
            dataSrc.setDataSourceId(((Integer) map4DataSrc.get("DataSourceId")).intValue());
            dataSrc.setDataSourceName((String) map4DataSrc.get("DataSourceName"));
            dataSrc.setDataSourceType((String) map4DataSrc.get("DataSourceType"));
            dataSrc.setDatabaseName((String) map4DataSrc.get("DatabaseName"));
            dataSrc.setDatabaseType((String) map4DataSrc.get("DatabaseType"));
            dataSrc.setHost((String) map4DataSrc.get("Host"));
            dataSrc.setPort((String) map4DataSrc.get("Port"));
            dataSrc.setUserName((String) map4DataSrc.get("UserName"));
            dataSrc.setPassword((String) map4DataSrc.get("Password"));
            dataSrc.setIsValid((String) map4DataSrc.get("IsValid"));
            dataSrc.setFileType((String) map4DataSrc.get("FileType"));
            dataSrc.setFilePath((String) map4DataSrc.get("FilePath"));
            dataSrc.setCreateTime((Date)map4DataSrc.get("CreateTime"));
            dataSrc.setStat((Integer)map4DataSrc.get("Stat"));
            dataSrcs.add(dataSrc);
        }
        jsonObject.put("dataSrcs", dataSrcs);
        jsonObject.put("totalPages", this.totalPages);
        return jsonObject;
    }

    public void setLastIndex() {
        if (totalRows < numPerPage) {
            this.lastIndex = totalRows;
        } else if ((totalRows % numPerPage == 0) || (totalRows % numPerPage != 0 && currentPage < totalPages)) {
            this.lastIndex = currentPage * numPerPage;
        } else if (totalRows % numPerPage != 0 && currentPage == totalPages) {//最后一页
            this.lastIndex = totalRows;
        }
    }

    public void setTotalPages() {
        if (totalRows % numPerPage == 0) {
            this.totalPages = totalRows / numPerPage;
        } else {
            this.totalPages = (totalRows / numPerPage) + 1;
        }
    }

    public List<DataSrc> findAll() {
        List<DataSrc> dataSrcs = new ArrayList<DataSrc>();
        String sql = "select * from t_datasource";
        List list = jdbcTemplate.queryForList(sql);
        Iterator it = list.iterator();
        DataSrc dataSrc = null;
        while (it.hasNext()) {
            Map map4DataSrc = (Map) it.next();
            dataSrc = new DataSrc();
            dataSrc.setDataSourceId(((Integer) map4DataSrc.get("DataSourceId")).intValue());
            dataSrc.setDataSourceName((String) map4DataSrc.get("DataSourceName"));
            dataSrc.setDataSourceType((String) map4DataSrc.get("DataSourceType"));
            dataSrc.setDatabaseName((String) map4DataSrc.get("DatabaseName"));
            dataSrc.setDatabaseType((String) map4DataSrc.get("DatabaseType"));
            dataSrc.setHost((String) map4DataSrc.get("Host"));
            dataSrc.setPort((String) map4DataSrc.get("Port"));
            dataSrc.setUserName((String) map4DataSrc.get("UserName"));
            dataSrc.setPassword((String) map4DataSrc.get("Password"));
            dataSrc.setIsValid((String) map4DataSrc.get("IsValid"));
            dataSrc.setFileType((String) map4DataSrc.get("FileType"));
            dataSrc.setFilePath((String) map4DataSrc.get("FilePath"));
            dataSrc.setCreateTime((Date)map4DataSrc.get("CreateTime"));
            dataSrc.setStat((Integer)map4DataSrc.get("Stat"));
            dataSrcs.add(dataSrc);
        }
        return dataSrcs;
    }

    public DataSrc findById(int id) {
        String sql = "select * from t_datasource where DataSourceId=?";
        DataSrc dataSrc = jdbcTemplate.queryForObject(sql, new Object[]{id}, new int[]{Types.INTEGER}, new DataSrcMapper());
        return dataSrc;
    }

    public boolean insert(DataSrc dataSrc) {
        boolean flag = false;
        String sql = "insert into t_datasource(DataSourceName,DataSourceType,DatabaseType,DatabaseName,Host,Port," +
                "UserName,Password,IsValid,FileType,FilePath,CreateTime,Stat) value(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        int i = jdbcTemplate.update(sql, new Object[]{dataSrc.getDataSourceName(), dataSrc.getDataSourceType(), dataSrc.getDatabaseType(),
                dataSrc.getDatabaseName(), dataSrc.getHost(), dataSrc.getPort(), dataSrc.getUserName(), dataSrc.getPassword(),
                dataSrc.getIsValid(), dataSrc.getFileType(), dataSrc.getFilePath(), dataSrc.getCreateTime(), dataSrc.getStat()});
        if (i > 0) {
            flag = true;
        }
        return flag;
    }

    public boolean update(DataSrc dataSrc) {
        boolean flag = false;
        String sql = "update t_datasource set DataSourceName=?,DataSourceType=?,DatabaseType=?,DatabaseName=?,Host=?,Port=?," +
                "UserName=?,Password=?,IsValid=?,FileType=?,FilePath=?,CreateTime=? ,Stat=? where DataSourceId=?";
        int i = jdbcTemplate.update(sql, new Object[]{dataSrc.getDataSourceName(), dataSrc.getDataSourceType(), dataSrc.getDatabaseType(),
                dataSrc.getDatabaseName(), dataSrc.getHost(), dataSrc.getPort(), dataSrc.getUserName(), dataSrc.getPassword(),
                dataSrc.getIsValid(), dataSrc.getFileType(), dataSrc.getFilePath(), dataSrc.getCreateTime(), dataSrc.getStat(), dataSrc.getDataSourceId()});
        if (i > 0) {
            flag = true;
        }
        return flag;
    }

    public boolean delete(int i) {
        boolean flag = false;
        String sql = "delete from t_datasource where DataSourceId=?";
        int j = jdbcTemplate.update(sql, new Object[]{i});
        if (j > 0) {
            flag = true;
        }
        return flag;
    }

    public List<String> relationalDatabaseTableList(String host, String port, String userName, String password, String databaseName) {
        List<String> l = new ArrayList<String>();
        String url = "jdbc:mysql://" + host + ":" + port + "/" + databaseName;
        boolean isValid = false;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //Connection con = DriverManager.getConnection(url, userName, password);
            IDataSource dataSource = DataSourceFactory.getDataSource("mysql");
            Connection con = dataSource.getConnection(host, port, userName, password, databaseName);
            ResultSet rs = con.getMetaData().getTables(null, null, null, new String[]{"TABLE", "VIEW"});
            while (rs.next()) {
                if (rs.getString(4) != null
                        && (rs.getString(4).equalsIgnoreCase("TABLE") || rs
                        .getString(4).equalsIgnoreCase("VIEW"))) {
                    String tableName = rs.getString(3);
                    l.add(tableName);
                }
            }
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                try {
                    con.close();
                } catch (SQLException e) {
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }

    public List<JSONObject> fileSourceFileList(String filePath) {
        List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
        File file = new File(filePath);
        if (!file.exists() || !file.isDirectory())
            return jsonObjects;
        File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", fileList[i].getPath().replaceAll("\\\\","%_%"));
            jsonObject.put("text", fileList[i].getName().replaceAll("\\\\","%_%"));
            if (fileList[i].isDirectory()) {
                jsonObject.put("type", "directory");
                JSONObject jo = new JSONObject();
                jo.put("disabled","true");
                jsonObject.put("state",jo);
            } else {
                jsonObject.put("type", "file");
            }
            jsonObjects.add(jsonObject);
        }
        Collections.sort(jsonObjects, new FileComparator());
        return jsonObjects;
    }

    public Map<String, Map<String, String>> getTableColumns(String host, String port, String userName, String password, String databaseName, String tableName) {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + databaseName;
        Connection con = null;
        Map<String, Map<String, String>> colInfos = new HashMap<String, Map<String, String>>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //con = DriverManager.getConnection(url, userName, password);
            IDataSource dataSource = DataSourceFactory.getDataSource("mysql");
            con = dataSource.getConnection(host, port, userName, password, databaseName);
            PreparedStatement statement = con.prepareStatement("SELECT  COLUMN_NAME ,DATA_TYPE,COLUMN_TYPE FROM information_schema.`COLUMNS` where" +
                    " TABLE_SCHEMA =? and TABLE_NAME =?");
            statement.setString(1, databaseName);
            statement.setString(2, tableName);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("type", rs.getString(2));
                map.put("value", "1");
                colInfos.put(rs.getString(1), map);
            }
            return colInfos;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            logger.error("无法加载数据库驱动", e);
            return colInfos;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("查询列信息出错", e);
            return colInfos;
        } finally {
            if (con != null)
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }

    public boolean validateSql(String sql, int dataSourceId) {
        DataSrc dataSrc = jdbcTemplate.queryForObject("select * from t_datasource where datasourceid=?", new DataSrcMapper(), dataSourceId);
        String host = dataSrc.getHost();
        String port = dataSrc.getPort();
        String databaseName = dataSrc.getDatabaseName();
        String url = "jdbc:mysql://" + host + ":" + port + "/" + databaseName;
        try (Connection con = DriverManager.getConnection(url, dataSrc.getUserName(), dataSrc.getPassword())) {
            Statement statement = con.createStatement();
            boolean result = statement.execute(sql);
            statement.close();
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return result;
        } catch (SQLException e) {
            logger.error("", e);
            return false;
        }
    }

    public int getRecordCount(String host, String port, String userName, String password, String databaseName, List<String> tableName) {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + databaseName;
        Connection con = null;
        int totalcount = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //con = DriverManager.getConnection(url, userName, password);
            IDataSource dataSource = DataSourceFactory.getDataSource("mysql");
            con = dataSource.getConnection(host, port, userName, password, databaseName);
            Statement statement = con.createStatement();
            for (int i=0;i<tableName.size();i++){
                String sql = "SELECT count(*) FROM "+tableName.get(i);
                ResultSet result = statement.executeQuery(sql);
                if (result.next())
                {
                    totalcount += result.getInt(1);
                }
            }
            return totalcount;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            logger.error("无法加载数据库驱动", e);
            return totalcount;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("查询列信息出错", e);
            return totalcount;
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    class FileComparator implements Comparator<JSONObject> {

        public int compare(JSONObject o1, JSONObject o2) {
            if ("directory".equals(o1.getString("type")) && "directory".equals(o2.getString("type"))) {
                return o1.getString("text").compareTo(o2.getString("text"));
            } else if ("directory".equals(o1.getString("type")) && !"directory".equals(o2.getString("type"))) {
                return -1;
            } else if (!"directory".equals(o1.getString("type")) && "directory".equals(o2.getString("type"))) {
                return 1;
            } else {
                return o1.getString("text").compareTo(o2.getString("text"));
            }
        }
    }

}
