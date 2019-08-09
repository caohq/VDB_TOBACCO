package cn.csdb.portal.repository;

import cn.csdb.portal.model.*;
import cn.csdb.portal.utils.dataSrc.DataSourceFactory;
import cn.csdb.portal.utils.dataSrc.IDataSource;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.*;
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
     * @Description: 根据PORTALID，删除表数据
     * @Param: [tableName, delPORTALID, dataSrc]
     * @return: int
     * @Author: zcy
     * @Date: 2019/5/20
     */
    public JSONObject deleteDate(String tableName, List<Object> listData, List<String> listColName,DataSrc dataSource) {
        List<String> list = selectPrimaryKeyColumn(dataSource, tableName);
        if (list.size() > 0) {
            return deleteDateByPrimaryKey(tableName, listData, listColName, dataSource, list);
        } else {
            return deleteDateByAllColumn(tableName, listData, listColName, dataSource);
        }
    }

    //    查询各个数据库主键和列
    public List<String> selectPrimaryKeyColumn(DataSrc dataSource, String tableName) {
        List<String> s = new ArrayList<>();

        Map<String, List<String>> listMap = getTableStructure(dataSource, tableName);

        List<String> list1 = listMap.get("COLUMN_NAME");
        List<String> list2 = listMap.get("pkColumn");
        for (int i = 0; i < list1.size(); i++) {
            if (list2.get(i).equals("true")) {
                s.add(list1.get(i));
            }
        }
        return s;
    }

    public JSONObject deleteDateByPrimaryKey(String tableName, List<Object> listData, List<String> listColName, DataSrc dataSrc, List<String> listPk) {
        int i = 0;
        JSONObject jsonObject=new JSONObject();
        IDataSource dataSource = DataSourceFactory.getDataSource(dataSrc.getDatabaseType());
        Connection connection = dataSource.getConnection(dataSrc.getHost(), dataSrc.getPort(), dataSrc.getUserName(), dataSrc.getPassword(), dataSrc.getDatabaseName());
        try {
            String sql = "";
                sql = "delete from " + tableName + " where ";

            String s = " ";
            for (int j = 0; j < listPk.size(); j++) {
                    s += listPk.get(j) + " =? and ";
            }
            s = s.substring(0, s.length() - 4);
            sql = sql + s;
            PreparedStatement ps = connection.prepareStatement(sql);
            for (int k = 0, j = 1; k < listColName.size(); k++) {
                for (int i1 = 0; i1 < listPk.size(); i1++) {
                    if (listColName.get(k).equals(listPk.get(i1))) {
                        ps.setObject(j, listData.get(k));
                        j++;
                    }
                }
            }
            System.out.println("主键删除：" + sql);
            i = ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("deleteReuslt",e);
        } finally {
            try {
                connection.close();
                if(i>0){
                    jsonObject.put("deleteReuslt","1");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return jsonObject;
    }


    public JSONObject deleteDateByAllColumn(String tableName, List<Object> listData, List<String> listColName, DataSrc dataSrc) {
        int i = 0;
        JSONObject jsonObject=new JSONObject();
        IDataSource dataSource = DataSourceFactory.getDataSource(dataSrc.getDatabaseType());
        Connection connection = dataSource.getConnection(dataSrc.getHost(), dataSrc.getPort(), dataSrc.getUserName(), dataSrc.getPassword(), dataSrc.getDatabaseName());;
        try {
            String sql = "";
            sql = "delete from " + tableName + " where ";


            String s = " ";
            for (int j = 0; j < listColName.size(); j++) {
                if (!listData.get(j).equals("") && listData.get(j) != null) {
                        s += listColName.get(j) + " =? and ";
                }
            }
            s = s.substring(0, s.length() - 4);
            sql = sql + s;
            PreparedStatement ps = connection.prepareStatement(sql);
            for (int k = 0, j = 1; k < listData.size(); k++) {
                if (!listData.get(k).equals("") && listData.get(k) != null) {
                    ps.setObject(j, listData.get(k));
                    j++;
                }
            }
            System.out.println("全匹配删除：" + sql);
            i = ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("deleteReuslt",e);
        } finally {
            try {
                connection.close();
                if(i>0){
                    jsonObject.put("deleteReuslt","1");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return jsonObject;
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
    public JSONObject updateDate(String tableName, DataSrc dataSrc, JSONArray jsonArray2, String subjectCode,
                          String[] enumnCoumns, JSONArray jsonArrayOld) {
        IDataSource dataSource = DataSourceFactory.getDataSource(dataSrc.getDatabaseType());
        Connection connection = dataSource.getConnection(dataSrc.getHost(), dataSrc.getPort(), dataSrc.getUserName(), dataSrc.getPassword(), dataSrc.getDatabaseName());
        int check = 0;
        JSONObject jsonObject=new JSONObject();
        ShowTypeInf showTypeInf = showTypeInfDao.checkData(tableName, subjectCode);
        try {
            String updatestr = " set ";
            for (int i = 0; i < jsonArray2.size(); i++) {
                String column = jsonArray2.getJSONObject(i).getString("name");
                Object value = jsonArray2.getJSONObject(i).getString("value");
                if (jsonArrayOld.get(i).equals(value)) {

                } else if ((jsonArrayOld.get(i).equals("") || jsonArrayOld.get(i) == null) && (value.equals("") || value == null)) {

                } else {
                        updatestr += "" + column + " = ? ,";
                }
            }
//            数据没有发生任何改变
            if (updatestr.equals(" set ")) {
                jsonObject.put("data","1");
            }
            updatestr = updatestr.substring(0, updatestr.length() - 2);
            String conditionstr = "";

            List<String> list = selectPrimaryKeyColumn(dataSrc, tableName);
            if (list.size() > 0) {  //已设置主键
                for (int i = 0; i < jsonArray2.size(); i++) {
                    String column = jsonArray2.getJSONObject(i).getString("name");
                    for (int j = 0; j < list.size(); j++) {
                        if (list.get(j).equals(column)) {
                                conditionstr += " " + column + " = '" + jsonArrayOld.get(i) + "'  and ";
                        }
                    }
                }
            } else {   //未设置主键
                for (int i = 0; i < jsonArray2.size(); i++) {
                    if (!jsonArrayOld.get(i).equals("") && jsonArrayOld.get(i) != null) {
                        String column = jsonArray2.getJSONObject(i).getString("name");
                            List<String> list1=getTableStructure(dataSrc,tableName).get("DATA_TYPE");
                            if("float".equals(list1.get(i))){
                                conditionstr += " " + column + " = " + jsonArrayOld.get(i) + "  and ";
                            }else {
                                conditionstr += " " + column + " = '" + jsonArrayOld.get(i) + "'  and ";
                            }
                    }
                }
            }
            conditionstr = conditionstr.substring(0, conditionstr.length() - 4);
            String sql = "";
            sql = "update " + tableName + "" + updatestr + " where " + conditionstr;

            PreparedStatement pst = connection.prepareStatement(sql);
            for (int i = 0, j = 1; i < jsonArray2.size(); i++) {
                String column = jsonArray2.getJSONObject(i).getString("name");
                Object value = jsonArray2.getJSONObject(i).getString("value");
                if (jsonArrayOld.get(i).equals(value)) {

                } else if ((jsonArrayOld.get(i).equals("") || jsonArrayOld.get(i) == null) && (value.equals("") || value == null)) {

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
            jsonObject.put("updateResult",e);
        } finally {
            try {
                connection.close();
                if(check>0){
                    jsonObject.put("data","1");
                }else{
                    jsonObject.put("data","0");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    /**
     * @Description: 新增数据
     * @Param: [dataSrc, tableName, pkyList, addAuto, jsonArray, subjectCode, enumnCoumns]
     * @return: int
     * @Author: zcy
     * @Date: 2019/5/22
     */
    public JSONObject addData(DataSrc dataSrc, String tableName, List<String> pkyList, List<String> addAuto, JSONArray jsonArray, String subjectCode, String[] enumnCoumns) {
        IDataSource dataSource = DataSourceFactory.getDataSource(dataSrc.getDatabaseType());
        Connection connection = dataSource.getConnection(dataSrc.getHost(), dataSrc.getPort(), dataSrc.getUserName(), dataSrc.getPassword(), dataSrc.getDatabaseName());
        JSONObject jsonObject=new JSONObject();
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
                        if (!val.toString().equals("") && val != null) {
                            val = getEnumKeyByVal(showTypeInf, enumnCoumns, col, val, dataSrc);
                            pst.setObject(j, val);
                            j++;
                    }
                }
            }
            System.out.println("新增：" + sql);
            check = pst.executeUpdate();

            System.out.println("影响数据行：" + check);
            pst.close();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("addResult",e);
        } finally {
            try {
                if(check>0){
                    jsonObject.put("data","1");
                }
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
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
    public List<List<DataComposeDemo>> getTableData(DataSrc dataSrc, String tableName, int pageNo, int pageSize) {
        IDataSource dataSource = DataSourceFactory.getDataSource(dataSrc.getDatabaseType());
        Connection connection = dataSource.getConnection(dataSrc.getHost(), dataSrc.getPort(), dataSrc.getUserName(), dataSrc.getPassword(), dataSrc.getDatabaseName());
        List<List<DataComposeDemo>> lists = new ArrayList<>();
        int start = pageSize * (pageNo - 1);
        int rownum = pageNo * pageSize;
        try {
            String sql = "";
            sql = "select * from " + tableName + " limit " + start + " ," + pageSize + "";

            System.out.println("查询的sql:" + sql);
            PreparedStatement pst = connection.prepareStatement(sql);
            ResultSet set = pst.executeQuery();
            ResultSetMetaData rsm = set.getMetaData();

            while (set.next()) {
                List<DataComposeDemo> list = new ArrayList<>();
                for (int i=1; i <= rsm.getColumnCount(); i++) {
                    DataComposeDemo dataComposeDemo = new DataComposeDemo();
                    if (set.getString(rsm.getColumnName(i)) == null) {
                        dataComposeDemo.setData(" ");
                    } else {
                        dataComposeDemo.setData(set.getString(rsm.getColumnName(i)));
                    }
                    list.add(dataComposeDemo);
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
     * @Description: 检索
     * @Param: [dataSrc, tableName, pageNo, pageSize, searchKey, columnName]
     * @return: java.util.List<java.util.Map < java.lang.String , java.lang.Object>>
     * @Author: zcy
     * @Date: 2019/5/23
*/
    public List<List<DataComposeDemo>> selectTableDataBySearchKey(DataSrc dataSrc, String tableName, int pageNo, int pageSize, String searchKey, List<String> columnName) {
        IDataSource dataSource = DataSourceFactory.getDataSource(dataSrc.getDatabaseType());
        Connection connection = dataSource.getConnection(dataSrc.getHost(), dataSrc.getPort(), dataSrc.getUserName(), dataSrc.getPassword(), dataSrc.getDatabaseName());
        List<List<DataComposeDemo>> lists = new ArrayList<>();
        int start = pageSize * (pageNo - 1);
        int rownum = pageNo * pageSize;
        searchKey = "%" + searchKey + "%";
        try {
            String s = "";
            for (int j = 0; j < columnName.size(); j++) {
                    s += columnName.get(j) + " like ? or ";
            }
            s = s.substring(0, s.length() - 3);
            String sql = "";
            sql = "select * from " + tableName + " where " + s + " limit " + start + " ," + pageSize + "";



            PreparedStatement pst = connection.prepareStatement(sql);
            for (int j = 0; j < columnName.size(); j++) {
                pst.setObject(j + 1, searchKey);
            }
            System.out.println("sql：" + sql);
            ResultSet set = pst.executeQuery();
            ResultSetMetaData rsm = set.getMetaData();

            while (set.next()) {
                List<DataComposeDemo> list = new ArrayList<>();
                for (int i=1; i <= rsm.getColumnCount(); i++) {
                    DataComposeDemo dataComposeDemo = new DataComposeDemo();
                    if (set.getString(rsm.getColumnName(i)) == null) {
                        dataComposeDemo.setData("");
                    } else {
                        dataComposeDemo.setData(set.getString(rsm.getColumnName(i)));
                    }
                    list.add(dataComposeDemo);
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
