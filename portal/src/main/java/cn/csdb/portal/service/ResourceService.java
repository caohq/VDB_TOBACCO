package cn.csdb.portal.service;

import cn.csdb.portal.model.Subject;
import cn.csdb.portal.model.User;
import cn.csdb.portal.repository.DataSrcDao;
import cn.csdb.portal.repository.ResourceDao;
import cn.csdb.portal.utils.FileTreeNode;
import cn.csdb.portal.utils.MailOperationUtil;
import cn.csdb.portal.utils.dataSrc.DataSourceFactory;
import cn.csdb.portal.utils.dataSrc.IDataSource;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @program: DataSync
 * @description: Resource Service
 * @author: xiajl
 * @create: 2018-10-23 16:19
 **/
@Service
public class ResourceService {

    @Resource
    private ResourceDao resourceDao;

    @Resource
    private DataSrcDao dataSrcDao;

    /**
     * Function Description: 保存资源
     *
     * @param:
     * @return:
     * @auther: Administrator
     * @date: 2018/10/23 16:22
     */
    @Transactional
    public String save(cn.csdb.portal.model.Resource resource) {
        return resourceDao.save(resource);
    }

    @Transactional
    public void saveFileInfo(cn.csdb.portal.model.FileInfo fileInfo) {
        resourceDao.saveFileInfo(fileInfo);
    }

    //在表中将删除的已审核数据集id记录下来
    @Transactional
    public void saveDeleteId(String id) {
        resourceDao.saveDeleteId(id);
    }

    @Transactional
    public void deleteFileInfo(String id) {
        resourceDao.deleteFileInfo(id);
    }

    /**
     * Function Description: 删除资源
     *
     * @param:
     * @return:
     * @auther: Administrator
     * @date: 2018/10/23 16:25
     */
    @Transactional
    public void delete(String id) {
        resourceDao.delete(id);
    }

    @Transactional
    public void delete(cn.csdb.portal.model.Resource resource) {
        resourceDao.delete(resource);
    }

    /**
     * Function Description: 分页查询获取资源List
     *
     * @param: subjectCode:专题库代码, title:资源名称, status:状态, pageNo:页数, pageSize:第页记录条数
     * @return: 资源List列表
     * @auther: Administrator
     * @date: 2018/10/23 16:28
     */
    @Transactional(readOnly = true)
    public List<cn.csdb.portal.model.Resource> getListByPage(String subjectCode, String title, String publicType, String status, int pageNo, int pageSize) {
        return resourceDao.getListByPage(subjectCode, title, publicType, status, pageNo, pageSize);
    }


    @Transactional(readOnly = true)
    public long countByPage(String subjectCode, String title, String publicType, String resState) {
        return resourceDao.countByPage(subjectCode, title, publicType, resState);
    }
    /*----------------------------------------------------------------------------------------*/

    public List<JSONObject> fileSourceFileList(String filePath) {
        System.out.println("filePath为" + filePath);
        List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
        File file = new File(filePath);
        if (!file.exists() || !file.isDirectory()) {
            return jsonObjects;
        }
        System.out.println("1");
//        String[] fp = filePath.split(";");
        File[] fp = file.listFiles();
        for (int i = 0; i < fp.length; i++) {
//            if(StringUtils.isBlank(fp[i])){
//                continue;
//            }
//            File file = new File(fp[i]);
//            if(!file.exists()){
//                continue;
//            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", fp[i].getPath().replaceAll("\\\\", "%_%"));
            jsonObject.put("text", fp[i].getName().replaceAll("\\\\", "%_%"));
            if (fp[i].isDirectory()) {
                jsonObject.put("type", "directory");
                JSONObject jo = new JSONObject();
                jo.put("disabled", "true");
                jsonObject.put("state", jo);
            } else {
                jsonObject.put("type", "file");
            }
            jsonObjects.add(jsonObject);
        }
        System.out.println("2");
        Collections.sort(jsonObjects, new FileComparator());
        System.out.println("3");
        return jsonObjects;

    }

    public List<JSONObject> fileSourceFileListFirst(String filePath) {
        List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
        File file = new File(filePath);
        if (!file.exists() || !file.isDirectory())
            return jsonObjects;
//        String[] fp = filePath.split(";");
        String separator = File.separator;
        String reg = "";
        if ("/".equals(separator)) {
            reg = separator;
        } else {
            reg = "\\\\";
        }
        File[] fp = file.listFiles();
        for (int i = 0; i < fp.length; i++) {
//            if(StringUtils.isBlank(fp[i])){
//                continue;
//            }
//            File file = new File(fp[i]);
//            if(!file.exists()){
//                continue;
//            }
            JSONObject jsonObject = new JSONObject();
            if (fp[i].getPath().indexOf("_sql") == -1) {
                if (fp[i].isDirectory()) {
                    if (fp[i].getPath().indexOf("_sql") == -1) {
                        jsonObject.put("id", fp[i].getPath().replaceAll(reg, "%_%"));
                        jsonObject.put("text", fp[i].getName().replaceAll(reg, "%_%"));
                        jsonObject.put("type", "directory");
//                        JSONObject jo = new JSONObject();
//                        jo.put("disabled", "true");
//                        jsonObject.put("state", jo);
                        jsonObjects.add(jsonObject);
                    }
                }
            }
        }
        Collections.sort(jsonObjects, new FileComparator());
        return jsonObjects;

    }

    /**
     * 配置前端文件树搜索功能 重新实现树首次加载方法
     *
     * @param filePath 当前文件路径
     * @param count    递归次数决定文件默认深度
     * @return 树值
     */
    public List<JSONObject> treeNodeFirst(String filePath, int count) {
        count--;
        List<JSONObject> jsonObjects = new ArrayList<JSONObject>(32);
        File file = new File(filePath);
        if (!file.exists() || !file.isDirectory()) {
            return jsonObjects;
        }
        String separator = File.separator;
        String reg = "";
        if ("/".equals(separator)) {
            reg = separator;
        } else {
            reg = "\\\\";
        }
        File[] fp = file.listFiles();
        for (int i = 0; i < fp.length; i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", fp[i].getPath().replaceAll("\\\\", "%_%"));
            jsonObject.put("text", fp[i].getName().replaceAll("\\\\", "%_%"));
            if (fp[i].isDirectory()) {
                jsonObject.put("type", "directory");
                if (count >= 0) {
                    jsonObject.put("children", treeNodeFirst(fp[i].getPath(), count));
                }
            } else {
                jsonObject.put("type", "file");
            }
            jsonObjects.add(jsonObject);
        }
        Collections.sort(jsonObjects, new FileComparator());
        return jsonObjects;
    }


    class FileComparator implements Comparator<JSONObject> {

        public int compare(JSONObject o1, JSONObject o2) {
            if ("directory".equals(o1.getString("type")) && "directory".equals(o2.getString("type"))) {
                return o1.getString("text").compareTo(o2.getString("text"));
            } else if ("directory".equals(o1.getString("type")) && !"directory".equals(o2.getString("type"))) {
                return -1;
            } else if (!"directory".equals(o1.getString("type")) && "directory".equals(o2.getString("type"))) {
                return 1;
            } else {
                return o1.getString("text").compareTo(o2.getString("text"));
            }
        }
    }

    public cn.csdb.portal.model.Resource getById(String resourceId) {


        return resourceDao.getById(resourceId);
    }

    public int getRecordCount(String host, String port, String userName, String password, String databaseName, List<String> tableName) {
        return dataSrcDao.getRecordCount(host, port, userName, password, databaseName, tableName);
    }

    public Map<String, Map<String, String>> getTableColumns(String host, String port, String userName, String password, String databaseName, String tableName) {
        return dataSrcDao.getTableColumns(host, port, userName, password, databaseName, tableName);
    }

    public List<FileTreeNode> loadingFileTree(String path, List<FileTreeNode> nodeList) {
        String systemName = System.getProperties().getProperty("os.name");
        File dirFile = new File(path);//获取文件第一层
        File[] fs = dirFile.listFiles();
        int isWindows = systemName.indexOf("Windows");
        for (int i = 0; i < fs.length; i++) {
            if (fs[i].isFile()) {//当对象为文件时
                String pidStr = "";
                if ("-1".equals(isWindows + "")) {//linux
                    pidStr = fs[i].toString().substring(0, fs[i].toString().lastIndexOf("/"));
                } else {
                    pidStr = fs[i].toString().substring(0, fs[i].toString().lastIndexOf("\\"));
                }
                nodeList.add(new FileTreeNode(fs[i].toString(), pidStr, fs[i].getName(), "false", "false", "false"));
            } else if (fs[i].isDirectory()) {//当对象为路径时
                String pidStr = "";
                if ("-1".equals(isWindows + "")) {//linux
                    pidStr = fs[i].toString().substring(0, fs[i].toString().lastIndexOf("/"));
                } else {
                    pidStr = fs[i].toString().substring(0, fs[i].toString().lastIndexOf("\\"));
                }
                nodeList.add(new FileTreeNode(fs[i].toString(), pidStr, fs[i].getName(), "false", "true", "false"));
                // nodeList=loadingTree(fs[i].toString(),nodeList);
            }
        }

        return nodeList;
    }

    public List<FileTreeNode> asynLoadingTree(String data, String path, String result) {
        System.out.println("服务器系统:" + System.getProperties().getProperty("os.name"));
        String systemName = System.getProperties().getProperty("os.name");
        int isWindows = systemName.indexOf("Windows");
        List<FileTreeNode> nodeList = new ArrayList<FileTreeNode>();
        File dirFile = new File(path);//获取文件第一层
        File[] fs = dirFile.listFiles();
        if ("init".equals(result)) {
            nodeList.add(new FileTreeNode(path, "0", path, "true", "true", "false"));
        }
        for (int i = 0; i < fs.length; i++) {
            if (fs[i].isFile()) {//当对象为文件时
                String pidStr = "";
                if ("-1".equals(isWindows + "")) {//linux
                    pidStr = fs[i].toString().substring(0, fs[i].toString().lastIndexOf("/"));
                } else {
                    pidStr = fs[i].toString().substring(0, fs[i].toString().lastIndexOf("\\"));
                }
                nodeList.add(new FileTreeNode(fs[i].toString(), pidStr, fs[i].getName(), "false", "false", "false"));
            } else if (fs[i].isDirectory()) {//当对象为路径时
                String pidStr = "";
                if ("-1".equals(isWindows + "")) {//linux
                    pidStr = fs[i].toString().substring(0, fs[i].toString().lastIndexOf("/"));
                } else {
                    pidStr = fs[i].toString().substring(0, fs[i].toString().lastIndexOf("\\"));
                }
                nodeList.add(new FileTreeNode(fs[i].toString(), pidStr, fs[i].getName(), "false", "true", "false"));
                //  nodeList=loadingTree(fs[i].toString(),nodeList);
            }
        }
        return nodeList;
    }

    public BigDecimal getTableLength(Subject subject, String tableName) {
        BigDecimal bigDecimal = new BigDecimal(0);
        String sql = "SELECT\n" +
                "\tDATA_LENGTH + INDEX_LENGTH as LENGTH\n" +
                "FROM\n" +
                "\tinformation_schema.`TABLES`\n" +
                "WHERE\n" +
                "\tTABLE_SCHEMA = ? \n" +
                "AND TABLE_NAME = ? \n ";

        IDataSource dataSource = DataSourceFactory.getDataSource("mysql");
        Connection connection = dataSource.getConnection(subject.getDbHost(), subject.getDbPort(),
                subject.getDbUserName(), subject.getDbPassword(), subject.getSubjectCode());
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, subject.getSubjectCode());
            preparedStatement.setString(2, tableName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long aLong = resultSet.getLong(1);
                bigDecimal = bigDecimal.add(new BigDecimal(aLong));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return bigDecimal;
    }

    public BigDecimal getTableRow(Subject subject, String tableName) {
        BigDecimal bigDecimal = new BigDecimal(0);
        String sql = "SELECT\n" +
                "\tTABLE_ROWS AS ROW\n" +
                "FROM\n" +
                "\tINFORMATION_SCHEMA.`TABLES`\n" +
                "WHERE\n" +
                "\tTABLE_SCHEMA = ? \n" +
                "AND TABLE_NAME = ? \n";

        IDataSource dataSource = DataSourceFactory.getDataSource("mysql");
        Connection connection = dataSource.getConnection(subject.getDbHost(), subject.getDbPort(),
                subject.getDbUserName(), subject.getDbPassword(), subject.getSubjectCode());
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, subject.getSubjectCode());
            preparedStatement.setString(2, tableName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long aLong = resultSet.getLong(1);
                bigDecimal = bigDecimal.add(new BigDecimal(aLong));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return bigDecimal;
    }

    public BigDecimal getFileLength(Subject subject, String filePath) {
        BigDecimal bigDecimal = new BigDecimal(0);
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            BigDecimal fileLength = new BigDecimal(file.length());
            bigDecimal = bigDecimal.add(fileLength);
        }
        return bigDecimal;
    }

    public void updateFileInfoTime(String resourceId) {
        resourceDao.updateFileInfoTime(resourceId);
    }

    //发送通用邮件通知
    public void sendForgotMail(User user, cn.csdb.portal.model.Resource resource, String reson){
        //生成重置密码加密链接
//        long endTimes = System.currentTimeMillis()+3600*1000;   //设置一小时有效时间
//        String password1 = userDao.getByPassword(loginId); //获取该用户的密码
//        String para = loginId+";"+password1+";"+endTimes; //拼接成串，用base64进行加密
//        String encode = UrlUtils.enCryptAndEncode(para);

        MailOperationUtil operation = new MailOperationUtil();
        String userName = "vdbcloud@cnic.cn";
        String host = "smtp.cnic.cn";
        String password = "sdc123456";
        String from = "vdbcloud@cnic.cn";
        String addressee= user.getEmail();// 收件人
        System.out.print("收件人为：" + addressee);
        String title = "数据集停用通知邮件";
        String resTitle=resource.getTitle();
        //邮箱内容
        StringBuffer sb = new StringBuffer();
        sb.append("<!DOCTYPE>" + "<div bgcolor='#f1fcfa'   style='border:1px solid #d9f4ee; font-size:14px; line-height:22px; color:#005aa0;padding-left:1px;padding-top:5px;   padding-bottom:5px;'><span style='font-weight:bold;'>温馨提示：</span>"
                + "<div style='width:950px;font-family:arial;'>尊敬的用户您好:<br/> 名称为&nbsp;'"+resTitle+"'&nbsp;的数据集已经停用。停用理由如下:<p>"+ reson+"</p>本邮件由系统自动发出，请勿回复。<br/>感谢您使用课题组数据管理与发布工具<br/></div>"
                + "</div>");
        try {
            String res = operation.sendMail(userName,host, password,from, addressee,
                    title, sb.toString());
            System.out.print("发送成功！！！");
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print(e);
        }
    }
}