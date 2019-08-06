package cn.csdb.portal.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 *  所有字段全部描述的关系型表
 *
 * Created by shiba on 2018/10/30.
 */
@Document(collection = "t_described_table")
public class Described_Table {
    @Id
    private String objectId;
    @Field("tableName")
    private String tableName; // 被描述的表名称
    @Field("dbName")
    private String dbName; // 表所属的数据库
    @Field("subjectCode")
    private String subjectCode; // 表所属的节点

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }
}
