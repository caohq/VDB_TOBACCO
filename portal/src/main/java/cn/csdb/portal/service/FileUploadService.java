package cn.csdb.portal.service;

import cn.csdb.portal.model.Subject;
import cn.csdb.portal.repository.CheckUserDao;
import cn.csdb.portal.utils.FtpUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.Map;

/**
 * @ClassName FileUploadService
 * @Description 文件 FTP 上传
 * @Author jinbao
 * @Date 2019/1/2 10:50
 * @Version 1.0
 **/
@Service
public class FileUploadService {
    private Logger logger = LoggerFactory.getLogger(FileUploadService.class);
    @Resource
    private CheckUserDao checkUserDao;

    private FtpUtil ftpUtil = new FtpUtil();

    public JSONObject ftpUpload(String subjectCode, Map.Entry<String, MultipartFile> map) {
        JSONObject jsonObject = new JSONObject();
        String key = map.getKey();
        MultipartFile value = map.getValue();
        FileItem fileItem = ((CommonsMultipartFile) value).getFileItem();
        try {
            InputStream inputStream = value.getInputStream();
            String name = fileItem.getName();
            System.out.println(name);
            Subject mysql = getDataSrc(subjectCode, "mysql");
            String host = mysql.getDbHost();
            String userName = mysql.getFtpUser();
            String password = mysql.getFtpPassword();
            boolean upload = ftpUtil.upload(name, fileItem.getInputStream());
            System.out.println(upload);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject addDirectory(String dirName, String parentURI) {
        JSONObject jsonObject = new JSONObject();
        String newDir = parentURI + File.separator + dirName;
        File file = new File(newDir);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            if (mkdirs) {
                jsonObject.put("code", "success");
                jsonObject.put("message", dirName + " 目录创建成功");
                String replaceDirName = newDir.replace(String.valueOf(File.separator), "%_%");
                jsonObject.put("data", replaceDirName);
            } else {
                jsonObject.put("code", "error");
                jsonObject.put("message", dirName + " 目录创建异常");
            }
        } else {
            jsonObject.put("code", "error");
            jsonObject.put("message", "当前目录已存在");
        }
        return jsonObject;
    }

    public JSONObject addFile(String parentURI, Map.Entry<String, MultipartFile> multipartFile) {
        JSONObject jsonObject = new JSONObject();
        File file = new File(parentURI);
        if (!file.exists()) {
            jsonObject.put("code", "error");
            jsonObject.put("message", "请检查目录");
        } else {
            String s = multipartFile.getKey();
            MultipartFile value = multipartFile.getValue();
            FileItem fileItem = ((CommonsMultipartFile) value).getFileItem();
            String fileName = fileItem.getName();
            try {
//                OutputStream outputStream = fileItem.getOutputStream();
                InputStream inputStream = fileItem.getInputStream();
                File f = new File(parentURI+File.separator+fileName);
                OutputStream  outputStream = new FileOutputStream(f);
                int bytesWritten = 0;
                int byteCount = 0;

                byte[] bytes = new byte[1024];

                while ((byteCount = inputStream.read(bytes)) != -1)
                {
                    outputStream.write(bytes, 0, bytes.length);
//                    bytesWritten += byteCount;
                }
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    private Subject getDataSrc(String subjectCode, String DatabaseType) {
        Subject subject = checkUserDao.getSubjectByCode(subjectCode);
//        DataSrc datasrc = new DataSrc();
//        datasrc.setDatabaseName(subject.getDbName());
//        datasrc.setDatabaseType(DatabaseType);
//        datasrc.setHost(subject.getDbHost());
//        datasrc.setPort(subject.getDbPort());
//        datasrc.setUserName(subject.getDbUserName());
//        datasrc.setPassword(subject.getDbPassword());
        return subject;
    }

}
