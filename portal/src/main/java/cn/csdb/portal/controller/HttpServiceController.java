package cn.csdb.portal.controller;

import cn.csdb.portal.model.DataTask;
import cn.csdb.portal.model.Subject;
import cn.csdb.portal.model.SynchronizationTable;
import cn.csdb.portal.service.ConfigPropertyService;
import cn.csdb.portal.service.DataTaskService;
import cn.csdb.portal.service.SubjectMgmtService;
import cn.csdb.portal.utils.FileUtil;
import cn.csdb.portal.utils.SqlUtil;
import cn.csdb.portal.utils.TreeNode;
import cn.csdb.portal.utils.ZipUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @program: DataSync
 * @description:
 * @author: huangwei
 * @create: 2018-10-12 10:15
 **/
@Controller
@RequestMapping("/service")
public class HttpServiceController {

    @Autowired
    private DataTaskService dataTaskService;

    @Autowired
    private SubjectMgmtService subjectMgmtService;

    @Autowired
    private ConfigPropertyService configPropertyService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("#{systemPro['ftpRootPath']}")
    private String ftpRootPath;

    @Value("#{systemPro['ftpFilePath']}")
    private String ftpFilePath;

    @Value("${db.username}")
    private String dbUserName;

    @Value("${db.password}")
    private String dbPassword;

    @Value("${dataAssemblerHost}")
    private String dataAssemblerHost;

    @RequestMapping(value = "/treeNodeAsync")
    @ResponseBody
    public JSONObject treeNodes(String subName, String asyncString) {
        boolean async = true;
        if (!"async".equals(asyncString)) {
            async = false;
        }
        String rootPath = ftpRootPath + subName + ftpFilePath;
        JSONObject jsonObject = new TreeNode().jsTreeNodes(rootPath, async);
        return jsonObject;
    }

    @ResponseBody
    @RequestMapping(value = "/getDataTask", method = {RequestMethod.POST, RequestMethod.GET})
    public String executeDataTask(@RequestBody String requestString) {

        // 解析请求字符串 获取dataTask对象
        JSONObject requestJson = JSON.parseObject(requestString);
        String subjectCode = requestJson.get("subjectCode").toString();
        Subject subject = subjectMgmtService.findByCode(subjectCode);
        String dataTaskString = requestJson.get("dataTask").toString();
        DataTask dataTask = JSON.parseObject(dataTaskString, DataTask.class);
        dataTask.setSubjectCode(subject.getSubjectCode());

        // 记录需要同步的任务
        String dataTaskType = dataTask.getDataTaskType();
        if (!"file".equalsIgnoreCase(dataTaskType)) {
            if ("true".equals(dataTask.getSync())) {
                saveSyncTableName(dataTask);
            }
        }

        // 创建操作空间
//        String siteFtpPath = subject.getFtpFilePath() + "temp/";
        String siteFtpPath = subject.getFilePath() + "temp/";
        try {
            FileUtil.createFileByPathAndType(siteFtpPath, "dir");
        } catch (IOException e) {
            e.printStackTrace();
            return "DIR_ERROR";
        }

        // 上传的ZIP文件
        String zipFilePath = "";
        // 解压的路劲
        String unzipFilePath = "";
        // 定义语句脚本文件
        String dmlFile = "";
        // 操控语句脚本文件
        String ddlFile = "";


        if ("file".equalsIgnoreCase(dataTaskType)) {
            // 文件类型任务 定位上传的ZIP 解压到指定位置
            zipFilePath = siteFtpPath + subjectCode + "_" + dataTask.getDataTaskId() + ".zip";
            unzipFilePath = dataTask.getRemoteuploadpath();
            File tempFile = new File(unzipFilePath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }

            File file = new File(zipFilePath);
            ZipUtil zipUtil = new ZipUtil();
            try {
                zipUtil.unZip(file, unzipFilePath);
            } catch (Exception e) {
                e.printStackTrace();
                return "UNZIP_FILE_ERROR";
            }
        } else {
            // 数据类型任务 定位上传的ZIP 解压到操作空间下某个以任务ID为名的路径下 执行其中的ddl脚本与dml脚本
            zipFilePath = siteFtpPath + dataTask.getDataTaskId() + ".zip";
            unzipFilePath = siteFtpPath + subjectCode + "_" + dataTask.getDataTaskId() + "_sql";
            dmlFile = unzipFilePath + File.separator + "data.sql";
            ddlFile = unzipFilePath + File.separator + "struct.sql";

            try {
                ZipUtil zipUtil = new ZipUtil();
                zipUtil.unZip(new File(zipFilePath), unzipFilePath);
            } catch (Exception e) {
                e.printStackTrace();
                return "UNZIP_SQL_ERROR";
            }

            String dbName = subject.getDbName();
            SqlUtil sqlUtil = new SqlUtil();
            try {
                sqlUtil.importSql(dataAssemblerHost, dbUserName, dbPassword, dbName, ddlFile, "");
                sqlUtil.insertSql(dataAssemblerHost, dbUserName, dbPassword, dbName, "", dmlFile);
            } catch (Exception e) {
                e.printStackTrace();
                return "EXECUTE_SQL_ERROR";
            }

            // 删除SQL脚本
            FileUtil.deleteFolder(unzipFilePath);
        }

        // 删除ZIP文件
        FileUtil.deleteFolder(zipFilePath);

        return "SUCCESS";
    }


    @ResponseBody
    @RequestMapping(value = "syncDataTask", method = {RequestMethod.POST, RequestMethod.GET})
    public int syncDataTask(@RequestBody String requestString) {
        System.out.println(requestString);
        JSONObject requestJson = JSON.parseObject(requestString);
        String dataTaskString = requestJson.get("dataTask").toString();
        DataTask dataTask = JSON.parseObject(dataTaskString, DataTask.class);
        String syncFilePath = requestJson.get("syncFilePath").toString();
        String subjectCode = requestJson.get("subjectCode").toString();

        if ("true".equals(dataTask.getSync()) && !"false".equals(syncFilePath)) {//启动同步操作
            saveSyncTableName(dataTask);
        } else if ("false".equals(syncFilePath)) {
            Query query = Query.query(Criteria.where("taskId").is(dataTask.getDataTaskId()));
            mongoTemplate.remove(query, SynchronizationTable.class);
            return 1;
        }

        String structDBFile = syncFilePath + File.separator + "syncStruct.sql";
        String dataDBFile = syncFilePath + File.separator + "syncData.sql";
        Subject subject = subjectMgmtService.findByCode(subjectCode);
        String username = configPropertyService.getProperty("db.username");
        String password = configPropertyService.getProperty("db.password");
        String dbName = subject.getDbName();
        File file = new File(structDBFile);
        File file2 = new File(dataDBFile);
        SqlUtil sqlUtil = new SqlUtil();
        try {
            System.out.println("passwprd------" + password);
            sqlUtil.importSql(configPropertyService.getProperty("dataAssemblerHost"), username, password, dbName, structDBFile, "");
            sqlUtil.insertSql(configPropertyService.getProperty("dataAssemblerHost"), username, password, dbName, "", dataDBFile);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (file.exists()) {
                boolean result = file.delete();
                System.out.println("删除文件：" + structDBFile + "  " + result + "");
            }
            if (file2.exists()) {
                boolean result = file2.delete();
                System.out.println("删除文件：" + dataDBFile + "   " + result + "");
            }
        }

        return 1;
    }

    @ResponseBody
    @RequestMapping(value = "saveSyncTableName", method = {RequestMethod.POST, RequestMethod.GET})
    public String saveSyncTableName(DataTask dataTask) {

        //查询任务是否已存在
        DBObject dbObject = QueryBuilder.start().and("taskId").is(dataTask.getDataTaskId()).get();
        Query query = new BasicQuery(dbObject);
        List<SynchronizationTable> synchronizationTableList = mongoTemplate.find(query, SynchronizationTable.class);
        if (synchronizationTableList.size() != 0) {
            System.out.println("此次为任务的重新上传！");
            return "";
        }

        String tableStr = dataTask.getTableName();
        String tableStr2 = dataTask.getSqlTableNameEn();
        System.out.println("保存同步表： " + tableStr + "  " + tableStr2);
        if (!";".equals(tableStr) && tableStr != null && !"".equals(tableStr)) {
            String[] array = tableStr.split(";");
            for (String tableName : array) {
                System.out.println("开始保存数据！");
                SynchronizationTable synchronizationTable = new SynchronizationTable();
                synchronizationTable.setTableName(tableName);
                synchronizationTable.setSubjectCode(dataTask.getSubjectCode());
                synchronizationTable.setTaskId(dataTask.getDataTaskId());//任务id
                synchronizationTable.setSystemName("dataPipe");//分布端标识
                mongoTemplate.save(synchronizationTable);
            }
        }
        if (!";".equals(tableStr2) && tableStr2 != null && !"".equals(tableStr2)) {
            String[] array = tableStr2.split(";");
            for (String tableName : array) {
                System.out.println("开始保存数据！");
                SynchronizationTable synchronizationTable = new SynchronizationTable();
                synchronizationTable.setTableName(tableName);
                synchronizationTable.setSubjectCode(dataTask.getSubjectCode());
                synchronizationTable.setTaskId(dataTask.getDataTaskId());//任务id
                synchronizationTable.setSystemName("dataPipe");//分布端标识
                mongoTemplate.save(synchronizationTable);
            }
        }


        return "";
    }
}
