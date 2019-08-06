package cn.csdb.portal.service;

import cn.csdb.portal.utils.FileUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.PatternSyntaxException;

/**
 * @Author jinbao
 * @Date 2019/3/12 11:22
 * @Description 网盘功能
 **/
@Service
public class FileNetService {

    /**
     * @param parentPath 父级目录
     * @return 指定目录中文件的 名称、类型、大小、最后修改时间、路径
     */
    public JSONObject getDir(String ftpRootPath, String parentPath) {
        JSONObject jsonObject = new JSONObject();
        File f = new File(parentPath);
        File[] files = f.listFiles();

        // 校验并返回父级目录
        Map<String, String> mParent = verifyParentPath(ftpRootPath, parentPath);
        jsonObject.put("parent", mParent);

        // 当前目录以及每一个上层目录
        List<Map<String, String>> parentModules = getParentModule(parentPath, ftpRootPath);
        jsonObject.put("parentModules", parentModules);

        if (files.length < 0) {
            return jsonObject;
        }
        // 对文件排序
        List<File> fileList = FileUtil.orderByNameAndSize(files);

        // 查询当前目录
        List<Map<String, String>> fList = getFileInfo(ftpRootPath, fileList);
        jsonObject.put("data", fList);
        return jsonObject;
    }

    private List<Map<String, String>> getFileInfo(String ftpRootPath, List<File> fileList) {
        List<Map<String, String>> fList = new ArrayList<>(20);
        for (File file : fileList) {
            Map<String, String> map = new HashMap<>(16);
            String fileName = file.getName();
            map.put("fileName", fileName);

            String fileType = file.isFile() ? "file" : "dir";
            map.put("fileType", fileType);

            if ("file".equals(fileType)) {
                String fileLength = getFormateFileLength(file.length());
                map.put("fileLength", fileLength);
            }

            long lastModified = file.lastModified();
            Date date = new Date(lastModified);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fileLastModified = sdf.format(date);
            map.put("fileLastModified", fileLastModified);

            String filePath = file.getPath();
            filePath = encodePath(filePath, ftpRootPath);
            map.put("filePath", filePath);

            fList.add(map);
        }
        return fList;
    }

    public JSONObject getDirByName(String parentPath, String ftpRootPath, String searchName) {
        JSONObject jsonObject = new JSONObject();
        File file = new File(parentPath);
        List<File> files = null;
        try {
            files = FileUtil.searchFileByName(file, searchName);
            File[] files1 = files.toArray(new File[files.size()]);

            // 校验并返回父级目录
            Map<String, String> mParent = verifyParentPath(ftpRootPath, parentPath);
            jsonObject.put("parent", mParent);

            // 当前目录以及每一个上层目录
            List<Map<String, String>> parentModules = getParentModule(parentPath, ftpRootPath);
            jsonObject.put("parentModules", parentModules);

            // 对文件排序
            List<File> fileList = FileUtil.orderByNameAndSize(files1);
            List<Map<String, String>> fileInfo = getFileInfo(ftpRootPath, fileList);
            jsonObject.put("data", fileInfo);
        } catch (PatternSyntaxException e) {
            e.printStackTrace();
            jsonObject.put("code", "error");
            jsonObject.put("message", "检索条件内容不能包含特殊字符 * . ( ^ / ...等！");
        }
        return jsonObject;

    }

    /**
     * @param ftpRootPath 用户文件根目录
     * @param parentPath  请求目录
     * @return 处理后的父级目录 不允许超过用户文件根目录
     */
    Map<String, String> verifyParentPath(String ftpRootPath, String parentPath) {
        Map<String, String> map = new HashMap<>(16);
        if (ftpRootPath != null && parentPath != null && ftpRootPath.equals(parentPath)) {
            String encodePath = encodePath(ftpRootPath, ftpRootPath);
            map.put("filePath", encodePath);
            map.put("fileName", "...");
        } else {
            File f = new File(parentPath);
            String encodePath = encodePath(f.getParent(), ftpRootPath);
            map.put("filePath", encodePath);
            map.put("fileName", "返回上级");
        }
        map.put("fileType", "dir");
        return map;
    }


    /**
     * @return 文件大小进位 例：1024B —> 1KB
     */
    private String getFormateFileLength(long fileLength) {
        if (fileLength <= 1024) {
            return fileLength + " B";
        } else {
            fileLength /= 1024;
        }

        if (fileLength <= 1024) {
            return fileLength + "KB";
        } else {
            fileLength /= 1024;
        }

        //因为如果以MB || GB为单位的话，要保留最后1位小数，
        //因此，把此数乘以100之后再取余
        if (fileLength <= 1024) {
            fileLength = fileLength * 100;
            return (fileLength / 100) + "." + (fileLength % 100) + "MB";
        } else {
            fileLength = fileLength * 100;
            return (fileLength / 100) + "." + (fileLength % 100) + "GB";
        }
    }


    /**
     * 编码文件根路径
     *
     * @param path     需要编码的路径
     * @param rootPath 编码的根
     * @return
     */
    public String encodePath(String path, String rootPath) {
        String replace = "";
        replace = path.replace(rootPath, "path:");
        return replace;
    }


    /**
     * 反编码文件根路径
     *
     * @param path     需要解码码的路径
     * @param rootPath 解码的根
     * @return
     */
    public String decodePath(String path, String rootPath) {
        String replace = "";
        if (path == null || "".equals(path)) {
            replace = rootPath;
        } else {
            replace = path.replace("path:", rootPath);
        }
        return replace;
    }


    /**
     * @param dirName   新增加的目录名称
     * @param parentURI 指定增加的父级目录
     * @return
     */
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

    /**
     * 获取当前目录到用户根目录之下的所有父级目录
     *
     * @param path        当前目录
     * @param ftpRootPath 用户根目录
     * @return
     */
    List<Map<String, String>> getParentModule(String path, final String ftpRootPath) {
        List<Map<String, String>> pList = new ArrayList<>(10);
        Map<String, String> map = new HashMap<>(2);
        String s = decodePath(path, ftpRootPath);
        File file = new File(s);
        String name = file.getName();
        map.put("name", name);
        String filePath = encodePath(file.getPath(), ftpRootPath);
        map.put("path", filePath);
        pList.add(map);
        if (!Objects.equals(path, ftpRootPath)) {
//        if (new File(path).getPath() == new File((ftpRootPath)).getPath()) {
            String p = file.getParentFile().getPath();
            List<Map<String, String>> parentModule = getParentModule(p, ftpRootPath);
            pList.addAll(parentModule);
        }

        return pList;
    }


    public JSONObject addFile(String parentURI, Map.Entry<String, MultipartFile> multipartFile) {
        JSONObject jsonObject = new JSONObject();
        File file = new File(parentURI);
        if (!file.exists()) {
            file.mkdirs();
        }
        String s = multipartFile.getKey();
        MultipartFile value = multipartFile.getValue();
        FileItem fileItem = ((CommonsMultipartFile) value).getFileItem();
        String fileName = fileItem.getName();
        try {
            InputStream inputStream = fileItem.getInputStream();
            File f = new File(parentURI + File.separator + fileName);
            OutputStream outputStream = new FileOutputStream(f);
            int byteCount = 0;

            byte[] bytes = new byte[1024];

            while ((byteCount = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, bytes.length);
            }
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject renameFile(String currentPath, String newName) {
        JSONObject jsonObject = FileUtil.renameFolder(currentPath, newName);
        return jsonObject;
    }

    public JSONObject copyFolder(String oldFile, String newFile) {
        JSONObject jsonObject = FileUtil.copyFolder(oldFile, newFile);
        return jsonObject;
    }

    public JSONObject deleteFolder(String oldFile) {
        JSONObject jsonObject = FileUtil.deleteFolder(oldFile);
        return jsonObject;
    }

    public JSONObject moveFile(String oldFile, String newFile) {
        JSONObject jsonObject1 = FileUtil.copyFolder(oldFile, newFile);
        JSONObject jsonObject = FileUtil.deleteFolder(oldFile);
        jsonObject.put("message", "移动完成");
        return jsonObject;

    }
}
