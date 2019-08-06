package cn.csdb.portal.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "t_synchronization_tables")
public class SynchronizationTable {
    @Id
    private String id;
    @Field("tableName")
    private String tableName; //同步表名
    @Field("subjectCode")
    private String subjectCode;
    @Field("loginId")
    private String loginId;
    @Field("frequency")
    private Long frequency; // 同步频次 毫秒值
    @Field("sqlString")
    private String sqlString; // 关联创建时的 DDL
    @Field("lastModifyTime")
    private Long lastModifyTime; // 最后一次修改的时间 毫秒值
    @Field("systemName")
    private String systemName; // 设置同步任务的系统所属
    @Field("taskId")
    private String taskId; // 任务Id


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public Long getFrequency() {
        return frequency == null ? 0L : frequency;
    }

    public void setFrequency(Long frequency) {
        this.frequency = frequency;
    }

    public String getSqlString() {
        return sqlString;
    }

    public void setSqlString(String sqlString) {
        this.sqlString = sqlString;
    }

    public Long getLastModifyTime() {
        return lastModifyTime == null ? 0L : lastModifyTime;
    }

    public void setLastModifyTime(Long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
