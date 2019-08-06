package cn.csdb.portal.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author jinbao
 * @Date 2019/2/28 10:37
 * @Description 文件 重命名，复制移动 ，删除等
 **/
public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 删除选中的目录||文件
     * 目录：删除当前目录已经目录下的所有
     * 文件：删除当前文件
     *
     * @param folderPath 选中的路径
     * @return code=success :删除成功
     */
    public static JSONObject deleteFolder(String folderPath) {
        JSONObject jsonObject = new JSONObject();
        File f = new File(folderPath);
        boolean key = false;
        try {
            if (f.exists() && f.isDirectory()) {
                String[] list = f.list();
                if (list.length > 0) {
                    String path = f.getAbsolutePath();
                    for (String s : list) {
                        String tempPath = path + File.separator + s;
                        deleteFolder(tempPath);
                    }
                    File tempFile = new File(path);
                    key = tempFile.delete();
                } else {
                    key = f.delete();
                }
            } else if (f.exists() && f.isFile()) {
                key = f.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "error");
            jsonObject.put("message", "文件删除异常！");
            logger.error("删除异常路径：" + folderPath);
        }
        if (key) {
            jsonObject.put("code", "success");
            jsonObject.put("message", "删除成功！");
        } else {
            jsonObject.put("code", "error");
            jsonObject.put("message", "文件删除失败！");
        }
        return jsonObject;
    }

    /**
     * 复制当前文件||目录（含目录下文件）到指定路径
     *
     * @param oldPath 要复制的文件||目录
     * @param newPath 目标目录
     */
    public static JSONObject copyFolder(String oldPath, String newPath) {
        JSONObject jsonObject = new JSONObject();
        File oldFile = new File(oldPath);
        File newFile = new File(newPath);
        CopyCheck copyCheck = copyeCheck(oldFile, newFile);
        if (copyCheck != CopyCheck.ALLOW) {
            jsonObject.put("code", "error");
            if (copyCheck.equals(CopyCheck.MYSELF)) {
                jsonObject.put("message", "目标文件是源文件的子文件");
            } else if (copyCheck.equals(CopyCheck.REPETITION)) {
                jsonObject.put("message", "文件名称重复");
            }
            return jsonObject;
        }
        ;
        try {
            if (oldFile.exists() && oldFile.isFile()) {
                jsonObject = copyFile(oldFile, newFile);
            } else if (oldFile.exists() && oldFile.isDirectory()) {
                // 新位置生成对应层级
                String oldName = oldFile.getName();
                File tempNewFile = new File(newPath, oldName);
                tempNewFile.mkdir();
                newPath = tempNewFile.getAbsolutePath();
                // System.out.println("新的文件位置：" + newPath);
                String[] list = oldFile.list();
                if (list.length != 0) {
                    for (String s : list) {
                        // 同一个作用域变量名称不要使用自拼接 !!!
                        // oldPath = oldPath + File.separator +s;
                        String tempPath = oldPath + File.separator + s;
                        copyFolder(tempPath, newPath);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "error");
            jsonObject.put("message", "文件复制失败！");
            logger.error("《" + oldPath + "》复制到=》《" + newPath + "》异常！");
        }
        jsonObject.put("code", "success");
        jsonObject.put("message", "文件复制成功！");
        return jsonObject;
    }

    /**
     * 检查被复制文件在新位置是否有同名文件
     *
     * @param oldFile
     * @param newFile
     * @return
     */
    private static CopyCheck copyeCheck(File oldFile, File newFile) {
        String oldFileName = oldFile.getName();
        String newFileName = newFile.getName();

        if (newFileName.equals(oldFileName)) {
            return CopyCheck.MYSELF;
        }

        String[] newFileNames = newFile.list();
        if (newFileNames != null && newFileNames.length != 0) {
            for (String name : newFileNames) {
                if (name.equals(oldFileName)) {
                    return CopyCheck.REPETITION;
                }
            }
        }
        return CopyCheck.ALLOW;
    }

    /**
     * 单纯复制 文件 到指定目录下
     *
     * @param oldFile 被复制文件
     * @param newFile 新的指定目录
     * @return 结果集
     */
    private static JSONObject copyFile(File oldFile, File newFile) {
        JSONObject jsonObject = new JSONObject();
        String name = oldFile.getName();
        String newPath = newFile.getAbsolutePath();
        File tempFile = new File(newPath, name);
        if (!tempFile.getParentFile().exists()) {
            tempFile.getParentFile().mkdirs();
        }
        if (tempFile.exists()) {
            jsonObject.put("code", "error");
            jsonObject.put("message", "当前文件下已存在" + name);
            return jsonObject;
        } else {
            try {
                tempFile.createNewFile();
                System.out.println("生成文件：" + tempFile.getAbsolutePath());
                FileInputStream fis = new FileInputStream(oldFile);
                FileOutputStream fos = new FileOutputStream(tempFile);
                byte[] b = new byte[1024];
                int k = 0;
                while ((k = fis.read(b)) != -1) {
                    fos.write(b);
                }
                fos.flush();
                fos.close();
                fis.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                jsonObject.put("code", "error");
                jsonObject.put("message", "复制读取流异常！");
            } catch (IOException e) {
                e.printStackTrace();
                jsonObject.put("code", "error");
                jsonObject.put("message", "复制写入流异常！");
            }
        }
        jsonObject.put("code", "success");
        jsonObject.put("message", "文件复制成功！");
        return jsonObject;
    }

    /**
     * 重命名当前文件||目录
     *
     * @param oldFilePath 被重命名文件||目录
     * @param fileNewName 新名称
     */
    public static JSONObject renameFolder(String oldFilePath, String fileNewName) {
        JSONObject jsonObject = new JSONObject();
        boolean reName = false;
        try {
            File oldFile = new File(oldFilePath);
            String fileOldParent = oldFile.getParent();
            String newName = "";
            if (oldFile.exists() && oldFile.isDirectory()) {
                newName = fileOldParent + File.separator + fileNewName;
            } else if (oldFile.exists() && oldFile.isFile()) {
                String oldFileName = oldFile.getName();
                int i = oldFileName.lastIndexOf(".");
                String fileSuffix = oldFileName.substring(i, oldFileName.length());
                newName = fileOldParent + File.separator + fileNewName + fileSuffix;
            } else {
                jsonObject.put("code", "error");
                jsonObject.put("message", oldFile.getName() + " 不存在！");
                return jsonObject;
            }
            File newFile = new File(newName);
            if (newFile.exists()) {
                jsonObject.put("code", "error");
                jsonObject.put("message", newFile.getName() + " 已存在！");
                return jsonObject;
            }
            reName = oldFile.renameTo(newFile);
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "error");
            jsonObject.put("message", "文件重命名异常！");
        }
        if (reName) {
            jsonObject.put("code", "success");
            jsonObject.put("message", "文件重命名成功！");
        } else {
            jsonObject.put("code", "error");
            jsonObject.put("message", "文件重命名失败！");
        }
        return jsonObject;
    }

    public static void createFileByPathAndType(String filepath, String fileType) throws IOException {
        File f = new File(filepath);
        if (!f.exists() && "file".equals(fileType)) {
            File parentFile = f.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            f.createNewFile();
        } else if (!f.exists() && "dir".equals(fileType)) {
            f.mkdirs();
        }
    }

    public static List<File> orderByNameAndSize(File[] files) {
        List fileList = Arrays.asList(files);
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile()) {
                    return -1;
                }
                if (o1.isFile() && o2.isDirectory()) {
                    return 1;
                }
                return o1.getName().compareTo(o2.getName());
            }
        });
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                boolean file = o1.isFile() && o2.isFile();
                if (file && (o1.length() > o2.length())) {
                    return -1;
                } else if (file && (o1.length() < o2.length())) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        return fileList;
    }

    public static List<File> searchFileByName(File file, String name) {
        ArrayList<File> files = new ArrayList<>();
        String regString = ".*" + name + ".*";
        Pattern pattern = Pattern.compile(regString);
        Path path = Paths.get(file.getAbsolutePath());
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                // 访问文件时触发
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    getSelectedFile(file, pattern, files);
                    return FileVisitResult.CONTINUE;
                }

                // 访问目录时触发
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    getSelectedFile(dir, pattern, files);
                    return FileVisitResult.CONTINUE;
                }

                private void getSelectedFile(Path dir, Pattern pattern, ArrayList<File> files) {
                    String fileName = dir.getFileName().toString();
                    Matcher matcher = pattern.matcher(fileName);
                    boolean matches = matcher.matches();
                    if (matches) {
                        files.add(new File(dir.toString()));
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    /**
     * @return 文件大小进位 例：1024B —> 1KB
     */
    public static String formateFileLength(long fileLength) {
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
    private enum CopyCheck {
        ALLOW,
        MYSELF,
        REPETITION
    }
}
