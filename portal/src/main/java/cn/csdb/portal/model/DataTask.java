package cn.csdb.portal.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * @program: DataSync
 * @author: huangwei
 * @create: 2018-10-12 15:37
 **/
@Document(collection = "t_datatask")
public class DataTask {
    @Id
    private String dataTaskId; // 任务ID
    @Field("dataSourceId")
    private int dataSourceId; // 数据源ID
    @Field("subjectCode")
    private String subjectCode; // 节点代码
    @Field("dataTaskType")
    private String dataTaskType; // 任务类型
    @Field("tableName")
    private String tableName; // 选中的表名称  多个以";"隔开
    @Field("sqlString")
    private String sqlString; // 建表语句
    @Field("sqlTableNameEn")
    private String sqlTableNameEn; // 新增表名称
    @Field("sqlFilePath")
    private String sqlFilePath; // sql脚本路径
    @Field("filePath")
    private String filePath; // 上传文件绝对路径
    @Field("createTime")
    private Date createTime; //创建时间
    @Field("creator")
    private String creator; // 创建人
    @Field("status")
    private String status; // 状态 0：未上传 1：已上传
    @Field("remoteuploadpath")
    private String remoteuploadpath; // 文件上传目标路径
    @Field("fileSize")
    private String fileSize; // 选中上传文件的大小
    @Field("sync")
    private String sync;//是否同步
    @Field("period")
    private String period;//周期
    @Field("synctime")
    private Date synctime;//同步时间


    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getDataTaskType() {
        return dataTaskType;
    }

    public void setDataTaskType(String dataTaskType) {
        this.dataTaskType = dataTaskType;
    }

    public String getDataTaskId() {
        return dataTaskId;
    }

    public void setDataTaskId(String dataTaskId) {
        this.dataTaskId = dataTaskId;
    }

    public int getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(int dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSqlString() {
        return sqlString;
    }

    public void setSqlString(String sqlString) {
        this.sqlString = sqlString;
    }

    public String getSqlTableNameEn() {
        return sqlTableNameEn;
    }

    public void setSqlTableNameEn(String sqlTableNameEn) {
        this.sqlTableNameEn = sqlTableNameEn;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSqlFilePath() {
        return sqlFilePath;
    }

    public void setSqlFilePath(String sqlFilePath) {
        this.sqlFilePath = sqlFilePath;
    }

    public String getRemoteuploadpath() {
        return remoteuploadpath;
    }

    public void setRemoteuploadpath(String remoteuploadpath) {
        this.remoteuploadpath = remoteuploadpath;
    }


    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getSync() {
        return sync;
    }

    public void setSync(String sync) {
        this.sync = sync;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Date getSynctime() {
        return synctime;
    }

    public void setSynctime(Date synctime) {
        this.synctime = synctime;
    }
}
