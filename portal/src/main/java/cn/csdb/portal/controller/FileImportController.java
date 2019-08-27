package cn.csdb.portal.controller;

import cn.csdb.portal.model.Period;
import cn.csdb.portal.model.TableField;
import cn.csdb.portal.service.FileImportService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.util.List;

/**
 * @ClassName FileImportController
 * @Description
 * @Author jinbao
 * @Date 2018/12/26 9:32
 * @Version 1.0
 **/
@Controller
@RequestMapping("/fileImport")
public class FileImportController {
    private Logger logger = LoggerFactory.getLogger(FileImportController.class);
    @Resource
    private FileImportService fileImportService;

    /**
     * 接到文件生成excel
     *
     * @param request
     * @param file
     * @return
     */
    @PostMapping("/excel")
    @ResponseBody
    public JSONObject fileImport(HttpServletRequest request, @RequestParam(value = "file") MultipartFile file) {
        JSONObject jsonObject = new JSONObject();
        String subjectCode = request.getParameter("subjectCode");
        String realPath = request.getRealPath("/") + "temp/";
        String tempFilePath = "";
        if (!file.isEmpty()) {
            try {
                tempFilePath = createTempFile(file, realPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if ("".equals(tempFilePath)) {
                jsonObject.put("code", "error");
                jsonObject.put("message", "文件缓冲异常，请联系管理员");
                return jsonObject;
            }
            jsonObject = fileImportService.processExcel(tempFilePath, subjectCode);
        } else {
            jsonObject.put("code", "error");
            jsonObject.put("message", "上传文件为空,请重新选择");
        }
        boolean b = deleteTempFile(tempFilePath);
        jsonObject.put("fileStatus", b);
        return jsonObject;
    }

    /** 
    * @Description: 导入式建表 
    * @Param: [request, file] 
    * @return: com.alibaba.fastjson.JSONObject 
    * @Author: zcy
    * @Date: 2019/8/27 
    */ 
    @PostMapping("/createTableAndInsertValue")
    @ResponseBody
    public JSONObject createTableAndInsertValue(HttpServletRequest request, @RequestParam(value = "file") MultipartFile file) {
        JSONObject jsonObject = new JSONObject();
        String subjectCode = request.getParameter("subjectCode");
        String tableData = request.getParameter("tableData");
        String tableName = request.getParameter("tableName");
        String realPath = request.getRealPath("/") + "temp/";
        String tempFilePath = "";
        List<TableField> tableFields = JSON.parseArray(tableData, TableField.class);
        if (!file.isEmpty()) {
            try {
                tempFilePath = createTempFile(file, realPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        jsonObject = fileImportService.createTableAndInsertValue(tableName, tableFields, tempFilePath, subjectCode);
        boolean b = deleteTempFile(tempFilePath);
        jsonObject.put("fileStatus", b);
        return jsonObject;
    }
    /** 
    * @Description: 表存在时，增量添加数据 
    * @Param: [request, file] 
    * @return: com.alibaba.fastjson.JSONObject 
    * @Author: zcy
    * @Date: 2019/8/27 
    */ 
    @PostMapping("/onlyInsertValue")
    @ResponseBody
    public JSONObject onlyInsertValue(HttpServletRequest request, @RequestParam(value = "file") MultipartFile file) {
        JSONObject jsonObject = new JSONObject();
        String subjectCode = request.getParameter("subjectCode");
        String tableData = request.getParameter("tableData");
        String tableName = request.getParameter("tableName");
        String realPath = request.getRealPath("/") + "temp/";
        String tempFilePath = "";
        List<TableField> tableFields = JSON.parseArray(tableData, TableField.class);
        if (!file.isEmpty()) {
            try {
                tempFilePath = createTempFile(file, realPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        jsonObject = fileImportService.onlyInsertValue(tableName, tableFields, tempFilePath, subjectCode);
        boolean b = deleteTempFile(tempFilePath);
        jsonObject.put("fileStatus", b);
        return jsonObject;
    }

    private String createTempFile(MultipartFile file, String realPath) throws IOException {
        String tempFilePath = "";
        InputStream inputStream = file.getInputStream();
        byte[] b = new byte[16];
        int flg = -1;
        String name = file.getOriginalFilename();
        File fileTemp = new File(realPath);
        if (!fileTemp.exists()) {
            fileTemp.mkdirs();
        }
        File f = new File(realPath + name);
        if (!f.exists()) {
            f.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(f);
        while ((flg = inputStream.read(b, 0, 16)) != -1) {
            fos.write(b, 0, 16);
        }
        fos.flush();
        fos.close();
        tempFilePath = f.getPath();
        return tempFilePath;
    }

    private boolean deleteTempFile(String realPath) {
        boolean del = false;
        File f = new File(realPath);
        if (f.exists()) {
            del = f.delete();
        }
        return del;
    }

    /**
     * 将上传的Excel模板保存在系统中以供用户下载使用
     */
    @RequestMapping(value = "/getExcelTemplate")
    public void downloads(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String realPath = request.getServletContext().getRealPath("/");
        String path = realPath + File.separator + "template" + File.separator;
        String fileName = "Excel上传模板.zip";
        File file = new File(path, fileName);

        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "multipart/form-data");
        fileName = new String(fileName.getBytes("gb2312"), "ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;fileName=\"" + fileName + "\"");
        InputStream input = new FileInputStream(file);
        OutputStream out = response.getOutputStream();
        byte[] buff = new byte[1024];
        int index = 0;
        while ((index = input.read(buff)) != -1) {
            out.write(buff, 0, index);
            out.flush();
        }
        out.close();
        input.close();
    }

    @PostMapping("/deleteTable")
    @ResponseBody
    public JSONObject deleteTableName(HttpServletRequest request) {
        String tableName = request.getParameter("tableName");
        String subjectCode = request.getParameter("subjectCode");
        JSONObject jsonObject = fileImportService.deleteTableName(tableName, subjectCode);
        return jsonObject;
    }
/** 
* @Description: 验证SQL语句 
* @Param: [newSql, request] 
* @return: java.lang.String 
* @Author: zcy
* @Date: 2019/8/27 
*/ 
    @PostMapping("/validateSqlString")
    @ResponseBody
    public String validateSqlString(String newSql, HttpServletRequest request) {
        String subjectCode = request.getSession().getAttribute("SubjectCode").toString();
        return fileImportService.validateSqlString(newSql, subjectCode);
    }

    /** 
    * @Description: 验证表名是否重复 
    * @Param: [newName, request] 
    * @return: java.lang.String 
    * @Author: zcy
    * @Date: 2019/8/27 
    */ 
    @PostMapping("/validateTableName")
    @ResponseBody
    public String validateTableName(String newName, HttpServletRequest request) {
        String subjectCode = request.getSession().getAttribute("SubjectCode").toString();
        return fileImportService.validateTableName(newName, subjectCode);
    }

    /** 
    * @Description: 创建表（关联创建表） 
    * @Param: [newSql, newName, period, request] 
    * @return: com.alibaba.fastjson.JSONObject 
    * @Author: zcy
    * @Date: 2019/8/27 
    */ 
    @PostMapping("/createTableBySql")
    @ResponseBody
    public JSONObject createTableBySql(String newSql, String newName, String period, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String subjectCode = request.getSession().getAttribute("SubjectCode").toString();
        String loginId = request.getSession().getAttribute("LoginId").toString();
        String tableBySql = null;
        try {
            tableBySql = fileImportService.createTableBySql(newSql, newName, subjectCode);
            jsonObject.put("message", tableBySql);
        } catch (SQLException e) {
            e.printStackTrace();
            return jsonObject;
        }
        if (!Strings.isBlank(period)) {
            long periodTime = Period.valueOf(period).getDataTime();
            fileImportService.createSynchronizeTask(newSql, newName, subjectCode, periodTime, loginId);
        }
        return jsonObject;
    }
/** 
* @Description: 预览数据
* @Param: [sqlString, request] 
* @return: com.alibaba.fastjson.JSONObject 
* @Author: zcy
* @Date: 2019/8/27 
*/ 
    @PostMapping("/previewSqlData")
    @ResponseBody
    public JSONObject previewSqlData(String sqlString, HttpServletRequest request) {
        String subjectCode = request.getSession().getAttribute("SubjectCode").toString();
        return fileImportService.previewSqlData(sqlString, subjectCode);

    }

    @PostMapping(value = "/updateSynchronizeTable")
    @ResponseBody
    public void updateSynchronizeTable(String synchronizeId, String frequency) {
        fileImportService.updateSynchronizeTable(synchronizeId, frequency);
    }

    @PostMapping("/selectSynchronizeInfo")
    @ResponseBody
    public JSONObject selectSynchronizeInfo(HttpServletRequest request) {
        String loginId = request.getSession().getAttribute("LoginId").toString();
        String subjectCode = request.getSession().getAttribute("SubjectCode").toString();
        JSONObject jsonObject = fileImportService.selectSynchronizeInfo(loginId, subjectCode);
        return jsonObject;
    }
}
