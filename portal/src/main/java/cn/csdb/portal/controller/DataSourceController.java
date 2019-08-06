package cn.csdb.portal.controller;

import cn.csdb.portal.model.*;
import cn.csdb.portal.repository.CheckUserDao;
import cn.csdb.portal.repository.TableFieldComsDao;
import cn.csdb.portal.service.DataRMDBService;
import cn.csdb.portal.service.EditDataService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shibaoping on 2018/10/29.
 */
@Controller
public class DataSourceController {
    private Logger logger= LoggerFactory.getLogger(DataSourceController.class);

    @Autowired
    private DataRMDBService dataRMDBservice;

    @Autowired
    private TableFieldComsDao tableFieldComsDao;

    @Autowired
    private CheckUserDao checkUserDao;

    @Autowired
    private EditDataService editDataService;

    @RequestMapping(value="/dataResourceStaticRegister1")
    public String dataResourceRegister1(){
        return "dataResourceRegister";
    }

    @RequestMapping(value="/dataResourceStaticRegister")
    public String dataResourceRegister(){
        return "dataConfiguration";
    }

    @RequestMapping(value="/relationalDatabaseTableList")
    public
    @ResponseBody
    JSONObject relationalDatabaseTableList(String subjectCode,String flag){
        JSONObject jsonObject = new JSONObject();
/*
        DataSrc dataSrc = dataSrcService.findById(dataSourceId);
*/
        Subject subject = checkUserDao.getSubjectByCode(subjectCode);
        DataSrc datasrc = new DataSrc();
        datasrc.setDatabaseName(subject.getDbName());
        datasrc.setDatabaseType("mysql");
        datasrc.setHost(subject.getDbHost());
        datasrc.setPort(subject.getDbPort());
        datasrc.setUserName(subject.getDbUserName());
        datasrc.setPassword(subject.getDbPassword());
        if("0".equals(flag)) {
//            List<String> list = dataSrcService.relationalDatabaseTableList(datasrc);
            List<String> list = editDataService.searchTableNames(subjectCode);
            List<Described_Table> list_describe = tableFieldComsDao.queryDescribeTable(subject.getDbName());
            for (Described_Table described_table : list_describe) {
                list.remove(described_table.getTableName());
            }
            jsonObject.put("list", list);
            jsonObject.put("dataSourceName", datasrc.getDataSourceName());
        }else{
            List<String> list = new ArrayList<>();
            List<Described_Table> list_describe = tableFieldComsDao.queryDescribeTable(subject.getDbName());
            for (Described_Table described_table : list_describe) {
                list.add(described_table.getTableName());
            }
            jsonObject.put("list", list);
            jsonObject.put("dataSourceName", datasrc.getDataSourceName());
        }
        return jsonObject;
    }

    @ResponseBody
    @RequestMapping(value = "/previewRelationalDatabaseByTableName")
    public JSONObject previewRelationalDatabaseByTableName(
            @RequestParam(required = false) String curDataSubjectCode,
            @RequestParam(required = false, name = "tableInfosList") String tableInfosListStr,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {
        logger.info("预览表数据");
        JSONObject jsonObject = new JSONObject();
        List<TableInfoR> tableInfosList = JSON.parseArray(tableInfosListStr, TableInfoR.class);
        HashMap<String, List<TableInfo>> maps = Maps.newHashMap();
        String tableName = null;
        if (tableInfosList != null && tableInfosList.size() == 1) {
            tableName = tableInfosList.get(0).getTableName();
            maps.put(tableName, tableInfosList.get(0).getTableInfos());
        } else {
            return jsonObject;
        }
        List<List<Object>> datas = dataRMDBservice.getDataByTable(tableName, maps, curDataSubjectCode, 0, pageSize);
        jsonObject.put("datas", datas);
        return jsonObject;
    }
}
