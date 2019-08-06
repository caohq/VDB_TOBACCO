package cn.csdb.portal.service;

import cn.csdb.portal.model.DataSrc;
import cn.csdb.portal.model.EnumData;
import cn.csdb.portal.model.Subject;
import cn.csdb.portal.repository.CheckUserDao;
import cn.csdb.portal.repository.EditDataDao;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EditDataService {
    @Autowired
    private EditDataDao editDataDao;

    @Autowired
    private CheckUserDao checkUserDao;


    //    根据登录人员的subjectCode，获得对应数据库连接信息
    public DataSrc getDataSrc(String subjectCode) {
        Subject subject = checkUserDao.getSubjectByCode(subjectCode);
        DataSrc datasrc = new DataSrc();
        datasrc.setDatabaseName(subject.getDbName());//数据库名
        datasrc.setDatabaseType("mysql");          //数据库类型
        datasrc.setHost(subject.getDbHost());       //连接地址
        datasrc.setPort(subject.getDbPort());       //端口号
        datasrc.setUserName(subject.getDbUserName());  //用户名
        datasrc.setPassword(subject.getDbPassword());  //密码

        return datasrc;
    }

    /**
     * @Description: 获取表结构
     * @Param: [dataSrc, tableName]
     * @return: java.util.Map<java.lang.String , java.util.List < java.lang.String>>
     * @Author: zcy
     * @Date: 2019/5/20
     */
    public Map<String, List<String>> getTableStructure(String subjectCode, String tableName) {
        DataSrc dataSrc = getDataSrc(subjectCode);
        Map<String, List<String>> map = editDataDao.getTableStructure(dataSrc, tableName);

        return map;
    }

    /**
     * @Description: 根据PORTALID查询数据
     * @Param: [dataSrc, tableName, PORTALID]
     * @return: java.util.List<java.lang.String>
     * @Author: zcy
     * @Date: 2019/5/20
     */
    public List<String> getDataByPORTALID(String subjectCode, String tableName, String PORTALID) {
        DataSrc dataSrc = getDataSrc(subjectCode);
        List<String> list = editDataDao.getDataByPORTALID(dataSrc, tableName, PORTALID);
        return list;
    }


    /**
     * @Description: 根据PORTALID，删除表数据
     * @Param: [tableName, delPORTALID, dataSrc]
     * @return: int
     * @Author: zcy
     * @Date: 2019/5/20
     */
    public int deleteDate(String tableName, String delPORTALID, String subjectCode) {
        DataSrc dataSrc = getDataSrc(subjectCode);
        int i = editDataDao.deleteDate(tableName, delPORTALID, dataSrc);

        return i;
    }


    /**
     * @Description: 获得表列名
     * @Param: [dataSrc, tableName]
     * @return: java.util.List<java.lang.String>
     * @Author: zcy
     * @Date: 2019/5/20
     */
    public List<String> getColumnName(String subjectCode, String tableName) {
        DataSrc dataSrc = getDataSrc(subjectCode);
        List<String> list1 = editDataDao.getColumnName(dataSrc, tableName);

        return list1;
    }

    /**
     * @Description: 更新数据
     * @Param: [condition, setData, tableName, dataSrc]
     * @return: int
     * @Author: zcy
     * @Date: 2019/5/20
     */
    public int updateDate(String tableName, String subjectCode, JSONArray jsonArray, String[] enumnCoumns, String delPORTALID) {
        DataSrc dataSrc = getDataSrc(subjectCode);
        int i = editDataDao.updateDate(tableName, dataSrc, jsonArray, subjectCode, enumnCoumns, delPORTALID);
        return i;
    }

    /**
     * 新增数据
     *
     * @Description:
     * @Param: [dataSrc, tableName, col, val]
     * @return: int
     * @Author: zcy
     * @Date: 2019/5/20
     */
    public int addData(String subjectCode, String tableName, List<String> pkyList, List<String> addAuto, JSONArray jsonArray, String[] enumnCoumns) {
        DataSrc dataSrc = getDataSrc(subjectCode);
        int i = editDataDao.addData(dataSrc, tableName, pkyList, addAuto, jsonArray, subjectCode, enumnCoumns);
        return i;
    }

    /**
     * @Description: 判断主键是否重复
     * @Param: [dataSrc, tableName, primaryKey, colName]
     * @return: int
     * @Author: zcy
     * @Date: 2019/5/20
     */
    public int checkPriKey(String subjectCode, String tableName, String primaryKey, String colName) {
        DataSrc dataSrc = getDataSrc(subjectCode);
        int i = editDataDao.checkPriKey(dataSrc, tableName, primaryKey, colName);
        return i;
    }


    /**
     * @Description: 分页，统计数据总数
     * @Param: [dataSrc, tableName]
     * @return: int
     * @Author: zcy
     * @Date: 2019/5/20
     */
    public int countData(String subjectCode, String tableName, String searchKey, List<String> columnName) {
        DataSrc dataSrc = getDataSrc(subjectCode);
        int count = 0;
        if (searchKey == null || searchKey.equals("")) {
            count = editDataDao.countData(dataSrc, tableName);
        } else {
            count = editDataDao.countDataBySerachKey(dataSrc, tableName, searchKey, columnName);
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
    public List<Map<String, Object>> getTableData(String subjectCode, String tableName, int pageNo, int pageSize, String searchKey, List<String> columnName) {
        DataSrc dataSrc = getDataSrc(subjectCode);
        List<Map<String, Object>> listMap = new ArrayList<>();
        if (searchKey.equals("") || searchKey == null) {
            listMap = editDataDao.getTableData(dataSrc, tableName, pageNo, pageSize);
        } else {
            listMap = editDataDao.selectTableDataBySearchKey(dataSrc, tableName, pageNo, pageSize, searchKey, columnName);
        }

        return listMap;
    }


    public List<EnumData> getEnumData(String subjectCode, String tableName, String colK, String colV) {
        DataSrc dataSrc = getDataSrc(subjectCode);
        List<EnumData> list = editDataDao.getEnumData(dataSrc, tableName, colK, colV);

        return list;
    }

    public List<String> getDataByColumn(String subjectCode, String tableName, String columnName) {
        DataSrc dataSrc = getDataSrc(subjectCode);
        List<String> list = editDataDao.getDataByColumn(dataSrc, tableName, columnName);
        return list;
    }


    /**
     * @Description: 显示表数据
     * @Param: [dataSrc, tableName, pageNo, pageSize]
     * @return: java.util.List<java.util.List   <   java.lang.Object>>
     * @Author: zcy
     * @Date: 2019/5/20
     */
    public List<List<Object>> getTableDataTestTmpl(String subjectCode, String tableName, int pageNo, int pageSize) {
        DataSrc dataSrc = getDataSrc(subjectCode);
        List<List<Object>> lists = editDataDao.getTableDataTestTmpl(dataSrc, tableName, pageNo, pageSize);

        return lists;
    }

    /**
     * @Description:
     * @Param: [list, enumDataList]
     * @return: cn.csdb.portal.model.EnumData
     * @Author: zcy
     * @Date: 2019/5/7
     */
    public EnumData enumCorresponding(List<String> list, List<EnumData> enumDataList) {
        EnumData enumDataList1 = new EnumData();
        String ss = "";
        if (list != null && enumDataList != null) {
            for (String s : list) {
                for (EnumData e : enumDataList) {
                    if (s != null && e != null) {
                        if (s.equals(e.getKey())) {
                            ss += e.getValue() + "|_|";
                        }
                    }
                }
            }
        }
        enumDataList1.setValue(ss);
        return enumDataList1;
    }

    /**
     * @Description: 新增数据，sql类型，根据val的值回找key值
     * @Param: [dataSrc, tableName, recolK, recolV]
     * @return: java.lang.String
     * @Author: zcy
     * @Date: 2019/5/7
     */
    public String getSqlEnumData(String subjectCode, String tableName, String recolK, String recolV, String recolValue) {
        DataSrc dataSrc = getDataSrc(subjectCode);
        String colKey = editDataDao.getSqlEnumData(dataSrc, tableName, recolK, recolV, recolValue);
        return colKey;
    }

    /**
     * @Description: 查询mysql中所有表名
     * @Param: [subjectCode]
     * @return: java.util.List<java.lang.String>
     * @Author: zcy
     * @Date: 2019/5/24
     */
    public List<String> searchTableNames(String subjectCode) {
        DataSrc dataSrc = getDataSrc(subjectCode);
        List<String> list = editDataDao.searchTableNames(dataSrc);

        return list;
    }
}
