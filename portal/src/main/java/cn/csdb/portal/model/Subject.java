package cn.csdb.portal.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "t_subject")
public class Subject {
    @Id
    private String id;
    @Field("subjectName")
    private String subjectName; // 节点名称
    @Field("subjectCode")
    private String subjectCode; // 节点代码
    @Field("imagePath")
    private String imagePath; // 节点图片
    @Field("brief")
    private String brief; // 简介
    @Field("admin")
    private String admin; // 登陆账号
    @Field("adminPasswd")
    private String adminPasswd; // 登陆密码
    @Field("contact")
    private String contact; // 负责人
    @Field("phone")
    private String phone; // 手机号
    @Field("email")
    private String email; // 邮箱
    @Field("ftpServerAddr")
    private String ftpServerAddr;
    @Field("ftpUser")
    private String ftpUser; // ftp用户账号
    @Field("ftpPassword")
    private String ftpPassword; // ftp密码
    @Field("SerialNo")
    private String serialNo; // 排序序列
    @Field("ftpFilePath")
    private String ftpFilePath; // ftp根路径
    @Field("dbName")
    private String dbName; // 关系型数据库名称
    @Field("dbUserName")
    private String dbUserName; // 关系型数据库用户名
    @Field("dbPassword")
    private String dbPassword; // 关系型数据库密码
    @Field("dbHost")
    private String dbHost; // 关系型数据库地址
    @Field("dbPort")
    private String dbPort; // 关系型数据库端口号

    @Field("filePath")
    private String filePath;  // 文件路径

    private long visitCount; // 访问量统计
    private long downCont; // 下载量统计

    public long getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(long visitCount) {
        this.visitCount = visitCount;
    }

    public long getDownCont() {
        return downCont;
    }

    public void setDownCont(long downCont) {
        this.downCont = downCont;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getAdminPasswd() {
        return adminPasswd;
    }

    public void setAdminPasswd(String adminPasswd) {
        this.adminPasswd = adminPasswd;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFtpServerAddr() {
        return ftpServerAddr;
    }

    public void setFtpServerAddr(String ftpServerAddr) {
        this.ftpServerAddr = ftpServerAddr;
    }

    public String getFtpUser() {
        return ftpUser;
    }

    public void setFtpUser(String ftpUser) {
        this.ftpUser = ftpUser;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getFtpFilePath() {
        return ftpFilePath;
    }

    public void setFtpFilePath(String ftpFilePath) {
        this.ftpFilePath = ftpFilePath;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbUserName() {
        return dbUserName;
    }

    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDbHost() {
        return dbHost;
    }

    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    public String getDbPort() {
        return dbPort;
    }

    public void setDbPort(String dbPort) {
        this.dbPort = dbPort;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
