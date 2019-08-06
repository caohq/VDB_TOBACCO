package cn.csdb.portal.controller;

import cn.csdb.portal.service.FileNetService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * @Author jinbao
 * @Date 2019/3/12 11:02
 * @Description 网盘功能模块
 **/
@Controller()
@RequestMapping("/fileNet")
public class FileNetController {

    @Resource
    private FileNetService fileNetService;

    @Value("#{systemPro['ResourceExtraFile']}")
    private String ResourceExtraFile;
    @Value("#{systemPro['synchronizeTableLogs']}")
    private String synchronizeTableLogs;


    /**
     * 获取展开目录信息
     *
     * @param request
     * @param rootPath 编码后需要展开的目录路径
     * @return
     */
    @RequestMapping(value = "/fileList")
    @ResponseBody
    public JSONObject getDirectoryUnderCurrent(HttpServletRequest request, String rootPath) {
        JSONObject jsonObject = new JSONObject();
        String parentPath = "";
        String ftpRootPath = removeFtpRootLastSeparator(request);
        if ("".equals(rootPath)) {
            parentPath = ftpRootPath;
        } else {
            parentPath = rootPath;
        }
        parentPath = fileNetService.decodePath(parentPath, ftpRootPath);
        jsonObject = fileNetService.getDir(ftpRootPath, parentPath);
        return jsonObject;
    }


    @PostMapping("/fileNetByName")
    @ResponseBody
    public JSONObject fileNetByName(HttpServletRequest request, String rootPath, String searchName) {
        JSONObject jsonObject = new JSONObject();
        String parentPath = "";
        String ftpRootPath = removeFtpRootLastSeparator(request);
        if ("".equals(rootPath)) {
            parentPath = ftpRootPath;
        } else {
            parentPath = rootPath;
        }
        parentPath = fileNetService.decodePath(parentPath, ftpRootPath);
        jsonObject = fileNetService.getDirByName(parentPath, ftpRootPath, searchName);
        return jsonObject;
    }

    private String removeFtpRootLastSeparator(HttpServletRequest request) {
        String ftpRootPath = (String) request.getSession().getAttribute("FtpFilePath");
        // TODO 部署时候请注释下一行
//        ftpRootPath = "H:\\Cache\\";

        boolean linux = ftpRootPath.endsWith("/");
        boolean win = ftpRootPath.endsWith("\\");
        if (linux || win) {
            ftpRootPath = ftpRootPath.substring(0, ftpRootPath.length() - 1);
            ftpRootPath += "/file";
            return ftpRootPath;
        }
        return ftpRootPath;
    }


    /**
     * @param response   返回文件
     * @param selectPath 选中需要下载的文件全路径
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/downloadFile")
    public HttpServletResponse downloadFile(HttpServletRequest request, HttpServletResponse response, String selectPath) throws IOException {
        String ftpRootPath = removeFtpRootLastSeparator(request);
        selectPath = fileNetService.decodePath(selectPath, ftpRootPath);
        downloadFile(response, selectPath);
        return response;
    }

    private void downloadFile(HttpServletResponse response, String selectPath) throws IOException {
        File file = new File(selectPath);
        String fileName = file.getName();
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


    /**
     * 添加目录
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/addDirectory")
    @ResponseBody
    public JSONObject addDirectory(HttpServletRequest request) {
        JSONObject jsonObject = null;
        String dirName = request.getParameter("dirName");
        String s = request.getParameter("parentURI");
        String parentURI = s.replace("%_%", File.separator);
        String ftpRootPath = removeFtpRootLastSeparator(request);
        parentURI = fileNetService.decodePath(parentURI, ftpRootPath);
        jsonObject = fileNetService.addDirectory(dirName, parentURI);
        return jsonObject;
    }


    /**
     * 上传文件到位置
     *
     * @return
     */
    @PostMapping("/addFile")
    @ResponseBody
    public JSONObject addFile(HttpServletRequest request) {
        String parentURI = request.getParameter("parentURI");
        String ftpRootPath = removeFtpRootLastSeparator(request);
        parentURI = parentURI.replace("%_%", File.separator);
        parentURI = fileNetService.decodePath(parentURI, ftpRootPath);
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartHttpServletRequest.getFileMap();
        Iterator<Map.Entry<String, MultipartFile>> iterator = fileMap.entrySet().iterator();
        synchronized (this) {
            while (iterator.hasNext()) {
                Map.Entry<String, MultipartFile> next = iterator.next();
                fileNetService.addFile(parentURI, next);
            }
        }
        JSONObject jsonObject = new JSONObject();

        return jsonObject;
    }

    /**
     * 重命名文件
     *
     * @param currentPath 当前要被重命名的文件
     * @param newName     新的名字
     * @return
     */
    @RequestMapping(value = "/renameFile")
    @ResponseBody
    public JSONObject renameFile(HttpServletRequest request, String currentPath, String newName) {
        JSONObject jsonObject = new JSONObject();
        String ftpRootPath = removeFtpRootLastSeparator(request);
        currentPath = fileNetService.decodePath(currentPath, ftpRootPath);
        jsonObject = fileNetService.renameFile(currentPath, newName);
        return jsonObject;
    }


    /**
     * 复制 || 移动文件
     *
     * @param oldFile 源文件
     * @param newFile 目标文件
     * @return
     */
    @RequestMapping(value = "/copyFile")
    @ResponseBody
    public JSONObject copyFile(HttpServletRequest request, String oldFile, String newFile, String operationType) {
        JSONObject jsonObject = new JSONObject();
        String ftpRootPath = removeFtpRootLastSeparator(request);
        oldFile = fileNetService.decodePath(oldFile, ftpRootPath);
        newFile = fileNetService.decodePath(newFile, ftpRootPath);
        jsonObject = fileNetService.copyFolder(oldFile, newFile);
        return jsonObject;
    }

    @RequestMapping(value = "/moveFile")
    @ResponseBody
    public JSONObject moveFile(HttpServletRequest request, String oldFile, String newFile) {
        JSONObject jsonObject = new JSONObject();
        String ftpRootPath = removeFtpRootLastSeparator(request);
        oldFile = fileNetService.decodePath(oldFile, ftpRootPath);
        newFile = fileNetService.decodePath(newFile, ftpRootPath);
        jsonObject = fileNetService.moveFile(oldFile, newFile);
        return jsonObject;
    }

    @RequestMapping(value = "/deleteFile")
    @ResponseBody
    public JSONObject deleteFile(HttpServletRequest request, String deletePath) {
        JSONObject jsonObject = new JSONObject();
        String ftpRootPath = removeFtpRootLastSeparator(request);
        deletePath = fileNetService.decodePath(deletePath, ftpRootPath);
        jsonObject = fileNetService.deleteFolder(deletePath);
        return jsonObject;
    }

    @RequestMapping(value = "/uploadResourceExtraFile")
    @ResponseBody
    public List<String> unloadFileOnLocal(HttpServletRequest request) {
        MultipartRequest multipartRequest = (MultipartRequest) request;
        String ftpRootPath = removeFtpRootLastSeparator(request);
        String ResourceExtraFile = this.ResourceExtraFile;
        String parentURI = ftpRootPath + ResourceExtraFile;
        List<MultipartFile> files = multipartRequest.getMultiFileMap().get("file_data");
        List<String> filePath = new ArrayList<>(10);
        for (MultipartFile file : files) {
            Map<String, MultipartFile> m = new HashMap<>();
            m.put("file", file);
            Map.Entry<String, MultipartFile> next = m.entrySet().iterator().next();
            fileNetService.addFile(parentURI, next);
            filePath.add(parentURI + "/" + file.getOriginalFilename());
        }
        return filePath;
    }

    @GetMapping(value = "/downloadLog")
    public HttpServletResponse downloadLog(HttpServletRequest request, HttpServletResponse response, String fileName) throws IOException {
        // TODO 部署时替换
        String filePath = synchronizeTableLogs + "/" + fileName;
//        String filePath = "G:" + synchronizeTableLogs + "/" + fileName;
        File f = new File(filePath);
        if (!f.exists()) {
            ServletOutputStream out = response.getOutputStream();
            OutputStreamWriter ow = new OutputStreamWriter(out, "GB2312");
            ow.write("当前表还未执行过同步");
            ow.flush();
            ow.close();
            return response;
        }
        downloadFile(response, filePath);
        return response;
    }
}
