package cn.csdb.portal.controller;

import cn.csdb.portal.model.DataComposeDemo;
import cn.csdb.portal.model.EnumData;
import cn.csdb.portal.model.ShowTypeDetail;
import cn.csdb.portal.model.ShowTypeInf;
import cn.csdb.portal.service.EditDataService;
import cn.csdb.portal.service.ShowTypeInfService;
import cn.csdb.portal.service.SynchronizationTablesService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class EditDataController {

    @Autowired
    private ShowTypeInfService showTypeInfService;

    @Autowired
    private SynchronizationTablesService synchronizationTablesService;

    @Autowired
    private EditDataService editDataService;


    /**
     * @Description: 显示所有表
     * @Param: [subjectCode]
     * @return: com.alibaba.fastjson.JSONObject
     * @Author: zcy
     * @Date: 2019/5/23
     */
    @ResponseBody
    @RequestMapping("/showTable")
    public JSONObject showTable(String subjectCode){
        JSONObject jsonObject=new JSONObject();
        List<String> list = editDataService.searchTableNames(subjectCode);

        List<ShowTypeInf> showTypeInfList1 = new ArrayList<>();
        for (String s : list) {
            if (showTypeInfService.getTableComment(s, subjectCode) != null) {
                ShowTypeInf showTypeInf = showTypeInfService.getTableComment(s, subjectCode);
                ShowTypeInf showTypeInf1 = new ShowTypeInf();
                showTypeInf1.setTableName(s);
                showTypeInf1.setTableComment(showTypeInf.getTableComment());
                showTypeInfList1.add(showTypeInf1);
            } else {
                ShowTypeInf showTypeInf1 = new ShowTypeInf();
                showTypeInf1.setTableName(s);
                showTypeInf1.setTableComment("");
                showTypeInfList1.add(showTypeInf1);
            }
        }

        jsonObject.put("list", showTypeInfList1);
        jsonObject.put("dataSourceName", subjectCode);
        return jsonObject;
    }

    /**
     * @Description: 查询表数据
     * @Param: [subjectCode, tableName, pageNo, pageSize, searchKey]
     * @return: com.alibaba.fastjson.JSONObject
     * @Author: zcy
     * @Date: 2019/5/23
     */
    @ResponseBody
    @RequestMapping("/showTableData")
    public JSONObject test(String subjectCode, String tableName, @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
                           @RequestParam(name = "pageSize", defaultValue = "10") int pageSize, String searchKey) {
        List<Map<String,Object>> list=new ArrayList<>();

        Map<String, List<String>> map = editDataService.getTableStructure(subjectCode, tableName);
        List<String> list3=map.get("COLUMN_NAME");
        List<String> list4=map.get("DATA_TYPE");
        List<String> list5=map.get("COLUMN_COMMENT");
        List<String> list6=map.get("pkColumn");
        List<String> list7=map.get("autoAdd");
        List<String> list8=map.get("COLUMN_TYPE");
        list = editDataService.getTableData(subjectCode, tableName, pageNo, pageSize, searchKey, list3);
        int countNum = editDataService.countData(subjectCode, tableName, searchKey, list3);

        JSONObject jsonObject=new JSONObject();
        ShowTypeInf showTypeInf = showTypeInfService.getTableComment(tableName, subjectCode);
        if (showTypeInf != null) {
            jsonObject.put("tableComment", showTypeInf.getTableComment());
        } else {
            jsonObject.put("tableComment", "");
        }
        int check = synchronizationTablesService.isSynchronizationTable(tableName, subjectCode);
        jsonObject.put("totalCount", countNum);
        jsonObject.put("currentPage", pageNo);
        jsonObject.put("pageSize", pageSize);
        jsonObject.put("totalPages", countNum % pageSize == 0 ? countNum / pageSize : countNum / pageSize + 1);

        jsonObject.put("isSync", check);
        jsonObject.put("dataDatil",list);
        jsonObject.put("columns",list3);
        jsonObject.put("dataType",list4);
        jsonObject.put("columnComment",list5);
        jsonObject.put("pkColumn",list6);
        jsonObject.put("autoAdd",list7);
        jsonObject.put("columnType",list8);
        return jsonObject;
    }


//    显示表数据，使用template，建立数据类，未完成
    @ResponseBody
    @RequestMapping("/showTableDataTestTmpl")
    public JSONObject showTableDataTestTmpl(String subjectCode, String tableName,@RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
                           @RequestParam(name = "pageSize", defaultValue = "10") int pageSize){
     
        List<List<Object>> list=new ArrayList<>();

        Map<String, List<String>> map = editDataService.getTableStructure(subjectCode, tableName);
        List<String> list3=map.get("COLUMN_NAME");
        List<String> list4=map.get("DATA_TYPE");
        List<String> list5=map.get("COLUMN_COMMENT");

        list = editDataService.getTableDataTestTmpl(subjectCode, tableName, pageNo, pageSize);
        int countNum = editDataService.countData(subjectCode, tableName, subjectCode, list3);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("totalCount", countNum);
        jsonObject.put("currentPage", pageNo);
        jsonObject.put("pageSize", pageSize);
        jsonObject.put("totalPages", countNum % pageSize == 0 ? countNum / pageSize : countNum / pageSize + 1);

        jsonObject.put("dataDatil",list);
        jsonObject.put("columns",list3);
        jsonObject.put("dataType",list4);
        jsonObject.put("columnComment",list5);

        return jsonObject;
    }

    /**
     * @Description: 新增数据，点击保存按钮
     * @Param: [subjectCode, tableName, addData, enumnCoumn]
     * @return: com.alibaba.fastjson.JSONObject
     * @Author: zcy
     * @Date: 2019/5/7
     */
    @RequestMapping("/addTableData")
    @ResponseBody
    public JSONObject addTableData(String subjectCode, String tableName, String addData, String enumnCoumn){

        JSONObject jsonObject=new JSONObject();
        JSONArray jsonArray=JSONArray.parseArray(addData);
        String enumnCoumns[] = enumnCoumn.split(",");

        Map<String, List<String>> map = editDataService.getTableStructure(subjectCode, tableName);

        List<String> list1=map.get("pkColumn");
        List<String> list2=map.get("autoAdd");
        List<String> list3=map.get("IS_NULLABLE");

        for(int i=0;i<jsonArray.size();i++){
            String col = jsonArray.getJSONObject(i).getString("columnName");
            String val = jsonArray.getJSONObject(i).getString("columnValue");

            if (list1.get(i).equals("PRI") && !list2.get(i).equals("auto_increment") && !col.equals("PORTALID")) {   //有主键但不自增，判断新增主键是否重复
                if(val!=null && !val.equals("")){
                    if (editDataService.checkPriKey(subjectCode, tableName, val, col) == 1) { //主键重复
                        jsonObject.put("data","0");
                        jsonObject.put("prikey",col);
                        return jsonObject;
                    }
                }else{
                    jsonObject.put("data", "-1");//主键不自增但值为空
                    jsonObject.put("prikey",col);
                    return jsonObject;
                }
            } else if (!col.equals("PORTALID") && list3.get(i).equals("NO")) {
                jsonObject.put("data", "-2+" + col);    //该列不能为空
                return jsonObject;
            }
        }
        int n = editDataService.addData(subjectCode, tableName, list1, list2, jsonArray, enumnCoumns);
        if(n==1){
            jsonObject.put("data","1");
        }else{
            jsonObject.put("data","-3");
        }
        return jsonObject;
    }

    /**
     * @Description: 删除表数据
     * @Param: [subjectCode, tableName, delPORTALID]
     * @return: com.alibaba.fastjson.JSONObject
     * @Author: zcy
     * @Date: 2019/5/23
     */
    @RequestMapping("deleteData")
    @ResponseBody
    public JSONObject deleteData(String subjectCode, String tableName, String delPORTALID){
        JSONObject jsonObject=new JSONObject();
        System.out.println(subjectCode+",,,"+tableName+",,,"+delPORTALID);
//        DataSrc datasrc=getDataSrc(subjectCode);
        int i = editDataService.deleteDate(tableName, delPORTALID, subjectCode);
        if(i==1){
            jsonObject.put("data","1");
        }else{
            jsonObject.put("data","0");
        }
        return jsonObject;
    }

    /**
     * @Description: 点击新增数据
     * @Param: [subjectCode, tableName]
     * @return: com.alibaba.fastjson.JSONObject
     * @Author: zcy
     * @Date: 2019/5/23
    */ 
    @ResponseBody
    @RequestMapping("/toaddTableData")
    public JSONObject toaddTableData(String subjectCode,String tableName){
        JSONObject jsonObject=new JSONObject();
//        DataSrc datasrc=getDataSrc(subjectCode);
        Map<String, List<String>> map = editDataService.getTableStructure(subjectCode, tableName);
        List<String> list1=map.get("COLUMN_NAME");//列明
        List<String> list2=map.get("DATA_TYPE");//字段类型
        List<String> list3=map.get("COLUMN_COMMENT"); //注释
        List<String> list4=map.get("autoAdd");  //自增
        List<String> list5=map.get("pkColumn");  //键
        jsonObject.put("COLUMN_NAME",list1);
        jsonObject.put("DATA_TYPE",list2);
        jsonObject.put("COLUMN_COMMENT",list3);
        jsonObject.put("autoAdd",list4);
        jsonObject.put("pkColumn",list5);


        List<EnumData> enumDataList = new ArrayList<>();
//        判断该表是否设置过显示类型
        ShowTypeInf showTypeInf = showTypeInfService.getTableComment(tableName, subjectCode);
        if (showTypeInf == null) {
            jsonObject.put("alert", 0);//该表没有设置过显示类型
        } else {
            enumDataList = getEnumData(list1, showTypeInf, subjectCode, tableName);
            jsonObject.put("enumDataList", enumDataList);
            jsonObject.put("alert", 1);
        }
        return jsonObject;
    }

    public List<EnumData> getEnumData(List<String> list1, ShowTypeInf showTypeInf, String subjectCode, String tableName) {
        List<EnumData> enumDataList = new ArrayList<>();
        for (int i = 0; i < list1.size(); i++) {
            ShowTypeDetail showTypeDetail = showTypeInfService.getShowTypeDetail(showTypeInf, list1.get(i));
            if (showTypeDetail != null) {
                EnumData enumData = new EnumData();
                String s = "";
                if (showTypeDetail.getType() == 2) {
                    if (showTypeDetail.getOptionMode().equals("1")) { //文本串

                        List<EnumData> enumText = showTypeDetail.getEnumData();

                        for (EnumData e : enumText) {
                            s += e.getValue() + "|_|";
                        }
                        enumData.setKey(list1.get(i));
                        enumData.setValue(s);
                        enumDataList.add(enumData);
                    }
                    if (showTypeDetail.getOptionMode().equals("2")) {  //sql
                        String reTable = showTypeDetail.getRelationTable();
                        String reColKey = showTypeDetail.getRelationColumnK();
                        String reColVal = showTypeDetail.getRelationColumnV();
                        List<String> list = editDataService.getDataByColumn(subjectCode, tableName, list1.get(i));
                        List<EnumData> enumSqlList = editDataService.getEnumData(subjectCode, reTable, reColKey, reColVal);
                        enumData = editDataService.enumCorresponding(list, enumSqlList);
                        enumData.setKey(list1.get(i));
                        enumDataList.add(enumData);
                    }
                }
            }
        }
        return enumDataList;
    }

    /**
     * @Description: 点击更新数据
     * @Param: [subjectCode, tableName, PORTALID]
     * @return: com.alibaba.fastjson.JSONObject
     * @Author: zcy
     * @Date: 2019/5/23
     */
    @RequestMapping("toupdateTableData")
    @ResponseBody
    public JSONObject toupdateTableData(String subjectCode, String tableName, String PORTALID){
        JSONObject jsonObject=new JSONObject();
        Map<String, List<String>> map = editDataService.getTableStructure(subjectCode, tableName);
        List<String> list1=map.get("COLUMN_NAME");
        List<String> list2=map.get("DATA_TYPE");//字段类型
        List<String> list3=map.get("COLUMN_COMMENT"); //注释
        List<String> list4=map.get("autoAdd");  //自增
        List<String> list5=map.get("pkColumn");  //键
        List<String> list6=map.get("COLUMN_TYPE"); //列类型

        jsonObject.put("COLUMN_NAME",list1);
        jsonObject.put("DATA_TYPE",list2);
        jsonObject.put("COLUMN_COMMENT",list3);
        jsonObject.put("autoAdd",list4);
        jsonObject.put("pkColumn",list5);
        jsonObject.put("COLUMN_TYPE",list6);
        List<String> list7 = editDataService.getDataByPORTALID(subjectCode, tableName, PORTALID);

        List<EnumData> enumDataList = new ArrayList<>();
//        判断该表是否设置过显示类型
        ShowTypeInf showTypeInf = showTypeInfService.getTableComment(tableName, subjectCode);
        if (showTypeInf == null) {
            jsonObject.put("alert", 0);//该表没有设置过显示类型
        } else {
            for (int i = 0; i < list1.size(); i++) {
                ShowTypeDetail showTypeDetail = showTypeInfService.getShowTypeDetail(showTypeInf, list1.get(i));
                if (showTypeDetail != null) {
                    EnumData enumData = new EnumData();
                    if (showTypeDetail.getType() == 2) {
                        String s = "";
                        if (showTypeDetail.getOptionMode().equals("1")) { //文本串

                            List<EnumData> enumText = showTypeDetail.getEnumData();

                            for (EnumData e : enumText) {
                                if (list7.get(i).equals(e.getKey())) {
                                    list7.set(i, e.getValue());
                                }
                                s += e.getValue() + "|_|";
                            }
                            enumData.setKey(list1.get(i));
                            enumData.setValue(s);
                            enumDataList.add(enumData);

                        }
                        String s_val = "";
                        if (showTypeDetail.getOptionMode().equals("2")) {  //sql
                            String reTable = showTypeDetail.getRelationTable();
                            String reColKey = showTypeDetail.getRelationColumnK();
                            String reColVal = showTypeDetail.getRelationColumnV();
                            List<String> list = editDataService.getDataByColumn(subjectCode, tableName, list1.get(i));
                            List<EnumData> enumSqlList = editDataService.getEnumData(subjectCode, reTable, reColKey, reColVal);
                            for (String ss : list) {
                                for (EnumData e : enumSqlList) {
                                    if (ss != null && e != null) {
                                        if (ss.equals(e.getKey())) {
                                            s_val += e.getValue() + "|_|";
                                        }
                                        if (list7.get(i).equals(e.getKey())) {
                                            list7.set(i, e.getValue());
                                        }
                                    }
                                }
                            }
                            enumData.setValue(s_val);
                            enumData.setKey(list1.get(i));
                            enumDataList.add(enumData);
                        }
                    }
                }
            }
        }
        jsonObject.put("enumDataList", enumDataList);
        jsonObject.put("data", list7);
        return jsonObject;
    }

    /**
     * @Description: 跟新数据保存
     * @Param: [newdata, subjectCode, tableName, delPORTALID, enumColumn]
     * @return: com.alibaba.fastjson.JSONObject
     * @Author: zcy
     * @Date: 2019/5/22
     */
    @RequestMapping("/saveTableData")
    @ResponseBody
    public JSONObject saveTableData(@Param("newdata") String newdata, String subjectCode, String tableName, String delPORTALID, String enumColumn) {
        JSONObject jsonObject = new JSONObject();
        String enumnCoumns[] = enumColumn.split(",");
        JSONArray jsonArray2 = JSONArray.parseArray(newdata);
        Map<String, List<String>> map = editDataService.getTableStructure(subjectCode, tableName);
//        旧数据
//    List<String> list = editDataService.getDataByPORTALID(subjectCode, tableName, delPORTALID);
        List<String> list1 = map.get("pkColumn");    //主键
        List<String> list3 = map.get("IS_NULLABLE");  //不为空

        for (int i = 0; i < jsonArray2.size(); i++) {
            String column = jsonArray2.getJSONObject(i).getString("name");
            String value = jsonArray2.getJSONObject(i).getString("value");

            if (list3.get(i).equals("NO") && (value == null || value.equals(""))) {
                jsonObject.put("data", "-2+");           //该列不能为空
                jsonObject.put("col", column);
                return jsonObject;
            }
            if (list1.get(i).equals("PRI")) {
                int n = editDataService.checkPriKey(subjectCode, tableName, value, column);
                if (n > 1) {
                    jsonObject.put("data", "-1");           //主键重复
                    return jsonObject;
                }
            }
        }

        int n = editDataService.updateDate(tableName, subjectCode, jsonArray2, enumnCoumns, delPORTALID);
        if (n == 1) {
            jsonObject.put("data", "1");
        } else {
            jsonObject.put("data", "0");
        }
        return jsonObject;
    }

    /**
     * @Description: 查看数据
     * @Param: [subjectCode, tableName, PORTALID]
     * @return: com.alibaba.fastjson.JSONObject
     * @Author: zcy
* @Date: 2019/5/23 
*/
    @RequestMapping("toCheckTableData")
    @ResponseBody
    public JSONObject toCheckTableData(String subjectCode, String tableName, String PORTALID){
        JSONObject jsonObject=new JSONObject();
        Map<String, List<String>> map = editDataService.getTableStructure(subjectCode, tableName);
        List<String> list1=map.get("COLUMN_NAME");
        List<String> list2=map.get("DATA_TYPE");//字段类型
        List<String> list3=map.get("COLUMN_COMMENT"); //注释
        List<String> list4=map.get("autoAdd");  //自增
        List<String> list5=map.get("pkColumn");  //键
        List<String> list6=map.get("COLUMN_TYPE"); //列类型
        List<String> list7=map.get("COLUMN_NAME");

        jsonObject.put("COLUMN_NAME",list1);
        jsonObject.put("DATA_TYPE",list2);
        jsonObject.put("COLUMN_COMMENT",list3);
        jsonObject.put("autoAdd",list4);
        jsonObject.put("pkColumn",list5);
        jsonObject.put("COLUMN_TYPE",list6);
        List<String> list = editDataService.getDataByPORTALID(subjectCode, tableName, PORTALID);
        List<DataComposeDemo> dataComposeDemos=new ArrayList<>();
        for(int i = 0; i<list7.size(); i++){
            DataComposeDemo composeDemo=new DataComposeDemo();
            composeDemo.setColName(list7.get(i));
            composeDemo.setAutoAdd(list4.get(i));
            composeDemo.setPkColumn(list5.get(i));
            composeDemo.setColumnComment(list3.get(i));
            composeDemo.setDataType(list2.get(i));
            composeDemo.setColumnType(list6.get(i));
            composeDemo.setData(list.get(i));
            dataComposeDemos.add(composeDemo);
        }
        jsonObject.put("data",dataComposeDemos);
        return jsonObject;
    }


}
