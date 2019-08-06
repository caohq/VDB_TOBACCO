package cn.csdb.portal.repository;

import cn.csdb.portal.model.Described_Table;
import cn.csdb.portal.model.EnumData;
import cn.csdb.portal.model.ShowTypeDetail;
import cn.csdb.portal.model.ShowTypeInf;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public class ShowTypeInfDao {

    @Resource
    private MongoTemplate mongoTemplate;

    //该表是否设置过显示类型,是否有数据
    public ShowTypeInf checkData(String tableName, String subjectCode) {
        ShowTypeInf showTypeInf = mongoTemplate.findOne(new Query(Criteria.where("subjectCode").is(subjectCode).and("tableName").is(tableName)), ShowTypeInf.class);
        if (showTypeInf == null) {
            return null;
        } else {
            return showTypeInf;
        }
    }

    /**
     * @Description: 根据subjectCode查询数据
     * @Param: [subjectCode]
     * @return: java.util.List<cn.csdb.portal.model.ShowTypeInf>
     * @Author: zcy
     * @Date: 2019/5/6
     */
    public List<ShowTypeInf> getShowTypeInfBySub(String subjectCode) {
        List<ShowTypeInf> list = mongoTemplate.find(new Query(Criteria.where("subjectCode").is(subjectCode)), ShowTypeInf.class);
        return list;
    }

    /**
     * @Description: 移除已设置的显示类型
     * @Param: [list, columnName]
     * @return: java.util.List<cn.csdb.portal.model.ShowTypeDetail>
     * @Author: zcy
     * @Date: 2019/5/17
     */
    public List<ShowTypeDetail> checkColumn(List<ShowTypeDetail> list, String columnName) {
        for (int i = 0; i < list.size(); i++) {
            if (columnName.equals(list.get(i).getColumnName())) {
                list.remove(i);
            }
        }
        return list;
    }


    /**
     * @Description: 判断该列是否设置过显示类型
     * @Param: [list, columnName]
     * @return: int
     * @Author: zcy
     * @Date: 2019/5/17
     */
    public int isColumn(List<ShowTypeDetail> list, String columnName) {
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            if (columnName.equals(list.get(i).getColumnName())) {
                count++;
            }
        }
        return count;
    }

    /**
     * @Description: 删除status=2的数据
     * @Param: [list, columnName]
     * @return: java.util.List<cn.csdb.portal.model.ShowTypeDetail>
     * @Author: zcy
     * @Date: 2019/5/17
     */
    public List<ShowTypeDetail> deleteColumn(List<ShowTypeDetail> list, String columnName) {
        for (int i = 0; i < list.size(); i++) {
            if (columnName.equals(list.get(i).getColumnName()) && list.get(i).getStatus() == 2) {
                list.remove(i);
            }
        }
        return list;
    }

    public ShowTypeInf setShowTypeInf(String tableName, String subjectCode) {
        ShowTypeInf showTypeInf = new ShowTypeInf();
        showTypeInf.setTableName(tableName);
        showTypeInf.setSubjectCode(subjectCode);
        return showTypeInf;
    }

    public ShowTypeDetail setShowTypeDetail(int DisplayType, String columnName, String optionMode, String address) {
        ShowTypeDetail showTypeDetail = new ShowTypeDetail();
        showTypeDetail.setType(DisplayType);
        showTypeDetail.setAddress(address);
        showTypeDetail.setColumnName(columnName);
        showTypeDetail.setOptionMode(optionMode);

        return showTypeDetail;
    }

    //    保存URL类型
    public void saveTypeURL(int DisplayType, String tableName, String columnName, String optionMode, String address, String subjectCode) {
        ShowTypeInf showTypeInf = setShowTypeInf(tableName, subjectCode);
        ShowTypeDetail showTypeDetail = setShowTypeDetail(DisplayType, columnName, optionMode, address);
        ShowTypeInf s = checkData(tableName, subjectCode);
        if (s != null) {
//            ShowTypeInf s = mongoTemplate.findOne(new Query(Criteria.where("tableName").is(tableName)), ShowTypeInf.class);
            showTypeInf.setTableComment(s.getTableComment());
            List<ShowTypeDetail> list = s.getShowTypeDetailList();
            int count = isColumn(list, columnName);
            if (count == 1) {
                showTypeDetail.setStatus(2);
            } else if (count == 0) {
                showTypeDetail.setStatus(1);
            } else {
                showTypeDetail.setStatus(2);
                list = deleteColumn(list, columnName);
            }
            list.add(showTypeDetail);
            showTypeInf.setShowTypeDetailList(list);
            Update update = Update.update("tableName", tableName).set("showTypeDetailList", list);
            mongoTemplate.updateFirst(new Query(Criteria.where("subjectCode").is(subjectCode).and("tableName").is(tableName)), update, ShowTypeInf.class);
        } else {
            showTypeDetail.setStatus(1);
            List<ShowTypeDetail> list = new ArrayList<>();
            list.add(showTypeDetail);
            showTypeInf.setShowTypeDetailList(list);
            mongoTemplate.insert(showTypeInf);
        }
    }

    //    返回枚举类型
    public List<EnumData> getEnumDatas(Map<String, String> map) {
        List<EnumData> list1 = new ArrayList<>();
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            EnumData enumData = new EnumData();
            Map.Entry entry = (Map.Entry) it.next();
            enumData.setKey(entry.getKey().toString());
            enumData.setValue(entry.getValue().toString());
            list1.add(enumData);
        }
        return list1;
    }

    //    保存枚举类型
    public void saveTypeEnumText(int DisplayType, String tableName, String columnName, String optionMode, Map<String, String> map, String subjectCode) {
        ShowTypeInf showTypeInf = setShowTypeInf(tableName, subjectCode);
        ShowTypeDetail showTypeDetail = setShowTypeDetail(DisplayType, columnName, optionMode, "");
        ShowTypeInf s = checkData(tableName, subjectCode);
        List<EnumData> list1 = new ArrayList<>();
        if (s != null) {
//            去掉ShowTypeInf中重复的ShowTypeDetail，
//            ShowTypeInf s = mongoTemplate.findOne(new Query(Criteria.where("tableName").is(tableName)), ShowTypeInf.class);
            showTypeInf.setTableComment(s.getTableComment());
            List<ShowTypeDetail> list = s.getShowTypeDetailList();
            int count = isColumn(list, columnName);
            if (count == 1) {
                showTypeDetail.setStatus(2);
            } else if (count == 0) {
                showTypeDetail.setStatus(1);
            } else {
                showTypeDetail.setStatus(2);
                list = deleteColumn(list, columnName);
            }
            list1 = getEnumDatas(map);
            showTypeDetail.setEnumData(list1);
            list.add(showTypeDetail);
            showTypeInf.setShowTypeDetailList(list);
            Update update = Update.update("tableName", tableName).set("showTypeDetailList", list);
            mongoTemplate.updateFirst(new Query(Criteria.where("subjectCode").is(subjectCode).and("tableName").is(tableName)), update, ShowTypeInf.class);
        } else {
            showTypeDetail.setStatus(1);
            list1 = getEnumDatas(map);
            showTypeDetail.setEnumData(list1);
            List<ShowTypeDetail> list = new ArrayList<>();
            list.add(showTypeDetail);

            showTypeInf.setShowTypeDetailList(list);
            mongoTemplate.insert(showTypeInf);
        }
    }

    //    枚举sql类型
    public void saveTypeEnumSql(int DisplayType, String tableName, String columnName, String optionMode,
                                String relationColumnK, String relationColumnV, String relationTable, String subjectCode) {
        ShowTypeInf showTypeInf = setShowTypeInf(tableName, subjectCode);
        ShowTypeDetail showTypeDetail = setShowTypeDetail(DisplayType, columnName, optionMode, "");
        showTypeDetail.setRelationTable(relationTable);
        showTypeDetail.setRelationColumnK(relationColumnK);
        showTypeDetail.setRelationColumnV(relationColumnV);

        ShowTypeInf s = checkData(tableName, subjectCode);
        if (s != null) {
//            ShowTypeInf s = mongoTemplate.findOne(new Query(Criteria.where("tableName").is(tableName)), ShowTypeInf.class);
            showTypeInf.setTableComment(s.getTableComment());
            List<ShowTypeDetail> list = s.getShowTypeDetailList();
            int count = isColumn(list, columnName);
            if (count == 1) {
                showTypeDetail.setStatus(2);
            } else if (count == 0) {
                showTypeDetail.setStatus(1);
            } else {
                showTypeDetail.setStatus(2);
                list = deleteColumn(list, columnName);
            }
            list.add(showTypeDetail);
            showTypeInf.setShowTypeDetailList(list);
            Update update = Update.update("tableName", tableName).set("showTypeDetailList", list);
            mongoTemplate.updateFirst(new Query(Criteria.where("tableName").is(tableName)), update, ShowTypeInf.class);
        }else{
            showTypeDetail.setStatus(1);
            List<ShowTypeDetail> list = new ArrayList<>();
            list.add(showTypeDetail);
            showTypeInf.setShowTypeDetailList(list);
            mongoTemplate.insert(showTypeInf);
        }

    }


    //    删除status=2的值
    public void deleteStatusTwo(String tableName) {
        ShowTypeInf s = mongoTemplate.findOne(new Query(Criteria.where("tableName").is(tableName)), ShowTypeInf.class);
        if (s != null) {
            List<ShowTypeDetail> list = s.getShowTypeDetailList();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getStatus() == 2) {
                    list.remove(i);
                }
            }
            Update update = Update.update("tableName", tableName).set("showTypeDetailList", list);
            mongoTemplate.updateFirst(new Query(Criteria.where("tableName").is(tableName)), update, ShowTypeInf.class);
        }

    }


    //    删除status=2的列的status=1的数据，并将status=2改为status=1；
    public void updateSatusOne(String tableName, String subjectCode) {
        ShowTypeInf s = mongoTemplate.findOne(new Query(Criteria.where("subjectCode").is(subjectCode).and("tableName").is(tableName)), ShowTypeInf.class);
        if (s != null) {
            List<ShowTypeDetail> list = s.getShowTypeDetailList();
            List<String> columns = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getStatus() == 2) {
                    columns.add(list.get(i).getColumnName());
                }
            }
            if (columns.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    for (int ii = 0; ii < columns.size(); ii++) {
                        if (list.get(i).getStatus() == 1 && list.get(i).getColumnName().equals(columns.get(ii))) {
                            list.remove(i);
                        }
                    }
                }
            }
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setStatus(1);
            }
            Update update = Update.update("tableName", tableName).set("showTypeDetailList", list);
            mongoTemplate.updateFirst(new Query(Criteria.where("tableName").is(tableName)), update, ShowTypeInf.class);
        }
    }

    //    保存表名注释
    public void saveTableComment(String tableName, String tableComment, String subjectCode) {
        Update update = Update.update("tableName", tableName).set("tableComment", tableComment);
        ShowTypeInf showTypeInf1 = checkData(tableComment, subjectCode);
        if (showTypeInf1.getTableComment().equals(tableComment)) {
        } else {
            mongoTemplate.updateFirst(new Query(Criteria.where("subjectCode").is(subjectCode).and("tableName").is(tableName)), update, ShowTypeInf.class);
        }
    }

    //    保存关联数据表
    public void saveTypeDatasheet(int DisplayType, String tableName, String columnName, String[] s_Table, String[] s_column, String subjectCode) {
        ShowTypeInf showTypeInf = setShowTypeInf(tableName, subjectCode);
        ShowTypeDetail showTypeDetail = setShowTypeDetail(DisplayType, columnName, "", "");
        List<EnumData> enumDataList = new ArrayList<>();

        for (int i = 0; i < s_Table.length; i++) {
            EnumData enumData = new EnumData();
            enumData.setKey(s_Table[i]);
            enumData.setValue(s_column[i]);
            enumDataList.add(enumData);
        }
        ShowTypeInf s = checkData(tableName, subjectCode);
        if (s != null) {
//            ShowTypeInf s = mongoTemplate.findOne(new Query(Criteria.where("tableName").is(tableName)), ShowTypeInf.class);
            showTypeInf.setTableComment(s.getTableComment());
            List<ShowTypeDetail> list = s.getShowTypeDetailList();
            int count = isColumn(list, columnName);
            if (count == 1) {
                showTypeDetail.setStatus(2);
            } else if (count == 0) {
                showTypeDetail.setStatus(1);
            } else {
                showTypeDetail.setStatus(2);
                list = deleteColumn(list, columnName);
            }
            showTypeDetail.setEnumData(enumDataList);
            list.add(showTypeDetail);
            showTypeInf.setShowTypeDetailList(list);
            Update update = Update.update("tableName", tableName).set("showTypeDetailList", list);
            mongoTemplate.updateFirst(new Query(Criteria.where("subjectCode").is(subjectCode).and("tableName").is(tableName)), update, ShowTypeInf.class);
        }else{
            showTypeDetail.setEnumData(enumDataList);
            showTypeDetail.setStatus(1);
            List<ShowTypeDetail> list = new ArrayList<>();
            list.add(showTypeDetail);
            showTypeInf.setShowTypeDetailList(list);
            mongoTemplate.insert(showTypeInf);
        }
    }

//    保存文件类型
public void saveTypeFile(int DisplayType, String tableName, String columnName, String address, String optionMode, String Separator, String subjectCode) {
    ShowTypeInf showTypeInf = setShowTypeInf(tableName, subjectCode);
        ShowTypeDetail showTypeDetail = setShowTypeDetail(DisplayType, columnName, optionMode, address);
        showTypeDetail.setSeparator(Separator);
    ShowTypeInf s = checkData(tableName, subjectCode);
    if (s != null) {
//            ShowTypeInf s = mongoTemplate.findOne(new Query(Criteria.where("tableName").is(tableName)), ShowTypeInf.class);
            showTypeInf.setTableComment(s.getTableComment());
            List<ShowTypeDetail> list = s.getShowTypeDetailList();
            int count = isColumn(list, columnName);
            if (count == 1) {
                showTypeDetail.setStatus(2);
            } else if (count == 0) {
                showTypeDetail.setStatus(1);
            } else {
                showTypeDetail.setStatus(2);
                list = deleteColumn(list, columnName);
            }
            list.add(showTypeDetail);
            showTypeInf.setShowTypeDetailList(list);
            Update update = Update.update("tableName", tableName).set("showTypeDetailList", list);
        mongoTemplate.updateFirst(new Query(Criteria.where("subjectCode").is(subjectCode).and("tableName").is(tableName)), update, ShowTypeInf.class);
        }else{
            showTypeDetail.setStatus(1);
            List<ShowTypeDetail> list = new ArrayList<>();
            list.add(showTypeDetail);
            showTypeInf.setShowTypeDetailList(list);
            mongoTemplate.insert(showTypeInf);
        }
    }

    //    查询已描述的所有表名
    public List<Described_Table> getAllDescribed(String subjectCode) {
        List<Described_Table> list = mongoTemplate.find(new Query(Criteria.where("subjectCode").is(subjectCode)), Described_Table.class);
        return list;
    }

    //查询该列已经关联的表
    public List<String> getSheetTable(String tableName, String column) {
        ShowTypeInf showTypeInf = mongoTemplate.findOne(new Query(Criteria.where("tableName").is(tableName)), ShowTypeInf.class);
        List<ShowTypeDetail> showTypeDetails = new ArrayList<>();
        ShowTypeDetail showTypeDetail = new ShowTypeDetail();
        List<String> list = new ArrayList<>();
        if (showTypeInf != null) {
            if (showTypeInf.getShowTypeDetailList() != null) {
                showTypeDetails = showTypeInf.getShowTypeDetailList();
                for (ShowTypeDetail s : showTypeDetails) {
                    if (s.getColumnName().equals(column)) {
                        showTypeDetail = s;
                    }
                }
                List<EnumData> enumDataList = showTypeDetail.getEnumData();
                if (enumDataList != null) {
                    for (EnumData e : enumDataList) {
                        list.add(e.getKey());
                    }
                }
            }
        }

        return list;
    }

    //    保存文本类型
    public void saveTypeText(String tableName, String column, String subjectCode, String tableComment) {
        ShowTypeInf s = checkData(tableName, subjectCode);
        List<ShowTypeDetail> list = s.getShowTypeDetailList();
        ShowTypeDetail showTypeDetail = new ShowTypeDetail();
        showTypeDetail.setType(0);
        showTypeDetail.setStatus(1);
        showTypeDetail.setColumnName(column);
        list = checkColumn(list, column);
        list.add(showTypeDetail);
        Update update = Update.update("tableName", tableName).set("showTypeDetailList", list).set("tableComment", tableComment);
        mongoTemplate.updateFirst(new Query(Criteria.where("subjectCode").is(subjectCode).and("tableName").is(tableName)), update, ShowTypeInf.class);
    }

    //    新建数据
    public void insertShowTypeInf(String tableComment, String tableName, String subjectCode, String s_column[]) {
        ShowTypeInf showTypeInf = new ShowTypeInf();
        showTypeInf.setTableComment(tableComment);
        showTypeInf.setTableName(tableName);
        showTypeInf.setSubjectCode(subjectCode);
        List<ShowTypeDetail> list = new ArrayList<>();
        for (int i = 0; i < s_column.length; i++) {
            ShowTypeDetail showTypeDetail = new ShowTypeDetail();
            showTypeDetail.setType(0);
            showTypeDetail.setStatus(1);
            showTypeDetail.setColumnName(s_column[i]);
            list.add(showTypeDetail);
        }
        showTypeInf.setShowTypeDetailList(list);
        mongoTemplate.insert(showTypeInf);
    }

}
