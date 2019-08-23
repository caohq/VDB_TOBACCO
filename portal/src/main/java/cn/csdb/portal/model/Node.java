package cn.csdb.portal.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "t_node")
public class Node {
    @Id
    private String id;
    @Field("nodeName")
    private String nodeName; // 节点名称
    @Field("nodeCode")
    private String nodeCode; // 节点代码
    @Field("subjectCode")
    private String subjectCode; // 主题库代码
    @Field("nodeURL")
    private String nodeURL;    //url地址
    @Field("createTime")
    private Date createTime;

    @Field("serialNo")
    private String serialNo; //序号

    @Field("status")
    private String status; //状态

    @Field("filePath")
    private String filePath; //文件路径

    @Field("dbPath")
    private String dbPath;   //excel文件路径


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getNodeURL() {
        return nodeURL;
    }

    public void setNodeURL(String nodeURL) {
        this.nodeURL = nodeURL;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDbPath() {
        return dbPath;
    }

    public void setDbPath(String dbPath) {
        this.dbPath = dbPath;
    }
}
