package cn.csdb.portal.repository;

import cn.csdb.portal.model.DataSrc;
import cn.csdb.portal.model.EnumData;
import cn.csdb.portal.model.ShowTypeDetail;
import cn.csdb.portal.model.ShowTypeInf;
import cn.csdb.portal.utils.dataSrc.DataSourceFactory;
import cn.csdb.portal.utils.dataSrc.IDataSource;
import com.alibaba.fastjson.JSONArray;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

@Repository
public class EditDataDao {

    @Resource
    private ShowTypeInfDao showTypeInfDao;
    /**
     * @Description: 获取表结构
     * @Param: [dataSrc, tableName]
     * @return: java.util.Map<java.lang.String   ,   java.util.List   <   java.lang.String>>
     * @Author: zcy
     * @Date: 2019/5/20
     */
    public Map<String, List<String>> getTableStructure(DataSrc dataSrc, String tableName) {
        IDataSource dataSource = DataSourceFactory.getDataSource(dataSrc.getDatabaseType());
        Connection connection = dataSource.getConnection(dataSrc.getHost(), dataSrc.getPort(), dataSrc.getUserName(), dataSrc.getPassword(), dataSrc.getDatabaseName());
        Map<String, List<String>> map = new HashMap<>();
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        List<String> list3 = new ArrayList<>();
        List<String> list4 = new ArrayList<>();
        List<String> list5 = new ArrayList<>();
        List<String> list6 = new ArrayList<>();
        List<String> list7 = new ArrayList<>();
        try {
            String sql = "SELECT COLUMN_NAME,IS_NULLABLE,DATA_TYPE,COLUMN_KEY,COLUMN_COMMENT,EXTRA,COLUMN_TYPE " +
                    "FROM information_schema. COLUMNS WHERE table_schema = ? AND table_name = ? ";

            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, dataSrc.getDatabaseName());
            pst.setString(2, tableName);
            ResultSet set = pst.executeQuery();
            while (set.next()) {
                list1.add(set.getString("COLUMN_NAME"));
                list2.add(set.getString("DATA_TYPE"));
                list3.add(set.getString("COLUMN_COMMENT"));
                list4.add(set.getString("COLUMN_KEY"));
                list5.add(set.getString("EXTRA"));
                list6.add(set.getString("IS_NULLABLE"));
                list7.add(set.getString("COLUMN_TYPE"));
            }
            map.put("COLUMN_NAME", list1);
            map.put("DATA_TYPE", list2);
            map.put("COLUMN_COMMENT", list3);
            map.put("pkColumn", list4);
            map.put("autoAdd", list5);
            map.put("IS_NULLABLE", list6);
            map.put("COLUMN_TYPE", list7);
            pst.close();
            set.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * @Description: 根据PORTALID查询数据
     * @Param: [dataSrc, tableName, PORTALID]
     * @return: java.util.List<java.lang.String>
     * @Author: zcy
     * @Date: 2019/5/20
     */
    public List<String> getDataByPORTALID(DataSrc dataSrc, String tableName, String PORTALID) {
        IDataSource dataSource = DataSourceFactory.getDataSource(dataSrc.getDatabaseType());
        Connection connection = dataSource.getConnection(dataSrc.getHost(), dataSrc.getPort(), dataSrc.getUserName(), dataSrc.getPassword(), dataSrc.getDatabaseName());
        List<String> list = new ArrayList<>();
        try {
            String sql = "SELECT * from " + tableName + " where PORTALID=?";

            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, PORTALID);
            ResultSet set = pst.executeQuery();
            ResultSetMetaData rsm = set.getMetaData();
            while (set.next()) {
                for (int i = 1; i <= rsm.getColumnCount(); i++) {
                    if (set.getString(rsm.getColumnName(i)) == null || set.getString(rsm.getColumnName(i)).equals("")) {
                        list.add("");
                    } else {
                        list.add(set.getString(rsm.getColumnName(i)));
                    }
//                    System.out.println("查询" + set.getString(rsm.getColumnName(i)));
                }
            }

            pst.close();
            set.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }


    /**
     * @Description: 根据PORTALID，删除表数据
     * @Param: [tableName, delPORTALID, dataSrc]
     * @return: int
     * @Author: zcy
     * @Date: 2019/5/20
     */
    public int deleteDate(String tableName, String delPORTALID, DataSrc dataSrc) {
        int i = 0;
        IDataSource dataSource = DataSourceFactory.getDataSource(dataSrc.getDatabaseType());
        Connection connection = dataSource.getConnection(dataSrc.getHost(), dataSrc.getPort(), dataSrc.getUserName(), dataSrc.getPassword(), dataSrc.getDatabaseName());
        try {
            String sql = "delete from " + tableName + " where PORTALID=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, delPORTALID);
            i = ps.executeUpdate();
            System.out.println(sql);

            ps.close();
        } catch (Exception e) {

        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return i;
    }


    /**
     * @Description: 获得表列名
     * @Param: [dataSrc, tableName]
     * @return: java.util.List<java.lang.String>
     * @Author: zcy
     * @Date: 2019/5/20
     */
    public List<String> getColumnName(DataSrc dataSrc, String tableName) {
        IDataSource dataSource = DataSourceFactory.getDataSource(dataSrc.getDatabaseType());
        Connection connection = dataSource.getConnection(dataSrc.getHost(), dataSrc.getPort(), dataSrc.getUserName(), dataSrc.getPassword(), dataSrc.getDatabaseName());
        List<String> list1 = new ArrayList<>();
        try {
            String sql = "SELECT COLUMN_NAME FROM information_schema. COLUMNS WHERE table_schema = ? AND table_name = ? ";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, dataSrc.getDatabaseName());
            pst.setString(2, tableName);
            ResultSet set = pst.executeQuery();
            while (set.next()) {
                list1.add(set.getString("COLUMN_NAME"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list1;
    }

    /**
     * @Description: 更新数据
     * @Param: [tableName, dataSrc, jsonArray2, subjectCode, enumnCoumns, delPORTALID]
     * @return: int
     * @Author: zcy
     * @Date: 2019/5/22
     */
    public int updateDate(String tableName, DataSrc dataSrc, JSONArray jsonArray2, String subjectCode,
                          String[] enumnCoumns, String delPORTALID) {
        int check = 0;
        IDataSource dataSource = DataSourceFactory.getDataSource(dataSrc.getDatabaseType());
        Connection connection = dataSource.getConnection(dataSrc.getHost(), dataSrc.getPort(), dataSrc.getUserName(), dataSrc.getPassword(), dataSrc.getDatabaseName());
        ShowTypeInf showTypeInf = showTypeInfDao.checkData(tableName, subjectCode);
//       旧数据
        List<String> list = getDataByPORTALID(dataSrc, tableName, delPORTALID);
        try {
            String updatestr = " set ";
            for (int i = 0; i < jsonArray2.size(); i++) {
                String column = jsonArray2.getJSONObject(i).getString("name");
                Object value = jsonArray2.getJSONObject(i).getString("value");
                if (list.get(i).equals(value)) {

                } else if ((list.get(i).equals("") || list.get(i) == null) && (value.equals("") || value == null)) {

                } else {
                    updatestr += "" + column + " = ? , ";
                }
            }
            if (updatestr.equals(" set ")) {
                check = 1;
                return check;
            }
            updatestr = updatestr.substring(0, updatestr.length() - 2);
            String conditionstr = " PORTALID  = '" + delPORTALID + "' ";
            String sql = "update " + tableName + "" + updatestr + " where " + conditionstr;

            PreparedStatement pst = connection.prepareStatement(sql);
            for (int i = 0, j = 1; i < jsonArray2.size(); i++) {
                String column = jsonArray2.getJSONObject(i).getString("name");
                Object value = jsonArray2.getJSONObject(i).getString("value");
                if (list.get(i).equals(value)) {

                } else if ((list.get(i).equals("") || list.get(i) == null) && (value.equals("") || value == null)) {

                } else {
                    value = getEnumKeyByVal(showTypeInf, enumnCoumns, column, value, dataSrc);
                    pst.setObject(j, value);
                    j++;
                }
            }
            System.out.println("更新：" + sql);
            check = pst.executeUpdate();
            pst.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return check;
    }

    /**
     * @Description: 新增数据
     * @Param: [dataSrc, tableName, pkyList, addAuto, jsonArray, subjectCode, enumnCoumns]
     * @return: int
     * @Author: zcy
     * @Date: 2019/5/22
     */
    public int addData(DataSrc dataSrc, String tableName, List<String> pkyList, List<String> addAuto, JSONArray jsonArray, String subjectCode, String[] enumnCoumns) {
        IDataSource dataSource = DataSourceFactory.getDataSource(dataSrc.getDatabaseType());
        Connection connection = dataSource.getConnection(dataSrc.getHost(), dataSrc.getPort(), dataSrc.getUserName(), dataSrc.getPassword(), dataSrc.getDatabaseName());
        int check = 0;
        ShowTypeInf showTypeInf = showTypeInfDao.checkData(tableName, subjectCode);
        try {
            String columns = "";
            String values = "";
            for (int i = 0; i < jsonArray.size(); i++) {
                String col = jsonArray.getJSONObject(i).getString("columnName");
                Object val = jsonArray.getJSONObject(i).getString("columnValue");
                if (pkyList.get(i).equals("PRI") && addAuto.get(i).equals("auto_increment")) { //有主键且自增
                } else {
                    if (!val.toString().equals("") && val != null) {
                        columns += "" + col + " ,";
                        values += " ? ,";
                    }
                    if (col.equals("PORTALID")) {
                        columns += "" + col + " ,";
                        values += " ? ,";
                    }

                }
            }
            columns = columns.substring(0, columns.length() - 1);
            values = values.substring(0, values.length() - 1);

            String sql = "insert into  " + tableName + "(" + columns + ")  values(" + values + ")";
            PreparedStatement pst = connection.prepareStatement(sql);
            for (int i = 0, j = 1; i < jsonArray.size(); i++) {
                String col = jsonArray.getJSONObject(i).getString("columnName");
                Object val = jsonArray.getJSONObject(i).getString("columnValue");
                if (pkyList.get(i).equals("PRI") && addAuto.get(i).equals("auto_increment")) { //有主键且自增
                } else {
                    if (col.equals("PORTALID")) {
                        String uuid = UUID.randomUUID().toString();
                        pst.setObject(j, uuid);
                        j++;
                    } else {
                        if (!val.toString().equals("") && val != null) {
                            val = getEnumKeyByVal(showTypeInf, enumnCoumns, col, val, dataSrc);
                            pst.setObject(j, val);
                            j++;
                        }
                    }

                }
            }
            System.out.println("新增：" + sql);
            check = pst.executeUpdate();

            System.out.println("影响数据行：" + check);
            pst.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return check;
    }

    /**
     * @Description: 获得字典枚举数据
     * @Param: [showTypeInf, enumnCoumns, col, val, dataSrc]
     * @return: java.lang.Object
     * @Author: zcy
     * @Date: 2019/5/23
     */
    public Object getEnumKeyByVal(ShowTypeInf showTypeInf, String[] enumnCoumns, String col, Object val, DataSrc dataSrc) {
        if (showTypeInf != null) {
            for (int ii = 0; ii < enumnCoumns.length; ii++) {
                if (col.equals(enumnCoumns[ii])) {
                    ShowTypeDetail showTypeDetail = selectShowTypeDetail(showTypeInf, enumnCoumns[ii]);
                    if (showTypeDetail != null) {
                        if (showTypeDetail.getOptionMode().equals("1")) {
                            List<EnumData> list = showTypeDetail.getEnumData();
                            for (EnumData e : list) {
                                if (val.equals(e.getValue())) {
                                    val = e.getKey();
                                }
                            }
                        }
                        if (showTypeDetail.getOptionMode().equals("2")) {
                            String reTable = showTypeDetail.getRelationTable();
                            String recolK = showTypeDetail.getRelationColumnK();
                            String recolV = showTypeDetail.getRelationColumnV();
                            val = getSqlEnumData(dataSrc, reTable, recolK, recolV, val);
                        }
                    }
                }

            }
        }
        return val;
    }

    /**
     * @Description:
     * @Param: [showTypeInf, columnName]
     * @return: cn.csdb.portal.model.ShowTypeDetail
     * @Author: zcy
     * @Date: 2019/5/23
     */
    public ShowTypeDetail selectShowTypeDetail(ShowTypeInf showTypeInf, String columnName) {
        List<ShowTypeDetail> list = showTypeInf.getShowTypeDetailList();
        for (ShowTypeDetail s : list) {
            if (s.getColumnName().equals(columnName) && s.getStatus() == 1) {
                return s;
            }
        }
        return null;
    }
    /**
     * @Description: 判断主键是否重复
     * @Param: [dataSrc, tableName, primaryKey, colName]
     * @return: int
     * @Author: zcy
     * @Date: 2019/5/20
     */
    public int checkPriKey(DataSrc dataSrc, String tableName, String primaryKey, String colName) {
        int i = 0;
        IDataSource dataSource = DataSourceFactory.getDataSource(dataSrc.getDatabaseType());
        Connection connection = dataSource.getConnection(dataSrc.getHost(), dataSrc.getPort(), dataSrc.getUserName(), dataSrc.getPassword(), dataSrc.getDatabaseName());
        try {
            String sql = "select count(" + colName + ") as num " + "from " + tableName + " where " + colName + "= ?";
            System.out.println("判断主键是否重复：" + sql);
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setObject(1, primaryKey);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                i = rs.getInt("num");
                System.out.println("是否重复" + i);
            }
            pst.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i;
    }


    /**
     * @Description: 分页，统计数据总数
     * @Param: [dataSrc, tableName]
     * @return: int
     * @Author: zcy
     * @Date: 2019/5/20
     */
    public int countData(DataSrc dataSrc, String tableName) {
        IDataSource dataSource = DataSourceFactory.getDataSource(dataSrc.getDatabaseType());
        Connection connection = dataSource.getConnection(dataSrc.getHost(), dataSrc.getPort(), dataSrc.getUserName(), dataSrc.getPassword(), dataSrc.getDatabaseName());
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        int count = 0;
        try {
            String sql = "select count(*) as num from " + tableName + "";

            //         时间格式的数据怎么获得
            PreparedStatement pst = connection.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                count = rs.getInt("num");
            }
            pst.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return count;
    }


    /**
     * @Description: 连接mysql数据库，根据表明查出所有数据，供编辑
     * @Param: [dataSrc, tableName, pageNo, pageSize]
     * @return: java.util.List<java.util.Map   <   java.lang.String   ,   java.lang.Object>>
     * @Author: zcy
     * @Date: 2019/5/20
     */
    public List<Map<String, Object>> getTableData(DataSrc dataSrc, String tableName, int pageNo, int pageSize) {
        IDataSource dataSource = DataSourceFactory.getDataSource(dataSrc.getDatabaseType());
        Connection connection = dataSource.getConnection(dataSrc.getHost(), dataSrc.getPort(), dataSrc.getUserName(), dataSrc.getPassword(), dataSrc.getDatabaseName());
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        int start = pageSize * (pageNo - 1);
        try {
//           select COLUMN_NAME,DATA_TYPE,COLUMN_COMMENT from information_schema.COLUMNS where table_name = '表名' and table_schema = '数据库名称';
            String sql = "select * from " + tableName + " limit " + start + " ," + pageSize + "";

            //         时间格式的数据怎么获得
            PreparedStatement pst = connection.prepareStatement(sql);
            ResultSet set = pst.executeQuery();
            ResultSetMetaData rsm = set.getMetaData();

            while (set.next()) {
                Map<String, Object> map = new LinkedHashMap<>();
                for (int i = 1; i <= rsm.getColumnCount(); i++) {
                    if (set.getString(rsm.getColumnName(i)) == null) {
                        map.put(rsm.getColumnName(i), "");
                    } else {
                        map.put(rsm.getColumnName(i), set.getString(rsm.getColumnName(i)));
                    }
                }
                listMap.add(map);
            }
            pst.close();
            set.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return listMap;
    }

    /**
     * @Description: 检索
     * @Param: [dataSrc, tableName, pageNo, pageSize, searchKey, columnName]
     * @return: java.util.List<java.util.Map < java.lang.String , java.lang.Object>>
     * @Author: zcy
     * @Date: 2019/5/23
*/ 
    public List<Map<String, Object>> selectTableDataBySearchKey(DataSrc dataSrc, String tableName, int pageNo, int pageSize, String searchKey, List<String> columnName) {
        IDataSource dataSource = DataSourceFactory.getDataSource(dataSrc.getDatabaseType());
        Connection connection = dataSource.getConnection(dataSrc.getHost(), dataSrc.getPort(), dataSrc.getUserName(), dataSrc.getPassword(), dataSrc.getDatabaseName());
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        int start = pageSize * (pageNo - 1);
        searchKey = "%" + searchKey + "%";
        try {
            String s = "";
            for (int j = 0; j < columnName.size(); j++) {
                if (!columnName.get(j).equals("PORTALID")) {
                    s += columnName.get(j) + " like ? or ";
                }
            }
            s = s.substring(0, s.length() - 3);
            String sql = "select * from " + tableName + " where " + s + " limit " + start + " ," + pageSize + "";

            PreparedStatement pst = connection.prepareStatement(sql);
            for (int j = 0, i = 1; j < columnName.size(); j++) {
                if (!columnName.get(j).equals("PORTALID")) {
                    pst.setObject(i, searchKey);
                    i++;
                }
            }
            System.out.println("sql：" + sql);
            ResultSet set = pst.executeQuery();
            ResultSetMetaData rsm = set.getMetaData();

            while (set.next()) {
                Map<String, Object> map = new LinkedHashMap<>();
                for (int i = 1; i <= rsm.getColumnCount(); i++) {
                    if (set.getString(rsm.getColumnName(i)) == null) {
                        map.put(rsm.getColumnName(i), "");
                    } else {
                        map.put(rsm.getColumnName(i), set.getString(rsm.getColumnName(i)));
                    }
                }
                listMap.add(map);
            }
            pst.close();
            set.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return listMap;
    }

    /**
     * @Description: 根据检索词查询数据总条数
     * @Param: [dataSrc, tableName, searchKey, columnName]
     * @return: int
     * @Author: zcy
     * @Date: 2019/5/23
*/
    public int countDataBySerachKey(DataSrc dataSrc, String tableName, String searchKey, List<String> columnName) {
        IDataSource dataSource = DataSourceFactory.getDataSource(dataSrc.getDatabaseType());
        Connection connection = dataSource.getConnection(dataSrc.getHost(), dataSrc.getPort(), dataSrc.getUserName(), dataSrc.getPassword(), dataSrc.getDatabaseName());
        int count = 0;
        try {
            searchKey = "%" + searchKey + "%";
            String s = "";
            for (int j = 0; j < columnName.size(); j++) {
                if (!columnName.get(j).equals("PORTALID")) {
                    s += columnName.get(j) + " like ? or ";
                }
            }
            s = s.substring(0, s.length() - 3);
            String sql = "select count(*) as num from " + tableName + " where " + s;

            //         时间格式的数据怎么获得
            PreparedStatement pst = connection.prepareStatement(sql);
            for (int j = 0, i = 1; j < columnName.size(); j++) {
                if (!columnName.get(j).equals("PORTALID")) {
                    pst.setObject(i, searchKey);
                    i++;
                }
            }
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                count = rs.getInt("num");
            }
            pst.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    /**
     * @Description: 获得字典枚举的数据
     * @Param: [dataSrc, tableName, colK, colV]
     * @return: java.util.List<cn.csdb.portal.model.EnumData>
     * @Author: zcy
     * @Date: 2019/5/23
     */
    public List<EnumData> getEnumData(DataSrc dataSrc, String tableName, String colK, String colV) {
        IDataSource dataSource = DataSourceFactory.getDataSource(dataSrc.getDatabaseType());
        Connection connection = dataSource.getConnection(dataSrc.getHost(), dataSrc.getPort(), dataSrc.getUserName(), dataSrc.getPassword(), dataSrc.getDatabaseName());
        List<EnumData> list = new ArrayList<>();
        try {
            String sql = "select distinct " + colK + "," + colV + " from " + tableName;
            PreparedStatement pst = connection.prepareStatement(sql);
            ResultSet set = pst.executeQuery();
            while (set.next()) {
                EnumData enumData = new EnumData();
                enumData.setKey(set.getString(colK));
                enumData.setValue(set.getString(colV));
                list.add(enumData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * @Description: 根据列名查询数据
     * @Param: [dataSrc, tableName, columnName]
     * @return: java.util.List<java.lang.String>
     * @Author: zcy
     * @Date: 2019/5/23
     */
    public List<String> getDataByColumn(DataSrc dataSrc, String tableName, String columnName) {
        IDataSource dataSource = DataSourceFactory.getDataSource(dataSrc.getDatabaseType());
        Connection connection = dataSource.getConnection(dataSrc.getHost(), dataSrc.getPort(), dataSrc.getUserName(), dataSrc.getPassword(), dataSrc.getDatabaseName());
        List<String> list = new ArrayList<>();
        try {
            String sql = "select distinct " + columnName + " from " + tableName;
            PreparedStatement pst = connection.prepareStatement(sql);
            ResultSet set = pst.executeQuery();
            while (set.next()) {
                list.add(set.getString(columnName));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }


    /**
     * @Description: 显示表数据
     * @Param: [dataSrc, tableName, pageNo, pageSize]
     * @return: java.util.List<java.util.List   <   java.lang.Object>>
     * @Author: zcy
     * @Date: 2019/5/20
     */
    public List<List<Object>> getTableDataTestTmpl(DataSrc dataSrc, String tableName, int pageNo, int pageSize) {
        IDataSource dataSource = DataSourceFactory.getDataSource(dataSrc.getDatabaseType());
        Connection connection = dataSource.getConnection(dataSrc.getHost(), dataSrc.getPort(), dataSrc.getUserName(), dataSrc.getPassword(), dataSrc.getDatabaseName());
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        List<List<Object>> lists = new ArrayList<>();

        int start = pageSize * (pageNo - 1);
        try {
//           select COLUMN_NAME,DATA_TYPE,COLUMN_COMMENT from information_schema.COLUMNS where table_name = '表名' and table_schema = '数据库名称';
            String sql = "select * from " + tableName + " limit " + start + " ," + pageSize + "";

            PreparedStatement pst = connection.prepareStatement(sql);
            ResultSet set = pst.executeQuery();
            ResultSetMetaData rsm = set.getMetaData();
            while (set.next()) {
                List<Object> list = new ArrayList<>();
                for (int i = 1; i <= rsm.getColumnCount(); i++) {
                    if (set.getString(rsm.getColumnName(i)) == null) {
                        list.add("");
                    } else {
                        list.add(set.getString(rsm.getColumnName(i)));
                    }
                }
                lists.add(list);
            }
            pst.close();
            set.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return lists;
    }


    /**
     * @Description: 新增数据，sql类型，根据val的值回找key值
     * @Param: [dataSrc, tableName, recolK, recolV]
     * @return: java.lang.String
     * @Author: zcy
     * @Date: 2019/5/7
     */
    public String getSqlEnumData(DataSrc dataSrc, String tableName, String recolK, String recolV, Object recolValue) {
        IDataSource dataSource = DataSourceFactory.getDataSource(dataSrc.getDatabaseType());
        Connection connection = dataSource.getConnection(dataSrc.getHost(), dataSrc.getPort(), dataSrc.getUserName(), dataSrc.getPassword(), dataSrc.getDatabaseName());
        String colKey = "";
        try {

            String sql = "select distinct " + recolK + " from " + tableName + " where " + recolV + "='" + recolValue + "'";
            System.out.println(sql);
            PreparedStatement pst = connection.prepareStatement(sql);
            ResultSet set = pst.executeQuery();
            while (set.next()) {
                colKey = set.getString(recolK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return colKey;
    }

    /**
     * @Description: 获取该数据库内所有表名
     * @Param: [dataSrc]
     * @return: java.util.List<java.lang.String>
     * @Author: zcy
     * @Date: 2019/5/23
     */
    public List<String> searchTableNames(DataSrc dataSrc) {
        IDataSource dataSource = DataSourceFactory.getDataSource(dataSrc.getDatabaseType());
        Connection conn = dataSource.getConnection(dataSrc.getHost(), dataSrc.getPort(), dataSrc.getUserName(), dataSrc.getPassword(), dataSrc.getDatabaseName());
        List<String> list = new ArrayList<String>();
        try {
            if (conn != null) {
                System.out.println("数据库连接成功");
            }
            String sql = "select table_name from information_schema.tables where table_schema='" + dataSrc.getDatabaseName() + "'";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                list.add(result.getString("TABLE_NAME"));
//            System.out.println(result.getString("TABLE_NAME"));
            }
            result.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
