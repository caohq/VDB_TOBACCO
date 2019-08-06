package cn.csdb.portal.controller;

import cn.csdb.portal.service.FileUploadService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Iterator;
import java.util.Map;

/**
 * @ClassName FileUploadController
 * @Description 文件上传至ftp服务器
 * @Author jinbao
 * @Date 2018/12/28 17:02
 * @Version 1.0
 **/
@Controller
@RequestMapping("/fileUpload")
public class FileUploadController {

    @Resource
    private FileUploadService fileUploadService;


    @PostMapping("/toFtp")
    @ResponseBody
    public JSONObject fileUploadToFtp(HttpServletRequest request) {
        String subjectCode = request.getParameter("subjectCode");
        JSONObject jsonObject = new JSONObject();
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = mRequest.getFileMap();
        Iterator<Map.Entry<String, MultipartFile>> iterator = fileMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, MultipartFile> next = iterator.next();
            jsonObject = fileUploadService.ftpUpload(subjectCode, next);

        }
        jsonObject.put("success", "成功");
        return jsonObject;
    }

    @PostMapping("/addDirectory")
    @ResponseBody
    public JSONObject addDirectory(HttpServletRequest request) {
        JSONObject jsonObject = null;
        String dirName = request.getParameter("dirName");
        String s = request.getParameter("parentURI");
        String parentURI = s.replace("%_%", File.separator);
        jsonObject = fileUploadService.addDirectory(dirName, parentURI);
        return jsonObject;
    }

    @PostMapping("/addFile")
    @ResponseBody
    public JSONObject addFile(HttpServletRequest request){
        String s = request.getParameter("parentURI");
        String parentURI = s.replace("%_%", File.separator);
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartHttpServletRequest.getFileMap();
        Iterator<Map.Entry<String, MultipartFile>> iterator = fileMap.entrySet().iterator();
        synchronized (this){
            while (iterator.hasNext()){
                Map.Entry<String, MultipartFile> next = iterator.next();
                fileUploadService.addFile(parentURI, next);
            }
        }
        JSONObject jsonObject = new JSONObject();

        return jsonObject;
    }
}
