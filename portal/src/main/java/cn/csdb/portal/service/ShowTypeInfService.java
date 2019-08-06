package cn.csdb.portal.service;

import cn.csdb.portal.model.Described_Table;
import cn.csdb.portal.model.ShowTypeDetail;
import cn.csdb.portal.model.ShowTypeInf;
import cn.csdb.portal.repository.ShowTypeInfDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShowTypeInfService {
    @Resource
    private ShowTypeInfDao showTypeInfDao;

    public void saveTypeURL(int DisplayType, String tableName, String columnName, String optionMode, String address, String subjectCode) {
        showTypeInfDao.saveTypeURL(DisplayType, tableName, columnName, optionMode, address, subjectCode);
    }

    public void saveTypeEnum(int DisplayType, String tableName, String columnName, String optionMode, String address, String enumData, String subjectCode) {
        String s[];
        Map<String, String> map = new HashMap<>();
        if (optionMode.equals("1")) {
            s = enumData.split(",");
            for (int i = 0; i < s.length; i++) {
                String ss[] = s[i].split("=");
                if (ss.length == 2)
                    map.put(ss[0], ss[1]);
            }
            showTypeInfDao.saveTypeEnumText(DisplayType, tableName, columnName, optionMode, map, subjectCode);
        }
        if (optionMode.equals("2")) {
            String s1[] = address.split(",");
            String c2[] = s1[0].trim().split("select");
            String c3[] = s1[1].trim().split("from");
            String s_col = c2[1].trim();
            String s_col2 = c3[0].trim();
            String s_Table = c3[1].trim();
            String relationTable = s_Table;
            String relationColumnK = s_col;
            String relationColumnV = s_col2;

//            System.out.println(s_col + "..." + s_col2 + "..." + s_Table);

            showTypeInfDao.saveTypeEnumSql(DisplayType, tableName, columnName, optionMode, relationColumnK, relationColumnV, relationTable, subjectCode);
        }
    }

    //    删除status=2的值
    public void deleteStatusTwo(String tableName) {
        showTypeInfDao.deleteStatusTwo(tableName);
    }

    //    保存表名注释
    public void saveTableComment(String tableName, String tableComment, String subjectCode) {
        showTypeInfDao.saveTableComment(tableName, tableComment, subjectCode);
    }

    //点击外层保存按钮
    public void updateSatusOne(String tableName, String s_column[], String s_type[], String subjectCode, String tableComment) {

        //删除status=2的列的status=1的数据，并将status=2改为status=1；
        showTypeInfDao.updateSatusOne(tableName, subjectCode);
        ShowTypeInf showTypeInf = showTypeInfDao.checkData(tableName, subjectCode);
        if (showTypeInf != null) {
            if (showTypeInf.getShowTypeDetailList() != null) {
//                如果该表中的字段已经设置了显示类型，如果showColumnType->s_type==1,保存为文本类型
                List<ShowTypeDetail> showTypeDetailList = showTypeInf.getShowTypeDetailList();
                for (int i = 0; i < showTypeDetailList.size(); i++) {
                    for (int j = 0; j < s_column.length; j++) {
                        if (showTypeDetailList.get(i).getColumnName().equals(s_column[j]) && s_type[j].equals("1")) {
                            showTypeInfDao.saveTypeText(tableName, s_column[j], subjectCode, tableComment);
                        }
                    }
                }
//                如果该字段没有设置过显示类型，默认保存为文本类型
                for (int ii = 0; ii < s_column.length; ii++) {
                    if (isColumnExit(showTypeDetailList, s_column[ii]) == 0) {
                        showTypeInfDao.saveTypeText(tableName, s_column[ii], subjectCode, tableComment);
                    }
                }
            }
        } else {
//            如果表中没有该表显示类型的设置，新建数据，所有字段默认设置文本型
            showTypeInfDao.insertShowTypeInf(tableComment, tableName, subjectCode, s_column);


        }
    }

    public int isColumnExit(List<ShowTypeDetail> showTypeDetailList, String columnName) {
        for (int i = 0; i < showTypeDetailList.size(); i++) {
            if (showTypeDetailList.get(i).getColumnName().equals(columnName)) {
                return 1;
            }
        }
        return 0;
    }

    //    数据库关联表
    public void saveTypeDatasheet(int DisplayType, String tableName, String columnName, String[] s_Table, String[] s_column, String subjectCode) {
        showTypeInfDao.saveTypeDatasheet(DisplayType, tableName, columnName, s_Table, s_column, subjectCode);
    }

    //    保存文件类型
    public void saveTypeFile(int DisplayType, String tableName, String columnName, String address, String optionMode, String Separator, String subjectCode) {
        showTypeInfDao.saveTypeFile(DisplayType, tableName, columnName, address, optionMode, Separator, subjectCode);
    }

    //    查询该表设置的显示类型,回显
    public ShowTypeInf getShowTypeInf(String tableName, String subjectCode) {
        ShowTypeInf showTypeInf = showTypeInfDao.checkData(tableName, subjectCode);
        return showTypeInf;
    }


    //    关联的表都是已描述的表
    public List<String> getAllDescribed(String subjectCode, String tableName, String columnName) {
        List<Described_Table> list = showTypeInfDao.getAllDescribed(subjectCode); //已描述表
        List<String> list1 = new ArrayList<>();
        if (list != null) {
            for (Described_Table d : list) {
                if (!d.getTableName().equals(tableName)) {
                    list1.add(d.getTableName());
                }
            }
        }
//        将已经关联的表过滤掉
//        List<String> exitTable = showTypeInfDao.getSheetTable(tableName, columnName);
//        if (exitTable != null) {
//            for (int i = 0; i < list1.size(); i++) {
//                for (int j = 0; j < exitTable.size(); j++) {
//                    if (list1.get(i).equals(exitTable.get(j))) {
//                        list1.remove(i);
//                    }
//                }
//            }
//        } else {
//            return list1;
//        }

        return list1;
    }

    /**
     * @Description: 查询表名注释
     * @Param: [subjectCode]
     * @return: java.util.List<cn.csdb.portal.model.ShowTypeInf>
     * @Author: zcy
     * @Date: 2019/5/6
     */
    public ShowTypeInf getTableComment(String tableName, String subjectCode) {
        ShowTypeInf showTypeInf = showTypeInfDao.checkData(tableName, subjectCode);
        return showTypeInf;
    }

    /**
     * @Description: 判断该列是否设置过显示类型
     * @Param: [showTypeInf, columnName]
     * @return: cn.csdb.portal.model.ShowTypeDetail
     * @Author: zcy
     * @Date: 2019/5/6
     */
    public ShowTypeDetail getShowTypeDetail(ShowTypeInf showTypeInf, String columnName) {
        List<ShowTypeDetail> list = showTypeInf.getShowTypeDetailList();
        for (ShowTypeDetail s : list) {
            if (s.getColumnName().equals(columnName) && s.getStatus() == 1) {
                return s;
            }
        }
        return null;
    }


}
